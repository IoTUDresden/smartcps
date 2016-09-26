package eu.vicci.ecosystem.standalone.controlcenter.android.smartcps.context;

import java.util.ArrayList;
import java.util.List;


/**
 * The ContextManager as a main "interface" to the context data.
 */
@SuppressWarnings("rawtypes")
public class ContextManager {

	private static List<Context> registeredContexts = new ArrayList<Context>();
	private static ContextModel contextModel;

	/**
	 * Initializes the context manager. Registers contexts by fully qualified class names
	 *
	 * @param relevantFullyQualifiedContextClasses the relevant context classes
	 */
	public static void initialize(List<String> relevantFullyQualifiedContextClasses) {
		// if a context property has a context component equally to a relevant context, it will be registered
		// if a context property has a context component unequally to any relevant context, it will be removed from the context model
		List<ContextProperty> contextPropertiesToRemove = new ArrayList<ContextProperty>();

		for (ContextProperty currentContextProperty : ContextManager.getContextModel().getContextProperties()) {
			if (relevantFullyQualifiedContextClasses.contains(currentContextProperty.getContext().getClass().getName())) {
				ContextManager.registerContextByObject(currentContextProperty.getContext());
			} else {
				contextPropertiesToRemove.add(currentContextProperty);
			}
		}
		ContextManager.getContextModel().getContextProperties().removeAll(contextPropertiesToRemove);

		// if a loaded context model object doesn't provide information to all relevant contexts, the missing ones are registered
		ContextManager.registerContextsByClassNames(relevantFullyQualifiedContextClasses);
	}

	/**
	 * Gets the context model lazily.
	 *
	 * @return the context model
	 */
	public static ContextModel getContextModel() {
		// lazy
		if (contextModel == null) {
			ContextModel newContextModel = new ContextModel();
			contextModel = newContextModel;
			return newContextModel;
		} else {
			return contextModel;
		}
	}

	/**
	 * Sets the context model.
	 *
	 * @param newContextModel the new context model
	 */
	public static void setContextModel(ContextModel newContextModel) {
		contextModel = newContextModel;
	}

	/**
	 * Gets the registered contexts.
	 *
	 * @return the registered contexts
	 */
	public static List<Context> getRegisteredContexts() {
		return registeredContexts;
	}

	/**
	 * Sets the registered contexts.
	 *
	 * @param registeredContexts the new registered contexts
	 */
	public static void setRegisteredContexts(List<Context> registeredContexts) {
		ContextManager.registeredContexts = registeredContexts;
	}

	/**
	 * Register contexts by class names.
	 *
	 * @param contextClassNames the context class names
	 */
	public static void registerContextsByClassNames(List<String> contextClassNames) {
		for (String currentContextClassNames : contextClassNames) {
			// get Class from fully qualified class name
			Class contextClass;
			try {
				contextClass = Class.forName(currentContextClassNames);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("Error: Context class not found.");
				continue;
			}
			// check for an existing context of that class (only one object per context class allowed)
			Context registeredContext = getRegisteredContextByClass(contextClass);
			if (registeredContext != null) {
				System.out.println("Error: Context already registered.");
			} else {
				// if no such context already existed -> create a new instance
				try {
					registeredContexts.add((Context) contextClass.newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Register context by object.
	 *
	 * @param contextObject the context object
	 * @return true, if successful
	 */
	public static boolean registerContextByObject(Context contextObject) {
		// check for an existing context of that class (only one object per context class allowed)
		Context registeredContext = getRegisteredContextByClass(contextObject.getClass());
		if (registeredContext != null) {
			System.out.println("Error: Context already registered.");
			return false;
		} else {
			// if no such context already existed -> create a new instance
			registeredContexts.add((Context) contextObject);
			return true;
		}
	}

	/**
	 * Gets the registered context by class.
	 *
	 * @param cls the cls
	 * @return the registered context by class
	 */
	public static Context getRegisteredContextByClass(Class cls) {
		for (Context currentRegisteredContext : registeredContexts) {
			if (currentRegisteredContext.getClass() == cls) {
				return currentRegisteredContext;
			}
		}
		return null;
	}

	/**
	 * Removes the registered contexts.
	 */
	public static void removeRegisteredContexts() {
		registeredContexts.clear();
	}

}
