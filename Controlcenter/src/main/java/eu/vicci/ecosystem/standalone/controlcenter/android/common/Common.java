package eu.vicci.ecosystem.standalone.controlcenter.android.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.javatuples.Quartet;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsFragmentActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.activities.general.BreadcrumbsPreferenceActivity;
import eu.vicci.ecosystem.standalone.controlcenter.android.fragments.ToastDialog;

/**
 * The Common class for offering helper methods.
 */
@SuppressWarnings("rawtypes")
public class Common {

    /**
     * Gets the content view as relative layout.
     * 
     * @param activity
     *            the activity
     * @return the content view relative layout
     */
    public static RelativeLayout getContentViewRelativeLayout(Activity activity) {
        ViewGroup currentViewGroup = (ViewGroup) activity.getWindow()
                .getDecorView();
        RelativeLayout contentView = (RelativeLayout) currentViewGroup
                .getChildAt(0);
        return contentView;
    }

    /**
     * Formats all text views to html.
     * 
     * @param contentViewGroup
     *            the content view group
     */
    public static void formatTextViewsToHtml(ViewGroup contentViewGroup) {
        for (int i = 0; i < contentViewGroup.getChildCount(); i++) {
            if (contentViewGroup.getChildAt(i) instanceof TextView) {
                // escape % signs for the formatter
                CharSequence styledText = Html.fromHtml(String
                        .format(((TextView) contentViewGroup.getChildAt(i))
                                .getText().toString().replaceFirst("%", "%%")));
                ((TextView) contentViewGroup.getChildAt(i)).setText(styledText);
            } else if (contentViewGroup.getChildAt(i) instanceof ViewGroup) {
                formatTextViewsToHtml((ViewGroup) contentViewGroup
                        .getChildAt(i));
            }
        }
    }

    /**
     * Format a string resource to html.
     * 
     * @param resourceId
     *            the resource id
     * @return the char sequence
     */
    public static CharSequence formatStringResourceToHtml(Integer resourceId) {
        return Html.fromHtml(String.format(SmartCPS_Impl.getAppContext()
                .getResources().getString(resourceId)));
    }

