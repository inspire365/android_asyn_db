package com.db.demo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.repository.dbservice.DbReqBase;
import com.repository.dbservice.DbRspBase;
import com.repository.dbservice.IDbHandler;


class SampleHandler implements IDbHandler {

	private static String kTable = "sample";
	
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
		// insert will return row number if successful, otherwise will return -1
		rsp.resultCode = db.insert(kTable, null, value);
		return rsp;
	}
	
	private DbRspBase handleQueryReq(DbReqBase request, SQLiteDatabase db){
		
		SampleQueryReq req = (SampleQueryReq)request;
		// that's a hidden row auto increment rowid in sqlite
		String[] projection = {"rowid", "msg"};
		String limit = " " + String.valueOf(req.offset) + " , " + String.valueOf(req.limit);
		Cursor cursor = db.query(kTable, projection, null, null, null, null, null, limit);
		if (cursor != null) {
			cursor.moveToFirst();
			SampleQueryRsp rsp = new SampleQueryRsp();
            while (!cursor.isAfterLast()) {
            	//int id = cursor.getInt(cursor.getColumnIndex("rowid"));
            	String msg = cursor.getString(cursor.getColumnIndex("msg"));
            	rsp.msgs.add(msg);
            	
            	cursor.moveToNext();
            }
            cursor.close();
            return rsp;
		}
		return null;
	}
	
	@Override
	public DbRspBase handle(DbReqBase req, Object database) {
		// TODO Auto-generated method stub
		if (req == null || database == null)
			return null;
		
		SQLiteDatabase db = (SQLiteDatabase) (database);
		if (req.cmd == SampleCmd.kCmdInsert){
			return this.handleInsertReq(req, db);
		} else if (req.cmd == SampleCmd.kCmdQuery){
			return this.handleQueryReq(req, db);
		}
		
		return null;
	}
	
}

