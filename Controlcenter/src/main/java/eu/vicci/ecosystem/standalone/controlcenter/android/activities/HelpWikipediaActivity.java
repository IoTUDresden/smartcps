package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import java.util.ArrayList;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsParcelable;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;

/**
 * The activity for showing further information in a wikipedia webview.
 */
public class HelpWikipediaActivity extends BreadcrumbsActivity {

    public static final String WIKIPEDIA_BASE_URL = "http://de.wikipedia.org/wiki/";
    public static final String WIKIPEDIA_LINK_KEY = "WikipediaLinkKey";
    private static Boolean restartActivity = false;
    private static BreadcrumbsParcelable breadcrumbHistory = new BreadcrumbsParcelable(
            new ArrayList<String>());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Common.setActivityTheme(this);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_wikipedia);

        
        if (!Common.isXLargeTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        WebView webView = (WebView) findViewById(R.id.helpWikipedia_webView);
        webView.setWebViewClient(new HelloWebViewClient());
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(getIntent().getExtras().getString(WIKIPEDIA_LINK_KEY));

        // SHK - get action bar for enabling navigation
        getActionBar().setBackgroundDrawable(new ColorDrawable(R.color.clActionBarTransparentBackground));
        getActionBar().setStackedBackgroundDrawable(new ColorDrawable(R.color.clActionBarOverlayColor));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getResources().getString(R.string.title_activity_wiki));
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onResume() {
        if (getRestartActivity()) {
            Common.restartActivity(this);
            setRestartActivity(false);
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    // SHK TODO going back messes up the breadcrumbhistory
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Common.startBreadcrumbsActivity(this, HelpListActivity.class);
                this.getBreadcrumbHistory().removeBreadcrumbHistoryItem("Hilfe");
                (this).overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public BreadcrumbsParcelable getBreadcrumbHistory() {
        return breadcrumbHistory;
    }

    @Override
    public void setBreadcrumbHistory(BreadcrumbsParcelable breadcrumbHistory) {
        HelpWikipediaActivity.breadcrumbHistory = breadcrumbHistory;
    }

    public static Boolean getRestartActivity() {
        return restartActivity;
    }

    public static void setRestartActivity(Boolean restartActivity) {
        HelpWikipediaActivity.restartActivity = restartActivity;
    }

}
