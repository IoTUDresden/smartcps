package eu.vicci.process.kpseus.connect;

import eu.vicci.process.kpseus.connect.handlers.AbstractClientHandler;
import eu.vicci.process.kpseus.connect.handlers.DeployInstanceHandler;
import eu.vicci.process.kpseus.connect.handlers.DeployModelHandler;
import eu.vicci.process.kpseus.connect.handlers.ModelListHandler;
import eu.vicci.process.kpseus.connect.handlers.ProcessInfosHandler;
import eu.vicci.process.kpseus.connect.handlers.ProcessInstanceInfoListHandler;
import eu.vicci.process.kpseus.connect.observers.ProcessIdListObserver;
import eu.vicci.process.kpseus.connect.observers.ProcessInstanceObserver;
import eu.vicci.process.kpseus.connect.observers.ProcessObserver;
import eu.vicci.process.kpseus.connect.observers.StringObserver;
import eu.vicci.process.model.sofia.Process;

/**
 * This class allows to administrate the deployment and upload of process
 * (instances). To deploy a process instance you must upload a model first and
 * then deploy a process before you can deploy the instance on its own.
 * 
 * @author Manuel
 * 
 */
public class ProcessDeployManager {
	private static ProcessDeployManager instance;

	private ProcessDeployManager() {

	}

	public static synchronized ProcessDeployManager getInstance() {
		if (instance == null) {
			instance = new ProcessDeployManager();
		}
		return instance;
	}

	
	/**
	 * Returns the ids of all processes which are available on the server.
	 * 
	 * @return
	 */
	public void getProcessIds(ProcessIdListObserver pilo) {
		ModelListHandler mlh = new ModelListHandler();
		mlh.addHandlerFinishedListener(pilo);
		ServerConnector.getInstance().getProcessEngineClient()
				.listUploadedProcessDefinitions(mlh);
	}

	/**
	 * deploy a process on the server and download its data structure.
	 * 
	 * @param id
	 *            - the ID of the deploying process
	 * @return the deployed process object
	 */
	public void deployProcess(final String id, final ProcessObserver po) {
		DeployModelHandler dmh = new DeployModelHandler(id);
		StringObserver so = new StringObserver() {

			@Override
			public void updateObserver(AbstractClientHandler o, String arg) {
				ProcessInfosHandler pih = new ProcessInfosHandler(id);
				pih.addHandlerFinishedListener(po);
				ServerConnector.getInstance().getProcessEngineClient()
						.getProcessInfos(pih);
			}

		};
		dmh.addHandlerFinishedListener(so);
		ServerConnector.getInstance().getProcessEngineClient()
				.deployProcess(dmh);
	}

	/**
	 * deploy a process instance of a deployed process
	 * 
	 * @param process
	 */
	public void deployProcessInstance(Process process,
			final ProcessInstanceObserver observer) {
		DeployInstanceHandler dih = new DeployInstanceHandler(process);
		/*
		StringObserver so = new StringObserver() {
			
			@Override
			public void updateObserver(Observable o, String arg) {
				ProcessInstanceInfosHandler piih = new ProcessInstanceInfosHandler(arg);
				piih.addObserver(observer);
				ServerConnector.getInstance().getProcessEngineClient().getProcessInstanceInfos(piih);
				
			}
		};
		*/
		dih.addHandlerFinishedListener(observer);
		ServerConnector.getInstance().getProcessEngineClient()
				.deployProcessInstance(dih);
	}

	public void getProcess(String id, ProcessObserver po) {
		ProcessInfosHandler pih = new ProcessInfosHandler(id);
		pih.addHandlerFinishedListener(po);
		ServerConnector.getInstance().getProcessEngineClient()
				.getProcessInfos(pih);
	}

	public void getProcessInstanceIds(ProcessIdListObserver pilo2) {
		ProcessInstanceInfoListHandler pilh = new ProcessInstanceInfoListHandler();
		pilh.addHandlerFinishedListener(pilo2);
		ServerConnector.getInstance().getProcessEngineClient().listProcessInstances(pilh);		
	}

}
