package eu.vicci.ecosystem.standalone.controlcenter.android.fragments;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.core.IJSONDataPortInstance;
import eu.vicci.process.model.util.serialization.jsonprocesssteps.core.IJSONDataPort;
import eu.vicci.process.model.util.serialization.jsontypeinstances.core.IJSONBooleanTypeInstance;
import eu.vicci.process.model.util.serialization.jsontypes.core.IJSONIntegerType;

public abstract class HumanTaskSimpleTypePortDialog extends HumanTaskPortDialogFragment<IJSONDataPortInstance> {
	private EditText txtValue;
	
	public HumanTaskSimpleTypePortDialog(IJSONDataPortInstance portInstance, RefreshableActivity parentToRefresh) {
		this(portInstance, true, parentToRefresh);
	}
	
	public HumanTaskSimpleTypePortDialog(IJSONDataPortInstance portInstance, boolean isReadonly, RefreshableActivity parentToRefresh) {
		super(portInstance, isReadonly, parentToRefresh);
	}
	
	protected int getInputType(){
		return 0;
	}
	
	protected boolean hasTypedInput(){
		return true;		
	}
	
	@Override
	protected void onAcceptClicked(View v) {
		String value = txtValue.getText().toString();
		try {
			portInstance.getDataTypeInstance().parseValue(value);
			dismiss();
		} catch (IllegalArgumentException e) {
			showParsingErrorToast(value, portInstance.getDataTypeInstance().getDataType());	
		}		
	}	

	@Override
	public void addTypeSpecificInput(View v) {
		LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.dialogInput);
		txtValue = new EditText(getActivity());
		if(hasTypedInput())
			txtValue.setInputType(getInputType());
		if(portInstance.getDataTypeInstance() != null)
			txtValue.setText(portInstance.getDataTypeInstance().getValueString());
		txtValue.setEnabled(!isReadonly);
		linearLayout.addView(txtValue);
	}
	
	protected String getInputValue(){
		return txtValue.getText().toString();
	}
	
	public static class HumanTaskIntTypePortDialog extends HumanTaskSimpleTypePortDialog{
		private int min;
		private int max;
		
		public HumanTaskIntTypePortDialog(IJSONDataPortInstance portInstance, RefreshableActivity parentToRefresh) {
			this(portInstance, true ,parentToRefresh);
		}
		
		public HumanTaskIntTypePortDialog(IJSONDataPortInstance portInstance, boolean isReadonly, RefreshableActivity parentToRefresh) {
			super(portInstance, isReadonly, parentToRefresh);
		}
		
		@Override
		protected int getInputType() {
			return InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED;
		}
		
		@Override
		protected void onAcceptClicked(View v) {
			if(!validateMinMax()){
				showToastShort("the value is not in the range between " + min + " and " + max);
				return;				
			}
			super.onAcceptClicked(v);
		}
		
		//input filter is maybe the better way for this
		private boolean validateMinMax(){
			IJSONDataPort port = (IJSONDataPort)portInstance.getPortType();
			IJSONIntegerType integerType = (IJSONIntegerType)port.getDataType();
			min = integerType.getMin();
			max = integerType.getMax();
			int curVal = Integer.parseInt(getInputValue());
			if(curVal < min || curVal > max)
				return false;
			return true;
		}
	}
	
	public static class HumanTaskStringTypePortDialog extends HumanTaskSimpleTypePortDialog{
		public HumanTaskStringTypePortDialog(IJSONDataPortInstance portInstance, RefreshableActivity parentToRefresh) {
			this(portInstance, true ,parentToRefresh);
		}
		
		public HumanTaskStringTypePortDialog(IJSONDataPortInstance portInstance, boolean isReadonly, RefreshableActivity parentToRefresh) {
			super(portInstance, isReadonly, parentToRefresh);
		}

		@Override
		protected boolean hasTypedInput() {
			return false;
		}			
	}
	
	public static class HumanTaskDoubleTypePortDialog extends HumanTaskSimpleTypePortDialog{
		public HumanTaskDoubleTypePortDialog(IJSONDataPortInstance portInstance, RefreshableActivity parentToRefresh) {
			this(portInstance, true, parentToRefresh);
		}
		
		public HumanTaskDoubleTypePortDialog(IJSONDataPortInstance portInstance, boolean isReadonly, RefreshableActivity parentToRefresh) {
			super(portInstance, isReadonly, parentToRefresh);
		}

		@Override
		protected int getInputType() {
			return InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED|InputType.TYPE_NUMBER_FLAG_DECIMAL;
		}		
	}
	
	public static class HumanTaskBooleanTypePortDialog extends HumanTaskSimpleTypePortDialog{		
		public HumanTaskBooleanTypePortDialog(IJSONDataPortInstance portInstance, RefreshableActivity parentToRefresh) {
			this(portInstance, true, parentToRefresh);
		}
		
		public HumanTaskBooleanTypePortDialog(IJSONDataPortInstance portInstance, boolean isReadonly, RefreshableActivity parentToRefresh) {
			super(portInstance, isReadonly, parentToRefresh);
		}		

		@Override
		public void addTypeSpecificInput(View v) {
			
		}
		
		@Override
		protected void addButtons(View v) {
			LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.dialogButtons);
			LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			linearLayout.setOrientation(LinearLayout.HORIZONTAL);			
			LinearLayout btnHolder = (LinearLayout)inflater.inflate(R.layout.btn_humantask_flat, null);	
			btnHolder.setOrientation(LinearLayout.HORIZONTAL);
			
			Button btnCancel = (Button) btnHolder.findViewById(R.id.btn_ht_cancel);			
			Button btnNo = (Button) btnHolder.findViewById(R.id.btn_ht_no);
			Button btnYes = (Button) btnHolder.findViewById(R.id.btn_ht_yes);			
			
			btnCancel.setText("Cancel");
			btnCancel.setOnClickListener(onCancelClicked);
			btnNo.setText("No");
//			btnNo.setId(noBtnId);
			btnNo.setOnClickListener(onAcceptClickedListener);
			btnYes.setText("Yes");
//			btnYes.setId(yesBtnId);
			btnYes.setOnClickListener(onAcceptClickedListener);
			linearLayout.addView(btnHolder);
		}
		
		@Override
		protected void onAcceptClicked(View v) {
			IJSONBooleanTypeInstance typeInstance = (IJSONBooleanTypeInstance)portInstance.getDataTypeInstance();
			if(typeInstance == null){
				showToastShort("no type instance is set");
				return;
			}
			setNewValue(typeInstance, v.getId());	
		}
		
		private void setNewValue(IJSONBooleanTypeInstance typeInstance, int btnClicked){
			if(btnClicked == R.id.btn_ht_no)
				typeInstance.setValue(false);
			else if (btnClicked == R.id.btn_ht_yes)
				typeInstance.setValue(true);
			else
				showToastShort("no option for btn id: " + btnClicked);
			dismiss();
		}
		
	}

}
