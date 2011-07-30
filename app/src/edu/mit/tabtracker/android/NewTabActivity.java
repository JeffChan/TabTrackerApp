/**
 * 
 */
package edu.mit.tabtracker.android;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author Mark Yen <markyen@mit.edu>
 *
 */
public class NewTabActivity extends Activity {
    private static final int REQUEST_CODE_PICK_CONTACT = 0;

    LayoutInflater mLayoutInflater;
    private ListView mList;
    private int mEditLocation;
    private ArrayAdapter<Item> mListAdapter;
    BigDecimal mTotal = new BigDecimal(0);
    
    private class ItemAdapter extends ArrayAdapter<Item>{
        private List<Item> items;

        public ItemAdapter(List<Item> items) {
            super(NewTabActivity.this, R.layout.new_tab, items);
            this.items = items;
        }

        class ViewHolder {
            public TextView payerView;
            public TextView costView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            View rowView = convertView;
            final Item item = items.get(position);
            if (!item.total) {
                if (rowView == null) {
                    LayoutInflater inflater = mLayoutInflater;
                    if (item.editable) {
                        rowView = inflater.inflate(R.layout.edit_item, null, true);
                    } else {
                        rowView = inflater.inflate(R.layout.view_item, null, true);
                    }
                    holder = new ViewHolder();
                    holder.payerView = (TextView) rowView.findViewById(R.id.payer);
                    holder.costView = (TextView) rowView.findViewById(R.id.cost);
                    rowView.setTag(holder);
                    holder.payerView.setText(item.payer);
                    holder.costView.setText(item.cost);
                } else if (rowView.getTag() != null) {
                    holder = (ViewHolder) rowView.getTag();
                    holder.payerView.setText(item.payer);
                    holder.costView.setText(item.cost);
                }
            } else {
                rowView = mLayoutInflater.inflate(R.layout.view_item, null, true);
                rowView.setOnClickListener(null);
                ((TextView) rowView.findViewById(R.id.payer)).setText("TOTAL:");
                ((TextView) rowView.findViewById(R.id.cost)).setText(mTotal.toPlainString());
            }
            return rowView;
            
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_tab);
        mLayoutInflater = (LayoutInflater) getLayoutInflater();
        mEditLocation = 0;
        mListAdapter = new ItemAdapter(new ArrayList<Item>(Arrays.asList(new Item[] { new Item(), new Item(true) })));
        final ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(mListAdapter);
        list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item prevEdit = mListAdapter.getItem(mEditLocation);
                if (TextUtils.isEmpty(prevEdit.payer) || TextUtils.isEmpty(prevEdit.cost)) {
                    mListAdapter.remove(prevEdit);
                } else {
                    prevEdit.editable = false;
                }
                mListAdapter.getItem(position).editable = true;
                mListAdapter.notifyDataSetChanged();
            }
        });
    }

    public void pickContactHandler(View target) {
        startActivityForResult(new Intent(this, ContactPickerActivity.class),
                REQUEST_CODE_PICK_CONTACT);
    }

    public void enterCostHandler(View target) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Yay").create().show();
    }

    public void newItemHandler(View target) {
        mListAdapter.getItem(mEditLocation).editable = false;
        mListAdapter.insert(new Item(), mEditLocation);
        mEditLocation = mListAdapter.getCount()-2;
        /*
        saveCurrentEdit();
        final TableRow newRow = (TableRow) mLayoutInflater.inflate(R.layout.edit_item, null);
        mEditLocation = mList.getChildCount()-3;
        mList.addView(newRow, mEditLocation);
        //*/
    }

//    public void editItemHandler(View target) {
//        saveCurrentEdit();
//        mEditLocation = mList.indexOfChild(target);
//        final CharSequence payer = ((TextView) target.findViewById(R.id.payer)).getText();
//        final CharSequence cost = ((TextView) target.findViewById(R.id.cost)).getText();
//        final TableRow edit = (TableRow) mLayoutInflater.inflate(R.layout.edit_item, null);
//        ((EditText) edit.findViewById(R.id.payer)).setText(payer);
//        ((EditText) edit.findViewById(R.id.cost)).setText(cost);
//        mList.removeViewAt(mEditLocation);
//        mList.addView(edit, mEditLocation);
//    }

//    private void saveCurrentEdit() {
//        final View edit = mList.getChildAt(mEditLocation);
//        final CharSequence payer = ((EditText) edit.findViewById(R.id.payer)).getText();
//        final CharSequence cost = ((EditText) edit.findViewById(R.id.cost)).getText();
//        if (!(TextUtils.isEmpty(payer) && TextUtils.isEmpty(cost))) {
//            final TableRow view = (TableRow) mLayoutInflater.inflate(R.layout.view_item, null);
//            ((TextView) view.findViewById(R.id.payer)).setText(payer);
//            ((TextView) view.findViewById(R.id.cost)).setText(cost);
//            final int index = mList.indexOfChild(edit);
//            mList.removeViewAt(index);
//            mList.addView(view, index);
//        } else {
//            mList.removeView(edit);
//        }
//    }

    /* (non-Javadoc)
     * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
     */
//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        Item prevEdit = mListAdapter.getItem(mEditLocation);
//        if (TextUtils.isEmpty(prevEdit.payer) || TextUtils.isEmpty(prevEdit.cost)) {
//            mListAdapter.remove(prevEdit);
//        } else {
//            prevEdit.editable = false;
//        }
//        mListAdapter.getItem(position).editable = true;
//        mListAdapter.notifyDataSetChanged();
//    }

    private static class Item {
        CharSequence payer = "";
        CharSequence cost = "";
        boolean editable = true;
        boolean total = false;
        public Item() { }
        public Item(boolean total) {
            this.total = total;
        }
    }
}
