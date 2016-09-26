package eu.vicci.ecosystem.standalone.controlcenter.android.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.core.IJSONPortInstance;
import eu.vicci.process.model.util.serialization.jsontypes.core.IJSONType;

/**
 * Base class for handling inputs for different value types. Note: nested
 * fragments wont work out of the box in api level <17
 */
public abstract class HumanTaskPortDialogFragment<T extends IJSONPortInstance> extends DialogFragment {
	protected T portInstance;
	protected boolean isReadonly;
	private RefreshableActivity parentToRefresh;

	/**
	 * 
	 * @param portInstance
	 * @param isReadonly
	 * @param parentToRefresh
	 *            if the accept button is clicked - the parent activity of this
	 *            dialog is {@link RefreshableActivity#refresh()} is called. Can also be null.
	 */
	public HumanTaskPortDialogFragment(T portInstance, boolean isReadonly, RefreshableActivity parentToRefresh) {
		this.portInstance = portInstance;
		this.isReadonly = isReadonly;
		setStyle(STYLE_NO_TITLE, 0);
		setCancelable(isReadonly);
		this.parentToRefresh = parentToRefresh;
	}

	/**
	 * readonly dialog
	 * 
	 * @param portInstance
	 * @param parentToRefresh 
	 * 	          if the accept button is clicked - the parent activity of this
	 *            dialog is {@link RefreshableActivity#refresh()} is called. Can also be null.
	 */
	public HumanTaskPortDialogFragment(T portInstance, RefreshableActivity parentToRefresh) {
		this(portInstance, true, parentToRefresh);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_humantask_dialog, null);
		addTopInfos(view);
		addTypeSpecificInput(view);
		if (!isReadonly)
			addButtons(view);
		return view;
	}

	/**
	 * Gets a value which indicates, if this dialog is readonly (no values can
	 * changed)
	 * 
	 * @return
	 */
	public boolean isReadonly() {
		return isReadonly;
	}

	/**
	 * Add here the type specific input fields and handlers
	 */
	protected abstract void addTypeSpecificInput(View view);

	private void addTopInfos(View v) {
		TextView name = (TextView) v.findViewById(R.id.dialogName);
		name.setText(portInstance.getName());
		TextView dialog = (TextView) v.findViewById(R.id.dialogDesc);
		dialog.setText(portInstance.getPortType().getDescription());

	}

	protected void addButtons(View v) {
		LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.dialogButtons);
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
//		View deactivate_view = inflater.inflate(R.layout.btn_humantask_dialog, null);
//		Button btnDeactivate = (Button) deactivate_view.findViewById(R.id.btn_dialog);
//		btnDeactivate.setText("Deactivate");

		View cancel_view = inflater.inflate(R.layout.btn_humantask_dialog, null);
		Button btnCancel = (Button) cancel_view.findViewById(R.id.btn_dialog);
		btnCancel.setText("Cancel");
		btnCancel.setOnClickListener(onCancelClicked);

		View accept_view = inflater.inflate(R.layout.btn_humantask_dialog, null);
		Button btnAccept = (Button) accept_view.findViewById(R.id.btn_dialog);
		btnAccept.setText("Accept");
		btnAccept.setOnClickListener(onAcceptClickedListener);

//		linearLayout.addView(deactivate_view);
		linearLayout.addView(cancel_view);
		linearLayout.addView(accept_view);
	}

	/**
	 * Override this for the accept click handling
	 * 
	 * @param v
	 */
	protected abstract void onAcceptClicked(View v);
	
	/**
	 * Shows a short toast with the given message
	 * @param value
	 */
	protected void showToastShort(String value) {
		Toast.makeText(getActivity(), value, Toast.LENGTH_LONG).show();
	}

	/**
	 * Shows Toast with error message
	 * 
	 * @param value
	 */
	protected void showParsingErrorToast(String value, IJSONType parseTo) {
		showToastShort("the value cant be parsed to " 
				+ parseTo.getDataTypeType().name() + ": " + value);
	}

	protected OnClickListener onAcceptClickedListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			onAcceptClicked(v);
			if(parentToRefresh != null)
				parentToRefresh.refresh();
		}
	};

	protected OnClickListener onCancelClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
		}
	};
}
