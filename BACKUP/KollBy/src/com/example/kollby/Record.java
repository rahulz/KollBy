package com.example.kollby;

import java.io.File;
import java.util.Date;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

public class Record {
	File file;
	String number;
	String name;
	String type;
	Date date;
	String callDur="";
	
	public Record(File file) {
		String s[]=file.getName().split("_");					
		this.number=s[0];
		this.type=s[1];
		this.date=new Date(Integer.parseInt(s[2]));
		int ct=Integer.parseInt(s[3].split("\\.")[0]);
		this.callDur=convertSecToStr(ct);
	}
	private String convertSecToStr(int ct) {
		String str="";
		int hr = ct/3600;
		int rem = ct%3600;
		int mn = rem/60;
		int sec = rem%60;
		String hrStr = (hr<10 ? "0" : "")+hr;
		String mnStr = (mn<10 ? "0" : "")+mn;
		String secStr = (sec<10 ? "0" : "")+sec;
		if(!hrStr.equals("00"))
			str=hrStr+":";
		str+=mnStr+":"+secStr;
		return str;
	}
	
	public static String getContactName(Context context, String phoneNumber) {
	    ContentResolver cr = context.getContentResolver();
	    Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
	    Cursor cursor = cr.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
	    if (cursor == null) {
	        return null;
	    }
	    String contactName = null;
	    if(cursor.moveToFirst()) {
	        contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
	    }

	    if(cursor != null && !cursor.isClosed()) {
	        cursor.close();
	    }

	    return contactName;
	}
}
