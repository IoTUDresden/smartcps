package eu.vicci.ecosystem.standalone.controlcenter.android.services;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.ContextManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.AgeContext;

/**
 * The HelpReminderService for reminding memory handicapped persons of the help
 * function.
 */
public class HelpReminderService {

	public static final Integer MESSAGE_DELAY = 10000;
	public static final Integer MESSAGE_PERIOD = 300;
	public static final Integer MESSAGE_DISPLAY_TIME = 10000;
	public static final Integer ACTION_BAR_ITEM_BLINK_TIMES = 5;
	private static MenuItem helpMenuItem;
	private static Activity activity;
	private static Timer timer;
	private static Boolean showMessage = true;

	/**
	 * Sets the help menu item.
	 * 
	 * @param helpMenuItem
	 *            the new help menu item
	 */
	public static void setHelpMenuItem(MenuItem helpMenuItem) {
		HelpReminderService.helpMenuItem = helpMenuItem;
	}

	/**
	 * Sets the activity.
	 * 
	 * @param activity
	 *            the new activity
	 */
	public static void setActivity(Activity activity) {
		HelpReminderService.activity = activity;
	}

	/**
	 * Start the service.
	 */
	public static void startService() {
		if (!Common.getBooleanPreference("settings_ui_help_reminder", true)) {
			showMessage = false;
			return;
		}

		// get the help drawable for the current design
		TypedArray a = activity.getTheme().obtainStyledAttributes(R.style.AppTheme,
				new int[] { R.attr.actionHelpDrawable });
		int attributeResourceId = a.getResourceId(0, 0);
		final Drawable helpDrawable = activity.getResources().getDrawable(attributeResourceId);
		a.recycle();

		showMessage = true;
		if (timer != null)
			timer.cancel();
		timer = new Timer();
		timer.schedule(new TimerTask() {
			Integer count = 0;

			@Override
			public void run() {
				if (!showMessage)
					return;
				activity.runOnUiThread(new Runnable() {

					ViewGroup clickableToast;

					@Override
					public void run() {
						if (count != 0 && count <= 2 * ACTION_BAR_ITEM_BLINK_TIMES) {
							if (count % 2 == 0)
								helpMenuItem.setIcon(helpDrawable);
							else
								helpMenuItem.setIcon(R.drawable.action_help_blue);
							count++;
						} else if (count != 0 && count < 120) {
							count++;
						} else if (count == 120 || count == 0) {
							count = 1;
							clickableToast = (ViewGroup) Common.addNewView(
									R.layout._clickable_toast,
									(ViewGroup) ((ViewGroup) activity.getWindow().getDecorView()
											.findViewById(android.R.id.content)).getChildAt(0),
									activity.getLayoutInflater());

							/*
							 * adaptation: check for age context -> different
							 * salutation
							 */
							AgeContext ageContext = (AgeContext) ContextManager
									.getRegisteredContextByClass(AgeContext.class);

							if (ContextManager.getContextModel().hasContextPropertyByGroup(
									ageContext,
									ageContext != null ? ageContext.getGroupByName(AgeContext.CONTEXT_GROUP_CHILDREN)
											: null))
								((TextView) clickableToast.findViewById(R.id.transient_notification_text))
										.setText(Common
												.formatStringResourceToHtml(R.string.help_reminderToast_Children));
							else
								((TextView) clickableToast.findViewById(R.id.transient_notification_text))
										.setText(Common.formatStringResourceToHtml(R.string.help_reminderToast));
							/* adaptation end */

							clickableToast.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									clickableToast.startAnimation(AnimationUtils.loadAnimation(activity,
											R.anim.fade_out));
									clickableToast.setVisibility(View.GONE);
									showMessage = false;
									helpMenuItem.setVisible(true);
									return;
								}
							});

							clickableToast.setOnLongClickListener(new OnLongClickListener() {
								@Override
								public boolean onLongClick(View v) {
									Builder alertDialogBuilder = new Builder(activity);

									/*
									 * adaptation: check for age context ->
									 * different salutation
									 */
									AgeContext ageContext = (AgeContext) ContextManager
											.getRegisteredContextByClass(AgeContext.class);

									if (ContextManager.getContextModel().hasContextPropertyByGroup(
											ageContext,
											ageContext != null ? ageContext
													.getGroupByName(AgeContext.CONTEXT_GROUP_CHILDREN) : null))
										alertDialogBuilder.setMessage(activity.getResources().getString(
												R.string.help_reminderStopDisplaying_Children));
									else
										alertDialogBuilder.setMessage(activity.getResources().getString(
												R.string.help_reminderStopDisplaying));
									/* adaptation end */
									alertDialogBuilder.setPositiveButton(
											activity.getResources().getString(R.string.btnYes),
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(DialogInterface dialog, int which) {
													Common.setBooleanPreference("settings_ui_help_reminder", false);
													clickableToast.startAnimation(AnimationUtils.loadAnimation(
															activity, R.anim.fade_out));
													clickableToast.setVisibility(View.GONE);
													showMessage = false;
												}
											});
									alertDialogBuilder.setNegativeButton(
											activity.getResources().getString(R.string.btnNo),
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(DialogInterface dialog, int which) {
												}
											});
									alertDialogBuilder.show();
									return true;
								}
							});

							clickableToast.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.fade_in));
							new Handler().postDelayed(new Runnable() {
								public void run() {
									// check, if already hidden by the user
									if (clickableToast.getVisibility() != View.GONE) {
										clickableToast.startAnimation(AnimationUtils.loadAnimation(activity,
												R.anim.fade_out));
										clickableToast.setVisibility(View.GONE);
									}
								}
							}, MESSAGE_DISPLAY_TIME);
						}
					}
				});
			}
		}, MESSAGE_DELAY, MESSAGE_PERIOD);
	}
}
