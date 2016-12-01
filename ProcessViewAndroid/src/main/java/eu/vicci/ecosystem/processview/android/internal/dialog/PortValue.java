package eu.vicci.ecosystem.processview.android.internal.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.core.IJSONPortInstance;

/**
 * Created by andre on 01.12.2016.
 */

public abstract class PortValue extends ProcessValue {

    public PortValue(Activity context, String name) {
        super(context, name);
    }


    protected View createPortBaseInfoView(IJSONPortInstance port){
        LinearLayout group = new LinearLayout(getContext());
        TextView lPName = new TextView(getContext());
        lPName.setText("  " + port.getName());

        LinearLayout stateGroup = new LinearLayout(getContext());
        stateGroup.setOrientation(LinearLayout.HORIZONTAL);

        TextView lLabel = new TextView(getContext());
        lLabel.setText("    State:");

        TextView lState = new TextView(getContext());
        lState.setText("  " + port.getExecutionState().toString());

        stateGroup.addView(lLabel);
        stateGroup.addView(lState);

        group.addView(lPName);
        group.addView(stateGroup);
        return group;
    }
}
