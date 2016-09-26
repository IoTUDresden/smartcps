package eu.vicci.ecosystem.standalone.controlcenter.android.util;

import eu.vicci.ecosystem.standalone.controlcenter.android.activities.DashboardActivity;

/**
 * Element of FilterDialog in {@link DashboardActivity}. Used to keep
 * selection and group of this option.
 * 
 * @author Andreas Hippler
 */
public class FilterElement implements Comparable<FilterElement> {
	private String name;
	private boolean selected;
	private int headerID;

	/**
	 * @param name
	 * @param headerID
	 * @param isSelected
	 */
	public FilterElement(String name, int headerID, boolean isSelected) {
		super();
		this.name = name;
		this.headerID = headerID;
		this.selected = isSelected;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the headerID
	 */
	public int getHeaderID() {
		return headerID;
	}

	@Override
	public boolean equals(Object o) {
		if (o.getClass().equals(FilterElement.class)) {
			FilterElement element = (FilterElement) o;
			return element.getName().equals(name) && element.getHeaderID() == headerID;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (name + headerID).hashCode();
	};

	@Override
	public int compareTo(FilterElement another) {
		if (this.getHeaderID() == another.getHeaderID()) {
			return this.getName().compareTo(another.getName());
		}
		return this.headerID < another.headerID ? -1 : 1;
	}
}
