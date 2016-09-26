package eu.vicci.ecosystem.standalone.controlcenter.android.activities.general;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.preference.PreferenceActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.HelpListActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;

/**
 * The Class BreadcrumbsPreferenceActivity.
 */
public abstract class BreadcrumbsPreferenceActivity extends PreferenceActivity {

	public static final String PARCEL_KEY = "BreadcrumbHistory";

	/**
	 * Initializes the breadcrumbs.
	 * 
	 * @param breadcrumbHistory
	 *            the breadcrumb history
	 */
	public void initBreadcrumbs(BreadcrumbsParcelable breadcrumbHistory) {
		if (!Common.isXLargeTablet(this))
			return;
		ActionBar actionBar = getActionBar();
		LinearLayout linearLayout = new LinearLayout(
				actionBar.getThemedContext());
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		TextView tvDivider = new TextView(actionBar.getThemedContext());
		tvDivider
				.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
		tvDivider.setBackgroundColor(Common.getThemedColor(getTheme(),
				R.attr.breadcrumbsDividerColor));
		linearLayout.addView(tvDivider);
		for (int i = 0; i < breadcrumbHistory.getBreadcrumbHistory().size(); i++) {
			final TextView tvBreadcrumb = new TextView(
					actionBar.getThemedContext());
			tvBreadcrumb.setText(breadcrumbHistory.getBreadcrumbHistory()
					.get(i));
			tvBreadcrumb.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
					.getDimensionPixelSize(R.dimen.tsBreadcrumbs));
			tvBreadcrumb.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
			tvBreadcrumb.setPadding(
					getResources().getDimensionPixelSize(
							R.dimen.paddingVerticalBreadcrumbs),
					0,
					getResources().getDimensionPixelSize(
							R.dimen.paddingVerticalBreadcrumbs), 0);
			tvBreadcrumb.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			linearLayout.addView(tvBreadcrumb);

			// splitter ">"
			if (i < breadcrumbHistory.getBreadcrumbHistory().size() - 1) {
				TextView tvSplitter = new TextView(actionBar.getThemedContext());
				tvSplitter.setText(">");
				tvSplitter.setTextSize(
						TypedValue.COMPLEX_UNIT_PX,
						getResources().getDimensionPixelSize(
								R.dimen.tsBreadcrumbs));
				tvSplitter.setTypeface(null, Typeface.BOLD);
				tvSplitter.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
				tvSplitter.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
				linearLayout.addView(tvSplitter);
			}
		}
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(linearLayout);
		getActionBar().setDisplayShowTitleEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_help:
			Common.startBreadcrumbsActivity(this, HelpListActivity.class);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Gets the breadcrumb history as a parcelable.
	 * 
	 * @return the breadcrumb history
	 */
	public abstract BreadcrumbsParcelable getBreadcrumbHistory();

	/**
	 * Sets the breadcrumb history as a parcelable
	 * 
	 * @param breadcrumbHistory
	 *            the new breadcrumb history
	 */
	public abstract void setBreadcrumbHistory(
			BreadcrumbsParcelable breadcrumbHistory);

}
