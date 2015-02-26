package com.example.kollby;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import android.support.v7.app.ActionBarActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;



public class MainActivity extends ActionBarActivity {
	private String dir="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.pref, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //String dc= sharedPref.getString("saveto", "N");
        dir = Environment.getExternalStorageDirectory().getPath()+ "/.kolby/";
        Log.e("DC",dir);
        File folder = new File(dir);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		ArrayList<Record> recordList = new ArrayList<Record>();
		Log.e("TOTAL RECORDS",""+folder.listFiles().length+"");
		for (File f : folder.listFiles()) {
			if (f.isFile()){
				try {
					Record r=new Record(f);
					recordList.add(r);
				} catch (Exception e) {
					Log.e("RECORD EROR",""+e.toString());
				}
			}
		}
		RecordAdaptor adapter = new RecordAdaptor(this,R.layout.list_row, recordList);
		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);

	}

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	Intent intent = new Intent(MainActivity.this,
        		      SettingsActivity.class);
        		      startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
