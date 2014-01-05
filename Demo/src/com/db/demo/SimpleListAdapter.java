package com.db.demo;

import java.util.ArrayList;
import java.util.List;

import com.repository.dbservice.DbRspBase;
import com.repository.dbservice.DbService;
import com.repository.dbservice.DbServiceConst;
import com.repository.dbservice.IDbObserver;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class SimpleListAdapter extends BaseAdapter implements IDbObserver {

	private Context context;
	private List<String> lst = new ArrayList<String>();
	
	private int offset = 0;
	private int limit  = 10;
	
	private DbService dbService;
	
	public SimpleListAdapter(Context ct){
		context =ct;
	}
	
	
	public void setDbService(DbService dbs){
		dbService = dbs;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lst.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lst.get(arg0);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public View getView(int pos, View v, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (v == null){
			v = new TextView(context);
		}
		TextView tv = (TextView)v;
		tv.setText(lst.get(pos));
		return tv;
	}
	
	
	public void addTextItem(String text){
		if (text != null){
			lst.add(text);
			this.notifyDataSetChanged();
		}
	}
	
	public void addTextList(List<String> texts){
		for (String text : texts){
			lst.add(text);
		}
		this.notifyDataSetChanged();
	}

	public void clearData(){
		lst.clear();
		this.notifyDataSetChanged();
	}
	
	
	public void queryNextBatchMsg(){
		SampleQueryReq req = new SampleQueryReq();
		req.appid = DbServiceConst.kSampleAppId;
		req.cmd = SampleCmd.kCmdQuery;
		req.offset = this.offset;
		req.limit = this.limit;
		dbService.putDBReq(req);
		
	}
	
	void handleQueryRsp(DbRspBase rsp){
		if (rsp == null)
			return ;
		final SampleQueryRsp ret = (SampleQueryRsp)rsp;
		
		if (ret.msgs.size() > limit){
			offset = 0;   // reset
		} else {
			offset += limit;   // advance for next operation
		}
		
		
		Activity act = (Activity)this.context;
		act.runOnUiThread(new Runnable() {      // if your do any operation relative to UI
                                                // make sure run on UI thread, otherwise it 
			                                    // will cause an eror
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SimpleListAdapter.this.addTextList(ret.msgs);
			}
			
		});
		
		
	}
	
	@Override
	public void notify(DbRspBase rsp) {
		// TODO Auto-generated method stub
		if (rsp.cmd == SampleCmd.kCmdInsert){
			Log.d("DB", "SimpleListAdapter::notify insert result: " + String.valueOf(rsp.resultCode));
		} else if (rsp.cmd == SampleCmd.kCmdQuery) {
			this.handleQueryRsp(rsp);
		}
	}
	
}

