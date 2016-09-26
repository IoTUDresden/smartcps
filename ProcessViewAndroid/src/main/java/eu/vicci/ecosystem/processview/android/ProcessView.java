package eu.vicci.ecosystem.processview.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import eu.vicci.ecosystem.processview.android.internal.GraphLinksModel;
import eu.vicci.ecosystem.processview.android.internal.util.ModelCreationTool;
import eu.vicci.ecosystem.processview.android.internal.util.ProcessColors;
import eu.vicci.process.model.sofia.Process;
import eu.vicci.process.model.sofia.ProcessStep;
import eu.vicci.process.model.sofiainstance.State;

/**
 * {@link ProcessView} shows a {@link Process} within a {@link WebView}. The
 * process is rendered via javascript library and uses an auto layout. <br>
 * <br>
 * There are also callbacks from javacode possible, e.g. register a
 * {@link ProcessClickedListener} via
 * {@link #setProcessClickedListener(ProcessClickedListener)} to receive events,
 * if a process was clicked. <br>
 * <br>
 * Also calls to javascript code are possible. You can check this in the method
 * {@link #changeProcessState(String, State)}. You can use this method, to show
 * in the ui, that the state of a process has changed. <br>
 * <br>
 * For best compatibility and exchangeability (if you would later use other
 * views) don't use interfaces that belong to the android webview, just use the
 * {@link IProcessView} methods. For simplicity this class doesn't wrap the
 * webview. <br>
 * An other solution of a process view beside this, would be to export the
 * process model on server side as svg. You can add javascript code to svg for
 * callbacks or changing the colors. Unfortunately graphiti doesn't allow the
 * programmatically export of diagrams yet. <br>
 * <br>
 * <strong>Attention:</strong> <br>
 * To use this View, you have to copy all files from the assets folder, to the
 * app's assets folder. At the moment, this is the fastest and simplest
 * workaround, otherwise the internal webview failes to load the base html page.
 * 
 */
public class ProcessView extends WebView implements IProcessView {
	private static final String E_PROCESS_NULL = "process is null, cant show";
	private static final String E_MODEL_FAILED = "model generation failed. something wrong or special with the process model?";
	
	private static final String VIEW_HTML = "file:///android_asset/processView.html";

	private ProcessClickedListener processClickedListener;
	private ProcessViewReadyListener processViewReadyListener;
	private GraphLinksModel lastCreatedModel;

	public ProcessView(Context context) {
		super(context);
		applyDefaultSettings();
	}

	public ProcessView(Context context, AttributeSet attrs) {
		super(context, attrs);
		applyDefaultSettings();
	}

	public ProcessView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		applyDefaultSettings();
	}

	/**
	 * Must be called within a ui thread
	 */
	public void showProcess(Process process) {
		if (process == null) {
			Log.e(ProcessView.class.getSimpleName(), E_PROCESS_NULL);
			return;
		}
		setWebChromeClient(client);
		setWebViewClient(wvClient);
		createModel(process);
		loadUrl(VIEW_HTML);
	}

	public void setProcessClickedListener(ProcessClickedListener listener) {
		processClickedListener = listener;
	}

	public void unsetProcessClickedListener() {
		processClickedListener = null;
	}

	public void changeProcessState(ProcessStep process, State state) {
		changeProcessState(process.getId(), state);
	}

	public void changeProcessState(String processId, State state) {
		// calling javascript
		String color = getColorString(state);
		loadUrl("javascript:changeProcessState('" + processId + "', '" + color + "')");
	}

	private void createModel(Process process) {
		ModelCreationTool tool = new ModelCreationTool(process);
		lastCreatedModel = null;
		// cause we dont know if the model has some errors we catch all
		// exceptions, to prevent the app from crashing
		try {
			lastCreatedModel = tool.createModel();
		} catch (Exception e) {
			Log.e(ProcessView.class.getSimpleName(), E_MODEL_FAILED, e);
		}
	}

	private void drawModel(GraphLinksModel model) {
		if (model == null) {
			return;
		}
		String json = model.exportJson();
		loadUrl("javascript:doDiagram(" + json + ")");
	}

	private String getColorString(State state) {
		switch (state) {
		case ACTIVE:
			return ProcessColors.active;
		case EXECUTED:
			return ProcessColors.executed;
		case INACTIVE:
			return ProcessColors.inactive;
		case EXECUTING:
			return ProcessColors.executing;
		case WAITING:
			return ProcessColors.waiting;
		case DEACTIVATED:
			return ProcessColors.deactivated;
		default:
			return ProcessColors.default_color;
		}
	}
	
	private WebViewClient wvClient = new WebViewClient(){
		public void onPageFinished(WebView view, String url) {
			drawModel(lastCreatedModel);			
		};
		
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			Log.e(ProcessView.class.getSimpleName(), description);
			Toast.makeText(getContext(), description, Toast.LENGTH_LONG).show();			
		};			
	};
	
	private WebChromeClient client = new WebChromeClient(){
		
		public void onConsoleMessage(String message, int lineNumber, String sourceID) {
			Log.d(ProcessView.class.getSimpleName(), "Line: " + lineNumber + " in '" + sourceID + "': " + message);
		};		
	};

	@SuppressLint("SetJavaScriptEnabled") // shows security hints
	private void applyDefaultSettings() {
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		getSettings().setJavaScriptEnabled(true);
		addJavascriptInterface(this, "ProcessView");
	}

	/**
	 * Javascript Interface for the WebView. JavaScript in underlying WebView
	 * can call this Method.
	 * 
	 * @param processId
	 */
	@JavascriptInterface
	public void onProcessClicked(String processId) {
		if (processClickedListener != null)
			processClickedListener.onProcessClicked(processId);
	}
	
	/**
	 * Gets called if the process is finally shown in the view
	 */
	@JavascriptInterface
	public void onProcessViewReady(){
		if(processViewReadyListener != null)
			processViewReadyListener.onProcessViewIsReady();
	}
	
	@Override
	public void setProcessViewReadyListener(ProcessViewReadyListener listener) {
		processViewReadyListener = listener;		
	}

}
