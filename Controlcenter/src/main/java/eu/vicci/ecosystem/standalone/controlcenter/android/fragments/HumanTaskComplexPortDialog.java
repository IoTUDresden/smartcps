package eu.vicci.ecosystem.standalone.controlcenter.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
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
import eu.vicci.process.model.util.serialization.jsontypeinstances.core.IJSONComplexTypeInstance;
import eu.vicci.process.model.util.serialization.jsontypeinstances.core.IJSONTypeInstance;
import eu.vicci.process.model.util.serialization.jsontypes.core.DataTypeType;

public class HumanTaskComplexPortDialog extends HumanTaskPortDialogFragment<IJSONDataPortInstance> {
	private List<IJSONTypeInstance> subTypes;
	private EditText textInput;
	RefreshableActivity parentToR;
	IJSONComplexTypeInstance typeInstance;
	int currentPosition = 0;
	private ComplexInputAdapter complexAdapter;
	private List<EditText> tInput = new ArrayList<EditText>();

	public HumanTaskComplexPortDialog(IJSONDataPortInstance portInstance, boolean isReadonly,
			RefreshableActivity parentToRefresh) {
		super(portInstance, isReadonly, parentToRefresh);
		parentToR = parentToRefresh;
		typeInstance = (IJSONComplexTypeInstance) portInstance.getDataTypeInstance();
		if (typeInstance == null)
			throw new IllegalArgumentException("expected a IJSONComplexTypeInstance which cant be null");

		subTypes = typeInstance.getSubTypes();
		if (subTypes == null)
			throw new IllegalArgumentException("subtype is empty");
		copyIfNotReadonly(typeInstance);
		complexAdapter = new ComplexInputAdapter(typeInstance);
	}

	// changes wont have any affect till accept is pressed
	private void copyIfNotReadonly(IJSONComplexTypeInstance tmp) {
		if (!isReadonly)
			typeInstance = tmp.makeCopy();
		else
			typeInstance = tmp;
	}

	@Override
	protected void addTypeSpecificInput(View view) {
		LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.dialogInput);
		if (!isReadonly)
			addInputField(linearLayout);
		addDataList(linearLayout);

	}

	private void addDataList(ViewGroup view) {
		if (complexAdapter == null) {
			view.addView(getErrorView("not supported type in list"));
			return;
		}

		ListView values = new ListView(getActivity());
		values.setAdapter(complexAdapter);
		view.addView(values);
	}

	private View getErrorView(String msg) {
		TextView err = new TextView(getActivity());
		err.setText(msg);
		return err;
	}

	private void addInputField(ViewGroup view) {
		for (IJSONTypeInstance type : subTypes) {
			switch (type.getDataTypeInstanceType()) {
			case BooleanTypeInstance:
				break;
			case ComplexTypeInstance:
				break;
			case DoubleTypeInstance:
				addSimpleInput(view, type);
				break;
			case IntegerTypeInstance:
				addSimpleInput(view, type);
				break;
			case StringTypeInstance:
				addSimpleInput(view, type);
				break;
			case ListTypeInstance:
				textInput = new EditText(getActivity());
				Button addNewButton = new Button(getActivity());
				addNewButton.setText("add");
				view.addView(textInput);
				view.addView(addNewButton);
				break;
			case SetTypeInstance:

				break;

			default:
				break;

			}

		}
		TextView textView = new TextView(getActivity());
		textView.setText("Current Data:");
		textView.setTypeface(null, Typeface.BOLD);
		textView.setPadding(10, 10, 0, 0);
		view.addView(textView);

	}

	@Override
	protected void onAcceptClicked(View v) {
		for (int i = 0; i < tInput.size(); i++) {
			String value = tInput.get(i).getText().toString();
			try {
				typeInstance.getSubTypes().get(i).parseValue(value);
				portInstance.setDataTypeInstance(typeInstance);
			} catch (IllegalArgumentException e) {
				showParsingErrorToast(value, typeInstance.getSubTypes().get(i).getDataType());
			}
		}
		dismiss();

	}

	private void addSimpleInput(ViewGroup linearLayout, IJSONTypeInstance type) {
		textInput = new EditText(getActivity());
		if (portInstance.getDataTypeInstance() != null) {
			textInput.setHint(type.getName());
		}
		textInput.setEnabled(!isReadonly);
		linearLayout.addView(textInput);
		tInput.add(textInput);

	}

	private final class ComplexInputAdapter extends BaseAdapter implements ListAdapter, DismissCallbacks {
		private IJSONComplexTypeInstance complexTypeInstance;

		public ComplexInputAdapter(IJSONComplexTypeInstance complexTypeInstance) {
			this.complexTypeInstance = complexTypeInstance;
		}

		@Override
		public int getCount() {
			return complexTypeInstance.getSubTypes().size();
		}

		@Override
		public IJSONTypeInstance getItem(int position) {
			return complexTypeInstance.getSubTypes().get(position);
		}

		@Override
		public long getItemId(int position) {
			return complexTypeInstance.getSubTypes().get(position).hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			currentPosition = position;
			LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.fragment_humantask_dialog, null);
			DataTypeType type = complexTypeInstance.getSubTypes().get(position).getDataType().getDataTypeType();
			
			switch (type) {
			case BooleanType:

				break;
			case ComplexType:
				break;
			case DoubleType:
			case IntegerType:
			case StringType:
				String value = typeInstance.getSubTypes().get(position).getValueString();
				String name = typeInstance.getSubTypes().get(position).getName();
				TextView textView = new TextView(getActivity());
				textView.setText(name + ": " + value);
				textView.setPadding(10, 0, 0, 0);
				textView.setTextSize(15);
				view = textView;
				break;
			case ListType:
				TextView txtValue = new TextView(getActivity());
				IJSONTypeInstance val = getItem(position);
				txtValue.setText(val.getValueString());
				txtValue.setHeight(50); // TODO remove the hardcoded size
				if (!isReadonly)
					txtValue.setOnTouchListener(new SwipeDismissTouchListener(txtValue, val, this));
				view = txtValue;
				break;
			case SetType:
				break;
			default:
				break;

			}

			return view;
		}

		/**
		 * Adds a new type instance
		 * 
		 * @param typeInstance
		 */
		// public void addTypeInstance(IJSONTypeInstance typeInstance) {
		// listTypeInstance.getValueAsObject().add(typeInstance);
		// notifyUi();
		// }

		@Override
		public boolean canDismiss(Object token) {
			return true;
		}

		@Override
		public void onDismiss(View view, Object token) {
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
