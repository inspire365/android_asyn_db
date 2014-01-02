package com.db.demo;

import java.util.ArrayList;
import java.util.List;

import com.repository.dbservice.DbRspBase;
import com.repository.dbservice.IDbObserver;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class SimpleListAdapter extends BaseAdapter implements IDbObserver {

	Context context;
	List<String> lst = new ArrayList<String>();
	
	public SimpleListAdapter(Context ct){
		context =ct;
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
	
	
	public void AddTextItem(String text){
		if (text != null){
			lst.add(text);
			this.notifyDataSetChanged();
		}
	}

	@Override
	public void Notify(DbRspBase rsp) {
		// TODO Auto-generated method stub
		
	}
	
}

