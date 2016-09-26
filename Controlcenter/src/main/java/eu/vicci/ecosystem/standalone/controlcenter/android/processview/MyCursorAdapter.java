package eu.vicci.ecosystem.standalone.controlcenter.android.processview;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.engine.core.IProcessInstanceInfo;
import eu.vicci.process.kpseus.connect.handlers.PauseInstanceHandler;
import eu.vicci.process.kpseus.connect.handlers.StopInstanceHandler;

public class MyCursorAdapter extends CursorAdapter {
	LayoutInflater inflater;

	private Context mContext;
	private boolean isInstance;
	private Set<View> views = new HashSet<View>();

	public MyCursorAdapter(Context context, Cursor c, int flags, boolean processInstance) {
		super(context, c, flags);
		inflater = LayoutInflater.from(context);
		this.mContext = context;
		isInstance = processInstance;
	}

	@Override
	public void bindView(final View view, final Context context, Cursor cursor) {
		views.add(view);
		final TextView textId = (TextView) view.findViewById(R.id.item_detail_id);
		final TextView textName = (TextView) view.findViewById(R.id.properties_port_name);
		final TextView textType = (TextView) view.findViewById(R.id.item_detail_type);
		final TextView textState = (TextView) view.findViewById(R.id.item_detail_status);
		final TextView textDescription = (TextView) view.findViewById(R.id.item_detail_description);
		
		textType.setText(cursor.getString(cursor.getColumnIndex(ProcessDatabaseHelper.COLUMN_TYPE)));
		textName.setText(cursor.getString(cursor.getColumnIndex(ProcessDatabaseHelper.COLUMN_NAME)));
		textId.setText(cursor.getString(cursor.getColumnIndex(ProcessDatabaseHelper.COLUMN_ID)));
		textDescription.setText(cursor.getString(cursor.getColumnIndex(ProcessDatabaseHelper.COLUMN_DESCRIPTION)));
		int stateColumn = cursor.getColumnIndex(ProcessDatabaseHelper.COLUMN_STATE);
		
		if (stateColumn != -1) {
			textState.setVisibility(View.VISIBLE);
			textState.setText(cursor.getString(stateColumn));
		} else {
			textState.setVisibility(View.INVISIBLE);
		}

		final Button bStart = (Button) view.findViewById(R.id.start);
		final Button bStop = (Button) view.findViewById(R.id.stop);
		final Button bPause = (Button) view.findViewById(R.id.pause);
		
		bStart.setOnClickListener(new OnStartClickListener(textId.getText().toString(), mContext, bPause, bStop, isInstance));
		bPause.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String id = textId.getText().toString();

				PauseInstanceHandler pih = new PauseInstanceHandler(id);
				ProcessEngineClient.getInstance().pauseProcessInstance(pih);
				// TODO: handle return values

		        bPause.setVisibility(View.INVISIBLE);
		        bStop.setVisibility(View.INVISIBLE);
		        bStart.setVisibility(View.VISIBLE);
				Toast.makeText(context, "Pause request has been sent to the server", Toast.LENGTH_SHORT).show();

			}
		});


		bStop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String id = textId.getText().toString();
				
				StopInstanceHandler stih = new StopInstanceHandler(id);
				List<IProcessInstanceInfo> x = ProcessEngineClient.getInstance().getProcessInstanceList();
				ProcessEngineClient.getInstance().stopProcessInstance(stih);
				// TODO: handle return values

				Toast.makeText(context, "Stop request has been sent to the server", Toast.LENGTH_SHORT).show();
			}
		});
		if (stateColumn == -1) {
			bPause.setVisibility(View.INVISIBLE);
			bStop.setVisibility(View.INVISIBLE);
		} else if (cursor.getString(stateColumn).equals("active")
				|| cursor.getString(stateColumn).equals("executing")) {
			bStart.setVisibility(View.INVISIBLE);
		} else {
			bPause.setVisibility(View.INVISIBLE);
			bStop.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.process_item_detail_list_row, parent, false);
		bindView(v, context, cursor);
		return v;
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

}