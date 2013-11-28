package com.repository.dbservice;


public class DbReqBase {
	
	public final static long DB_REQ_PRIORITY_LOW    = 1;
	public final static long DB_REQ_PRIORITY_NORMAL = 2;
	public final static long DB_REQ_PRIORITY_HIGH   = 3;
	
	public long appid;
	public long priority;
	public long cmd;
	public long reqCode;

	public DbReqBase() {
		priority = DbReqBase.DB_REQ_PRIORITY_NORMAL;
		// leave empty
	}

	public DbReqBase(long appid, long cmd, long reqCode) {
		this.appid = appid;
		this.cmd = cmd;
		this.reqCode = reqCode;
		this.priority = DbReqBase.DB_REQ_PRIORITY_NORMAL;
	}
};
