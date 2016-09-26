package eu.vicci.ecosystem.standalone.controlcenter.android.processview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.kpseus.connect.handlers.StartProcessHandler;
import eu.vicci.process.model.sofia.BooleanType;
import eu.vicci.process.model.sofia.ComplexType;
import eu.vicci.process.model.sofia.DataType;
import eu.vicci.process.model.sofia.DoubleType;
import eu.vicci.process.model.sofia.IntegerType;
import eu.vicci.process.model.sofia.ListType;
import eu.vicci.process.model.sofia.NumericType;
import eu.vicci.process.model.sofia.Port;
import eu.vicci.process.model.sofia.Process;
import eu.vicci.process.model.sofia.StartDataPort;
import eu.vicci.process.model.sofia.StringType;
import eu.vicci.process.model.sofiainstance.BooleanTypeInstance;
import eu.vicci.process.model.sofiainstance.ComplexTypeInstance;
import eu.vicci.process.model.sofiainstance.DataTypeInstance;
import eu.vicci.process.model.sofiainstance.DoubleTypeInstance;
import eu.vicci.process.model.sofiainstance.IntegerTypeInstance;
import eu.vicci.process.model.sofiainstance.ListTypeInstance;
import eu.vicci.process.model.sofiainstance.SofiaInstanceFactory;
import eu.vicci.process.model.sofiainstance.StringTypeInstance;

public class StartDialog extends DialogFragment {
	private Process process;
	private LinearLayout rootView;
	private LinkedList<StartDataPort> startDataPorts;
	private Map<StartDataPort, Object> mapping = new HashMap<StartDataPort, Object>();
	private Map<String, DataTypeInstance> inputData = new HashMap<String, DataTypeInstance>();
	private int lineCount = 0;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		String id = this.getTag();
		process = ProcessEngineClient.getInstance().getLocalProcesses().get(id);
		rootView = new LinearLayout(getActivity());
		rootView.setOrientation(LinearLayout.VERTICAL);
		startDataPorts = new LinkedList<StartDataPort>();

		for (Port p : process.getPorts()) {
			if (p instanceof StartDataPort) {
				startDataPorts.add((StartDataPort) p);
			}
		}
		makeViews();
		builder.setView(rootView);
		builder.setTitle(R.string.start_dialog_title);
		builder.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				for (StartDataPort port : mapping.keySet()) {
					Object mapped = mapping.get(port);

					DataType dataType = port.getPortDatatype();
					DataTypeInstance typeInstance = null;

					if (mapped instanceof View) {
						if (mapped instanceof EditText) {
							String inputStr = ((EditText) mapped).getText().toString();
							if (dataType instanceof IntegerType) {
								try {
									if (inputStr == null || inputStr.equals(""))
										inputStr = "0";
									int value = Integer.parseInt(inputStr);

									IntegerTypeInstance iti = SofiaInstanceFactory.eINSTANCE.createIntegerTypeInstance();
									typeInstance = iti;
									iti.setDataTypeType(dataType);
									iti.setIntegerTypeType((IntegerType) dataType);
									iti.setValue(value);
									inputData.put(dataType.getId(), iti);
								} catch (NumberFormatException e) {
								}
							}
							if (dataType instanceof DoubleType) {
								try {
									if (inputStr == null || inputStr.equals(""))
										inputStr = "0";
									double value = Double.parseDouble(inputStr);
									DoubleTypeInstance doubti = SofiaInstanceFactory.eINSTANCE.createDoubleTypeInstance();
									typeInstance = doubti;
									doubti.setDoubleTypeType((DoubleType) dataType);
									doubti.setValue(value);
									inputData.put(dataType.getId(), doubti);
								} catch (NumberFormatException e) {
								}
							}
							if (dataType instanceof StringType) {
								String value = inputStr;
								StringTypeInstance sti = SofiaInstanceFactory.eINSTANCE.createStringTypeInstance();
								typeInstance = sti;
								sti.setDataTypeType(dataType);
								sti.setStringTypeType((StringType) dataType);
								sti.setValue(value);
								inputData.put(dataType.getId(), sti);
							}
						}
						if (dataType instanceof BooleanType) {
							boolean value = ((CheckBox) mapped).isChecked();
							BooleanTypeInstance bti = SofiaInstanceFactory.eINSTANCE.createBooleanTypeInstance();
							typeInstance = bti;
							bti.setBooleanTypeType((BooleanType) dataType);
							bti.setValue(value);
							inputData.put(dataType.getId(), bti);
						}
					} else {
						if (dataType instanceof ListType) {
							List<View> inputs = (List<View>) mapped;
							DataType collectionType = ((ListType) dataType).getCollectionItemType();
							ListTypeInstance lti = SofiaInstanceFactory.eINSTANCE.createListTypeInstance();
							typeInstance = lti;
							lti.setDataTypeType(dataType);
							lti.setCollectionItemType(collectionType);

							for (View input : inputs) {
								makeSubType(collectionType, lti, input);
							}
							inputData.put(dataType.getId(), lti);

						}

						if (dataType instanceof ComplexType) {
							ComplexType ct = (ComplexType) dataType;
							List<View> inputs = (List<View>) mapped;
							ComplexTypeInstance cti = SofiaInstanceFactory.eINSTANCE.createComplexTypeInstance();
							typeInstance = cti;
							cti.setDataTypeType(dataType);
							for (int i = 0; i < inputs.size(); i++) {
								View input = inputs.get(i);
								DataType sub = ct.getSubtypes().get(i);
								makeSubType(sub, cti, input);
							}
							inputData.put(dataType.getId(), cti);
						}

					}

					typeInstance.setName(dataType.getName() + "_instance");
					typeInstance.setInstanceId(dataType.getId() + "_instance");
					typeInstance.setTypeId(dataType.getId());
				}

