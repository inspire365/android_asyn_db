package com.repository.dbservice;

import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.util.Log;

public class DbService {

	public static final int SQLITE_TYPE_PLAIN  = 0;
	public static final int SQLITE_TYPE_CIPHER = 1;
	
	DbWorkerBase worker = null;

	private boolean mIsAlive = false;

	private Context appcontext = null;
	private int dbtype = 0;
	private int appversion = 0;
	private String dbname = null;

	protected final Map<Long, IDbHandler>        handlers  = new TreeMap<Long, IDbHandler>();
	protected final Map<Long, List<IDbObserver>> observers = new TreeMap<Long, List<IDbObserver>>();


	public boolean isAlive() {
		return mIsAlive;
	}

	/**
	 * Setup DB env
	 * 
	 * @param appversion
	 * @param dbpath   database full path
	 * @param dbtype   database type( sqlite, sqlcipher ...) only support
	 *                 sqlite at the moments
	 */
	public synchronized void setupService(Context appcontext, int appversion,
			String dbname, int dbtype) {
		this.appcontext = appcontext;
		this.dbname = dbname;
		this.appversion = appversion;
		this.dbtype = dbtype;
		String info = "setupService file version " + this.appversion
				+ " passed version: " + appversion;
		Log.i("DB", "DbService::setupService " + info);
	}

	public synchronized boolean startService() {
		if (worker == null) {
			if (dbtype == DbService.SQLITE_TYPE_PLAIN){
			    worker = new SqliteDbWorker(this.appcontext, this.appversion,
					this.dbname, this.handlers, this.observers);
			} else {
				worker = new CipherDbWorker(this.appcontext, this.appversion,
						this.dbname, this.handlers, this.observers);
			}
			Log.d("YY", "Worker create...");
			mIsAlive = true;
			return worker.init();
		}
		return false;
	}

	/**
	 * Register an observer to get notification when DB operation done
	 * @param appid
	 * @param observer  observer to get notification
	 * @return 0 for successful
	 */
	public int registerObserver(long appid, IDbObserver observer) {
		synchronized (observers) // local scope lock
		{
			List<IDbObserver> obs = observers.get(appid);
			if (obs == null) {
				obs = new ArrayList<IDbObserver>();
				observers.put(appid, obs);
			}
			if (!obs.contains(observer)) {
				obs.add(observer);
			}
		}
		return 0;
	}

	/**
	 * UnRegister an observer to stop getting DB notification
	 * @param appid
	 * @param observer  observer to unregister
	 * @return 0 for successful
	 */
	public int unRegisterObserver(long appid, IDbObserver observer) {
		synchronized (observers) // local scope lock
		{
			List<IDbObserver> obs = observers.get(appid);
			if (obs != null) {
				obs.remove(observer);
			}
		}
		return 0;
	}

	/**
	 * Register a Handler to process DB business logic
	 * 
	 * In order to get rid of synchronized issue,
	 * Make sure all the handlers had been registered
	 * before the worker thread started
	 * RegisterHandler/unRegisterHandler must be executed
	 * in single thread env
	 * @param appid
	 * @param Handler
	 * @return 0 for successful
	 */
	public int registerHandler(long appid, IDbHandler Handler) {
		
		if (worker == null){
	      handlers.put(appid, Handler); // just insert or replace
	      return 0;
		}
        Log.e("DB", "DbService::RegisterHandler Fail, the thread had been started");
        return 1;
	}

	/**
	 * UnRegister a Handler
	 * 
	 * In order to get rid of synchronized issue,
	 * Make sure all the handlers had been registered
	 * before the worker thread started or exited
	 * RegisterHandler/unRegisterHandler must be executed
	 * in single thread env
	 * @param appid
	 * @param Handler
	 * @return 0 for successful
	 */
	public int unRegisterHandler(long appid) {
		
		if (worker == null){
			handlers.remove(appid); // just remove if any
			return 0;
		}
        Log.e("DB", "DbService::unRegisterHandler fail, the thread had been started");
		return 1;
	}

	/**
	 * Puts an DB request to DbService
	 * @param req req information
	 * @return 0 for successful
	 */
	public synchronized int putDBReq(DbReqBase req) {
		Log.d("DB", "[+]DbService::putDBReq");
		if (worker == null) {
			Log.d("DB", "DbService::putDBReq worker is null Put req fail");
			return 1;
		}
		return worker.putDBReq(req);
	}

}; // end class DbService
