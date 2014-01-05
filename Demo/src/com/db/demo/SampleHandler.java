package com.db.demo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.repository.dbservice.DbReqBase;
import com.repository.dbservice.DbRspBase;
import com.repository.dbservice.IDbHandler;


class SampleHandler implements IDbHandler {

	@Override
	public String initDBSQL() {
		// TODO Auto-generated method stub
		String sql = "create table sample (msg TEXT NOT NULL)";
		return sql;
	}

	@Override
	public String migrateDBSQL(long appid, int currentversion, int regversion) {
		// TODO Auto-generated method stub
		return null;
	}

	private DbRspBase handleInsertReq(DbReqBase request, SQLiteDatabase db){
		SampleInsertReq req = (SampleInsertReq)request;
		ContentValues value = new ContentValues();
		value.put("msg", req.msg);
		DbRspBase rsp = new DbRspBase();
		rsp.resultCode = db.insert("sample", null, value);
		return rsp;
	}
	
	
	@Override
	public DbRspBase handle(DbReqBase req, Object database) {
		// TODO Auto-generated method stub
		if (req == null || database == null)
			return null;
		
		SQLiteDatabase db = (SQLiteDatabase) (database);
		if (req.cmd == SampleCmd.kCmdInsert){
			return this.handleInsertReq(req, db);
		}
		
		return null;
	}
	
}

