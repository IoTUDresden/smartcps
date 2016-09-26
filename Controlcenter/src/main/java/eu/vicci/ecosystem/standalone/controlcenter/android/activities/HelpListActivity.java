package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsFragmentActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsParcelable;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;
import eu.vicci.ecosystem.standalone.controlcenter.android.fragments.HelpDetailFragment;
import eu.vicci.ecosystem.standalone.controlcenter.android.fragments.HelpListFragment;
import eu.vicci.ecosystem.standalone.controlcenter.android.listeners.TwoPaneItemSelectListener;

/**
 * An activity representing a list of Helps. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link HelpDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link HelpListFragment} and the item details (if present) is a
 * {@link HelpDetailFragment}.
 * <p>
 * This activity also implements the required {@link HelpListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class HelpListActivity extends BreadcrumbsFragmentActivity implements
TwoPaneItemSelectListener {

    private static BreadcrumbsParcelable breadcrumbHistory;
    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Common.setActivityTheme(this);
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_help_list);

        if (findViewById(R.id.help_detail_container) != null) {
            twoPane = true;
            HelpListFragment fragment = (HelpListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.help_list);
            fragment.setActivateOnItemClick(true);
            fragment.setActivatedPosition(0);
            onItemSelected("0");
        }

        if (!Common.isXLargeTablet(this))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // SHK - get action bar for enabling navigation
        getActionBar().setBackgroundDrawable(new ColorDrawable(R.color.clActionBarTransparentBackground));
        getActionBar().setStackedBackgroundDrawable(new ColorDrawable(R.color.clActionBarOverlayColor));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getResources().getString(R.string.title_activity_helplist));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onItemSelected(String id) {
        if (twoPane) {
            // In two-pane mode, show the detail view in this activity by a
            // fragment
            Bundle arguments = new Bundle();
            arguments.putString(HelpDetailFragment.HELP_ITEM_KEY, id);
            HelpDetailFragment fragment = new HelpDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
            .replace(R.id.help_detail_container, fragment).commit();

        } else {
            // In single-pane mode, simply start the detail activity for the
            // selected item ID.
            Intent detailIntent = new Intent(this, HelpDetailActivity.class);
            detailIntent.putExtra(HelpDetailFragment.HELP_ITEM_KEY, id);
            startActivity(detailIntent);
        }
    }

    // SHK - callback to handle navigation to parent activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
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
        HelpListActivity.breadcrumbHistory = breadcrumbHistory;
    }
}
