package com.repository.dbservice;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class SqliteDbWorker extends DbWorkerBase{

	private static SqliteDbOpenHelper helper = null;
	
	SqliteDbWorker(
			Context appcontext,
			int appversion,
			String dbname,
			Map<Long, IDbHandler> handlers,
			Map<Long, List<IDbObserver>> observers) {
		super(appcontext, appversion, dbname, handlers, observers);
		// TODO Auto-generated constructor stub
	}
	
	
	protected DbRspBase execute(DbReqBase task) {
		Log.d("DB", "[+]SqliteSqliteDbWorker::Execute");
		if (task == null)
			return null;

		Log.d("DB", "SqliteDbWorker::Execute get task to exe, appid is: " + task.appid + " priority: " + String.valueOf(task.priority));
		DbRspBase rsp = null;
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			IDbHandler handler = handlers.get(task.appid);
			// TODO Can I do this, get the processor out and do without lock
			if (handler != null) {
				Log.d("DB", "SqliteDbWorker get processor to exe...");
				rsp = handler.handle(task, db);
			}
		} catch (Exception e) // To further support different DB, use a based
								// Exception class
		{
			e.printStackTrace();
			Log.i("DB", "DBWoker::Execute Exception: " + e.getMessage());
			rsp = new DbRspBase();
			rsp.resultCode = 1;
			rsp.info = e.getMessage();
		}
		if (rsp != null) {
			Log.d("DB", "SqliteDbWorker::Execute const context parameters");
			rsp.appid = task.appid;
			rsp.cmd = task.cmd;
		}
		if (db != null){
			db.close();
		}
		return rsp;
	}

	public void initDB(Object database) {
		if (database == null)
			return ;
		
		SQLiteDatabase db = (SQLiteDatabase)(database);
		for (Map.Entry<Long, IDbHandler> entry : handlers.entrySet()) {
			IDbHandler handler = entry.getValue();
			String sql = handler.initDBSQL();
			if (sql != null && sql.length() > 5) {
				db.execSQL(sql);
			}
		}
	}

	public void migrateDB(Object database, int oldversion, int newversion) {
		if (database == null)
			return ;
		SQLiteDatabase db = (SQLiteDatabase)(database);
		for (Map.Entry<Long, IDbHandler> entry : handlers.entrySet()) {
			long appid = entry.getKey();
			IDbHandler handler = entry.getValue();
			String sql = handler.migrateDBSQL(appid, oldversion, appversion);
			if (sql != null && sql.length() > 5) {
				db.execSQL(sql);
			}
		}
	}

	protected boolean prepareDB() {
		Log.d("DB", "SqliteDbWorker::prepareDB");
		boolean ret = true;
		try {
			helper = new SqliteDbOpenHelper(appcontext, appversion, dbname, this);
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("DB", "SqliteDbWorker::prepareDB exception: " + e.getMessage());
			ret = false;
		}
		return ret;
	}
	
};



