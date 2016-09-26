package eu.vicci.process.kpseus.connect;


import eu.vicci.process.client.android.ProcessEngineClient;


/**
 * ServerConnector is the general client access for communication to the server.
 * It configures the connection to the server and keeps an instance of
 * {@link ProcessEngineClient}.
 * 
 * @author Manuel
 * 
 */
public class ServerConnector {
	private static ServerConnector instance;
	private ProcessEngineClient pec;
	
	private ServerConnector() {
		pec = ProcessEngineClient.getInstance();
	}

	/**
	 * does not connect via {@link ProcessEngineClient#connect(String, String)}!
	 * @return
	 */
	public static synchronized ServerConnector getInstance() {
		if (instance == null) {
			instance = new ServerConnector();
		}
		return instance;
	}

	public ProcessEngineClient getProcessEngineClient() {
		return pec;
	}
	
}
