package eu.vicci.ecosystem.processview.android.internal.dialog;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import eu.vicci.process.model.util.messages.core.IStateChangeMessage;

/**
 * Created by andre on 01.12.2016.
 */

public class ProcessValuesListAdapter extends BaseAdapter {
    private List<ProcessValue> values = new ArrayList<>();
    private final Activity context;

    public ProcessValuesListAdapter(Activity context){
        this.context = context;
    }

    public void fillWith(IStateChangeMessage message){
        values.clear();
        if(message == null) return;
        values.add(new NameValue(context, message.getProcessName(), message.getState()));
        values.add(new DataPortValue.StartDataPortValue(context, message.getStartDataPorts().values() ));
        values.add(new ControlPortValue.StartControlPortValue(context, message.getStartControlPorts().values()));
        values.add(new DataPortValue.EndDataPortValue(context, message.getEndDataPorts().values()));
        values.add(new ControlPortValue.EndControlPortValue(context, message.getEndControlPorts().values()));
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public ProcessValue getItem(int position) {
        return position >= getCount() ? null : values.get(position);
    }

    @Override
    public long getItemId(int position) {
        ProcessValue obj = getItem(position);
        return obj == null ? 0 : obj.hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProcessValue obj = getItem(position);
        return obj == null ? null : obj.getView();
    }
}
