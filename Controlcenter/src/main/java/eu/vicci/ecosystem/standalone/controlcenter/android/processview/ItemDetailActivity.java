package eu.vicci.ecosystem.standalone.controlcenter.android.processview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;

/**
 * An activity representing a single Item detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link ItemListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ItemDetailFragment}.
 */

//TODO: This activity should extend SmartCPSActivity.

public class ItemDetailActivity extends FragmentActivity {

//	private ProcessDatabaseHelper dbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.process_activity_item_detail);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(ItemDetailFragment.ARG_ITEM_ID, getIntent()
					.getStringExtra(ItemDetailFragment.ARG_ITEM_ID));
			ItemDetailFragment fragment = new ItemDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.item_detail_container, fragment).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this,
					new Intent(this, ItemListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
//	public void searchForProcess(View v) {
//		SearchView searchView = (SearchView) findViewById(R.id.item_detail_searchview);
//		String query = searchView.getQuery().toString();
//
//		// search implementation
//
//		dbHelper = new ProcessDatabaseHelper(this);
//
//		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
//				"select * from "+ProcessDatabaseHelper.TABLE_PROCESSES+" where name LIKE ?",
//				new String[]{"%"+query+"%"});
//		Log.i(Cursor.class.toString(), "query result length: " + cursor.getCount());
//		
//		MyCursorAdapter scAdapter = new MyCursorAdapter(this, cursor, 2);
//
//		final ListView listview = (ListView) findViewById(R.id.item_detail_listview_process);
//		listview.setAdapter(scAdapter);
//		dbHelper.close();
//		
//		
//		Cursor cursorIns = dbHelper.getReadableDatabase().rawQuery(
//				"select * from "+ProcessDatabaseHelper.TABLE_INSTANCES+" where name LIKE ?",
//				new String[]{"%"+query+"%"});
//		Log.i(Cursor.class.toString(), "query result length: " + cursorIns.getCount());
//		
//		MyCursorAdapter scAdapterIns = new MyCursorAdapter(this, cursorIns, 2);
//
//		final ListView listviewIns = (ListView) findViewById(R.id.item_detail_listview_process_instance);
//		
//		listviewIns.setAdapter(scAdapterIns);
//		dbHelper.close();
//		
//		listviewIns.setOnItemLongClickListener(new OnItemLongClickListener() {
//			@Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view,
//                    int pos, long id) {
//
//				TextView selectIdView = (TextView) parent
//						.findViewById(R.id.item_detail_id);
//				String selectId = selectIdView
//						.getText()
//						.toString();
//				Toast.makeText(view.getContext(), "clicked!", Toast.LENGTH_SHORT)
//						.show();
//				Intent intent = new Intent(view.getContext(),
//						SequenceActivity.class);
//				intent.putExtra(ItemDetailFragment.EXTRA_MESSAGE_PROCESS_ID, selectId);
//				startActivity(intent);
//
//                return true;
//            }
//        }); 
//		
//	}
//	
//	private static final int FILE_SELECT_CODE = 0;
//
//	public void showFileChooser() {
//		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//		intent.setType("text/xml");
//		intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//		try {
//			startActivityForResult(
//					Intent.createChooser(intent, "Select a File to Upload"),
//					FILE_SELECT_CODE);
//		} catch (android.content.ActivityNotFoundException ex) {
//			// Potentially direct the user to the Market with a Dialog
//			Toast.makeText(this, "Please install a File Manager.",
//					Toast.LENGTH_SHORT).show();
//		}
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		switch (requestCode) {
//		case FILE_SELECT_CODE:
//			if (resultCode == RESULT_OK) {
//				// Get the Uri of the selected file
//				Uri uri = data.getData();
//
//				String path = uri.getPath();
//
//				((TextView) findViewById(R.id.item_detail)).setText(path);
//				ProcessDeployManager.getInstance().uploadModel(path);
//				Toast.makeText(getApplicationContext(), "Model Uploaded",
//						Toast.LENGTH_LONG);
//				// Get the file instance
//				// File file = new File(path);
//				// Initiate the upload
//			}
//			break;
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}
	
}