    /**
     * Show toast.
     * 
     * @param text
     *            the text
     * @param applicationContext
     *            the application context
     */
    public static void showToast(String text, Context applicationContext) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show();
    }

    /**
     * Show a toast dialog.
     * 
     * @param text
     *            the text
     * @param activity
     *            the activity
     */
    public static void showToastDialog(String text, Activity activity) {
        ToastDialog toastDialog = new ToastDialog();
        toastDialog.setText(text);
        toastDialog.show(activity.getFragmentManager(), "ToastDialogFragment");
    }

    /**
     * Shows a buttoned toast dialog.
     * 
     * @param text
     *            the text
     * @param buttonText
     *            the button text
     * @param activity
     *            the activity
     */
    public static void showButtonedToastDialog(String text, String buttonText,
            Activity activity) {
        ToastDialog toastDialog = new ToastDialog();
        toastDialog.setText(text);
        toastDialog.setButtonText(buttonText);
        toastDialog.setShowButton(true);
        toastDialog.show(activity.getFragmentManager(), "ToastDialogFragment");
    }

    /**
     * Starts a breadcrumbs activity and puts the according extras.
     * 
     * @param fromActivity
     *            the from activity
     * @param toClass
     *            the to class
     */
    public static void startBreadcrumbsActivity(
            BreadcrumbsFragmentActivity fromActivity, Class toClass) {
        Intent intent = new Intent(fromActivity.getApplicationContext(),
                toClass);
        intent.putExtra(BreadcrumbsActivity.PARCEL_KEY,
                fromActivity.getBreadcrumbHistory());
        fromActivity.startActivity(intent);
    }

    /**
     * Starts a breadcrumbs activity and puts the according extras.
     * 
     * @param fromActivity
     *            the from activity
     * @param toClass
     *            the to class
     */
    public static void startBreadcrumbsActivity(
            BreadcrumbsActivity fromActivity, Class toClass) {
        Intent intent = new Intent(fromActivity.getApplicationContext(),
                toClass);
        intent.putExtra(BreadcrumbsActivity.PARCEL_KEY,
                fromActivity.getBreadcrumbHistory());
        fromActivity.startActivity(intent);
    }

    /**
     * Starts a breadcrumbs activity and puts the according extras.
     * 
     * @param fromActivity
     *            the from activity
     * @param toClass
     *            the to class
     * @param key
     *            the key
     * @param extra
     *            the extra
     */
    public static void startBreadcrumbsActivityWithStringExtra(
            BreadcrumbsFragmentActivity fromActivity, Class toClass,
            String key, String extra) {
        Intent intent = new Intent(fromActivity.getApplicationContext(),
                toClass);
        intent.putExtra(BreadcrumbsActivity.PARCEL_KEY,
                fromActivity.getBreadcrumbHistory());
        intent.putExtra(key, extra);
        fromActivity.startActivity(intent);
    }

    /**
     * Starts a breadcrumbs activity and puts the according extras.
     * 
     * @param fromActivity
     *            the from activity
     * @param toClass
     *            the to class
     */
    public static void startBreadcrumbsActivity(
            BreadcrumbsPreferenceActivity fromActivity, Class toClass) {
        Intent intent = new Intent(fromActivity.getApplicationContext(),
                toClass);
        intent.putExtra(BreadcrumbsActivity.PARCEL_KEY,
                fromActivity.getBreadcrumbHistory());
        fromActivity.startActivity(intent);
    }

    /**
     * Gets a visible view's environment. For the list [1,2,3,4,5] the
     * environment of 3 is [2,3,4,5]
     * 
     * @param views
     *            the views
     * @return the visible views environment
     */
    public static Quartet<View, View, View, View> getVisibleViewsEnvironment(
            List<View> views) {
        for (View currentView : views) {
            if (currentView.getVisibility() == View.VISIBLE) {
                int i = views.indexOf(currentView);
                return new Quartet<View, View, View, View>(i - 1 < 0 ? null
                        : views.get(i - 1), currentView,
                        i + 1 >= views.size() ? null : views.get(i + 1),
                                i + 2 >= views.size() ? null : views.get(i + 2));
            }
        }
        return null;
    }

    /**
     * Draws a resized bitmap.
     * 
     * @param bitmap
     *            the bitmap
     * @param canvas
     *            the canvas
     * @param width
     *            the width
     * @param height
     *            the height
     * @param x
     *            the x
     * @param y
     *            the y
     * @param paint
     *            the paint
     */
    public static void drawResizedBitmap(Bitmap bitmap, Canvas canvas,
            Integer width, Integer height, Float x, Float y, Paint paint) {
        Bitmap scaledImage = Bitmap.createScaledBitmap(bitmap, width, height,
                true);
        canvas.drawBitmap(scaledImage, x, y, paint);
    }

    /**
     * Gets the themed attribute specified in R.attr.
     * 
     * @param theme
     *            the theme
     * @param attr
     *            the attr
     * @return the themed attr
     */
    public static TypedValue getThemedAttr(Theme theme, int attr) {
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(attr, typedValue, true);
        return typedValue;
    }

    /**
     * Gets a themed color.
     * 
     * @param theme
     *            the theme
     * @param attr
     *            the attr
     * @return the themed color
     */
    public static int getThemedColor(Theme theme, int attr) {
        return Color.parseColor(getThemedAttr(theme, attr).coerceToString()
                .toString());
    }

    /**
     * Gets a string based on the current theme. -> e.g. for color strings
     * 
     * @param theme
     *            the theme
     * @param attr
     *            the attr
     * @return the themed string
     */
    public static String getThemedString(Theme theme, int attr) {
        return SmartCPS_Impl.getAppContext().getResources()
                .getString(getThemedAttr(theme, attr).resourceId);
    }

    /**
     * Gets a themed drawable.
     * 
     * @param theme
     *            the theme
     * @param attr
     *            the attr
     * @param resources
     *            the resources
     * @return the themed drawable
     */
    public static Drawable getThemedDrawable(Theme theme, int attr,
            Resources resources) {
        return resources.getDrawable(getThemedAttr(theme, attr).resourceId);
    }

    /**
     * Adds a new view.
     * 
     * @param resourceId
     *            the resource id
     * @param rootElement
     *            the root element
     * @param layoutInflater
     *            the layout inflater
     * @return the view
     */
    public static View addNewView(Integer resourceId, ViewGroup rootElement,
            LayoutInflater layoutInflater) {
        return ((ViewGroup) layoutInflater.inflate(resourceId, rootElement,
                true)).getChildAt(rootElement.getChildCount() - 1);
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    public static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Sets the activity's theme.
     * 
     * @param activity
     *            the new activity theme
     */
    public static void setActivityTheme(Activity activity) {
        if (PreferenceManager
                .getDefaultSharedPreferences(SmartCPS_Impl.getAppContext())
                .getString("settings_ui_theme", "0").equals("1"))
            activity.setTheme(R.style.ThemeDark);
        else
            activity.setTheme(R.style.ThemeLight);
    }

    /**
     * Gets a boolean preference.
     * 
     * @param key
     *            the key
     * @param defaultValue
     *            the default value
     * @return the boolean preference
     */
    public static Boolean getBooleanPreference(String key, Boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(
                SmartCPS_Impl.getAppContext()).getBoolean(key, defaultValue);
    }

    /**
     * Gets a string preference.
     * 
     * @param key
     *            the key
     * @param defaultValue
     *            the default value
     * @return the string preference
     */
    public static String getStringPreference(String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(
                SmartCPS_Impl.getAppContext()).getString(key, defaultValue);
    }

    /**
     * Sets a string preference.
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     */
    public static void setStringPreference(String key, String value) {
        PreferenceManager
        .getDefaultSharedPreferences(SmartCPS_Impl.getAppContext())
        .edit().putString(key, value).commit();
    }

    /**
     * Sets a boolean preference.
     * 
     * @param key
     *            the key
     * @param value
     *            the value
     */
    public static void setBooleanPreference(String key, Boolean value) {
        PreferenceManager
        .getDefaultSharedPreferences(SmartCPS_Impl.getAppContext())
        .edit().putBoolean(key, value).commit();
    }

    /**
     * Restart an activity.
     * 
     * @param activity
     *            the activity
     */
    public static void restartActivity(Activity activity) {
        Intent intent = activity.getIntent();
        // activity.overridePendingTransition(0, 0);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.finish();
        // activity.overridePendingTransition(0, 0);
        activity.startActivity(intent);
    }

    /**
     * Gets the current date and time.
     * 
     * @param pattern
     *            the pattern
     * @return the date time
     */
    public static String getDateTime(String pattern) {
        return new SimpleDateFormat(pattern, Locale.GERMANY).format(Calendar
                .getInstance().getTime());
    }

    /**
     * Vibrates the device.
     * 
     * @param context
     *            the context
     */
    public static void vibrate(Context context) {
        ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE))
        .vibrate(100);
    }

    public static void addFragments(int containerId,
            FragmentManager fragmentManager, Fragment... fragments) {
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        for (Fragment currentFragment : fragments) {
            fragmentTransaction.add(containerId, currentFragment);
        }

        fragmentTransaction.commit();
    }

    /**
     * Adds fragments to a container view.
     * 
     * @param containerId
     *            the container id
     * @param fragmentManager
     *            the fragment manager
     * @param fragments
     *            the fragments
     */
    public static void addFragments(int containerId,
            FragmentManager fragmentManager, List<Fragment> fragments) {
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        for (Fragment currentFragment : fragments) {
            fragmentTransaction.add(containerId, currentFragment);
        }

        fragmentTransaction.commit();
    }



}
