package com.example.kollby;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TreeSet;

import com.example.kollby.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordsAdapter2 extends BaseAdapter implements Filterable {
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
	private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
	ArrayList<Record> mStringFilterList;

	public ArrayList<Record> mData = new ArrayList<Record>();
	private LayoutInflater mInflater;

	private TreeSet mSeparatorsSet = new TreeSet();

	public RecordsAdapter2(Context context) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void addItem(final Record item) {
		mData.add(item);
		notifyDataSetChanged();
	}

	public void addSeparatorItem(final Record item) {
		mData.add(item);
		// save separator position
		mSeparatorsSet.add(mData.size() - 1);

		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Record getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		int type = getItemViewType(position);
		Record r = mData.get(position);
		System.out.println("getView " + position + " " + convertView
				+ " type = " + type);
		ViewHolderRecord holder;
		ViewHolderSep holder2;
		switch (type) {
		case TYPE_ITEM:
			if (convertView == null) {
				holder = new ViewHolderRecord();
				convertView = mInflater.inflate(R.layout.list_row, null);
			} else {
				holder = (ViewHolderRecord) convertView.getTag();
			}

			holder.callDur = (TextView) convertView
					.findViewById(R.id.callDuration);
			holder.callerImg = (ImageView) convertView
					.findViewById(R.id.callerImg);
			holder.callTime = (TextView) convertView
					.findViewById(R.id.calltime);
			holder.caller = (TextView) convertView.findViewById(R.id.caller);
			holder.caller.setTag(r);
			holder.typeImg = (ImageView) convertView.findViewById(R.id.typeImg);
			if (r.callerImg == null) {
				Log.e("CALLER IMAGE", "NOT EXISTS");
				holder.callerImg.setImageResource(R.drawable.nopic);
			} else {
				Log.e("CALLER IMAGE", "EXISTS");
				holder.callerImg.setImageBitmap(r.callerImg);
			}
			holder.callDur.setText(r.callDur);
			if (r.name == null) {
				holder.caller.setText(r.number);
			} else {
				holder.caller.setText(r.name);
			}
			if (r.type.equals("in")) {
				holder.typeImg.setImageResource(R.drawable.in);
			} else {
				holder.typeImg.setImageResource(R.drawable.out);
			}
			holder.callTime.setText(new SimpleDateFormat("h:m a")
					.format(r.date));
			convertView.setTag(holder);

			break;
		case TYPE_SEPARATOR:
			if (convertView == null) {
				holder2 = new ViewHolderSep();
				convertView = mInflater.inflate(R.layout.day_separator, null);
			} else {
				holder2 = (ViewHolderSep) convertView.getTag();
			}
			holder2.sepStr = (TextView) convertView.findViewById(R.id.dayText);
			holder2.sepStr.setText(r.sepStr);
			convertView.setTag(holder2);
			break;
		}
		return convertView;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}
}

class ViewHolderSep {
	public TextView sepStr;
}

class ViewHolderRecord {
	public TextView caller;
	public ImageView callerImg;
	public ImageView typeImg;
	public TextView callDur;
	public TextView callTime;
}
