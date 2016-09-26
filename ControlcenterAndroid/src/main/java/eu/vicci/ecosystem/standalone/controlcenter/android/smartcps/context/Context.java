package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Context.
 *
 * @param <E> the element type
 */
@SuppressWarnings("rawtypes")
public abstract class Context<E extends ContextGroup> implements Serializable {

	private static final long serialVersionUID = -1949286598741209959L;
	private String name = "";
	private List<E> groups = new ArrayList<E>();

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Adds the group.
	 *
	 * @param group the group
	 */
	public void addGroup(E group) {
		this.groups.add(group);
	}

	/**
	 * Adds the group.
	 *
	 * @param index the index
	 * @param group the group
	 */
	public void addGroup(int index, E group) {
		this.groups.add(index, group);
	}

	/**
	 * Removes the group.
	 *
	 * @param group the group
	 */
	public void removeGroup(E group) {
		this.groups.remove(group);
	}

	/**
	 * Removes the group.
	 *
	 * @param index the index
	 */
	public void removeGroup(int index) {
		this.groups.remove(index);
	}

	/**
	 * Gets the group by name.
	 *
	 * @param name the name
	 * @return the group by name
	 */
	public E getGroupByName(String name) {
		for (E currentGroup : this.groups) {
			if (currentGroup.getName().equals(name)) {
				return currentGroup;
			}
		}
		return null;
	}

	/**
	 * Gets a group by value.
	 *
	 * @param value the value
	 * @return the group by value
	 */
	public E getGroupByValue(Object value) {
		for (E currentGroup : this.groups) {
			// context groups based on discrete states (e.g. technical skill)
			if (currentGroup instanceof StateContextGroup) {
				if (value.toString().equals(((StateContextGroup) currentGroup).getValue().toString())) {
					return currentGroup;
				}
			} else {
				// interval-based context groups (e.g. age)
				if (currentGroup instanceof BoundedContextGroup) {
					if (value instanceof Integer || value instanceof Float || value instanceof String) {
						Float parsedProperty = Float.parseFloat(value.toString());
						if (parsedProperty >= Float.parseFloat(((BoundedContextGroup) currentGroup).getLowerBound().toString())
							&& parsedProperty <= Float.parseFloat(((BoundedContextGroup) currentGroup).getUpperBound().toString())) {
							return currentGroup;
						}
					}
				}
			}
		}
		return null;
	}

	public String toString() {
		return this.name;
	}
}
