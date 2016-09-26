package eu.vicci.ecosystem.standalone.controlcenter.android.visualization;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;

public class ProcessInfo extends InfoVisusalization {
	private TextView processStateTextView;
	private TextView processTypeTextView;

	public ProcessInfo(Context context) {
		this(context, null);
	}

	public ProcessInfo(Context context, AttributeSet attrs) {
		super(context, attrs);

		View.inflate(context, R.layout.process_info, this);
		processStateTextView = (TextView) findViewById(R.id.process_state);
		processTypeTextView = (TextView) findViewById(R.id.process_type);
	}

	/**
	 * sets the values in this order: state, type
	 */
	@Override
	public void setValue(String[] value) {
		if (value == null || value.length != 2)
			return;
		setState(value[0]);
		setType(value[1]);
	}

	public void setState(String value) {
		processStateTextView.setText(value);
	}

	public void setType(String value) {
		processTypeTextView.setText(value);
	}

}
