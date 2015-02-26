package com.example.kollby;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private String dir = "";
	private RecordsAdapter2 mAdapter;
	BroadcastReceiver mReceiver;
	public static TelephonyManager telephony;
	private String filter = null;
	private View activeView;
	private boolean doubleBackToExitPressedOnce = false;
	public static int flag = 0;
	public static Boolean newRec = false;

	@Override
	protected void onResume() {
		super.onResume();
		ShowRecordings();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PreferenceManager.setDefaultValues(this, R.xml.pref, false);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		// SharedPreferences sharedPref =
		// PreferenceManager.getDefaultSharedPreferences(this);
		// String dc= sharedPref.getString("saveto", "N");
		ShowRecordings();
	}

	// Row OnClick Listener
	public void viewRec(View v) {
		activeView = v;
		TextView tv = (TextView) v.findViewById(R.id.caller);
		activeView.setBackgroundColor(Color.LTGRAY);
		final Record r = (Record) tv.getTag();
		PopupMenu popup = new PopupMenu(this, v);
		popup.getMenuInflater().inflate(R.menu.cntxt_menu, popup.getMenu());
		popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
			@Override
			public void onDismiss(PopupMenu arg0) {
				activeView.setBackgroundColor(Color.TRANSPARENT);

			}
		});
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.share:
					Intent share = new Intent(Intent.ACTION_SEND);
					share.setType("audio/amr");
					share.putExtra(Intent.EXTRA_STREAM,
							Uri.parse(r.file.getAbsolutePath()));
					startActivity(Intent
							.createChooser(share, "Share Recording"));
					break;
				case R.id.play:
					Intent intent = new Intent();
					intent.setAction(android.content.Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(r.file), "audio/amr");
					startActivity(intent);
					break;
				case R.id.delete:
					new AlertDialog.Builder(MainActivity.this)
							.setTitle("Delete Recording?")
							.setMessage(
									"Do you really want to delete selected recording?")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton(android.R.string.yes,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											r.file.delete();
											ShowRecordings();
										}
									})
							.setNegativeButton(android.R.string.no, null)
							.show();
					break;
				case R.id.filter_number:
					if (!(filter == null)) {
						if (filter.contains("|"))
							filter = filter.split("|")[0];
						if (filter.equals("in") || filter.equals("out"))
							filter += "|" + r.callerLabel;
						else
							filter = r.callerLabel;
					} else
						filter = r.callerLabel;
					Log.e("LABLE", filter);
					ShowRecordings();
				default:
					break;
				}

				return true;
			}
		});

		popup.show();
	}

	private void ShowRecordings() {
		ListView listView = (ListView) findViewById(R.id.list);
		dir = Environment.getExternalStorageDirectory().getPath() + "/.kolby/";
		Log.e("DC", dir);
		File folder = new File(dir);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		ArrayList<Record> recordList = new ArrayList<Record>();
		Log.e("TOTAL RECORDS", "" + folder.listFiles().length + "");
		listView = (ListView) findViewById(R.id.list);
		mAdapter = new RecordsAdapter2(this);
		for (File f : folder.listFiles()) {
			if (f.isFile()) {
				try {
					Record r = new Record(f, this);
					if (filter == null)
						recordList.add(r);
					else {
						if (!filter.contains("|")) {
							if (filter.equals("in") || filter.equals("out")) {
								if (r.type.equals(filter))
									recordList.add(r);
							} else {
								Log.e("MM", r.callerLabel);
								if (r.callerLabel.equals(filter))
									recordList.add(r);
							}
						} else {
							if (r.type.equals(filter.split("|")[0])
									&& r.callerLabel
											.equals(filter.split("|")[1]))
								recordList.add(r);
						}
					}
				} catch (Exception e) {
					Log.e("INVALID FILE NAME", "" + e.toString());
				}
			}
		}
		if (recordList.size() == 0)
			listView.setVisibility(View.GONE);
		else
			listView.setVisibility(View.VISIBLE);
		Collections.sort(recordList, new RecordComparator());
		addSep(recordList);
		mAdapter.mData.clear();
		for (Record record : recordList) {
			if (record.isSep) {
				mAdapter.addSeparatorItem(record);
			} else {
				mAdapter.addItem(record);
			}
		}
		listView.setAdapter(mAdapter);
	}

	@SuppressWarnings("deprecation")
	private void addSep(ArrayList<Record> recordList) {
		Date d = new Date(0);
		for (int i = 0; i <= recordList.size() - 1; i++) {
			if (recordList.get(i).date != null)
				if (recordList.get(i).date.getDate() != d.getDate()) {
					d = recordList.get(i).date;
					String ptime = formatToYesterdayOrToday(recordList.get(i).date);
					Log.e("DATEEEEEEEEE", ptime);
					recordList.add(i, new Record(ptime));

				}
		}

	}

	public static String formatToYesterdayOrToday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Calendar today = Calendar.getInstance();
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DATE, -1);

		if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
				&& calendar.get(Calendar.DAY_OF_YEAR) == today
						.get(Calendar.DAY_OF_YEAR)) {
			return "Today "
					+ new SimpleDateFormat("EEE MMM d, yyyy").format(date);
		} else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR)
				&& calendar.get(Calendar.DAY_OF_YEAR) == yesterday
						.get(Calendar.DAY_OF_YEAR)) {
			return "Yesterday "
					+ new SimpleDateFormat("EEE MMM d, yyyy").format(date);
		} else {
			return new SimpleDateFormat("EEE MMM d, yyyy").format(date);
		}
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
		} else if (id == R.id.filter_in) {
			filter = "in";
			ShowRecordings();
			getSupportActionBar().setTitle("KollBy: " + "Incoming calls");
		} else if (id == R.id.filter_out) {
			filter = "out";
			ShowRecordings();
			getSupportActionBar().setTitle("KollBy: " + "Outgoing calls");
		} else if (id == R.id.filter_none) {
			filter = null;
			getSupportActionBar().setTitle("KollBy");
			ShowRecordings();
		} else if (id == R.id.action_showAll) {
			filter = null;
			getSupportActionBar().setTitle("Kollby");
			ShowRecordings();
		}
		return super.onOptionsItemSelected(item);
	}

	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}
		filter = null;
		getSupportActionBar().setTitle("Kollby");
		ShowRecordings();
		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Please click BACK again to exit",
				Toast.LENGTH_SHORT).show();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
	}
}

class RecordComparator implements Comparator<Record> {

	@Override
	public int compare(Record lhs, Record rhs) {
		return rhs.date.compareTo(lhs.date);
	}
}