package com.example.kollby;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordAdaptor extends ArrayAdapter<Record> {

	public RecordAdaptor(Context context, int listRow, ArrayList<Record> records) {
		super(context,listRow , records);
	
		// TODO Auto-generated constructor stub
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		Record r = getItem(position);
		 if (convertView == null) {
			 LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             convertView = vi.inflate(R.layout.list_row, null);
	       }
		 TextView caller = (TextView) convertView.findViewById(R.id.caller);
		 TextView callTime = (TextView) convertView.findViewById(R.id.calltime);
		 TextView callDur = (TextView) convertView.findViewById(R.id.callDuration);
		 ImageView typeImg= (ImageView) convertView.findViewById(R.id.typeImg);
		 //TextView caller = (TextView) convertView.findViewById(R.id.caller);
	     if(r.name==null)
	    	 caller.setText(r.number);
	     else
	    	 caller.setText(r.name);
	     if(r.type.equals("in"))
	    	 typeImg.setImageResource(R.drawable.in);
	     else
	    	 typeImg.setImageResource(R.drawable.out);
	    	 
	     callTime.setText(new SimpleDateFormat("h:m a").format(r.date));
	     callDur.setText(r.callDur);
		
		 return convertView;
		 
	}
}
