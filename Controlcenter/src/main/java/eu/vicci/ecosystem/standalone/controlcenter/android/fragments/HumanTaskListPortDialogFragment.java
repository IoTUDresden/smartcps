package eu.vicci.ecosystem.standalone.controlcenter.android.fragments;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.controller.SwipeDismissTouchListener;
import eu.vicci.ecosystem.standalone.controlcenter.android.robot.controller.SwipeDismissTouchListener.DismissCallbacks;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.core.IJSONDataPortInstance;
import eu.vicci.process.model.util.serialization.jsontypeinstances.JSONTypeInstanceUtil;
import eu.vicci.process.model.util.serialization.jsontypeinstances.core.IJSONListTypeInstance;
import eu.vicci.process.model.util.serialization.jsontypeinstances.core.IJSONTypeInstance;
import eu.vicci.process.model.util.serialization.jsontypes.core.IJSONType;

public class HumanTaskListPortDialogFragment extends HumanTaskPortDialogFragment<IJSONDataPortInstance>  {
	private ListInputAdapter listAdapter;
	private IJSONListTypeInstance listTypeInstance;
	private IJSONType collectionType;
	private int instanceNumber;

	private EditText textInput;

	public HumanTaskListPortDialogFragment(IJSONDataPortInstance portInstance, RefreshableActivity parentToRefresh) {
		this(portInstance, true, parentToRefresh);
	}

	public HumanTaskListPortDialogFragment(IJSONDataPortInstance portInstance, boolean isReadonly, 
			RefreshableActivity parentToRefresh) {
		super(portInstance, isReadonly, parentToRefresh);
		IJSONListTypeInstance tmp = (IJSONListTypeInstance) portInstance.getDataTypeInstance();
		if (tmp == null)
			throw new IllegalArgumentException("expected a IJSONListTypeInstance which cant be null");
		collectionType = tmp.getCollectionType();
		instanceNumber = portInstance.getInstanceNumber();
		if (collectionType == null)
			throw new IllegalArgumentException("collection type cant be null");
		copyIfNotReadonly(tmp);
		listAdapter = getTypeSpecificListAdapter();
	}

	// changes wont have any affect till accept is pressed
	private void copyIfNotReadonly(IJSONListTypeInstance tmp) {
		if (!isReadonly)
			listTypeInstance = tmp.makeCopy();
		else
			listTypeInstance = tmp;
	}

	@Override
	protected void addTypeSpecificInput(View v) {
		// TODO find cool, simple way for subtypes (same as the simple types)
		// ther should be no code duplications
		if (!isReadonly)
			addInputField(v);
		addDataList(v);
	}

	@Override
	protected void onAcceptClicked(View v) {
		portInstance.setDataTypeInstance(listTypeInstance);
		dismiss();
	}

	private void addDataList(View view){
		LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.dialogInput);
		if(listAdapter == null){
			linearLayout.addView(getErrorView("not supported type in list"));
			return;
		}

		ListView values = new ListView(getActivity());
		values.setAdapter(listAdapter);
		linearLayout.addView(values);
	}

	private ListInputAdapter getTypeSpecificListAdapter() {
		switch (collectionType.getDataTypeType()) {
		case StringType:
		case IntegerType:
		case DoubleType:
		case BooleanType:
			return new ListInputAdapter(listTypeInstance);
		default:
			return null;
		}
	}

	
	private void addInputField(View view){
		LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.dialogInput);

		textInput = new EditText(getActivity());
		Button addNewButton = new Button(getActivity());
		addNewButton.setText("add");
		addNewButton.setOnClickListener(onAddClick);
		linearLayout.addView(textInput);
		linearLayout.addView(addNewButton);
		//viewGroup.addView(inputGroup);

		//linearLayout.addView(textInput);
		//linearLayout.addView(addNewButton);
//		view.addView(linearLayout);		
	}

	private OnClickListener onAddClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String value = textInput.getText().toString();
			IJSONTypeInstance stringTypeInstance = JSONTypeInstanceUtil
					.createTypeInstance(collectionType, instanceNumber);	
			try {
				stringTypeInstance.parseValue(value);
				textInput.setText("");
				listAdapter.addTypeInstance(stringTypeInstance);
			} catch (IllegalArgumentException e) {
				showParsingErrorToast(value, collectionType);
			}
		}
	};

	private View getErrorView(String msg) {
		TextView err = new TextView(getActivity());
		err.setText(msg);
		return err;
	}

	
	private final class ListInputAdapter extends BaseAdapter implements ListAdapter, DismissCallbacks {
		private IJSONListTypeInstance listTypeInstance;

		public ListInputAdapter(IJSONListTypeInstance listTypeInstance) {
			this.listTypeInstance = listTypeInstance;
		}

		@Override
		public int getCount() {
			return listTypeInstance.getValueAsObject().size();
		}

		@Override
		public IJSONTypeInstance getItem(int position) {
			return listTypeInstance.getValueAsObject().get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView txtValue = new TextView(getActivity());
			IJSONTypeInstance val = getItem(position);
			txtValue.setText(val.getValueString());
			txtValue.setHeight(50); // TODO remove the hardcoded size
			if (!isReadonly)
				txtValue.setOnTouchListener(new SwipeDismissTouchListener(txtValue, val, this));
			return txtValue;
		}

		/**
		 * Adds a new type instance
		 * 
		 * @param typeInstance
		 */
		public void addTypeInstance(IJSONTypeInstance typeInstance) {
			listTypeInstance.getValueAsObject().add(typeInstance);
			notifyUi();
		}

		@Override
		public boolean canDismiss(Object token) {
			return true;
		}

		@Override
		public void onDismiss(View view, Object token) {
			listTypeInstance.getValueAsObject().remove(token);
			notifyUi();
		}
		
		private void notifyUi() {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					notifyDataSetChanged();
				}
			});
		}
	}
}
