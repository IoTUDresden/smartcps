package eu.vicci.ecosystem.processview.android.internal.util;

import eu.vicci.ecosystem.processview.android.internal.GraphLinksModel;
import eu.vicci.ecosystem.processview.android.internal.GroupModel;
import eu.vicci.ecosystem.processview.android.internal.LinkModel;
import eu.vicci.ecosystem.processview.android.internal.NodeModel;
import eu.vicci.ecosystem.processview.android.internal.PortModel;
import eu.vicci.process.model.sofia.And;
import eu.vicci.process.model.sofia.CompositeStep;
import eu.vicci.process.model.sofia.EndPort;
import eu.vicci.process.model.sofia.FalseTransition;
import eu.vicci.process.model.sofia.Port;
import eu.vicci.process.model.sofia.Process;
import eu.vicci.process.model.sofia.ProcessStep;
import eu.vicci.process.model.sofia.StartPort;
import eu.vicci.process.model.sofia.Transition;
import eu.vicci.process.model.sofia.TrueTransition;

public class ModelCreationTool {
	private GraphLinksModel model;
	private Process process;
	
	
	public ModelCreationTool(Process process) {
		this.process = process;
		model = new GraphLinksModel();	
		
	}
	
	public GraphLinksModel createModel(){
		addGroupModel(process);
		return model;				
	}
	
	private void addGroupModel(CompositeStep process){
		GroupModel m = process.getParentstep() == null ?
				new GroupModel(process.getId(), process.getName())
				: new GroupModel(process.getId(), process.getName(), process.getParentstep().getId());
		
		addPortsToNode(m, process);
		model.addNodeModel(m);
		addSubsteps(process);
	}
	
	private void addSubsteps(CompositeStep process){
		for (ProcessStep step : process.getSubSteps()) {
			if(step instanceof CompositeStep){
				addGroupModel((CompositeStep)step);
			}
			else if (step instanceof And){
				addNode(step); // todo for and, if and the other stuff
			}
			else {
				addNode(step);
			}
		}		
	}
	
	private void addTransitions(Port port){
		for (Transition t : port.getOutTransitions()) {		
			Port target = t.getTargetPort();
			if(target != null){
				ProcessStep tStep = target.getProcessStep();
				ProcessStep sStep = port.getProcessStep();
				if(tStep != null){
					String color = getLinkColor(t);
					LinkModel m = new LinkModel(sStep.getId(), tStep.getId(), port.getId(), target.getId(), color);
					model.addLinkModel(m);
				}
			}			
		}		
	}
	
	private String getLinkColor(Transition t){
		boolean trueOrFalse = t instanceof TrueTransition || t instanceof FalseTransition;
		if(!trueOrFalse)
			return LinkModel.DEFAULT_COLOR;
		return t instanceof TrueTransition ? LinkModel.DEFAULT_TRUE_COLOR : LinkModel.DEFAULT_FALSE_COLOR;		
	}
	
	private void addNode(ProcessStep step){
		NodeModel m = new NodeModel(step.getId(), step.getName(), step.getParentstep().getId());
		addPortsToNode(m, step);
		model.addNodeModel(m);
	}
	
	private void addPortsToNode(NodeModel node, ProcessStep process){
		for (Port p : process.getPorts()) {
			addTransitions(p);
			if(p instanceof StartPort){
				node.addToLeftArray(new PortModel(p.getId(), true));				
			}	
			else if (p instanceof EndPort) {
				node.addToRightArray(new PortModel(p.getId(), false));					
			}
		}		
	}
	
	

}
