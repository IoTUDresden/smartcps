package eu.vicci.ecosystem.processview.android;

import eu.vicci.process.model.sofia.Process;
import eu.vicci.process.model.sofia.ProcessStep;
import eu.vicci.process.model.sofiainstance.State;

/**
 * Interface for a process view, which shows a complete process model. Just code against this interface, 
 * and the process view is easy to exchange against other, future solutions.
 */
public interface IProcessView {

	/**
	 * Shows the given process in the view.
	 * 
	 * @param process
	 */
	void showProcess(Process process);

	/**
	 * Set listener to get callbacks, when a process was clicked/tapped.
	 * 
	 * @param listener
	 */
	void setProcessClickedListener(ProcessClickedListener listener);

	/**
	 * removes the listener.
	 */
	void unsetProcessClickedListener();

	/**
	 * Change the {@link State} of a process. This method only changes the color
	 * of the process, to show that his state has changed.
	 * 
	 * @param processId
	 *            the process id (not instance id) of the process
	 * @param state
	 */
	void changeProcessState(String processId, State state);

	/**
	 * Change the {@link State} of a process. This method only changes the color
	 * of the process, to show that his state has changed.
	 * 
	 * @param process
	 * @param state
	 */
	void changeProcessState(ProcessStep process, State state);
	
	/**
	 * Sets the {@link ProcessViewReadyListener} to get informed, if the process view is ready.
	 * @param listener
	 */
	void setProcessViewReadyListener(ProcessViewReadyListener listener);

	/**
	 * Listener to get a callback if a process was clicked within the {@link IProcessView}
	 */
	interface ProcessClickedListener {
		/**
		 * Process was clicked within the {@link IProcessView}.
		 * @param processId
		 */
		void onProcessClicked(String processId);
	}
	
	interface ProcessViewReadyListener{
		/**
		 * Gets called, if the process view is finally showing the process step.
		 */
		void onProcessViewIsReady();		
	}

}
