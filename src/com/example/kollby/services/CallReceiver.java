package com.example.kollby.services;

import com.example.kollby.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {

	public static int flag = 0;
	public static TelephonyManager telephony;
	public static String phoneNumber;
	public static String CALL_IN_OR_OUT;

	public static String valueOf(String obj) {
		return (obj == null) ? "null" : obj;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle bundle = intent.getExtras();

		if (bundle == null || MainActivity.flag == 1)
			return;

		MyPhoneStateListener phoneListener = new MyPhoneStateListener();

		telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
		String state = bundle.getString(TelephonyManager.EXTRA_STATE);
		Log.e("TELEMNGR:STATE", valueOf(state));
		Log.e("TELEMNGR:EXTPHNO",
				valueOf(bundle.getString(intent.EXTRA_PHONE_NUMBER)));
		Log.e("TELEMNGR:TN:EXTRA_INCOMING_NUMBER", valueOf(bundle
				.getString(TelephonyManager.EXTRA_INCOMING_NUMBER.toString())));

		if ((state != null)
				&& (state
						.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING))) {
			phoneNumber = bundle
					.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
			CALL_IN_OR_OUT = "in";

		}
		// Outgoing call
		else if (state == null) {
			phoneNumber = bundle.getString(Intent.EXTRA_PHONE_NUMBER);
			CALL_IN_OR_OUT = "out";
		}
		// phoneNumber is null sometimes
		if (phoneNumber == null) {
			phoneNumber = bundle.getString(Intent.EXTRA_PHONE_NUMBER);
			if (phoneNumber == null)
				phoneNumber = "NONUMBER";
			Log.e("NONUMBER", "NO NUMBER FOUND");
			Log.e("NONUMBER", phoneNumber.toString() + "::");
		}

	}

}
