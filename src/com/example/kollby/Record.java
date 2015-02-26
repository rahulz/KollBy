package com.example.kollby;

import java.io.File;
import java.util.Date;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;

public class Record {
	File file;
	String number;
	String name = null;
	String type;
	String callerLabel;
	Date date;
	String callDur = "";
	String sepStr = "";
	Boolean isSep = false;
	Bitmap callerImg;

	public Record(String sepStr) {
		this.sepStr = sepStr;
		this.isSep = true;
	}

	public Record(File file, Context context) {
		this.file = file;
		String s[] = file.getName().split("_");
		this.number = s[0];
		this.name = getContactName(context, number);
		if (this.name != null)
			this.callerLabel = this.name;
		else
			this.callerLabel = this.number;
		this.type = s[1];
		this.date = new Date(Long.parseLong(s[2]));
		long ct = Long.parseLong(s[3].split("\\.")[0]);
		this.callDur = convertSecToStr(ct);
		this.callerImg = new GetContactImage(context, number).getThumbnail();
	}

	private String convertSecToStr(long ct) {
		ct = ct / 1000;
		String str = "";
		long hr = ct / 3600;
		long rem = ct % 3600;
		long mn = rem / 60;
		long sec = rem % 60;
		String hrStr = (hr < 10 ? "0" : "") + hr;
		String mnStr = (mn < 10 ? "0" : "") + mn;
		String secStr = (sec < 10 ? "0" : "") + sec;
		if (!hrStr.equals("00"))
			str = hrStr + ":";
		str += mnStr + ":" + secStr;
		return str;
	}

	public static String getContactName(Context context, String phoneNumber) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		Cursor cursor = cr.query(uri,
				new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		if (cursor == null) {

			return null;
		}
		String contactName = null;
		if (cursor.moveToFirst()) {
			contactName = cursor.getString(cursor
					.getColumnIndex(PhoneLookup.DISPLAY_NAME));
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return contactName;
	}

}
