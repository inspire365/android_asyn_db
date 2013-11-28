package com.repository.dbservice;


import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import android.util.Log;

public class CipherDbOpenHelper extends SQLiteOpenHelper {
	CipherDbWorker worker = null;

	public CipherDbOpenHelper(Context context, int appversion, String dbname,
			CipherDbWorker worker) {
		super(context, dbname, null, appversion);
		this.worker = worker;
		Log.d("DB", "CipherDbOpenHelper::Ctor dbname: " + dbname + " version: " + appversion);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		Log.d("DB", "CipherDbOpenHelper::OnCreate");
		worker.InitDB(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("DB", "CipherDbOpenHelper::onUpgrade oldversion: " + 
				String.valueOf(oldVersion) + " newVersion: " + String.valueOf(newVersion));
		if (worker != null) {
			worker.MigrateDB(db, oldVersion, newVersion);
		}
	}

};
