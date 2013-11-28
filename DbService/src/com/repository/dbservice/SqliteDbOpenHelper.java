package com.repository.dbservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

public class SqliteDbOpenHelper extends SQLiteOpenHelper {
	
	SqliteDbWorker worker = null;

	public SqliteDbOpenHelper(Context context, int appversion, String dbname,
			SqliteDbWorker worker) {
		super(context, dbname, null, appversion);
		this.worker = worker;
		Log.d("DB", "SqliteDbOpenHelper::Ctor dbname: " + dbname + " version: " + appversion);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		Log.d("DB", "SqliteDbOpenHelper::OnCreate");
		worker.initDB(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("DB", "SqliteDbOpenHelper::onUpgrade oldversion: " + 
				String.valueOf(oldVersion) + " newVersion: " + String.valueOf(newVersion));
		if (worker != null) {
			worker.migrateDB(db, oldVersion, newVersion);
		}
	}

};
