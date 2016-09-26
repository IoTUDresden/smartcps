package eu.vicci.ecosystem.standalone.controlcenter.android.humantask;

import eu.vicci.process.model.util.messages.HumanTaskResponse;
import eu.vicci.process.model.util.messages.core.IHumanTaskRequest;
import eu.vicci.process.model.util.messages.core.IHumanTaskResponse;

//TODO maybe move this to the process util project
public class HumanTaskResponseUtil {
	
	private HumanTaskResponseUtil() {
	}
	
	/**
	 * Creates a basic response according to request. E.g. fills the ports with the 
	 * with the values from the request, sets the humantask instance id, ...
	 * @param request
	 * @return
	 */
	public static IHumanTaskResponse createResponseFromRequest(IHumanTaskRequest request){
		IHumanTaskResponse response = new HumanTaskResponse();
		response.setHumanTaskInstanceId(request.getHumanTaskInstanceId());		
		response.setEndDataPorts(request.getEndDataPorts());
		response.setStartDataPorts(request.getStartDataPorts());
		response.setDescription(request.getDescription());
		response.setHumanTaskType(request.getHumanTaskType());
		response.setHumanTaskUseCase(request.getHumanTaskUseCase());
		response.setEndControlPorts(request.getEndControlPorts());
		response.setName(request.getName());
		return response;		
	}

}
