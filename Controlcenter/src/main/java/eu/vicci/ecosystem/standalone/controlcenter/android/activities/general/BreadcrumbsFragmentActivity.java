package eu.vicci.ecosystem.standalone.controlcenter.android.activities.general;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.HelpListActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.HomeActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.SettingsActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;

/**
 * The BreadcrumbsFragmentActivity.
 */
public abstract class BreadcrumbsFragmentActivity extends FragmentActivity {

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
        tvDivider.setBackgroundColor(getResources().getColor(R.color.clBreadcrumbsDividerDark));
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

            tvBreadcrumb.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    String toClass = (String) tvBreadcrumb.getText();

                    // TODO hard coding is bad
                    if ("Home".equals(toClass)) {
                        Common.startBreadcrumbsActivity(
                                (BreadcrumbsFragmentActivity) v.getContext(),
                                HomeActivity.class);
                        ((Activity) v.getContext()).overridePendingTransition(
                                R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    // Toast.makeText(getApplicationContext(),
                    // tvBreadcrumb.getText() + "was clicked!!", 2).show();
                }
            });

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_help:
                Common.startBreadcrumbsActivity(this, HelpListActivity.class);
                return true;
            case R.id.menu_settings:
                Common.startBreadcrumbsActivity(this, SettingsActivity.class);
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
     * Sets the breadcrumb history as a parcelable.
     * 
     * @param breadcrumbHistory
     *            the new breadcrumb history
     */
    public abstract void setBreadcrumbHistory(
            BreadcrumbsParcelable breadcrumbHistory);

}
