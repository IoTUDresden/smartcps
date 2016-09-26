package eu.vicci.ecosystem.standalone.controlcenter.android.sequence;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.process.kpseus.connect.subscribers.StateChangeMessageHandler;
import eu.vicci.process.model.sofia.DataPort;
import eu.vicci.process.model.sofia.DataType;
import eu.vicci.process.model.sofia.Port;

public class PortListAdapter extends ArrayAdapter<Port> {

	private List<Port> ports;
	private Context context;
	private String processInstanceId;
	private String messageID;
	private String lastMessageID;
	
	public PortListAdapter(Context context, int resource, List<Port> ports, String processInstanceId, String messageID) {
		super(context, resource, ports);
		this.ports = ports;
		this.context = context;
		this.processInstanceId = processInstanceId;
		this.messageID = messageID;
	}

	public void setLastPorts(String lastMessageID){
		this.lastMessageID = lastMessageID;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.sequence_view_port_item, parent, false);
        String currentValue = StateChangeMessageHandler.getInstance().getPortValue(ports.get(position), processInstanceId, messageID);
        if(messageID != null){
        	String lastPortValue=null;
        	if(lastMessageID != null)
        		lastPortValue = StateChangeMessageHandler.getInstance().getPortValue(ports.get(position), processInstanceId, lastMessageID);
        	if(lastMessageID == null || !currentValue.equals(lastPortValue))
        		rowView.setBackground(rowView.getResources().getDrawable(R.drawable.card_marked));
        	else
        		rowView.setAlpha(0.6f);
        }
        	
        // 3. Get the two text view from the rowView
        TextView portName = (TextView) rowView.findViewById(R.id.properties_port_name);
        TextView portType = (TextView) rowView.findViewById(R.id.properties_port_type);
        TextView portValue = (TextView) rowView.findViewById(R.id.properties_port_value);

        // 4. Set the text for textView 
        portName.setText(ports.get(position).getName());
        if(ports.get(position) instanceof DataPort){
        	DataType dataType = ((DataPort)ports.get(position)).getPortDatatype();
        	if(dataType != null){
        		portType.setText(dataType.getName() + " ("+dataType.getClass().getSimpleName()+")");
        		portValue.setVisibility(View.VISIBLE);
        		((TextView) rowView.findViewById(R.id.process_item_value_label)).setVisibility(View.VISIBLE);;
        	}
        	else{
        		portType.setText("DataPort - no DataType");
        	}
        	portValue.setText(currentValue);
        }
        else{
        	portType.setText("ControlPort: "+ports.get(position).getName());
        }

        return rowView;
	}
}
