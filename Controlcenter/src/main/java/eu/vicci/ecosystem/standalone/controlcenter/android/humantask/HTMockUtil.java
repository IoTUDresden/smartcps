package eu.vicci.ecosystem.standalone.controlcenter.android.humantask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.vicci.process.model.sofia.HumanTaskType;
import eu.vicci.process.model.sofia.HumanTaskUseCase;
import eu.vicci.process.model.util.messages.HumanTaskRequest;
import eu.vicci.process.model.util.messages.HumanTaskResponse;
import eu.vicci.process.model.util.messages.core.IHumanTaskRequest;
import eu.vicci.process.model.util.messages.core.IHumanTaskResponse;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.JSONDataPortInstance;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.JSONPortInstance;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.core.IJSONDataPortInstance;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.core.IJSONPortInstance;
import eu.vicci.process.model.util.serialization.jsonprocessstepinstances.core.PortInstanceType;
import eu.vicci.process.model.util.serialization.jsonprocesssteps.JSONDataPort;
import eu.vicci.process.model.util.serialization.jsonprocesssteps.JSONPort;
import eu.vicci.process.model.util.serialization.jsonprocesssteps.core.IJSONDataPort;
import eu.vicci.process.model.util.serialization.jsonprocesssteps.core.IJSONPort;
import eu.vicci.process.model.util.serialization.jsonprocesssteps.core.PortType;
import eu.vicci.process.model.util.serialization.jsontypeinstances.JSONDoubleTypeInstance;
import eu.vicci.process.model.util.serialization.jsontypeinstances.JSONIntegerTypeInstance;
import eu.vicci.process.model.util.serialization.jsontypeinstances.JSONListTypeInstance;
import eu.vicci.process.model.util.serialization.jsontypeinstances.JSONStringTypeInstance;
import eu.vicci.process.model.util.serialization.jsontypeinstances.core.DataTypeInstanceType;
import eu.vicci.process.model.util.serialization.jsontypeinstances.core.IJSONDoubleTypeInstance;
import eu.vicci.process.model.util.serialization.jsontypeinstances.core.IJSONIntegerTypeInstance;
import eu.vicci.process.model.util.serialization.jsontypeinstances.core.IJSONListTypeInstance;
import eu.vicci.process.model.util.serialization.jsontypeinstances.core.IJSONStringTypeInstance;
import eu.vicci.process.model.util.serialization.jsontypeinstances.core.IJSONTypeInstance;
import eu.vicci.process.model.util.serialization.jsontypes.JSONDoubleType;
import eu.vicci.process.model.util.serialization.jsontypes.JSONIntegerType;
import eu.vicci.process.model.util.serialization.jsontypes.JSONListType;
import eu.vicci.process.model.util.serialization.jsontypes.JSONStringType;
import eu.vicci.process.model.util.serialization.jsontypes.core.DataTypeType;
import eu.vicci.process.model.util.serialization.jsontypes.core.IJSONDoubleType;
import eu.vicci.process.model.util.serialization.jsontypes.core.IJSONIntegerType;
import eu.vicci.process.model.util.serialization.jsontypes.core.IJSONListType;
import eu.vicci.process.model.util.serialization.jsontypes.core.IJSONStringType;

public class HTMockUtil {

	private HTMockUtil() {
	}

