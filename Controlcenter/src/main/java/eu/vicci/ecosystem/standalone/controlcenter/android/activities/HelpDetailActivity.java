package eu.vicci.ecosystem.standalone.controlcenter.android.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsFragmentActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsParcelable;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;
import eu.vicci.ecosystem.standalone.controlcenter.android.fragments.HelpDetailFragment;

/**
 * An activity representing a single Help detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link HelpListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link HelpDetailFragment}.
 */
public class HelpDetailActivity extends BreadcrumbsFragmentActivity {

	private static BreadcrumbsParcelable breadcrumbHistory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Common.setActivityTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_detail);

		// check for existing fragments
		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(HelpDetailFragment.HELP_ITEM_KEY, getIntent()
					.getStringExtra(HelpDetailFragment.HELP_ITEM_KEY));
			HelpDetailFragment fragment = new HelpDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.help_detail_container, fragment).commit();
		}

		breadcrumbHistory = new BreadcrumbsParcelable(new ArrayList<String>());
		initBreadcrumbs(breadcrumbHistory);

		if (!Common.isXLargeTablet(this))
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// the home or up button -> in this case the up button
		case android.R.id.home:
			NavUtils.navigateUpTo(this,
					new Intent(this, HelpListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public BreadcrumbsParcelable getBreadcrumbHistory() {
		return breadcrumbHistory;
	}

	public void setBreadcrumbHistory(BreadcrumbsParcelable breadcrumbHistory) {
		HelpDetailActivity.breadcrumbHistory = breadcrumbHistory;
	}
}
