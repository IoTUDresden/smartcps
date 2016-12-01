package eu.vicci.ecosystem.processview.android.internal.dialog;

import android.app.Activity;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;

/**
 * Created by andre on 01.12.2016.
 */

public abstract class ProcessValue {
    protected final static String ERR_MISSING = "ERROR: VALUE MISSING";


    private final String name;
    private final Activity context;


    public  ProcessValue(Activity context, String name){
        this.context = context;
        this.name = name;
    }

    /**
     * Gets the view for this type
     * @return
     */
    public abstract View getView();

    public String getName(){
        return name;
    }

    protected Activity getContext(){
        return context;
    }

    protected View createNameView(){
        TextView lName = new TextView(getContext());
        lName.setText(getName());
        return lName;
    }




}
