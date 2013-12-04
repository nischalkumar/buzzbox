package com.elevenestates.buzzbox;

import java.util.LinkedList;
import java.util.List;



import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Contact extends ListActivity {
	ContactsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		ListContactTask ltask= new ListContactTask(getBaseContext());
		ListView lview=getListView();
		ltask.execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact, menu);
		return true;
	}
	
	
	private class ContactsAdapter extends ArrayAdapter<ContactItem> {
		private final Context mcontext;
		private List<ContactItem> list=null;
		//private final Activity context;
		public ContactsAdapter(Context context, int resource,
				List<ContactItem> objects) {
			super(context, resource, objects);
			this.mcontext=context;
			this.list=list;
			
			// TODO Auto-generated constructor stub
		}

		class ViewHolder {
	        TextView key;
	        TextView value;
		}
		
		
		@Override
		  public View getView(int position, View convertView, ViewGroup parent) {
			Log.d("adapter","inside getview");
			View view = null;
			  LayoutInflater inflator = ((Activity) mcontext).getLayoutInflater();
			  view = inflator.inflate(R.layout.rowlayout, null);
			  final ViewHolder viewHolder = new ViewHolder();
			  viewHolder.key = (TextView) view.findViewById(R.id.contact_name);
			  viewHolder.value = (TextView) view.findViewById(R.id.contact_no);
			  Log.d("adapter",viewHolder.key+" "+viewHolder.value);
			  ContactItem pair = (ContactItem) getItem(position);
			//ViewHolder holder = (ViewHolder) view.getTag();
			viewHolder.key.setText(pair.mDisplayName);
			viewHolder.value.setText(pair.mPhone);
			//return convertView;
			return view;

		} 
		
		
	}

	
	
	private class ListContactTask extends AsyncTask<Void, Void, List<ContactItem>> {

		private Context mContext;
	    public ListContactTask (Context context){
	         mContext = context;
	    }
		@Override
		protected List<ContactItem> doInBackground(Void... params) {
			ProgressBar pbar= (ProgressBar) findViewById(R.id.progressBar1);
			pbar.setVisibility(View.VISIBLE);
			// TODO Auto-generated method stub
			Log.d("Asynctask", "start of doinback");
			Cursor c = mContext.getContentResolver().query(Data.CONTENT_URI,
			        new String[] {Data._ID, Data.DISPLAY_NAME,Phone.NUMBER, Data.CONTACT_ID,Phone.TYPE, Phone.LABEL},
			                      Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'",
			                      null, 
			                      Data.DISPLAY_NAME);
			    
			Log.d("Asynctask", "cursor extraction success");
			    int count = c.getCount();
			    boolean b = c.moveToFirst();
			    String[] columnNames = c.getColumnNames();
			    int displayNameColIndex = c.getColumnIndex("display_name");
			    int idColIndex = c.getColumnIndex("_id");
			    int col2Index = c.getColumnIndex(columnNames[2]);
			    int col3Index = c.getColumnIndex(columnNames[3]);
			    int col4Index = c.getColumnIndex(columnNames[4]);
			                
			    List<ContactItem> contactItemList = new LinkedList<ContactItem>();
			    for(int i = 0; i < count ; i ++) {
			        String displayName = c.getString(displayNameColIndex);
			        String phoneNumber = c.getString(col2Index);
			        int contactId = c.getInt(col3Index);
			        String phoneType = c.getString(col4Index);
			       //Log.d("Contact",displayName+" "+phoneNumber);
			        long _id = c.getLong(idColIndex);
			        ContactItem contactItem = new ContactItem();
			        contactItem.mId= _id;
			        contactItem.mContactId = contactId;
			        contactItem.mDisplayName = displayName;
			        contactItem.mPhone = phoneNumber;
			        contactItemList.add(contactItem);
			        boolean b2 = c.moveToNext();
			    }
			    c.close();
			    Log.d("Async","parsed contact");
			    return contactItemList;
		}
		
		@Override
		protected void onPostExecute(List<ContactItem> result) { 
			ProgressBar pbar= (ProgressBar) findViewById(R.id.progressBar1);
			pbar.setVisibility(View.GONE);
			ListView lview=getListView();
			adapter=new ContactsAdapter(Contact.this,0, result);
			lview.setAdapter(adapter);
		}
	}


}
