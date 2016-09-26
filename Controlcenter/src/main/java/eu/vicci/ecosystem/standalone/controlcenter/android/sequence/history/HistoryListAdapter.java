package eu.vicci.ecosystem.standalone.controlcenter.android.sequence.history;

import java.text.DateFormatSymbols;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.sequence.PortListAdapter;
import eu.vicci.process.model.sofia.ProcessStep;
import eu.vicci.process.model.util.messages.core.IStateChangeMessage;

public class HistoryListAdapter extends ArrayAdapter {

	private List<IStateChangeMessage> messages;
	private String processInstanceId;

	private ProcessStep step;
	private Context context;

	public HistoryListAdapter(Context context, int resource, List<IStateChangeMessage> objects, String processInstanceId, ProcessStep step) {
		super(context, resource, objects);
		sort(new Comparator<IStateChangeMessage>() {

			@Override
			public int compare(IStateChangeMessage lhs, IStateChangeMessage rhs) {
				int diff = (int) (lhs.getTimeStamp()-rhs.getTimeStamp());
				
				return diff==0?diff:diff/Math.abs(diff);
			}
			
		});
		this.messages = objects;
		this.processInstanceId = processInstanceId;
		this.step = step;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		IStateChangeMessage message = messages.get(position);
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(R.layout.process_instance_history_item, parent, false);
		} else {
			view = convertView;
		}

		LinearLayout portView = (LinearLayout) view.findViewById(R.id.process_instance_history_port_list);
		PortListAdapter adapter = new PortListAdapter(context, R.layout.sequence_view_port_item, step.getPorts(), processInstanceId, message.getMessageId());
		
		portView.removeAllViews();
		for(int i=0; i<adapter.getCount(); i++){
			if(position>0)
				adapter.setLastPorts(messages.get(position-1).getMessageId());
			View item = adapter.getView(i, null, null);
			portView.addView(item);
		}
		
		TextView status = (TextView) view.findViewById(R.id.process_instance_history_status);
		TextView time = (TextView) view.findViewById(R.id.process_instance_history_time);
		status.setText(message.getState().getLiteral());
		Date timeStamp = new Date(message.getTimeStamp());
		String day = timeStamp.getDate()+"";
		
		String month = new DateFormatSymbols().getMonths()[timeStamp.getMonth()];
		if(month.length()<2) month="0"+month;
		
		String year = (timeStamp.getYear()-100+2000)+"";
		
		String hours = timeStamp.getHours()+"";
		if(hours.length()<2) hours="0"+hours;
		
		String minutes = timeStamp.getMinutes()+"";
		if(minutes.length()<2) minutes="0"+minutes;
		
		String seconds = timeStamp.getSeconds()+"";
		if(seconds.length()<2) seconds="0"+seconds;
		
		String timeString = hours + ":" + minutes + ":" + seconds;
		time.setText(day + ". " + month + "." + year + " \n" + timeString);

		return view;
	}

}