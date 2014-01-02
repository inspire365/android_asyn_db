package com.db.demo;

import com.repository.dbservice.DbService;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.repository.dbservice.DbServiceConst;

public class DemoActivity extends Activity {

	private ListView lsv;
	private Button   btn_load_db;
	private Button   btn_add_text;
	private Button   btn_clear;
	private EditText input_box;
	
	private SimpleListAdapter slAdapter;
	private DbService dbService;
	
	
	private void InitDbService(){
		dbService = new DbService();
		dbService.setupService(this, DbServiceConst.appversion, "sample.db", DbService.SQLITE_TYPE_PLAIN);
		dbService.registerHandler(DbServiceConst.kSampleAppId, new SampleHandler());
		dbService.registerObserver(DbServiceConst.kSampleAppId, slAdapter);
		dbService.startService();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo);
		lsv = (ListView)findViewById(R.id.text_list);
		slAdapter = new SimpleListAdapter(this);
		lsv.setAdapter(slAdapter);
		input_box = (EditText)findViewById(R.id.edit_box);
		btn_add_text= (Button)findViewById(R.id.btn_plus);
		btn_add_text.setOnClickListener(new OnClickListener(){
      
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String text = input_box.getText().toString();
				input_box.setText("");   // reset
				slAdapter.AddTextItem(text);
			}
			
		});
		btn_load_db=(Button)findViewById(R.id.btn_load_db);
		btn_clear=(Button)findViewById(R.id.btn_clear);
	    this.InitDbService();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.demo, menu);
		return true;
	}

}
