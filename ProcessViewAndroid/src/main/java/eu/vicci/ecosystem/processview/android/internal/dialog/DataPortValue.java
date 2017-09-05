package eu.vicci.ecosystem.processview.android.internal.dialog;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;

import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.core.IJSONDataPortInstance;

/**
 * Created by andre on 01.12.2016.
 */

public abstract class DataPortValue extends PortValue {
    private final Collection<IJSONDataPortInstance> ports;

    public DataPortValue(Activity context, String name, Collection<IJSONDataPortInstance> ports) {
        super(context, name);
        this.ports = ports;
    }

    @Override
    public View getView() {
        LinearLayout group = new LinearLayout(getContext());
        group.setOrientation(LinearLayout.VERTICAL);

        group.addView(createNameView());

        for (IJSONDataPortInstance port: ports) {
            addPortToRoot(group, port);
        }
        return group;
    }

    private void addPortToRoot(ViewGroup root, IJSONDataPortInstance port){
        root.addView(createPortBaseInfoView(port));

        LinearLayout group = new LinearLayout(getContext());
        group.setOrientation(LinearLayout.HORIZONTAL);

        TextView lLabel = new TextView(getContext());
        lLabel.setText("    Value:");

        TextView lValue = new TextView(getContext());
        String valueString = port.getDataTypeInstance() == null ? "" : port.getDataTypeInstance().getValueString();
        lValue.setText("  " + valueString);

        group.addView(lLabel);
        group.addView(lValue);
        root.addView(group);
    }


    // -----------------------------------------
    // Implementations--------------------------
    //------------------------------------------

    public static class EndDataPortValue extends DataPortValue {

        public EndDataPortValue(Activity context, Collection<IJSONDataPortInstance> ports) {
            super(context, "EndData Ports", ports);
        }
    }

    public static class StartDataPortValue extends DataPortValue {

        public StartDataPortValue(Activity context, Collection<IJSONDataPortInstance> ports) {
            super(context, "StartData Ports", ports);
        }

    }

}
