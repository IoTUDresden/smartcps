package eu.vicci.ecosystem.standalone.controlcenter.android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.common.Common;

/**
 * The ToastDialog.
 */
public class ToastDialog extends DialogFragment {

	private String text;
	private String buttonText;
	private Boolean showButton = false;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Resources resources = SmartCPS_Impl.getAppContext().getResources();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		TextView tvMessage = new TextView(SmartCPS_Impl.getAppContext());
		tvMessage.setText(text);
		tvMessage.setGravity(Gravity.CENTER_HORIZONTAL);
		tvMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				resources.getDimensionPixelSize(R.dimen.tsToast));
		Integer paddingToast = resources
				.getDimensionPixelSize(R.dimen.paddingToast);
		tvMessage.setPadding(paddingToast, paddingToast, paddingToast,
				paddingToast);
		tvMessage.setTextColor(Common.getThemedColor(getActivity().getTheme(),
				R.attr.textColor));
		builder.setView(tvMessage);
		if (this.showButton) {
			builder.setPositiveButton(buttonText,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						}
					});
		}
		return builder.create();
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * 
	 * @param text
	 *            the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets the button text.
	 * 
	 * @return the button text
	 */
	public String getButtonText() {
		return buttonText;
	}

	/**
	 * Sets the button text.
	 * 
	 * @param buttonText
	 *            the new button text
	 */
	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	/**
	 * Gets the show button.
	 * 
	 * @return the show button
	 */
	public Boolean getShowButton() {
		return showButton;
	}

	/**
	 * Sets the show button.
	 * 
	 * @param showButton
	 *            the new show button
	 */
	public void setShowButton(Boolean showButton) {
		this.showButton = showButton;
	}
}
