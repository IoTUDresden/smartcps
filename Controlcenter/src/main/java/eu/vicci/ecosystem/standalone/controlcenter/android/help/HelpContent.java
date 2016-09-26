package eu.vicci.ecosystem.standalone.controlcenter.android.help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.res.Resources;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;
import eu.vicci.ecosystem.standalone.controlcenter.android.SmartCPS_Impl;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.ContextManager;
import eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context.concrete.AgeContext;

/**
 * The class containing help content.
 */
public class HelpContent {

	private static List<HelpItem> helpItems = new ArrayList<HelpItem>();
	private static Map<String, HelpItem> helpItemsMap = new HashMap<String, HelpItem>();

	/**
	 * Adds the help item.
	 * 
	 * @param item
	 *            the item
	 */
	public static void addHelpItem(HelpItem item) {
		helpItems.add(item);
		helpItemsMap.put(item.getId(), item);
	}

	/**
	 * Removes the all help items.
	 */
	public static void removeAllHelpItems() {
		helpItems.clear();
		helpItemsMap.clear();
	}

	/**
	 * Gets the help items.
	 * 
	 * @return the help items
	 */
	public static List<HelpItem> getHelpItems() {
		return helpItems;
	}

	/**
	 * Gets the help items map.
	 * 
	 * @return the help items map
	 */
	public static Map<String, HelpItem> getHelpItemsMap() {
		return helpItemsMap;
	}

	/**
	 * Adds the general adapted help items.
	 */
	public static void addGeneralAdaptedHelpItems() {
		Resources resources = SmartCPS_Impl.getAppContext().getResources();
		/* adaptation: check for age context -> different salutation */
		AgeContext ageContext = (AgeContext) ContextManager
				.getRegisteredContextByClass(AgeContext.class);

		if (ContextManager.getContextModel().hasContextPropertyByGroup(
				ageContext,
				ageContext != null ? ageContext
						.getGroupByName(AgeContext.CONTEXT_GROUP_CHILDREN)
						: null)) {
			addHelpItem(new HelpItem(
					resources
							.getString(R.string.help_whatIsSwiping_txtHeadline),
					resources
							.getString(R.string.help_whatIsSwiping_contentHtml_Children)));
			addHelpItem(new HelpItem(
					resources
							.getString(R.string.help_whatIsLongClick_txtHeadline),
					resources
							.getString(R.string.help_whatIsLongClick_contentHtml_Children)));
		} else {
			addHelpItem(new HelpItem(
					resources
							.getString(R.string.help_whatIsSwiping_txtHeadline),
					resources
							.getString(R.string.help_whatIsSwiping_contentHtml)));
			addHelpItem(new HelpItem(
					resources
							.getString(R.string.help_whatIsLongClick_txtHeadline),
					resources
							.getString(R.string.help_whatIsLongClick_contentHtml)));
		}
		/* adaptation end */
	}

	/**
	 * Adds the general help items.
	 */
	public static void addGeneralHelpItems() {
		Resources resources = SmartCPS_Impl.getAppContext().getResources();
		/* adaptation: check for age context -> different salutation */
		AgeContext ageContext = (AgeContext) ContextManager
				.getRegisteredContextByClass(AgeContext.class);

		if (ContextManager.getContextModel().hasContextPropertyByGroup(
				ageContext,
				ageContext != null ? ageContext
						.getGroupByName(AgeContext.CONTEXT_GROUP_CHILDREN)
						: null)) {
			addHelpItem(new HelpItem(
					resources
							.getString(R.string.help_whatIsSmartCPS_txtHeadline),
					resources
							.getString(R.string.help_whatIsSmartCPS_contentHtml_Children)));
			addHelpItem(new HelpItem(
					resources
							.getString(R.string.help_howDoesTheReelMenuWork_txtHeadline),
					resources
							.getString(R.string.help_howDoesTheReelMenuWork_contentHtml_Children)));
			addHelpItem(new HelpItem(
					resources
							.getString(R.string.help_howDoIControlADevice_txtHeadline),
					resources
							.getString(R.string.help_howDoIControlADevice_contentHtml_Children)));
			addHelpItem(new HelpItem(
					resources
							.getString(R.string.help_howDoIMoveADevice_txtHeadline),
					resources
							.getString(R.string.help_howDoIMoveADevice_contentHtml_Children)));
		} else {
			addHelpItem(new HelpItem(
					resources
							.getString(R.string.help_whatIsSmartCPS_txtHeadline),
					resources
							.getString(R.string.help_whatIsSmartCPS_contentHtml)));
			addHelpItem(new HelpItem(
					resources
							.getString(R.string.help_howDoesTheReelMenuWork_txtHeadline),
					resources
							.getString(R.string.help_howDoesTheReelMenuWork_contentHtml)));
			addHelpItem(new HelpItem(
					resources
							.getString(R.string.help_howDoIControlADevice_txtHeadline),
					resources
							.getString(R.string.help_howDoIControlADevice_contentHtml)));
			addHelpItem(new HelpItem(
					resources
							.getString(R.string.help_howDoIMoveADevice_txtHeadline),
					resources
							.getString(R.string.help_howDoIMoveADevice_contentHtml)));
		}
		/* adaptation end */
	}

}
