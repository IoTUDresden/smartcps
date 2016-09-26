package eu.vicci.ecosystem.standalone.controlcenter.android.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.core.IJSONDataPortInstance;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.core.IJSONPortInstance;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.core.PortInstanceType;
import eu.vicci.process.model.util.serialization.jsonprocesssteps.core.IJSONDataPort;
import eu.vicci.process.model.util.serialization.jsontypeinstances.core.IJSONBooleanTypeInstance;
import eu.vicci.process.model.util.serialization.jsontypes.core.DataTypeType;

public class HumanTaskPortListAdapter extends BaseAdapter implements ListAdapter{
	private final Context context;
	private final List<IJSONDataPortInstance> values;
	
	public HumanTaskPortListAdapter(Context context, List<IJSONDataPortInstance> inputPortMap) {
		this.context = context;
		this.values = inputPortMap;		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);	    
	    View rowView = inflater.inflate(R.layout.fragment_humantask_row, parent, false);
	    TextView id = (TextView) rowView.findViewById(R.id.PortID); 	    
	    IJSONPortInstance portInstance = values.get(position);
	    String tmp_id = portInstance.getPortType().getId() == null ? "" : portInstance.getPortType().getId();
	    id.setText("ID : " + tmp_id);	    
	    setPortValues(portInstance, rowView);	    
	    return rowView;
	}

	
	private void setPortValues(IJSONPortInstance portInstance, View rowView){
		ImageView imageView = (ImageView) rowView.findViewById(R.id.Datatype);
	    TextView name = (TextView) rowView.findViewById(R.id.PortName);
	    TextView value = (TextView) rowView.findViewById(R.id.DataValue);  
	    Boolean isStartPort = false;	    
		PortInstanceType type = portInstance.getPortInstanceType();
		
		switch(type){
    	case StartDataPortInstance:
    		isStartPort = true;
    		// kein Break, da gleiche Logik wie EndDataPort
    	case EndDataPortInstance:
    		IJSONDataPortInstance dataP = (IJSONDataPortInstance)portInstance;
    		IJSONDataPort port = dataP.getPortType();
    		String portName = port.getName() ;
    		if(!isStartPort && !port.isOptional()){
    			portName = portName + " (*) ";
    		}else portName = portName+ " ";
    		name.setText(portName);
    		
//    		name.setText(port.getName()+ " ");
    		DataTypeType typeType = port.getDataType().getDataTypeType();
			setPortDataType(typeType, imageView, isStartPort);    		
    		String valueAsString = dataP.getDataTypeInstance() == null ? "":  dataP.getDataTypeInstance().getValueString();   
    		if(typeType == DataTypeType.BooleanType)
    			valueAsString = ((IJSONBooleanTypeInstance)dataP.getDataTypeInstance()).getValue() ? "Yes" : "No";
    		value.setText(valueAsString);   		    		
    		break;
    		
    	case StartControlPortInstance:
    		isStartPort = true;
    	case EndControlPortInstance:
    		IJSONPortInstance controlDataP = portInstance;
    		name.setText("Control Port : " + controlDataP.getName());
    		imageView.setImageResource(R.drawable.output_control);
    		break;
    	case EscalationPortInstance:
    		break;
		default:
			break;
		}
	}
	
	//Letter Icons by Nathan David Smith from the Noun Project
	// ControlPort Icon by Sylvain Amatoury from the Noun Project
	// changed Color
	private void setPortDataType(DataTypeType type, ImageView iView, Boolean startPort){		
		switch(type){
			case StringType:
				if(startPort){
					iView.setImageResource(R.drawable.input_string);}
				else{ iView.setImageResource(R.drawable.output_string);}
				break;
			case BooleanType:
				if(startPort)
					iView.setImageResource(R.drawable.input_bool);
				else iView.setImageResource(R.drawable.output_bool);
				break;
			case ComplexType:
				if(startPort)
					iView.setImageResource(R.drawable.input_complex);
				else iView.setImageResource(R.drawable.output_complex);
				break;
			case IntegerType:
				if(startPort)
					iView.setImageResource(R.drawable.input_integer);
				else iView.setImageResource(R.drawable.output_integer);
				break;
			case DoubleType:
				if(startPort)
					iView.setImageResource(R.drawable.input_double);
				else iView.setImageResource(R.drawable.output_double);
				break;
			case ListType:
				if(startPort)
					iView.setImageResource(R.drawable.input_list);
				else iView.setImageResource(R.drawable.output_list);
				break;
			case SetType:
				if(startPort)
					iView.setImageResource(R.drawable.input_unknown);
				else iView.setImageResource(R.drawable.output_unknown);
				break;
			default:
				if(startPort)
					iView.setImageResource(R.drawable.input_unknown);
				else iView.setImageResource(R.drawable.output_unknown);
				
		}
	}
	
	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public Object getItem(int position) {
		if (position >= values.size())
			return null;
		
		else{
			List<IJSONPortInstance> item = new ArrayList<IJSONPortInstance>();
			item.add(values.get(position));
			return item;
		}
		
	}

	@Override
	public long getItemId(int position) {
		if (position >= values.size())
			return 0;
		
		else{
			return values.get(position).hashCode();
		}
	}
}
