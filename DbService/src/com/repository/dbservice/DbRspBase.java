package com.repository.dbservice;

// user extends this base class to provide more info
public class DbRspBase {
	public long appid;
	public long cmd;
	public long reqCode;
	public long resultCode; // operation result code
	public String info;
	// extends to implement others
};