	public static void addHTMocks(Map<String, IHumanTaskRequest> requests) {
		String uid = "mockforthomas";
		HumanTaskRequest ht1 = createHumanTask(uid);
		ht1.setHumanTaskType(HumanTaskType.WARNING);
		ht1.setHumanTaskUseCase(HumanTaskUseCase.COFFEE);
		ht1.setName("Brewing Coffee");
		requests.put(uid, ht1);

		String uid2 = UUID.randomUUID().toString();
		HumanTaskRequest ht2 = createHumanTask(uid2);
		ht2.setHumanTaskType(HumanTaskType.ERROR);
		ht2.setHumanTaskUseCase(HumanTaskUseCase.HEATING);
		ht2.setName("Heating");
		requests.put(uid2, ht2);

		String uid3 = UUID.randomUUID().toString();
		HumanTaskRequest ht3 = createHumanTask(uid3);
		ht3.setHumanTaskType(HumanTaskType.HINT);
		ht3.setHumanTaskUseCase(HumanTaskUseCase.ORDER);
		ht3.setName("Refrigerator Empty");
		requests.put(uid3, ht3);

		String uid4 = UUID.randomUUID().toString();
		HumanTaskRequest ht4 = createHumanTask(uid4);
		ht4.setHumanTaskType(HumanTaskType.INTERACTION);
		ht4.setHumanTaskUseCase(HumanTaskUseCase.PLANTS);
		ht4.setName("Plants");
		requests.put(uid4, ht4);

		String uid5 = UUID.randomUUID().toString();
		HumanTaskRequest ht5 = createHumanTask(uid5);
		ht5.setHumanTaskType(HumanTaskType.HINT);
		ht5.setHumanTaskUseCase(HumanTaskUseCase.PLANTS);
		ht5.setName("Plants2");
		requests.put(uid5, ht5);

		String uid6 = UUID.randomUUID().toString();
		HumanTaskRequest ht6 = createHumanTask(uid6);
		ht6.setHumanTaskType(HumanTaskType.ERROR);
		ht6.setHumanTaskUseCase(HumanTaskUseCase.PLANTS);
		ht6.setName("Plants3");
		requests.put(uid6, ht6);

		String uid7 = UUID.randomUUID().toString();
		HumanTaskRequest ht7 = createHumanTask(uid7);
		ht7.setHumanTaskType(HumanTaskType.WARNING);
		ht7.setHumanTaskUseCase(HumanTaskUseCase.PLANTS);
		ht7.setName("Plants5S");
		requests.put(uid7, ht7);

		String uid8 = "intmock";
		HumanTaskRequest ht8 = createIntHt(uid8);
		ht8.setHumanTaskType(HumanTaskType.WARNING);
		ht8.setHumanTaskUseCase(HumanTaskUseCase.HEATING);
		ht8.setName("i <3 int");
		requests.put(uid8, ht8);

		String uid9 = "doublemock";
		HumanTaskRequest ht9 = createDoubleHt(uid9);
		ht9.setHumanTaskType(HumanTaskType.WARNING);
		ht9.setHumanTaskUseCase(HumanTaskUseCase.HEATING);
		ht9.setName("i <3 double");
		requests.put(uid9, ht9);

		String uid10 = "simpleUI";
		HumanTaskRequest ht10 = createSimpleUIHt(uid10);
		ht10.setHumanTaskType(HumanTaskType.HINT);
		ht10.setHumanTaskUseCase(HumanTaskUseCase.HEATING);
		ht10.setName("Simple UI");
		requests.put(uid10, ht10);

		String uid11 = "listintmock";
		HumanTaskRequest ht11 = createIntListHt(uid11);
		ht11.setHumanTaskType(HumanTaskType.WARNING);
		ht11.setHumanTaskUseCase(HumanTaskUseCase.HEATING);
		ht11.setName("i <3 lists with int");
		requests.put(uid11, ht11);
		
		IHumanTaskRequest ht12 = getComplexMock();
		if(ht12 != null)
			requests.put(ht12.getHumanTaskInstanceId(), ht12);
		IHumanTaskRequest ht13 = getBooleanMock();
		if(ht13 != null)
			requests.put(ht13.getHumanTaskInstanceId(), ht13);
	}
	
