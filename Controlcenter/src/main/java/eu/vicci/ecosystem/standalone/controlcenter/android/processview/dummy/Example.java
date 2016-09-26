package eu.vicci.ecosystem.standalone.controlcenter.android.processview.dummy;

import eu.vicci.process.kpseus.instances.ProcessInstanceKPImpl;
import eu.vicci.process.model.sofia.AtomicStep;
import eu.vicci.process.model.sofia.DataType;
import eu.vicci.process.model.sofia.EndDataPort;
import eu.vicci.process.model.sofia.Process;
import eu.vicci.process.model.sofia.SofiaFactory;
import eu.vicci.process.model.sofia.StartDataPort;
import eu.vicci.process.model.sofia.Transition;
import eu.vicci.process.model.sofia.impl.SofiaFactoryImpl;
import eu.vicci.process.model.sofiainstance.ProcessInstance;
import eu.vicci.process.model.sofiainstance.SofiaInstanceFactory;
import eu.vicci.process.model.sofiainstance.impl.SofiaInstanceFactoryImpl;

public class Example {
	private static int inst_count = 0;

	/**
	 * {@link Process} with subprocesses and ports as following:<br>
	 * <ul>
	 * <li>p[sdp] -[t1]-> p1[sdp1]
	 * <li>p1[edp11] -[t2]-> p2[sdp2]
	 * <li>p1[edp12] -[t3]-> p31[sdp31]
	 * <li>p2[edp2] -[t4]-> p4[sdp41]
	 * <li>p31[edp31] -[t5]-> p32[sdp32]
	 * <li>p32[edp32] -[t6]-> p4[sdp42]
	 * <li>p4[edp4] -[t7]-> p[edp]
	 * </ul>
	 * 
	 * @return
	 */
	public static Process getExampleProcess() {
		SofiaFactory sf = SofiaFactoryImpl.init();
		Process p = sf.createProcess();
		p.setName("Complete Process");
		p.setId("p");
		p.setType("bla");

		AtomicStep p1, p2, p31, p32, p4;
		p1 = sf.createProcessSlot();
		p1.setId("p1");
		p2 = sf.createHumanTask();
		p2.setId("p2");
		p31 = sf.createDataDuplicationStep();
		p31.setId("p31");
		p32 = sf.createHumanTask(); // TODO: Subprocesses
		p32.setId("p32");
		p4 = sf.createRESTInvoke();
		p4.setId("p4");

		p1.setParentstep(p);
		p2.setParentstep(p);
		p31.setParentstep(p);
		p32.setParentstep(p);
		p4.setParentstep(p);

		DataType dt = sf.createBooleanType();
		StartDataPort sdp, sdp1, sdp2, sdp31, sdp32, sdp41, sdp42;
		EndDataPort edp, edp11, edp12, edp2, edp31, edp32, edp4;
		sdp = sf.createStartDataPort();
		sdp.setId("sdp");
		sdp.setName("Start Data Port of complete process");
		sdp.setProcessStep(p);
		sdp.setPortDatatype(dt);

		sdp1 = sf.createStartDataPort();
		sdp1.setId("sdp1");
		sdp1.setName("Start Data Port of p1");
		sdp1.setProcessStep(p1);
		sdp1.setPortDatatype(dt);

		sdp2 = sf.createStartDataPort();
		sdp2.setId("sdp2");
		sdp2.setName("Start Data Port of p2");
		sdp2.setProcessStep(p2);
		sdp2.setPortDatatype(dt);

		sdp31 = sf.createStartDataPort();
		sdp31.setId("sdp31");
		sdp31.setName("Start Data Port of p31");
		sdp31.setProcessStep(p31);
		sdp31.setPortDatatype(dt);

		sdp32 = sf.createStartDataPort();
		sdp32.setId("sdp32");
		sdp32.setName("Start Data Port of p32");
		sdp32.setProcessStep(p32);
		sdp32.setPortDatatype(dt);

		sdp41 = sf.createStartDataPort();
		sdp41.setId("sdp41");
		sdp41.setName("first Start Data Port of p4");
		sdp41.setProcessStep(p4);
		sdp41.setPortDatatype(dt);

		sdp42 = sf.createStartDataPort();
		sdp42.setId("sdp42");
		sdp42.setName("seconnd Start Data Port of p4");
		sdp42.setProcessStep(p4);
		sdp42.setPortDatatype(dt);

		edp = sf.createEndDataPort();
		edp.setId("edp");
		edp.setName("End Data Port of p");
		edp.setProcessStep(p);
		edp.setPortDatatype(dt);

		edp11 = sf.createEndDataPort();
		edp11.setId("edp11");
		edp11.setName("first End Data Port of p1");
		edp11.setProcessStep(p1);
		edp11.setPortDatatype(dt);

		edp12 = sf.createEndDataPort();
		edp12.setId("edp12");
		edp12.setName("second End Data Port of p1");
		edp12.setProcessStep(p1);
		edp12.setPortDatatype(dt);

		edp2 = sf.createEndDataPort();
		edp2.setId("edp2");
		edp2.setName("End Data Port of p2");
		edp2.setProcessStep(p2);
		edp2.setPortDatatype(dt);

		edp31 = sf.createEndDataPort();
		edp31.setId("edp31");
		edp31.setName("End Data Port of p31");
		edp31.setProcessStep(p31);
		edp31.setPortDatatype(dt);

		edp32 = sf.createEndDataPort();
		edp32.setId("edp32");
		edp32.setName("End Data Port of p32");
		edp32.setProcessStep(p32);
		edp32.setPortDatatype(dt);

		edp4 = sf.createEndDataPort();
		edp4.setId("edp4");
		edp4.setName("End Data Port of p4");
		edp4.setProcessStep(p4);
		edp4.setPortDatatype(dt);

		Transition t = sf.createTransition();
		t.setId("t1");
		t.setName("Transition 1");
		t.setSourcePort(sdp);
		t.setTargetPort(sdp1);
		
		t = sf.createTransition();
		t.setId("t2");
		t.setName("Transition 2");
		t.setSourcePort(edp11);
		t.setTargetPort(sdp2);

		t = sf.createTransition();
		t.setId("t3");
		t.setName("Transition 3");
		t.setSourcePort(edp12);
		t.setTargetPort(sdp31);

		t = sf.createTransition();
		t.setId("t4");
		t.setName("Transition 4");
		t.setSourcePort(edp2);
		t.setTargetPort(sdp41);

		t = sf.createTransition();
		t.setId("t5");
		t.setName("Transition 5");
		t.setSourcePort(edp31);
		t.setTargetPort(sdp32);

		t = sf.createTransition();
		t.setId("t6");
		t.setName("Transition 6");
		t.setSourcePort(edp32);
		t.setTargetPort(sdp42);

		t = sf.createTransition();
		t.setId("t7");
		t.setName("Transition 7");
		t.setSourcePort(edp4);
		t.setTargetPort(edp);

		return p;
	}

	/**
	 * {@link ProcessInstance} as modeled in {@link Process} <i>p</i>. If
	 * <i>p</i> is <code>null</code> a Proces from {@link #getExampleProcess()}
	 * will be used to deploy.
	 * @param p - {@link Process} which models the new instance
	 * @return
	 */
	@Deprecated
	public static ProcessInstance getExampleProcessInstance(Process p) {
		SofiaInstanceFactory sif = SofiaInstanceFactoryImpl.init();
		if (p == null) {
			p = getExampleProcess();
		}
		ProcessInstance pi = new ProcessInstanceKPImpl(p);
		//pi.setInstanceId(p.getId() + "_instance_" + (inst_count++));
		//pi.setExecutionState(State.UNDEPLOYED);
		pi.deploy(sif.createMappingUtil());
		return pi;
	}
}
