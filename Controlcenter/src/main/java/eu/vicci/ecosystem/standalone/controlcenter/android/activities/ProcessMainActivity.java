package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.SmartCPSActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.adapters.ProcessListAdapter;
import eu.vicci.ecosystem.standalone.controlcenter.android.processview.ItemDetailActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.processview.ItemDetailFragment;
import eu.vicci.ecosystem.standalone.controlcenter.android.processview.ItemListFragment;
import eu.vicci.ecosystem.standalone.controlcenter.android.util.ProcessMainViewDialogBuilder;
import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.kpseus.connect.handlers.UploadModelHandler;

public class ProcessMainActivity extends SmartCPSActivity implements ItemListFragment.Callbacks {

	private static final int FILE_SELECT_CODE = 0;

	private ProcessListAdapter adapterInstances;
	private ProcessListAdapter adapter;
	private ListView listviewIns;
	private ListView processDetailListView;
	
	private ProcessMainViewDialogBuilder dialogBuilder = new ProcessMainViewDialogBuilder(this);


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.process_main_activity);
		listviewIns = (ListView) findViewById(R.id.item_detail_listview_process_instance);
		processDetailListView = (ListView) findViewById(R.id.item_detail_listview_process);
		adapter = new ProcessListAdapter(this, false);
		adapterInstances = new ProcessListAdapter(this, true);
		processDetailListView.setAdapter(adapter);
		listviewIns.setAdapter(adapterInstances);
		setClickListener();
		adapter.getProcesses();
		adapterInstances.getProcesses();
	}

	private void setClickListener() {
		processDetailListView.setOnItemClickListener(onDetailClickListener);
		processDetailListView.setOnItemLongClickListener(onDetailLongClickListener);
		listviewIns.setOnItemClickListener(onProcessClickListener);
		listviewIns.setOnItemLongClickListener(onProcessLongClickListener);
	}

	public void searchForProcess(View v) {
		listviewIns.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {

				TextView selectIdView = (TextView) parent.findViewById(R.id.item_detail_id);
				String selectId = selectIdView.getText().toString();
				Intent intent = new Intent(view.getContext(), SequenceActivity.class);
				intent.putExtra(ItemDetailFragment.EXTRA_MESSAGE_MODEL_ID, selectId);
				startActivity(intent);
				return true;
			}
		});
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
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onItemSelected(String id) {
		Intent detailIntent = new Intent(this, ItemDetailActivity.class);
		detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
		startActivity(detailIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.process_activity_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.menu_filter){
			dialogBuilder.showFilterDialog(adapterInstances);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private OnItemLongClickListener onDetailLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(final AdapterView<?> parent, final View v, int position, long id) {
			for (int i = 0; i < parent.getChildCount(); i++) {
				final View listItem = parent.getChildAt(i);
				TextView[] views = { (TextView) listItem.findViewById(R.id.item_detail_description),
						(TextView) listItem.findViewById(R.id.item_detail_description_label) };
				ViewGroup container = (ViewGroup) listItem.findViewById(R.id.item_frame);
				container.getLayoutTransition().setStartDelay(LayoutTransition.CHANGE_APPEARING,
						container.getLayoutTransition().getDuration(LayoutTransition.DISAPPEARING));
				for (final TextView view : views) {
					if (v == listItem) {
						view.setVisibility(View.VISIBLE);
					} else {
						view.setVisibility(View.VISIBLE);
						view.setVisibility(View.GONE);
					}
				}
			}

			return true;
		}
	};

	private OnItemClickListener onDetailClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
			ProcessListHelper inst = (ProcessListHelper) adapter.getItem(pos);
			Intent intent = new Intent(getApplicationContext(), ProcessViewActivity.class);
			intent.putExtra(ProcessViewActivity.EXTRA_MESSAGE_MODEL_ID, inst.getId());
			startActivity(intent);
		}
	};

	private OnItemLongClickListener onProcessLongClickListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
			for (int i = 0; i < parent.getChildCount(); i++) {
				final View listItem = parent.getChildAt(i);
				TextView[] views = { (TextView) listItem.findViewById(R.id.item_detail_description),
						(TextView) listItem.findViewById(R.id.item_detail_description_label) };
				ViewGroup container = (ViewGroup) listItem.findViewById(R.id.item_frame);
				container.getLayoutTransition().setStartDelay(LayoutTransition.CHANGE_APPEARING,
						container.getLayoutTransition().getDuration(LayoutTransition.DISAPPEARING));
				for (final TextView view : views) {
					if (v == listItem) {
						view.setVisibility(View.VISIBLE);
					} else {
						view.setVisibility(View.VISIBLE);
						view.setVisibility(View.GONE);
					}
				}
			}
			return true;
		}
	};

	private OnItemClickListener onProcessClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ProcessListHelper inst = (ProcessListHelper) adapterInstances.getItem(position);
			Intent intent = new Intent(getApplicationContext(), ProcessViewActivity.class);
			intent.putExtra(ProcessViewActivity.EXTRA_MESSAGE_MODEL_ID, inst.getId());
			intent.putExtra(ProcessViewActivity.EXTRA_MESSAGE_IS_INSTANCE, true);
			intent.putExtra(ProcessViewActivity.EXTRA_MESSAGE_INSTANCE_ID, inst.getInstanceId());
			startActivity(intent);
		}
	};
}
