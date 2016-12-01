package eu.vicci.ecosystem.processview.android.internal.dialog;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Collection;
import java.util.List;

import eu.vicci.ecosystem.processview.android.internal.util.ProcessColors;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.core.IJSONDataPortInstance;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.core.IJSONPortInstance;

/**
 * Created by andre on 01.12.2016.
 */

public abstract class ControlPortValue extends PortValue {
    private final Collection<IJSONPortInstance> ports;


    public ControlPortValue(Activity context, String name, Collection<IJSONPortInstance> ports) {
        super(context, name);
        this.ports = ports;
    }

    @Override
    public View getView() {
        LinearLayout group = new LinearLayout(getContext());
        group.setOrientation(LinearLayout.VERTICAL);

        group.addView(createNameView());
        for (IJSONPortInstance port : ports) {
            addPortToRoot(group, port);
        }
        return group;
    }

    private void addPortToRoot(ViewGroup root, IJSONPortInstance port){
        root.addView(createPortBaseInfoView(port));
    }

    // -----------------------------------------
    // Implementations--------------------------
    //------------------------------------------

    public static class StartControlPortValue extends ControlPortValue {

        public StartControlPortValue(Activity context, Collection<IJSONPortInstance> ports) {
            super(context, "StartControl Ports", ports);
        }

    }

    public static class EndControlPortValue extends ControlPortValue {

        public EndControlPortValue(Activity context, Collection<IJSONPortInstance> ports) {
            super(context, "EndControl Ports", ports);
        }

    }
}
