package eu.vicci.ecosystem.standalone.controlcenter.android.processview;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.kpseus.connect.handlers.StartProcessHandler;
import eu.vicci.process.model.sofia.BooleanType;
import eu.vicci.process.model.sofia.DataType;
import eu.vicci.process.model.sofia.DoubleType;
import eu.vicci.process.model.sofia.IntegerType;
import eu.vicci.process.model.sofia.ListType;
import eu.vicci.process.model.sofia.Port;
import eu.vicci.process.model.sofia.Process;
import eu.vicci.process.model.sofia.StartDataPort;
import eu.vicci.process.model.sofia.StringType;
import eu.vicci.process.model.sofiainstance.BooleanTypeInstance;
import eu.vicci.process.model.sofiainstance.DataTypeInstance;
import eu.vicci.process.model.sofiainstance.DoubleTypeInstance;
import eu.vicci.process.model.sofiainstance.IntegerTypeInstance;
import eu.vicci.process.model.sofiainstance.SofiaInstanceFactory;
import eu.vicci.process.model.sofiainstance.StringTypeInstance;

public class StartDialogFragment extends DialogFragment {

	private View rootView;
	private SQLiteDatabase database;
	private ProcessDatabaseHelper dbHelper;
	private Process process;
	private Map<String, DataTypeInstance> inputData = new HashMap<String, DataTypeInstance>();
	private List<StartDataPort> startDataPorts;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		String id = this.getTag();
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		rootView = inflater.inflate(R.layout.process_start_list, null);

		process = ProcessEngineClient.getInstance().getLocalProcesses().get(id);

		dbHelper = new ProcessDatabaseHelper(this.getActivity());
		database = dbHelper.getWritableDatabase();
		database.delete(ProcessDatabaseHelper.TABLE_PORTS, null, null);

		startDataPorts = new LinkedList<StartDataPort>();

		for (Port p : process.getPorts()) {
			if (p instanceof StartDataPort) {
				startDataPorts.add((StartDataPort) p);
			}
		}

		ContentValues values = new ContentValues();

		for (StartDataPort sdp : startDataPorts) {
			int pos = sdp.getPortDatatype().getClass().getSimpleName().indexOf("TypeImpl");
			String type = sdp.getPortDatatype().getClass().getSimpleName().substring(0, pos);
			if (sdp.getPortDatatype() instanceof ListType) {
				ListType listType = (ListType) sdp.getPortDatatype();
				type = type + "(" + listType.getCollectionItemType().getClass().getSimpleName() + ")";
			}
			values.put(ProcessDatabaseHelper.COLUMN_ID, sdp.getId());
			values.put(ProcessDatabaseHelper.COLUMN_NAME, sdp.getName());
			values.put(ProcessDatabaseHelper.COLUMN_TYPE, type);
			database.insert(ProcessDatabaseHelper.TABLE_PORTS, null, values);
			Log.i(dbHelper.toString(), "insert " + sdp.getName());
		}

		dbHelper.close();
		database.close();

		final ListView listview = (ListView) rootView.findViewById(R.id.item_start_listview);

		Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from " + ProcessDatabaseHelper.TABLE_PORTS, null);

		DialogCursorAdapter scAdapter = new DialogCursorAdapter(getActivity(), cursor, 2);

		listview.setAdapter(scAdapter);
		dbHelper.close();

		builder.setView(rootView).setTitle(id).setPositiveButton(R.string.start_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				int count = listview.getAdapter().getCount();
				String[] portData = new String[count];
				for (int i = 0; i < count; i++) {
					View view = listview.getChildAt(i);
					TextView portInput = (TextView) view.findViewById(R.id.start_port_input);
					portData[i] = portInput.getText().toString();
					Cursor cursor = (Cursor) listview.getAdapter().getItem(i);
					String portId = cursor.getString(cursor.getColumnIndex(ProcessDatabaseHelper.COLUMN_ID));
					for (StartDataPort sdp : startDataPorts) {
						if (sdp.getId().equals(portId)) {
							DataType dt = sdp.getPortDatatype();
							if (dt instanceof StringType) {
								String value = portData[i];
								StringTypeInstance sti = SofiaInstanceFactory.eINSTANCE.createStringTypeInstance();
								sti.setName(dt.getName() + "_instance");
								sti.setInstanceId(dt.getId() + "_instance");
								sti.setTypeId(dt.getId());
								sti.setStringTypeType((StringType) dt);
								sti.setValue(value);
								inputData.put(dt.getId(), sti);
							} else if (dt instanceof IntegerType) {
								try {
									int value = Integer.valueOf(portData[i]);

									IntegerTypeInstance iti = SofiaInstanceFactory.eINSTANCE.createIntegerTypeInstance();
									iti.setName(dt.getName() + "_instance");
									iti.setInstanceId(dt.getId() + "_instance");
									iti.setTypeId(dt.getId());
									iti.setIntegerTypeType((IntegerType) dt);
									iti.setValue(value);
									inputData.put(dt.getId(), iti);
								} catch (NumberFormatException e) {
								}
							} else if (dt instanceof DoubleType) {
								try {
									double value = Double.valueOf(portData[i]);
									DoubleTypeInstance doubti = SofiaInstanceFactory.eINSTANCE.createDoubleTypeInstance();
									doubti.setName(dt.getName() + "_instance");
									doubti.setInstanceId(dt.getId() + "_instance");
									doubti.setTypeId(dt.getId());
									doubti.setDoubleTypeType((DoubleType) dt);
									doubti.setValue(value);
									inputData.put(dt.getId(), doubti);
								} catch (NumberFormatException e) {
								}
							} else if (dt instanceof BooleanType) {
								boolean value = Boolean.valueOf(portData[i]);
								BooleanTypeInstance bti = SofiaInstanceFactory.eINSTANCE.createBooleanTypeInstance();
								bti.setName(dt.getName() + "_instance");
								bti.setInstanceId(dt.getId() + "_instance");
								bti.setTypeId(dt.getId());
								bti.setBooleanTypeType((BooleanType) dt);
								bti.setValue(value);
								inputData.put(dt.getId(), bti);
							}
						}
					}

					// TODO: Lists and other complex types
				}

				StartProcessHandler sph = new StartProcessHandler(process.getId());
				ProcessEngineClient.getInstance().startProcess(sph, inputData);

				// TODO: handle return message from server

				Toast.makeText(getActivity(), "Start request has been sent", Toast.LENGTH_SHORT).show();
			}
		}).setNegativeButton(R.string.start_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				StartDialogFragment.this.getDialog().cancel();
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

		return dialog;
	}

}