	private static IHumanTaskRequest getBooleanMock(){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(ComplexMock.booleanMock, HumanTaskRequest.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static IHumanTaskRequest getComplexMock(){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(ComplexMock.COMPLEX, HumanTaskRequest.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void addHTResponseMocks(Map<String, IHumanTaskResponse> response) {
//		String uid = "mockforthomas";
//		HumanTaskRequest ht1 = createHumanTask(uid);
//		ht1.setHumanTaskType(HumanTaskType.WARNING);
//		ht1.setHumanTaskUseCase(HumanTaskUseCase.COFFEE);
//		ht1.setName("Brewing Coffee");
//		requests.put(uid, ht1);
		HumanTaskResponse r1 = new HumanTaskResponse();
	}

	private static HumanTaskRequest createIntListHt(String uid) {
		HumanTaskRequest req = new HumanTaskRequest();
		req.setDescription("To test the list with values");
		req.setHumanTaskInstanceId(uid);
		req.setStartDataPorts(createStartDataPortsMapListInt());
		req.setEndDataPorts(createEndDataPortsMapListInt());
		return req;
	}

	private static HumanTaskRequest createDoubleHt(String uid) {
		HumanTaskRequest req = new HumanTaskRequest();
		req.setDescription("To test the double values");
		req.setHumanTaskInstanceId(uid);
		req.setStartDataPorts(createStartDataPortsMap());
		req.setEndDataPorts(createEndDataPortsMapDouble());
		return req;
	}

	private static HumanTaskRequest createIntHt(String uid) {
		HumanTaskRequest req = new HumanTaskRequest();
		req.setDescription("To test the int values");
		req.setHumanTaskInstanceId(uid);
		req.setStartDataPorts(createStartDataPortsMap());
		req.setEndDataPorts(createEndDataPortsMapInt());
		return req;
	}

	private static HumanTaskRequest createSimpleUIHt(String uid) {
		HumanTaskRequest req = new HumanTaskRequest();
		req.setDescription("Simple UI");
		req.setHumanTaskInstanceId(uid);
		req.setStartDataPorts(createEmptyStartDataPortsMap());
		req.setEndControlPorts(createEndControlPortsMap());
		return req;
	}

	private static HumanTaskRequest createHumanTask(String uid) {
		HumanTaskRequest req = new HumanTaskRequest();
		req.setDescription("Here goes the description for the human task process. "
				+ "In some cases the description may be a bit looooooooonger, so we have to take care of it");
		req.setName("ProcessName");
		req.setHumanTaskInstanceId(uid);
		req.setStartDataPorts(createStartDataPortsMap());
		req.setEndDataPorts(createEndDataPortsMap());
		req.setEndControlPorts(createEndControlPortsMap());
		return req;
	}

	private static Map<String, IJSONPortInstance> createEndControlPortsMap() {
		Map<String, IJSONPortInstance> map = new HashMap<String, IJSONPortInstance>();
		map.put(ePort5, createEndControlPortDto("EndPort_5", false));
		return map;
	}

	private static Map<String, IJSONDataPortInstance> createEndDataPortsMap() {
		Map<String, IJSONDataPortInstance> map = new HashMap<String, IJSONDataPortInstance>();
		map.put(ePort1, createEndDataPortDto("EndPort_1", ePort1));
		map.put(ePort2, createEndDataPortDto("EndPort_2", ePort2));
		map.put(ePort3, createEndDataPortDto("EndPort_3", ePort3));
		map.put(ePort4, createEndDataPortDto("EndPort_4", ePort4));
		return map;
	}

	private static Map<String, IJSONDataPortInstance> createEndDataPortsMapInt() {
		Map<String, IJSONDataPortInstance> map = new HashMap<String, IJSONDataPortInstance>();
		map.put(ePort1, createEndDataPortDtoInt(ePort1, "EndPort_1", 13));
		map.put(ePort2, createEndDataPortDtoInt(ePort2, "EndPort_2", 23));
		map.put(ePort3, createEndDataPortDtoInt(ePort3, "EndPort_3", 33));
		map.put(ePort4, createEndDataPortDtoInt(ePort4, "EndPort_4", 43));
		return map;
	}

	private static Map<String, IJSONDataPortInstance> createEndDataPortsMapDouble() {
		Map<String, IJSONDataPortInstance> map = new HashMap<String, IJSONDataPortInstance>();
		map.put(ePort1, createEndDataPortDtoDouble(ePort1, "EndPort_1", 13.234234));
		map.put(ePort2, createEndDataPortDtoDouble(ePort2, "EndPort_2", 23.546456));
		map.put(ePort3, createEndDataPortDtoDouble(ePort3, "EndPort_3", 33.342344));
		map.put(ePort4, createEndDataPortDtoDouble(ePort4, "EndPort_4", 43.2342343454));
		return map;
	}

	private static Map<String, IJSONDataPortInstance> createEndDataPortsMapListInt() {
		Map<String, IJSONDataPortInstance> map = new HashMap<String, IJSONDataPortInstance>();
		map.put(ePort1, createEndDataPortListDto(ePort1, "EndPort_1", 13.234234));
		map.put(ePort2, createEndDataPortListDto(ePort2, "EndPort_2", 23.546456));
		map.put(ePort3, createEndDataPortDtoDouble(ePort3, "EndPort_3", 33.342344));
		map.put(ePort4, createEndDataPortDtoDouble(ePort4, "EndPort_4", 43.2342343454));
		return map;
	}

	private static Map<String, IJSONDataPortInstance> createStartDataPortsMap() {
		Map<String, IJSONDataPortInstance> map = new HashMap<String, IJSONDataPortInstance>();
		map.put(sPort1, createStartDataPortDto("StartPort_1", "value 1"));
		map.put(sPort2, createStartDataPortDto("StartPort 2", "value 2"));
		map.put(sPort3, createStartDataPortDto("StartPort 3", "value 3"));
		map.put(sPort4, createStartDataPortDto("StartPort 4", "value 4"));
		return map;
	}
	
	private static Map<String, IJSONDataPortInstance> createStartDataPortsMapListInt() {
		Map<String, IJSONDataPortInstance> map = new HashMap<String, IJSONDataPortInstance>();
		map.put(sPort1, createStartDataPortListDto(sPort1, "value 1"));
		map.put(sPort2, createStartDataPortListDto(sPort2, "value 2"));
		map.put(sPort3, createStartDataPortListDto(sPort3, "value 3"));
		map.put(sPort4, createStartDataPortListDto(sPort4, "value 4"));
		return map;
	}

	private static Map<String, IJSONDataPortInstance> createEmptyStartDataPortsMap() {
		Map<String, IJSONDataPortInstance> map = new HashMap<String, IJSONDataPortInstance>();
		return map;
	}

	private static IJSONDataPortInstance createEndDataPortDto(String portName, String portId) {
		// TODO Thomas look here!!!!!

		// make an port instance
		IJSONDataPortInstance dto = new JSONDataPortInstance();
		dto.setName(portName);
		// This is a endDataPort
		dto.setPortInstanceType(PortInstanceType.EndDataPortInstance);
		dto.setInstanceNumber(1);
		dto.setInstanceId(portId + "_instance_" + 1);

		// make an type
		String dTypeId = UUID.randomUUID().toString();
		IJSONStringType type = new JSONStringType();
		type.setID(dTypeId);
		type.setDataTypeType(DataTypeType.StringType);
		type.setName("ExampleForStringType");
		type.setTypeClass("");// no for setting this

		// make the port
		IJSONDataPort port = new JSONDataPort();
		port.setName(portName);
		port.setOptional(false);// if optional, the value can be set, but it is
								// not important
		port.setId(portId);
		port.setDataType(type);
		port.setPortType(PortType.EndDataPort);

		// add the port to the instance
		dto.setPortType(port);

		// make the type instance (note on endports can the dataType instance
		// also be null in a request!
		// but the response must contain a datatype instance)
		IJSONStringTypeInstance dTypeDto = new JSONStringTypeInstance();
		dTypeDto.setDataTypeID(type.getID());
		dTypeDto.setDataType(type);
		dTypeDto.setDataTypeInstanceType(DataTypeInstanceType.StringTypeInstance);
		dTypeDto.setName("ExampleForStringTypeInstance");
		dTypeDto.setValue("This is the value for a StringTypeInstance");

		// add the DataTypeInstance to the DataPortInstance
		dto.setDataTypeInstance(dTypeDto);
		return dto;
	}

	private static IJSONDataPortInstance createStartDataPortDto(String portName, String value) {
		IJSONDataPortInstance dto = new JSONDataPortInstance();
		dto.setPortInstanceType(PortInstanceType.StartDataPortInstance);
		dto.setName(portName);
		dto.setInstanceId(portName + "_instance_" + 1);
		//
		JSONStringTypeInstance dTypeInstanceDto = new JSONStringTypeInstance();
		String id = UUID.randomUUID().toString();
		dTypeInstanceDto.setDataTypeID(id);
		dTypeInstanceDto.setInstanceID(id + "_" + "instance_1");
		dTypeInstanceDto.setValue(value);
		dTypeInstanceDto.setName("instanceFor_" + portName);

		// make an type
		// String dTypeId = UUID.randomUUID().toString();
		IJSONStringType type = new JSONStringType();
		type.setID(id);
		type.setDataTypeType(DataTypeType.StringType);
		type.setName("ExampleForStringType");
		type.setTypeClass("");// no for setting this

		// make the port
		IJSONDataPort port = new JSONDataPort();
		port.setName(portName);
		port.setOptional(false);// if optional, the value can be set, but it is
								// not important
		port.setId(portName);
		port.setDataType(type);
		port.setPortType(PortType.EndDataPort);

		// add the port to the instance
		dto.setPortType(port);

		// make the type instance (note on endports can the dataType instance
		// also be null in a request!
		// but the response must contain a datatype instance)
		IJSONStringTypeInstance dTypeDto = new JSONStringTypeInstance();
		dTypeDto.setDataTypeID(type.getID());
		dTypeDto.setDataType(type);
		dTypeDto.setDataTypeInstanceType(DataTypeInstanceType.StringTypeInstance);
		dTypeDto.setName("ExampleForStringTypeInstance");
		dTypeDto.setValue("Value Value Value");

		// add the DataTypeInstance to the DataPortInstance
		dto.setDataTypeInstance(dTypeDto);
		return dto;
		// TODO setDataInstance
	}

	private static IJSONPortInstance createEndControlPortDto(String portName, boolean isOptional) {
		IJSONPortInstance dto = new JSONPortInstance();
		dto.setName(portName);
		dto.setPortInstanceType(PortInstanceType.EndControlPortInstance);
		IJSONPort portType = new JSONPort();
		dto.setPortType(portType);
		portType.setId(portName);
		return dto;
	}

	private static IJSONDataPortInstance createEndDataPortDtoInt(String portId, String portName, int value) {
		String id = UUID.randomUUID().toString();

		IJSONDataPortInstance dto = new JSONDataPortInstance();
		dto.setPortInstanceType(PortInstanceType.EndDataPortInstance);
		dto.setName(portName);
		dto.setInstanceId(portId + "_instance_" + 1);
		dto.setTypeId(portId);

		IJSONIntegerType type = new JSONIntegerType();
		type.setID(id);
		type.setDataTypeType(DataTypeType.IntegerType);
		type.setName("ExampleForIntType");
		type.setTypeClass("");// no for setting this

		IJSONDataPort port = new JSONDataPort();
		port.setName(portName);
		port.setOptional(false);// if optional, the value can be set, but it is
								// not important
		port.setId(portId);
		port.setDataType(type);
		port.setPortType(PortType.EndDataPort);
		dto.setPortType(port);

		IJSONIntegerTypeInstance dTypeInstanceDto = new JSONIntegerTypeInstance();
		dTypeInstanceDto.setDataTypeID(id);
		dTypeInstanceDto.setInstanceID(id + "_" + "instance_1");
		dTypeInstanceDto.setValue(value);
		dTypeInstanceDto.setName("ExampleForIntType");
		dTypeInstanceDto.setDataTypeInstanceType(DataTypeInstanceType.IntegerTypeInstance);
		dto.setDataTypeInstance(dTypeInstanceDto);

		return dto;
	}

	private static IJSONDataPortInstance createEndDataPortDtoDouble(String portId, String portName, double value) {
		String id = UUID.randomUUID().toString();

		IJSONDataPortInstance dto = new JSONDataPortInstance();
		dto.setPortInstanceType(PortInstanceType.EndDataPortInstance);
		dto.setName(portName);
		dto.setInstanceId(portId + "_instance_" + 1);
		dto.setTypeId(portId);

		IJSONDoubleType type = new JSONDoubleType();
		type.setID(id);
		type.setDataTypeType(DataTypeType.DoubleType);
		type.setName("ExampleForDoubleType");
		type.setTypeClass("");// no for setting this

		IJSONDataPort port = new JSONDataPort();
		port.setName(portName);
		port.setOptional(false);// if optional, the value can be set, but it is
								// not important
		port.setId(portId);
		port.setDataType(type);
		port.setPortType(PortType.EndDataPort);
		dto.setPortType(port);

		IJSONDoubleTypeInstance dTypeInstanceDto = new JSONDoubleTypeInstance();
		dTypeInstanceDto.setDataTypeID(id);
		dTypeInstanceDto.setInstanceID(id + "_" + "instance_1");
		dTypeInstanceDto.setValue(value);
		dTypeInstanceDto.setName("ExampleForDoubleType");
		dTypeInstanceDto.setDataTypeInstanceType(DataTypeInstanceType.DoubleTypeInstance);
		dto.setDataTypeInstance(dTypeInstanceDto);

		return dto;
	}
	
	private static IJSONDataPortInstance createStartDataPortListDto(String portId, String portName, double... values) {
		String typeId = UUID.randomUUID().toString();
		String typeName = "ExampleIntType";
		String listTypeId = UUID.randomUUID().toString();
		String listName = "ExampleListWithInt";

		IJSONListTypeInstance listTypeInstance = createListTypeInstance(typeId, listName);
		List<IJSONTypeInstance> typeInstance = createIntList(listTypeId, typeName, 3);
		listTypeInstance.setCollectionType(typeInstance.get(0).getDataType());
		listTypeInstance.getDataType().setCollectionType(typeInstance.get(0).getDataType());
		listTypeInstance.setSubTypes(typeInstance);

		IJSONDataPortInstance dto = new JSONDataPortInstance();
		dto.setPortInstanceType(PortInstanceType.StartDataPortInstance);
		dto.setName(portName);
		dto.setInstanceId(portId + "_instance_" + 1);
		dto.setTypeId(portId);
		dto.setDataTypeInstance(listTypeInstance);

		IJSONDataPort port = new JSONDataPort();
		port.setName(portName);
		port.setOptional(false);// if optional, the value can be set, but it is
								// not important
		port.setId(portId);
		port.setDataType(listTypeInstance.getDataType());
		port.setPortType(PortType.StartDataPort);
		dto.setPortType(port);

		return dto;
	}

	private static IJSONDataPortInstance createEndDataPortListDto(String portId, String portName, double... values) {
		String typeId = UUID.randomUUID().toString();
		String typeName = "ExampleIntType";
		String listTypeId = UUID.randomUUID().toString();
		String listName = "ExampleListWithInt";

		IJSONListTypeInstance listTypeInstance = createListTypeInstance(typeId, listName);
		List<IJSONTypeInstance> typeInstance = createIntList(listTypeId, typeName, 3);
		listTypeInstance.setCollectionType(typeInstance.get(0).getDataType());
		listTypeInstance.getDataType().setCollectionType(typeInstance.get(0).getDataType());
		listTypeInstance.setSubTypes(typeInstance);

		IJSONDataPortInstance dto = new JSONDataPortInstance();
		dto.setPortInstanceType(PortInstanceType.EndDataPortInstance);
		dto.setName(portName);
		dto.setInstanceId(portId + "_instance_" + 1);
		dto.setTypeId(portId);
		dto.setDataTypeInstance(listTypeInstance);

		IJSONDataPort port = new JSONDataPort();
		port.setName(portName);
		port.setOptional(false);// if optional, the value can be set, but it is
								// not important
		port.setId(portId);
		port.setDataType(listTypeInstance.getDataType());
		port.setPortType(PortType.EndDataPort);
		dto.setPortType(port);

		return dto;
	}

	private static List<IJSONTypeInstance> createIntList(String typeId, String typeName, int count) {
		List<IJSONTypeInstance> list = new ArrayList<IJSONTypeInstance>();
		for (int i = 0; i < count; i++)
			list.add(createIntTypeInstance(typeId, typeName, 123456));
		return list;
	}

	private static IJSONListTypeInstance createListTypeInstance(String typeId, String typeName) {
		IJSONListType type = new JSONListType();
		type.setID(typeId);
		type.setDataTypeType(DataTypeType.ListType);
		type.setName(typeName);

		IJSONListTypeInstance dTypeInstanceDto = new JSONListTypeInstance();
		dTypeInstanceDto.setDataTypeID(typeId);
		dTypeInstanceDto.setInstanceID(typeId + "_" + "instance_1");
		dTypeInstanceDto.setName(typeName);
		dTypeInstanceDto.setDataTypeInstanceType(DataTypeInstanceType.ListTypeInstance);
		dTypeInstanceDto.setDataType(type);
		return dTypeInstanceDto;
	}

	private static IJSONIntegerTypeInstance createIntTypeInstance(String typeId, String typeName, int value) {
		IJSONIntegerType type = new JSONIntegerType();
		type.setID(typeId);
		type.setDataTypeType(DataTypeType.IntegerType);
		type.setName(typeName);

		IJSONIntegerTypeInstance dTypeInstanceDto = new JSONIntegerTypeInstance();
		dTypeInstanceDto.setDataTypeID(typeId);
		dTypeInstanceDto.setInstanceID(typeId + "_" + "instance_1");
		dTypeInstanceDto.setValue(value);
		dTypeInstanceDto.setName(typeName);
		dTypeInstanceDto.setDataTypeInstanceType(DataTypeInstanceType.IntegerTypeInstance);
		dTypeInstanceDto.setDataType(type);
		return dTypeInstanceDto;
	}

	private static final String sPort1 = "start_portId_1";
	private static final String sPort2 = "start_portId_2";
	private static final String sPort3 = "start_portId_3";
	private static final String sPort4 = "start_portId_4";

	private static final String ePort1 = "end_portId_1";
	private static final String ePort2 = "end_portId_2";
	private static final String ePort3 = "end_portId_3";
	private static final String ePort4 = "end_portId_4";
	private static final String ePort5 = "end_portId_5";

}
