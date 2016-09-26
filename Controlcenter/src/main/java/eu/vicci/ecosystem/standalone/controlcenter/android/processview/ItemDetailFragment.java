package eu.vicci.ecosystem.standalone.controlcenter.android.processview;

import java.util.List;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.SequenceActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.processview.dummy.DummyContent;
import eu.vicci.ecosystem.standalone.controlcenter.android.sequence.SequenceInstanceActivity;
import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.engine.core.IProcessInfo;
import eu.vicci.process.engine.core.IProcessInstanceInfo;
import eu.vicci.process.kpseus.connect.ServerConnector;
import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;
import eu.vicci.process.kpseus.connect.handlers.HandlerFinishedListener;
import eu.vicci.process.kpseus.connect.handlers.ProcessInstanceInfoListHandler;
import eu.vicci.process.kpseus.connect.handlers.ProcessListHandler;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class ItemDetailFragment extends Fragment implements HandlerFinishedListener {
	public static String EXTRA_MESSAGE_MODEL_ID = "eu.vicci.ecosystem.standalone.controlcenter.android.processview.MESSAGE";
	public static String EXTRA_MESSAGE_PROCESS_INSTANCEID = "eu.vicci.ecosystem.standalone.controlcenter.android.processview.INSTANCEID";
	public static final String EXTRA_MESSAGE_ACTIVE_ID = "activeInstanceId";
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyContent.DummyItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	
	private SQLiteDatabase database;
	private ProcessDatabaseHelper dbHelper;
	
	private View rootView;
	
	public ItemDetailFragment() {
//		dbHelper = new ProcessDatabaseHelper(this.
//				getActivity());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mItem.content == "Overview") {
			rootView = inflater.inflate(R.layout.process_fragment_item_detail_overview,
					container, false);
			
			ProcessEngineClient pec = ServerConnector.getInstance().getProcessEngineClient();
			
			if (pec.isConnected()) {
				
//				DatabaseDummy.getInstance().init();
							
				ProcessListHandler plh = new ProcessListHandler();
				plh.addHandlerFinishedListener(this);
				pec.listDeployedProcesses(plh);
				
				ProcessInstanceInfoListHandler pilh = new ProcessInstanceInfoListHandler();
				pilh.addHandlerFinishedListener(this);
				pec.listProcessInstances(pilh);
				
			}

			return rootView;
		}
		
		if(mItem.content == "Import") {
			View rootView = inflater.inflate(R.layout.process_fragment_item_detail_others,
					container, false);
//			to do...
			
			((ItemListActivity) getActivity()).showFileChooser();
			
			return rootView;
		}
		
		View rootView = inflater.inflate(R.layout.process_fragment_item_detail_others,
				container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.item_detail))
					.setText(mItem.content);
		}

		return rootView;
	}

	@Override
	public void onHandlerFinished(AbstractClientHandler handler, Object arg) {
		if (handler instanceof ProcessListHandler) {
			
			List<IProcessInfo> processes = ServerConnector.getInstance()
					.getProcessEngineClient().getProcesslist();
			
			dbHelper = new ProcessDatabaseHelper(this.getActivity());
			database = dbHelper.getWritableDatabase();
			database.delete(ProcessDatabaseHelper.TABLE_PROCESSES,null,null);
			
			ContentValues values = new ContentValues();
			
			for (IProcessInfo pi : processes) {
				values.put(ProcessDatabaseHelper.COLUMN_ID, pi.getProcessId());
				values.put(ProcessDatabaseHelper.COLUMN_NAME, pi.getProcessName());
				values.put(ProcessDatabaseHelper.COLUMN_TYPE, pi.getProcessType());
				values.put(ProcessDatabaseHelper.COLUMN_DESCRIPTION, pi.getProcessDescription());
				database.insert(ProcessDatabaseHelper.TABLE_PROCESSES, null, values);
			    Log.i(dbHelper.toString(), "insert " + pi.getProcessId());
			}
						
			dbHelper.close();
			database.close();
			
			final ListView listview = (ListView) rootView
					.findViewById(R.id.item_detail_listview_process);

			Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
					"select * from "+ProcessDatabaseHelper.TABLE_PROCESSES, null);
			
			MyCursorAdapter scAdapter = new MyCursorAdapter(getActivity(), cursor, 2, false);

			listview.setAdapter(scAdapter);
			dbHelper.close();		
			listview.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
	            public boolean onItemLongClick(AdapterView<?> parent, View view,
	                    int pos, long id) {
					Cursor cursor = (Cursor)listview.getAdapter().getItem(pos);
					String selectId = cursor.getString(cursor.getColumnIndex(ProcessDatabaseHelper.COLUMN_ID));
					Toast.makeText(getActivity(), "clicked2!", Toast.LENGTH_SHORT)
							.show();
					Intent intent = new Intent(getActivity(),
							SequenceActivity.class);
					intent.putExtra(ItemDetailFragment.EXTRA_MESSAGE_MODEL_ID,
							selectId);
					startActivity(intent);

					return true;
	            }
	        }); 
			
		} else if (handler instanceof ProcessInstanceInfoListHandler) {
			
			List<IProcessInstanceInfo> processInstances = ServerConnector.getInstance()
					.getProcessEngineClient().getProcessInstanceList();
			
			dbHelper = new ProcessDatabaseHelper(this.getActivity());
			database = dbHelper.getWritableDatabase();
			
			database.delete(ProcessDatabaseHelper.TABLE_INSTANCES,null,null);
			ContentValues valuesIns = new ContentValues();
			
			for (IProcessInstanceInfo pii : processInstances) {
				valuesIns.put(ProcessDatabaseHelper.COLUMN_ID, pii.getProcessInstanceId());
				valuesIns.put(ProcessDatabaseHelper.COLUMN_NAME, pii.getProcessName());
				valuesIns.put(ProcessDatabaseHelper.COLUMN_TYPE, pii.getProcessType());
				valuesIns.put(ProcessDatabaseHelper.COLUMN_STATE, pii.getState().toString());
				valuesIns.put(ProcessDatabaseHelper.COLUMN_DESCRIPTION, pii.getProcessDescription());
			    database.insert(ProcessDatabaseHelper.TABLE_INSTANCES, null, valuesIns);
			    Log.i(dbHelper.toString(), "insert " + pii.getProcessId());
			}
			
		    dbHelper.close();
		    database.close();
		   
		    final ListView listviewIns = (ListView) rootView
					.findViewById(R.id.item_detail_listview_process_instance);

			Cursor cursorIns = dbHelper.getReadableDatabase().rawQuery(
					"select * from "+ProcessDatabaseHelper.TABLE_INSTANCES, null);
	
			MyCursorAdapter scAdapterIns = new MyCursorAdapter(getActivity(), cursorIns, 2, true);
			
			listviewIns.setAdapter(scAdapterIns);
			listviewIns.setOnItemClickListener(new OnItemClickListener() {
				@Override
	            public void onItemClick(AdapterView<?> parent, View view,
	                    int pos, long id) {
					Cursor cursor = (Cursor)listviewIns.getAdapter().getItem(pos);
					String selectId = cursor.getString(cursor.getColumnIndex(ProcessDatabaseHelper.COLUMN_ID));
					Toast.makeText(getActivity(), "clicked3!", Toast.LENGTH_SHORT)
							.show();
					Intent intent = new Intent(getActivity(),
							SequenceInstanceActivity.class);
					intent.putExtra(ItemDetailFragment.EXTRA_MESSAGE_PROCESS_INSTANCEID,
							selectId);
					startActivity(intent);
//					return true;

	            }
	        }); 

		}
		
	}
	
	
}