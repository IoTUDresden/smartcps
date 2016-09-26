package eu.vicci.ecosystem.standalone.controlcenter.android.humantask;

import eu.vicci.process.model.util.messages.core.IHumanTaskRequest;

/**
 * Listener for receiving updates on the {@link HumanTaskDataManager}
 */
public interface HumanTaskDataManagerListener {

	/**
	 * Called if an {@link IHumanTaskRequest} was added to the
	 * {@link HumanTaskDataManager}
	 * 
	 * @param humanTaskRequest
	 *            the newly added {@link IHumanTaskRequest}
	 */
	void onHumanTaskAdded(IHumanTaskRequest humanTaskRequest);

	/**
	 * Calles if an {@link IHumanTaskRequest} was handled by an other client.
	 * The {@link IHumanTaskRequest} will be removed from the
	 * {@link HumanTaskDataManager} and then this method will be called.
	 * 
	 * @param humanTaskRequest
	 *            the already handled request
	 */
	void onHumanTaskHandledByOther(IHumanTaskRequest humanTaskRequest);

}
