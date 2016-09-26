package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The ContextModel.
 */
@SuppressWarnings("rawtypes")
public class ContextModel implements Serializable {

	private static final long serialVersionUID = -490662159312851632L;
	List<ContextProperty> contextProperties = new ArrayList<ContextProperty>();

	/**
	 * 
	 */
	public ContextModel() {
	}

	/**
	 * Gets the context properties.
	 *
	 * @return the context properties
	 */
	protected List<ContextProperty> getContextProperties() {
		return contextProperties;
	}

	/**
	 * Sets the context properties.
	 *
	 * @param contextProperties the new context properties
	 */
	protected void setContextProperties(List<ContextProperty> contextProperties) {
		this.contextProperties = contextProperties;
	}

	/**
	 * Gets the context group by value.
	 *
	 * @param registeredContextInstance the registered context instance
	 * @param value the value
	 * @return the context group by value
	 */
	public ContextGroup getContextGroupByValue(Context registeredContextInstance, Object value) {
		return registeredContextInstance.getGroupByValue(value);
	}

	/**
	 * Adds the context property.
	 *
	 * @param contextProperty the context property
	 */
	public void addContextProperty(ContextProperty contextProperty) {
		if (contextProperty.getContext() != null) {
			this.contextProperties.add(contextProperty);
		} else {
			System.out.println("Warning: Value \"" + contextProperty.getValue() + "\" is linked to a non-registered context -> not added to context model");
		}
	}
	
	/**
	 * Gets the context properties by context.
	 *
	 * @param context the context
	 * @return the context properties by context
	 */
	public List<ContextProperty> getContextPropertiesByContext(Context context) {
		List<ContextProperty> result = new ArrayList<ContextProperty>();
		for (ContextProperty currentContextProperty : contextProperties) {
			if (currentContextProperty.getContext().equals(context))
				result.add(currentContextProperty);
		}
		return result;
	}

	/**
	 * Adds the context properties.
	 *
	 * @param contextProperties the context properties
	 */
	public void addContextProperties(List<ContextProperty> contextProperties) {
		for (ContextProperty currentContextProperty : contextProperties) {
			if (currentContextProperty.getContext() != null) {
				this.contextProperties.add(currentContextProperty);
			} else {
				System.out.println("Warning: Value \"" + currentContextProperty.getValue() + "\" is linked to a non-registered context -> not added to context model");
			}
		}
	}

	/**
	 * Removes the all context properties.
	 */
	public void removeAllContextProperties() {
		this.contextProperties.clear();
	}
	
	/**
	 * Removes the context property.
	 *
	 * @param property the property
	 */
	public void removeContextProperty(ContextProperty property) {
		this.contextProperties.remove(property);
	}

	/**
	 * Checks for context property.
	 *
	 * @param registeredContextInstance the registered context instance
	 * @param value the value
	 * @return true, if successful
	 */
	public boolean hasContextProperty(Context registeredContextInstance, Object value) {
		for (ContextProperty property : this.contextProperties) {
			if (property.getContext().equals(registeredContextInstance) && property.getValue().equals(value))
				return true;
		}
		return false;
	}

	/**
	 * Checks for context property by group.
	 *
	 * @param registeredContextInstance the registered context instance
	 * @param contextGroup the context group
	 * @return true, if successful
	 */
	public boolean hasContextPropertyByGroup(Context registeredContextInstance, ContextGroup contextGroup) {
		for (ContextProperty currentContextProperty : this.contextProperties) {
			if (currentContextProperty.getContext() == registeredContextInstance
				&& registeredContextInstance.getGroupByValue(currentContextProperty.getValue()) == contextGroup)
				return true;
		}
		return false;
	}

}
