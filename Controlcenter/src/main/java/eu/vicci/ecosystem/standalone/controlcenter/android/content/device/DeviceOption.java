package eu.vicci.ecosystem.standalone.controlcenter.android.content.device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;

/**
 * Optionen für Devices
 *
 * @author André Kühnert
 */
public class DeviceOption {

    /**
     * Gibt an, ob Device umbenannt werden kann
     */
    public static final String CAN_RENAME = 
    		SmartCPS_Impl.getAppContext().getString(R.string.deviceOption_can_rename);

    /**
     * Gibt an, ob der Raum fÃ¼r das GerÃ¤t geÃ¤ndert werden kann
     */
    public static final String CAN_CHANGE_ROOM = 
    		SmartCPS_Impl.getAppContext().getString(R.string.deviceOption_can_change_room);

    /**
     * Gibt an, ob die Visualisierung angepasst werden kann
     */
    public static final String CAN_CHANGE_VISUALISATION = 
    		SmartCPS_Impl.getAppContext().getString(R.string.deviceOption_can_change_vis);

    /**
     * Gibt an, ob Grenzwerte angepasst werden können
     */
    public static final String CAN_CHANGE_MIN_MAX = 
    		SmartCPS_Impl.getAppContext().getString(R.string.deviceOption_can_change_min_max);

    /**
     * Gibt an, ob das GerÃ¤t ausgeblendet werden kann
     */
    public static final String CAN_HIDE = 
    		SmartCPS_Impl.getAppContext().getString(R.string.deviceOption_can_hide);

    /**
     * Liefert Liste, welche immer gleich sortiert ist
     *
     * @param params - Optionen welche gewählt werden können
     * @return leere Liste falls keine Parameter angegeben
     */
    public static List<String> getOptionsArray(String... params) {
        List<String> strings = Arrays.asList(params);
        List<String> output = new ArrayList<String>();
        if (strings.contains(CAN_RENAME))
            output.add(CAN_RENAME);
        if (strings.contains(CAN_CHANGE_ROOM))
            output.add(CAN_CHANGE_ROOM);
        if (strings.contains(CAN_CHANGE_VISUALISATION))
            output.add(CAN_CHANGE_VISUALISATION);
        if (strings.contains(CAN_CHANGE_MIN_MAX))
            output.add(CAN_CHANGE_MIN_MAX);
        if (strings.contains(CAN_HIDE))
            output.add(CAN_HIDE);

        return output;
    }
}
