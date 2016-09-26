package eu.vicci.process.kpseus.connect.handlers;

import java.io.IOException;
import java.io.StringReader;

import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.xml.sax.InputSource;

import eu.vicci.process.model.sofia.Process;
import eu.vicci.process.model.util.configuration.ReplyState;
import ws.wamp.jawampa.Reply;

public class ProcessInfosHandler extends AbstractClientHandler {
	//the received process Infos
	private String processInfos;
	private Process process;
	private String processId = "";
	
	public ProcessInfosHandler(String processId){
		this.processId = processId;
	}

	@Override
	public void onNext(Reply t) {
		processInfos = t.arguments().get(0).asText();
		
		XMLResource resource = new XMLResourceImpl();
		try {
			resource.load(new InputSource(new StringReader(processInfos)), null);
		} catch (IOException e) {
		}
		
		process = (Process)resource.getContents().get(0);		
	}
	
	@Override
	public void onCompleted() {
		state = ReplyState.SUCCESS;
		super.onCompleted();
		informListeners(process);
	}

	public String getProcessInfos() {
		return processInfos;
	}

	public Process getProcess() {
		return process;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
}
