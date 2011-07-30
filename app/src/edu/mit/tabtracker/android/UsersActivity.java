package edu.mit.tabtracker.android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class UsersActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		
	    super.onCreate(savedInstanceState);
		
		TableLayout tl=new TableLayout(this);
		
		tl.setColumnStretchable(1, true);
		
			//get and display name
		
		readWebpage(tl);
			
		//render
		setContentView(tl);
		
		//View v = findViewById(R.id.main);
		
	    //setContentView(R.layout.main);
	}

	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
		
		private TableLayout tableLayout;
		private Context context;
		
		public DownloadWebPageTask(TableLayout tableLayout, Context context)
		{
			this.tableLayout = tableLayout;
			this.context = context;
		}
		
		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			for (String url : urls) {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse execute = client.execute(httpGet);
					InputStream content = execute.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.i("here", result);
			//parse
			String[] pairs = result.split(" ");
			
			//categorize friends (oweYou, youOwe, none)
			ArrayList<String[]> oweYouList = new ArrayList<String[]>();
			ArrayList<String[]> youOweList = new ArrayList<String[]>();
			
			for (int i = 0; i < pairs.length; i++)
			{
				//index 0 = userID
				//index 1 = owed amount
				
				String[] pair = pairs[i].split(",");
				
/*				if (pair[1] == "0")
					continue;
				*/
				
				//owes you
				if (Integer.parseInt(pair[1]) >= 0)
				{
					
					oweYouList.add(pair);
				}
				else if (Integer.parseInt(pair[1]) < 0)
					youOweList.add(pair);
				
			}
			
			/** populate "owes you" */
			//header "you owes"
			tableLayout.addView(makeHeader("You owe", context));
			
			//populate "you owes" by row
			populate(oweYouList, tableLayout, context);
			
			//header "owes you"
			tableLayout.addView(makeHeader("Owes you", context));
			
			//populate "owes you" by row
			populate(youOweList, tableLayout, context);
			
			//setContentView(tableLayout);
		}
	}

	public void readWebpage(TableLayout tableLayout) {
		DownloadWebPageTask task = new DownloadWebPageTask(tableLayout, this);
		String fbID = "5";
		task.execute(new String[] { "http://tabtracker.zurias.com/tabtrack/getuserdata/" + fbID});
		
	}
	
	private TableRow makeHeader(String header, Context context)
	{
		TableRow headerRow = new TableRow(context);
		TextView headerText = new TextView(context);
		headerText .setTextSize(20);
		headerText .setText(header);
		headerText .setTypeface(null, Typeface.BOLD);
		headerRow.addView(headerText);
		return headerRow;
	}
	
	private void populate(ArrayList<String[]> list, TableLayout tableLayout, Context context) 
	{
		for (int i = 0; i < list.size(); i++)
		{
			
			TableRow tr = new TableRow(context);
			
			//name (fbID)
			TextView nameTV = new TextView(context);
			nameTV.setText(list.get(i)[0]);
			nameTV.setGravity(Gravity.LEFT);
			nameTV.setPadding(2, 1, 2, 1);
			tr.addView(nameTV);
			
			//cost
			TextView costTV = new TextView(context);
			costTV.setText(list.get(i)[1]);
			costTV.setGravity(Gravity.CENTER);
			tr.addView(costTV);
			
			//add details button
			Button btnDetails=new Button(context);
			btnDetails.setText("Details");
			btnDetails.setId(0);
			btnDetails.setOnClickListener( new OnClickListener()
			{
				public void onClick(View v)
				{
					//bring up details page
				}
				
				
			});
			tr.addView(btnDetails);  
			
			//add clear button
			Button btnClear=new Button(context);
			btnClear.setText("Pay");
			btnClear.setId(0);
			btnClear.setOnClickListener( new OnClickListener()
			{
				public void onClick(View v)
				{
					//clear owed amount
				}
				
			});
			
			tr.addView(btnClear);  
			
			//add each row
			tableLayout.addView(tr);
		}

	}
	
	
}