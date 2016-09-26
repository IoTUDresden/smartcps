package eu.vicci.ecosystem.standalone.controlcenter.android.listeners;

/**
 * The listener interface for receiving twoPaneItemSelect events. The class that
 * is interested in processing a twoPaneItemSelect event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addTwoPaneItemSelectListener<code> method. When
 * the twoPaneItemSelect event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see TwoPaneItemSelectEvent
 */
public interface TwoPaneItemSelectListener {

	/**
	 * On item selected.
	 * 
	 * @param id
	 *            the id
	 */
	public void onItemSelected(String id);

}
