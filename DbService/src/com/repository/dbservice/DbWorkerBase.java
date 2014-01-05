package com.repository.dbservice;

import java.lang.Thread;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.util.Log;
import android.content.Context;


public class DbWorkerBase extends Thread {
	
	private final DbReqQueue tasks = new DbReqQueue();
	
	protected Context appcontext = null;
	protected int appversion = 1;
	protected String dbname = null;

	protected Map<Long, IDbHandler> handlers = null;
	private Map<Long, List<IDbObserver> > observers = null;

	// flag to tell thread exit
	private volatile int terminate = 1; // not 0 to exit thread

	DbWorkerBase(Context appcontext, int appversion, String dbname,
			Map<Long, IDbHandler> handlers,
			Map<Long, List<IDbObserver>> observers) {
		this.appcontext = appcontext;
		this.dbname = dbname;
		this.appversion = appversion;
		this.handlers = handlers;
		this.observers = observers;
		String info = "DbWorkerBase::ctor constructor file version " + this.appversion
				+ " passed version: " + appversion;
		Log.i("DB", info);
	}

	public boolean init() {
		if (terminate > 0) {
			terminate = 0;
			// can do this without lock, because the outer service
			// lock it and the thread have not been started
			this.start(); // start the thread
		}
		return true;
	}

	public void termDB() {
		
	}
	
	public void term() {
		// tell the thread to exit;
		terminate = 1; 
		DbReqBase req = new DbReqBase();
		req.appid = 0;
		this.putDBReq(req);  // an empty req to wake the thread
		try {
			this.join(1000000L); // milliseconds
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("DB", "DbWorkerBase::Term exception: " + e.getMessage());
		}
		if (!this.isInterrupted()) {
			this.interrupt();
		}
        this.termDB();
	}

	public int putDBReq(DbReqBase req) {
		Log.d("DB", "[+]DbWorkerBase::putDBReq");
		if (req != null) {
			tasks.push(req);
			return 0;
		}
		return 1;
	}

	private void notifyObservers(DbRspBase rsp) {
		if (rsp == null)
			return;

		List<IDbObserver> obs = null;
		synchronized (observers) {
			obs = observers.get(rsp.appid);
			if (obs == null)
				return;

			// TODO can do without lock ?
			// iterate through the set and notify
			Iterator<IDbObserver> it = obs.iterator();
			while (it.hasNext()) {
				IDbObserver observer = it.next();
				observer.notify(rsp);
			}
		} // synchronized
	}

	protected DbRspBase execute(DbReqBase task) {
		return null;
	}

	public void initDB(Object db) {

	}

	public void migrateDB(Object db, int oldversion, int newversion) {
		
	}

	protected boolean prepareDB() {
      return true;
	}

	private void ExecuteTask() {
		while (true) {
			// check if to exit
			if (terminate != 0)
				break;
			// Log.i("DB", "DbWorkerBase::ExecuteTask...");
			// TODO Execute Batch task
			DbReqBase task = null;
			try {
				task = tasks.poll(10000L);
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("DB",	"DbWorkerBase::ExecuteTask Exception: " + e.getMessage());
			}
			try{
				DbRspBase rsp = this.execute(task);
				this.notifyObservers(rsp);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		} // end while
	}

	@Override
	public void run() {
		Log.d("DB", "DbWorkerBase::run is running...");
		try {
			boolean ret = this.prepareDB();
			if (ret) {
				Log.i("DB", "DbWorkerBase::run Init DB OK, Now start executing task......");
				this.ExecuteTask();
			} else {
				Log.e("DB", "DbWorkerBase::run Fail to init DB...");
			}
			Log.d("DB", "DbWorkerBase::run is told to exit...");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("DB", "DbWorkerBase::run Exception: " + e.getMessage());
		} finally {
          this.termDB();
		} // finally
		Log.i("DB", "DBWorkder::run is exiting...");
	} // run

}; // end calss DbWorkerBase

