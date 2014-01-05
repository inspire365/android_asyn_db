package com.repository.dbservice;

public interface IDbObserver {
	/**
	 * CallBack to notify DB operation
	 * 
	 * @param rsp  DB operation response return from IDbHandler
	 */
	public abstract void notify(DbRspBase rsp);

}
