package com.repository.dbservice;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


public class DbReqQueue {
	
	private int total  = 0;
	private final int MAX_AVAILABLE = 100;
	private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);
    private final List<DbReqBase> low_tasks    = new ArrayList<DbReqBase>();
    private final List<DbReqBase> normal_tasks = new ArrayList<DbReqBase>();
	private final List<DbReqBase> high_tasks   = new ArrayList<DbReqBase>();
	
	// TODO: How to drop requests if too many?
	protected synchronized void enqueue(DbReqBase req){

	  if (req.priority == DbReqBase.DB_REQ_PRIORITY_HIGH){
		  high_tasks.add(req);
	  } else if (req.priority == DbReqBase.DB_REQ_PRIORITY_LOW){
		  low_tasks.add(req);
	  } else {
		  normal_tasks.add(req);
	  }
	  ++ total;
	}
	
	public void push(DbReqBase req){
      if (req == null)
        return ;
      
	  this.enqueue(req);
	  available.release();
	}
	
	public synchronized DbReqBase dequeue(){
		if (total < 1)
			return null;
		
		if (!high_tasks.isEmpty()){
			--total;
			return high_tasks.remove(0);
		}
		
		if (!normal_tasks.isEmpty()){
			--total;
			return normal_tasks.remove(0);
		}
		
		if (!low_tasks.isEmpty()){
			--total;
			return low_tasks.remove(0);
		}
		return null;
	}
	
	// MILLISECONDS
	public DbReqBase poll(long timeout){
	  try {
		available.tryAcquire(timeout, TimeUnit.MILLISECONDS);
	  } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  }
	  return dequeue();
	}

	public int size(){
		return total;
	}
	
};
