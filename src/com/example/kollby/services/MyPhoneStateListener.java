package com.example.kollby.services;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyPhoneStateListener extends PhoneStateListener {

	// private static String time;
	public static String phoneNo;
	MyCallRecorder callRecorder = new MyCallRecorder();

	public void onCallStateChanged(int state, String incomingNumber) {
		switch (state) {
		case TelephonyManager.CALL_STATE_IDLE:
			callRecorder.stopRecord();
			Log.e("State", "Idle");
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			callRecorder.recordCall();
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			// Log.e("DEBUG", "RINGING");
			// Log.e("inc", incomingNumber);
			phoneNo = incomingNumber;
			break;
		}
	}
}