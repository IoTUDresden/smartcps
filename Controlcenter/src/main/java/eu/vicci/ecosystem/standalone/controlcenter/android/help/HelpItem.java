package eu.vicci.ecosystem.standalone.controlcenter.android.help;

/**
 * The HelpItem.
 */
public class HelpItem {

	private String id;
	private String headline;
	private String contentHtml;
	private Boolean contextHelpItem;

	/**
	 * Instantiates a new help item.
	 * 
	 * @param headline
	 *            the headline
	 * @param contentHtml
	 *            the content html
	 */
	public HelpItem(String headline, String contentHtml) {
		this.setId(String.valueOf(HelpContent.getHelpItems().size()));
		this.headline = headline;
		this.setContentHtml(contentHtml);
		this.contextHelpItem = false;
	}

	/**
	 * Instantiates a new help item.
	 * 
	 * @param headline
	 *            the headline
	 * @param contentHtml
	 *            the content html
	 * @param contextHelpItem
	 *            the context help item
	 */
	public HelpItem(String headline, String contentHtml, Boolean contextHelpItem) {
		this.setId(String.valueOf(HelpContent.getHelpItems().size()));
		this.headline = headline;
		this.setContentHtml(contentHtml);
		this.contextHelpItem = contextHelpItem;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the content html.
	 * 
	 * @return the content html
	 */
	public String getContentHtml() {
		return contentHtml;
	}

	/**
	 * Sets the content html.
	 * 
	 * @param contentHtml
	 *            the new content html
	 */
	public void setContentHtml(String contentHtml) {
		this.contentHtml = contentHtml;
	}

	/**
	 * Gets the headline.
	 * 
	 * @return the headline
	 */
	public String getHeadline() {
		return headline;
	}

	/**
	 * Sets the headline.
	 * 
	 * @param headline
	 *            the new headline
	 */
	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String toString() {
		return this.headline;
	}

	/**
	 * Gets the context help item.
	 * 
	 * @return the context help item
	 */
	public Boolean getContextHelpItem() {
		return contextHelpItem;
	}

	/**
	 * Sets the context help item.
	 * 
	 * @param contextHelpItem
	 *            the new context help item
	 */
	public void setContextHelpItem(Boolean contextHelpItem) {
		this.contextHelpItem = contextHelpItem;
	}

}
