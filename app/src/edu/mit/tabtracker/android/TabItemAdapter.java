package edu.mit.tabtracker.android;

import java.math.BigDecimal;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TabItemAdapter extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] descriptions;
	private final int[] amounts;

	public TabItemAdapter(Activity context, String[] descriptions, int[] amounts) {
		super(context, R.layout.tabitemrow, descriptions);
		this.context = context;
		this.amounts = amounts;
		this.descriptions = descriptions;
	}
	
	static class ViewHolder {
		public TextView amountTextView;
		public TextView descriptionTextView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.tabitemrow, null, true);
			holder = new ViewHolder();
			holder.amountTextView = (TextView) rowView.findViewById(R.id.amount);
			holder.descriptionTextView = (TextView) rowView.findViewById(R.id.description);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		Log.i("app", String.valueOf(position));
		Log.i("app", String.valueOf(amounts[0]));
		Log.i("app", descriptions[0]);
		BigDecimal decimal = new BigDecimal(amounts[position]/100.0);
		holder.amountTextView.setText(String.valueOf(decimal.setScale(2, BigDecimal.ROUND_UP)));
		holder.descriptionTextView.setText(descriptions[position]);
		
		return rowView;
	}
}