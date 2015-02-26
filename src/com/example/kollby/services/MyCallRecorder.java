package com.example.kollby.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import com.example.kollby.MainActivity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class MyCallRecorder {

	private MediaRecorder recorder;
	private String file;

	public void recordCall() {
		if (MainActivity.flag == 0) {
			Log.i("RECORDING", "STARTED");
			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			file = getFilename();
			recorder.setOutputFile(file);
			recorder.setOnErrorListener(errorListener);
			recorder.setOnInfoListener(infoListener);
			try {
				recorder.prepare();
				recorder.start();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MainActivity.flag = 1;
		}

		Log.i("DEBUG", "OFFHOOK");

	}

	public void stopRecord() {
		if (recorder != null) {
			try {
				Log.i("Recording", "Stoped");
				recorder.stop();
			} catch (IllegalStateException e) {
				e.printStackTrace();
				// TODO: handle exception
			}

			recorder.reset();
			recorder.release();
			recorder = null;
			MediaPlayer mp = new MediaPlayer();
			try {
				FileInputStream stream = new FileInputStream(file);
				mp.setDataSource(stream.getFD());
				stream.close();
				mp.prepare();
				long duration = mp.getDuration();
				mp.release();
				mp = null;
				File from = new File(file);
				File to = new File(file + duration + ".amr");
				from.renameTo(to);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			MainActivity.flag = 0;
		}

	}

	private String getFilename() {
		String filepath = Environment.getExternalStorageDirectory().getPath()
				+ "/.kolby/";
		File file = new File(filepath);
		long time = new Date().getTime();
		if (!file.exists()) {
			file.mkdirs();
		}
		String phoneNo = CallReceiver.phoneNumber;
		if (phoneNo == null)
			Log.e("ERROR", "No phone number");
		filepath += phoneNo + "_" + CallReceiver.CALL_IN_OR_OUT + "_" + time
				+ "_";
		Log.e("FILEPATH", phoneNo);
		return filepath;
	}

	private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
		public void onError(MediaRecorder mr, int what, int extra) {
			// AppLog.logString("Error: " + what + ", " + extra);
		}
	};

	private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
		public void onInfo(MediaRecorder mr, int what, int extra) {
			// AppLog.logString("Warning: " + what + ", " + extra);
		}
	};

}
