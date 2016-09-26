package eu.vicci.ecosystem.standalone.controlcenter.android.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.process.client.android.ProcessEngineClient;
import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;
import eu.vicci.process.kpseus.connect.handlers.HandlerFinishedListener;
import eu.vicci.process.kpseus.connect.handlers.SemanticPersonHandler;
import eu.vicci.process.model.util.messages.core.SemanticPerson;
import ws.wamp.jawampa.ApplicationError;

//TODO first check in showDialog, if connected and can receive users
// second prepare the received data in onPrepare
public class ActiveUserListPreference extends ListPreferenceWithSummary implements HandlerFinishedListener {
	private String dialogTitle;
	private String dialogBtn;
	private String errorMessage;
	private ProcessEngineClient pec;
	private Bundle lastState;
	private CharSequence[] oldEntries;
	private CharSequence[] oldValues;
	
	private List<SemanticPerson> lastReceivedPersons;

	public ActiveUserListPreference(Context context) {
		this(context, null);
	}

	public ActiveUserListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		dialogTitle = context.getString(R.string.usersettings_settings_error_dialog_title);
		dialogBtn = context.getString(R.string.usersettings_settings_error_dialog_button);
		pec = ProcessEngineClient.getInstance();
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		//set the new received data
		if(lastReceivedPersons == null){
			super.onPrepareDialogBuilder(builder);
			return;
		}
		
		setInitialEntriesAndValues();
		processReceivedEntriesAndValues();	
		super.onPrepareDialogBuilder(builder);
	}

	@Override
	protected void showDialog(Bundle state) {
		if (!pec.isConnected()) {
			errorMessage = getContext().getString(R.string.usersettings_settings_error_no_connection);
			showErrorDialogInUiThread();
			return;
		}

		// TODO Show waiting stuff or something
		lastState = state;
		SemanticPersonHandler handler = new SemanticPersonHandler();
		handler.addHandlerFinishedListener(this);
		pec.listSemanticPersons(handler);
	}
	
	private void processReceivedEntriesAndValues(){
		// we must use a arrayList here. asList will return a list, 
		// which does not implement add(Object)
		List<CharSequence> tmpEntries = new ArrayList<CharSequence>();
		if(oldEntries != null)
			tmpEntries.addAll(Arrays.asList(oldEntries));
			
		List<CharSequence> tmpValues = new ArrayList<CharSequence>();
		if(oldValues != null)
			tmpValues.addAll( Arrays.asList(oldValues));
		
		for (SemanticPerson p : lastReceivedPersons) {
			tmpEntries.add(p.getFirstName());
			tmpValues.add(p.getUid());
		}

		setEntries(tmpEntries.toArray(new CharSequence[tmpEntries.size()]));
		setEntryValues(tmpValues.toArray(new CharSequence[tmpValues.size()]));	
	}
	
	//setting the initial values only in the first round
	private void setInitialEntriesAndValues(){
		if(oldEntries == null && oldValues == null){
			oldEntries = getEntries();
			oldValues = getEntryValues();			
		}		
	}

	private AlertDialog getErrorDialog(String errorText) {
		return new AlertDialog.Builder(getContext()).setTitle(dialogTitle).setMessage(errorText)
				.setNegativeButton(dialogBtn, null).create();
	}

	@Override
	public void onHandlerFinished(AbstractClientHandler handler, Object arg) {
		if (handler.hasError()) {
			errorMessage = getErrorMessage(handler);
			if(errorMessage == null)
				errorMessage = getContext().getString(R.string.usersettings_settings_error_unknown);
			showErrorDialogInUiThread();
			return;
		}
		errorMessage = null;
		lastReceivedPersons = getListFromArg(arg);
		openDialogInUiThread();
	}
	
	@Override
	public CharSequence getSummary() {
		if(errorMessage == null)
			return super.getSummary();
		return errorMessage;
	}

	private static String getErrorMessage(AbstractClientHandler handler) {
		Throwable cause = handler.getLastError();
		if (cause == null || !(cause instanceof ApplicationError)) return null;
		ApplicationError error = (ApplicationError)cause;
		if(error.arguments().size() == 0) return null;
		return error.arguments().get(0).asText(null);
	}
	
	private void runInUiThread(Runnable runnable){
		Handler handler = new Handler(getContext().getMainLooper());
		handler.post(runnable);		
	}
	
	private void openDialogInUiThread(){
		runInUiThread(new Runnable() {			
			@Override
			public void run() {
				ActiveUserListPreference.super.showDialog(lastState);				
			}
		});		
	}

	private void showErrorDialogInUiThread() {
		runInUiThread(new Runnable() {
			@Override
			public void run() {
				getErrorDialog(errorMessage).show();
				setSummary(errorMessage);
				updateActivityIfPossible();
			}
		});
	}
	
	@SuppressWarnings("unchecked")//check is done
	private static List<SemanticPerson> getListFromArg(Object arg){
		if(arg != null 
		&& arg instanceof List<?> 
		&& !((List<?>) arg).isEmpty()){
			if(((List<?>) arg).get(0) instanceof SemanticPerson)
				return (List<SemanticPerson>)arg;
		}
		return new ArrayList<SemanticPerson>();
	}
	
	//FIXME summary wont update the error state
	private void updateActivityIfPossible(){
		OnPreferenceChangeListener listener = getOnPreferenceChangeListener();
		if(listener != null)
			listener.onPreferenceChange(this, getValue());
	}

}
