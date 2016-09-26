package eu.vicci.ecosystem.standalone.controlcenter.android.activities.general;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;

/**
 * The BreadcrumbsActivity for normal activities.
 */
public abstract class BreadcrumbsActivity extends Activity {

	public static final String PARCEL_KEY = "BreadcrumbHistory";
	
	private BreadcrumbsParcelable breadcrumbHistory = new BreadcrumbsParcelable(
            new ArrayList<String>());

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

		LinearLayout linearLayout = new LinearLayout(actionBar.getThemedContext());
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		TextView tvDivider = new TextView(actionBar.getThemedContext());
		tvDivider.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
		tvDivider.setBackgroundColor(getResources().getColor(R.color.clBreadcrumbsDividerDark));
		linearLayout.addView(tvDivider);
		for (int i = 0; i < breadcrumbHistory.getBreadcrumbHistory().size(); i++) {
			final TextView tvBreadcrumb = new TextView(actionBar.getThemedContext());
			tvBreadcrumb.setText(breadcrumbHistory.getBreadcrumbHistory().get(i));
			tvBreadcrumb.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					getResources().getDimensionPixelSize(R.dimen.tsBreadcrumbs));
			tvBreadcrumb.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
			tvBreadcrumb.setPadding(getResources().getDimensionPixelSize(R.dimen.paddingVerticalBreadcrumbs), 0,
					getResources().getDimensionPixelSize(R.dimen.paddingVerticalBreadcrumbs), 0);
			tvBreadcrumb.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			linearLayout.addView(tvBreadcrumb);

			// splitter ">"
			if (i < breadcrumbHistory.getBreadcrumbHistory().size() - 1) {
				TextView tvSplitter = new TextView(actionBar.getThemedContext());
				tvSplitter.setText(">");
				tvSplitter.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						getResources().getDimensionPixelSize(R.dimen.tsBreadcrumbs));
				tvSplitter.setTypeface(null, Typeface.BOLD);
				tvSplitter.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
				tvSplitter.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
				linearLayout.addView(tvSplitter);
			}
		}
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(linearLayout);
		setBreadcrumbHistory(breadcrumbHistory);
	}

	/**
	 * Gets the breadcrumb history as a parcelable.
	 * 
	 * @return the breadcrumb history
	 */
	public BreadcrumbsParcelable getBreadcrumbHistory() {
		return breadcrumbHistory;
	}

	/**
	 * Sets the breadcrumb history as a parcelable.
	 * 
	 * @param breadcrumbHistory
	 *            the new breadcrumb history
	 */
	public void setBreadcrumbHistory(BreadcrumbsParcelable breadcrumbHistory) {
		this.breadcrumbHistory = breadcrumbHistory;
	}


}