				StartProcessHandler sph = new StartProcessHandler(process.getId());
				ProcessEngineClient.getInstance().startProcess(sph, inputData);
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		return dialog;
	}

	private void makeViews() {
		for (StartDataPort startPort : startDataPorts) {
			makePortView(startPort);
		}
	}

	private LinearLayout makePortView(StartDataPort startPort) {
		LinearLayout result = null;
		DataType portType = startPort.getPortDatatype();

		LayoutInflater inflater = getActivity().getLayoutInflater();
		LinearLayout line = (LinearLayout) inflater.inflate(R.layout.start_process_dialog_line, rootView);
		line = (LinearLayout) line.getChildAt(line.getChildCount() - 1);
		line.setBackgroundColor(lineCount % 2 == 1 ? ltgray : Color.TRANSPARENT);

		if (portType instanceof IntegerType) {
			result = makeNumberInput(startPort, true, line, new ArrayList<View>());
		}
		if (portType instanceof DoubleType) {
			result = makeNumberInput(startPort, false, line, new ArrayList<View>());
		}
		if (portType instanceof StringType) {
			result = makeStringInput(startPort, line, new ArrayList<View>());
		}
		if (portType instanceof BooleanType) {
			result = makeBooleanInput(startPort, line, new ArrayList<View>());
		}
		if (portType instanceof ListType) {
			result = makeListInput(startPort, line, new ArrayList<View>());
			String type = startPort.getPortDatatype().getClass().getSimpleName();
			type = type + " (" + ((ListType) portType).getCollectionItemType().getClass().getSimpleName() + ")";
			type = type.replace("Impl", "").replace("Type", "");
			((TextView) result.findViewById(R.id.portType)).setText(type);
		}

		if (portType instanceof ComplexType) {
			rootView.removeAllViews();
			line = (LinearLayout) inflater.inflate(R.layout.start_process_dialog_complex, rootView);
			result = makeComplexInput(startPort, rootView);
		}

		((TextView) result.findViewById(R.id.portNameLabel)).setText("Port '" + startPort.getName() + "':");
		((TextView) result.findViewById(R.id.portName)).setText(startPort.getPortDatatype().getName());
		if (((TextView) result.findViewById(R.id.portType)).getText().length() == 0)
			((TextView) result.findViewById(R.id.portType)).setText(startPort.getPortDatatype().getClass().getSimpleName().replace("Impl", "").replace("Type", ""));

		((TextView) result.findViewById(R.id.portText)).setText((startPort.getValue().toString()));
		
		lineCount++;
		return result;
	}

	private int ltgray = Color.argb(50, 0, 0, 0);

	private LinearLayout makeNumberInput(StartDataPort startPort, boolean isWholeNumber, LinearLayout line, List<View> inputs) {

		EditText input = (EditText) line.findViewById(R.id.portText);
		input.setVisibility(View.VISIBLE);
		if (line.getParent() == rootView) {
			mapping.put(startPort, input);
		} else {
			inputs.add(input);
		}
		if (isWholeNumber)
			input.setInputType(InputType.TYPE_CLASS_NUMBER);
		else
			input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		return line;
	}

	private LinearLayout makeStringInput(StartDataPort startPort, LinearLayout line, List<View> inputs) {
		EditText input = (EditText) line.findViewById(R.id.portText);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		input.setVisibility(View.VISIBLE);
		if (line.getParent() == rootView) {
			mapping.put(startPort, input);
		} else {
			inputs.add(input);
		}
		return line;
	}

	private LinearLayout makeBooleanInput(StartDataPort startPort, LinearLayout line, List<View> inputs) {
		CheckBox input = (CheckBox) line.findViewById(R.id.portBool);
		input.setVisibility(View.VISIBLE);
		if (line.getParent() == rootView) {
			mapping.put(startPort, input);
		} else {
			inputs.add(input);
		}
		return line;
	}

	private LinearLayout makeListInput(StartDataPort startPort, LinearLayout line, List<View> inputsParent) {
		final DataType listType = ((ListType) startPort.getPortDatatype()).getCollectionItemType();
		final LinearLayout list = (LinearLayout) line.findViewById(R.id.portListLayout);

		RelativeLayout container = (RelativeLayout) line.findViewById(R.id.portListContainer);
		container.setVisibility(View.VISIBLE);

		ImageView addButton = (ImageView) line.findViewById(R.id.portListAddButton);
		final ArrayList<View> inputs = new ArrayList<View>();
		addListItem(list, listType, inputs);
		if (line.getParent() == rootView)
			mapping.put(startPort, inputs);
		else
			inputsParent.addAll(inputs);
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addListItem(list, listType, inputs);
				inputs.get(inputs.size() - 1).requestFocus(View.FOCUS_DOWN);
			}
		});

		return line;
	}

	private LinearLayout makeComplexInput(StartDataPort startPort, LinearLayout line) {
		final EList<DataType> subTypes = ((ComplexType) startPort.getPortDatatype()).getSubtypes();
		line = (LinearLayout) line.getChildAt(line.getChildCount() - 1);
		line.setBackgroundColor(lineCount % 2 == 1 ? ltgray : Color.TRANSPARENT);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		List<View> inputs = new ArrayList<View>();
		for (DataType subType : subTypes) {
			LinearLayout subTypeLayout = (LinearLayout) inflater.inflate(R.layout.start_process_dialog_complex_line, line);
			subTypeLayout = (LinearLayout) subTypeLayout.getChildAt(subTypeLayout.getChildCount() - 1);
			((TextView) subTypeLayout.findViewById(R.id.portName)).setText(subType.getName());
			((TextView) subTypeLayout.findViewById(R.id.portType)).setText(subType.getClass().getSimpleName());

			if (subType instanceof IntegerType)
				makeNumberInput(startPort, true, subTypeLayout, inputs);
			if (subType instanceof DoubleType)
				makeNumberInput(startPort, false, subTypeLayout, inputs);
			if (subType instanceof StringType)
				makeStringInput(startPort, subTypeLayout, inputs);
			if (subType instanceof BooleanType)
				makeBooleanInput(startPort, subTypeLayout, inputs);
			if (subType instanceof ListType)
				makeListInput(startPort, subTypeLayout, inputs);
		}
		mapping.put(startPort, inputs);
		return line;
	}

	private void makeSubType(DataType collectionType, DataTypeInstance composite, View input) {
		DataTypeInstance dti = null;
		String inputText = ((EditText) input).getText().toString();
		if (collectionType instanceof StringType) {
			String value = inputText;
			StringTypeInstance sti = SofiaInstanceFactory.eINSTANCE.createStringTypeInstance();
			sti.setName(collectionType.getName() + "_instance");
			sti.setInstanceId(collectionType.getId() + "_instance");
			sti.setTypeId(collectionType.getId());
			sti.setStringTypeType((StringType) collectionType);
			sti.setValue(value);
			dti = sti;
		}
		if (collectionType instanceof IntegerType) {
			try {
				if (inputText.equals(""))
					inputText = "0";
				int value = Integer.parseInt(inputText);

				IntegerTypeInstance iti = SofiaInstanceFactory.eINSTANCE.createIntegerTypeInstance();
				iti.setName(collectionType.getName() + "_instance");
				iti.setInstanceId(collectionType.getId() + "_instance");
				iti.setTypeId(collectionType.getId());
				iti.setIntegerTypeType((IntegerType) collectionType);
				iti.setValue(value);
				dti = iti;
			} catch (NumberFormatException e) {
			}
		}
		if (collectionType instanceof DoubleType) {
			try {
				if (inputText.equals(""))
					inputText = "0";
				double value = Double.parseDouble(inputText);
				DoubleTypeInstance doubti = SofiaInstanceFactory.eINSTANCE.createDoubleTypeInstance();
				doubti.setName(collectionType.getName() + "_instance");
				doubti.setInstanceId(collectionType.getId() + "_instance");
				doubti.setTypeId(collectionType.getId());
				doubti.setDoubleTypeType((DoubleType) collectionType);
				doubti.setValue(value);
				dti = doubti;
			} catch (NumberFormatException e) {
			}
		}
		if (collectionType instanceof BooleanType) {
			boolean value = ((CheckBox) input).isChecked();
			BooleanTypeInstance bti = SofiaInstanceFactory.eINSTANCE.createBooleanTypeInstance();
			bti.setName(collectionType.getName() + "_instance");
			bti.setInstanceId(collectionType.getId() + "_instance");
			bti.setTypeId(collectionType.getId());
			bti.setBooleanTypeType((BooleanType) collectionType);
			bti.setValue(value);
			dti = bti;
		}

		if (composite instanceof ListTypeInstance)
			((ListTypeInstance) composite).getSubtypes().add(dti);
		if (composite instanceof ComplexTypeInstance)
			((ComplexTypeInstance) composite).getSubtypes().add(dti);
	}

	private View addListItem(LinearLayout list, DataType listType, ArrayList<View> inputs) {
		View item = null;
		if (listType instanceof StringType || listType instanceof NumericType) {
			EditText editText = new EditText(getActivity());
			item = editText;
			editText.setLayoutParams(new LayoutParams(dpToPx(100), LayoutParams.WRAP_CONTENT));
			if (listType instanceof IntegerType)
				editText.setInputType(InputType.TYPE_CLASS_NUMBER);
			else if (listType instanceof DoubleType)
				editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			else
				editText.setInputType(InputType.TYPE_CLASS_TEXT);
			list.addView(editText);

		}

		if (listType instanceof BooleanType) {
			CheckBox checkBox = new CheckBox(getActivity());
			item = checkBox;
			list.addView(checkBox);
		}
		item.setVisibility(View.GONE);
		item.setVisibility(View.VISIBLE);
		inputs.add(item);
		return item;
	}

	public int dpToPx(int dp) {
		DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
		int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return px;
	}
}
