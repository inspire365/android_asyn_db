package com.db.demo;

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

	@Override
	public DbRspBase handle(DbReqBase req, Object database) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

