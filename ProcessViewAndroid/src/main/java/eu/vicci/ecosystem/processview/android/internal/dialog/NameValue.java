package eu.vicci.ecosystem.processview.android.internal.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import eu.vicci.process.model.sofiainstance.State;

/**
 * Created by andre on 01.12.2016.
 */

public class NameValue extends ProcessValue {
    private final String name;
    private final State state;

    public NameValue(Activity context, String name, State state) {
        super(context, "Process Name");
        this.name = name;
        this.state = state;
    }

    @Override
    public View getView() {
        LinearLayout group = new LinearLayout(getContext());
        group.setOrientation(LinearLayout.HORIZONTAL);

        TextView lName = new TextView(getContext());
        lName.setText(name);

        TextView lState = new TextView(getContext());
        lState.setText(state.toString());
        lState.setTextColor(getStateColor());

        group.addView(lName);
        group.addView(lState);
        return group;
    }

    private int getStateColor(){
        switch (state){
            case EXECUTED:
                return Color.GREEN;
            case FAILED:
                return Color.RED;
            default:
                return Color.GRAY;

        }
    }
}
