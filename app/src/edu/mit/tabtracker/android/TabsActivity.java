/**
 * 
 */
package edu.mit.tabtracker.android;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author
 *
 */
public class TabsActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String[] descriptions = new String[] {"Victor","Mark","Victor","Mark"};
		int[] amounts = new int[] { 230, 123, 240, 110};
		setListAdapter(new TabItemAdapter(this, descriptions, amounts));
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Object o = this.getListAdapter().getItem(position);
		String keyword = o.toString();
		Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_SHORT)
				.show();

	}

}
