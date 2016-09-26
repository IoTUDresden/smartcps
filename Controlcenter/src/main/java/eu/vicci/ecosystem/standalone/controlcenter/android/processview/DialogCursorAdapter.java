package eu.vicci.ecosystem.standalone.controlcenter.android.processview;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;


public class DialogCursorAdapter extends CursorAdapter{
    LayoutInflater inflater;
    private Context mContext;
    
    public DialogCursorAdapter(Context context, Cursor c, int flags){
        super(context,c,flags);
        inflater = LayoutInflater.from(context);
        this.mContext=context;
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor){
		final TextView portName = (TextView) view
				.findViewById(R.id.start_port_name);
		portName.setText(cursor.getString(cursor
				.getColumnIndex(ProcessDatabaseHelper.COLUMN_NAME)));
		final TextView portType = (TextView) view
				.findViewById(R.id.start_port_type);
		portType.setText(cursor.getString(cursor
				.getColumnIndex(ProcessDatabaseHelper.COLUMN_TYPE)));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.process_start_list_row, parent, false);
        bindView(v,context,cursor);
        return v;
    }

}