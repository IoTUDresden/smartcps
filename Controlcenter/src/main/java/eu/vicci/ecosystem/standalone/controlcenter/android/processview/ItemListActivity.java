package eu.vicci.ecosystem.standalone.controlcenter.android.processview;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.SequenceActivity;
import eu.vicci.process.client.android.ObservableSession;
import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.kpseus.connect.handlers.UploadModelHandler;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ItemDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details (if present) is a
 * {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required {@link ItemListFragment.Callbacks}
 * interface to listen for item selections.
 */

//TODO: I am not sure if this activity is necessary at all. You should start with the process overview.
//TODO: This activity should extend SmartCPSActivity.

public class ItemListActivity extends FragmentActivity implements
		ItemListFragment.Callbacks, Observer {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	private ProcessDatabaseHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.process_activity_item_list);

		/*
		 * Process p = Example.getExampleProcess();
		 * Toast.makeText(getApplicationContext(), "Process name: "+p.getName(),
		 * Toast.LENGTH_LONG).show(); ProcessInstance pi =
		 * Example.getExampleProcessInstance(p);
		 * Toast.makeText(getApplicationContext(),
		 * "ProcessInstance id: "+pi.getInstanceId(), Toast.LENGTH_LONG).show();
		 */
		
		ProcessEngineClient pec = ProcessEngineClient.getInstance();
		pec.getOs().addObserver(this);
		
		// if (pec.connect(ip, "8081")){
		// DatabaseDummy.getInstance().init();
		// }
		// ProcessDeployManager.getInstance().getProcessIds(TestCases.getIds());
		// ProcessDeployManager.getInstance().deployProcess("_DbZrsJu9EeOut92bGX6EeB",
		// TestCases.getProcess());

		if (findViewById(R.id.item_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ItemListFragment) getSupportFragmentManager().findFragmentById(
					R.id.item_list)).setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(ItemDetailFragment.ARG_ITEM_ID, id);
			ItemDetailFragment fragment = new ItemDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ItemDetailActivity.class);
			detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		
		if (observable instanceof ObservableSession) {
			boolean connectionState = ProcessEngineClient.getInstance().isConnected();
			if (connectionState) {
				Context context = getApplicationContext();
				CharSequence text = "Connected to Process Engine";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			} else if (connectionState) {
				Context context = getApplicationContext();
				CharSequence text = "Not connected to Process Engine";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		}
		
	}


	public void searchForProcess(View v) {
		SearchView searchView = (SearchView) findViewById(R.id.item_detail_searchview);
		String query = searchView.getQuery().toString();
		// search implementation

		dbHelper = new ProcessDatabaseHelper(this);

		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
				"select * from "+ProcessDatabaseHelper.TABLE_PROCESSES+" where name LIKE ?",
				new String[]{"%"+query+"%"});
		Log.i(Cursor.class.toString(), "query result length: " + cursor.getCount());
		
		MyCursorAdapter scAdapter = new MyCursorAdapter(this, cursor, 2, false);

		final ListView listview = (ListView) findViewById(R.id.item_detail_listview_process);
		listview.setAdapter(scAdapter);
		dbHelper.close();
		
		
		Cursor cursorIns = dbHelper.getReadableDatabase().rawQuery(
				"select * from "+ProcessDatabaseHelper.TABLE_INSTANCES+" where name LIKE ?",
				new String[]{"%"+query+"%"});
		Log.i(Cursor.class.toString(), "query result length: " + cursorIns.getCount());
		
		MyCursorAdapter scAdapterIns = new MyCursorAdapter(this, cursorIns, 2, true);

		final ListView listviewIns = (ListView) findViewById(R.id.item_detail_listview_process_instance);
		
		listviewIns.setAdapter(scAdapterIns);
		dbHelper.close();
		
		listviewIns.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int pos, long id) {

				TextView selectIdView = (TextView) parent
						.findViewById(R.id.item_detail_id);
				String selectId = selectIdView
						.getText()
						.toString();
				Toast.makeText(view.getContext(), "clicked1!", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(view.getContext(),
						SequenceActivity.class);
				intent.putExtra(ItemDetailFragment.EXTRA_MESSAGE_MODEL_ID, selectId);
				startActivity(intent);
				return true;
            }
        }); 
		
	}
	
	private static final int FILE_SELECT_CODE = 0;

	public void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("text/xml");
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		try {
			startActivityForResult(
					Intent.createChooser(intent, "Select a File to Upload"),
					FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			// Potentially direct the user to the Market with a Dialog
			Toast.makeText(this, "Please install a File Manager.",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case FILE_SELECT_CODE:
			if (resultCode == RESULT_OK) {
				// Get the Uri of the selected file
				Uri uri = data.getData();

				String path = uri.getPath();

				((TextView) findViewById(R.id.item_detail)).setText(path);
				UploadModelHandler umh = new UploadModelHandler();
				ProcessEngineClient.getInstance().uploadProcessDefinition(path, umh);
				Toast.makeText(getApplicationContext(), "Model Uploaded",
						Toast.LENGTH_LONG);
				// Get the file instance
				// File file = new File(path);
				// Initiate the upload
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}