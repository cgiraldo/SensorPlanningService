package org.n52.sps.util.convert;

import net.opengis.swe.x20.AbstractDataComponentType;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.sps.sensor.model.InitParameter;
import org.n52.sps.sensor.model.SensorConfiguration;
import org.n52.sps.sensor.model.TaskingConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.x52North.schemas.sps.v2.InsertSensorOfferingDocument;
import org.x52North.schemas.sps.v2.InsertSensorOfferingDocument.InsertSensorOffering.SensorTaskingParametersSet.SingleParameterSet;


import es.itg.schemas.sensorconfig.SensorSetupDocument;
import es.itg.schemas.sensorconfig.SensorSetupDocument.SensorSetup;
import es.itg.schemas.sensorconfig.SensorSetupDocument.SensorSetup.SetupParameter;
import es.itg.sensorweb.services.helpers.swecommon.AbstractDataComponentHelperService;


@Service
public class TaskingConfigurationHelperService {
	
	@Autowired
	private AbstractDataComponentHelperService swecommonHelper;

	// Convert from simplified model to SWE XML
	public SensorConfiguration getSensorConfiguration(TaskingConfiguration taskingConfiguration) throws XmlException{
		SensorConfiguration sensorConfiguration = new SensorConfiguration();
		sensorConfiguration.setProcedure(taskingConfiguration.getProcedureId());
		sensorConfiguration.setSensorPluginType(taskingConfiguration.getPlugin());
		if (taskingConfiguration.getTaskingParameters()!=null){
			SingleParameterSet singleParameterSet = InsertSensorOfferingDocument.InsertSensorOffering
					.SensorTaskingParametersSet.SingleParameterSet.Factory.newInstance();
			singleParameterSet.setAbstractDataComponent(
					(AbstractDataComponentType) swecommonHelper.serializeSwecommon_v20Type(taskingConfiguration.getTaskingParameters()));
			sensorConfiguration.setTaskingParametersTemplate(singleParameterSet.getAbstractDataComponent());
		}
		if (taskingConfiguration.getInit() != null){
			sensorConfiguration.setSensorSetup(getSensorSetup(taskingConfiguration.getInit()));
		}
		return sensorConfiguration;	
	}
	
	public XmlObject getSensorSetup(InitParameter[] initParameters){
		 SensorSetupDocument sensorSetupDoc = SensorSetupDocument.Factory.newInstance();
		SensorSetup sensorSetup = sensorSetupDoc.addNewSensorSetup();
		for(InitParameter initParameter:initParameters){
			SetupParameter setupParameter = sensorSetup.addNewSetupParameter();
			setupParameter.setName(initParameter.getName());
			setupParameter.setDescription(initParameter.getDescription());
			setupParameter.setValue(initParameter.getValue());
		}
		return sensorSetupDoc;
	}
	
	// Convert from SWE XML to simplified model
	
	public TaskingConfiguration getTaskingConfiguration(SensorConfiguration sensorConfiguration) throws XmlException{
		TaskingConfiguration taskingConfiguration = new TaskingConfiguration();
		taskingConfiguration.setProcedureId(sensorConfiguration.getProcedure());
		taskingConfiguration.setPlugin(sensorConfiguration.getSensorPluginType());
		taskingConfiguration.setInit(getInitParameters(sensorConfiguration.getSensorSetup()));
		taskingConfiguration.setTaskingParameters(swecommonHelper.deserialize(sensorConfiguration.getTaskingParametersTemplate()));
		return taskingConfiguration;
	}
	
	public static InitParameter[] getInitParameters(XmlObject sensorSetupDoc){
		InitParameter[] initParameters = null;
		if (sensorSetupDoc !=null){
			SensorSetupDocument setupDoc = (SensorSetupDocument)sensorSetupDoc;
			initParameters = new InitParameter[setupDoc.getSensorSetup().getSetupParameterArray().length];
			for(int i=0;i<initParameters.length;i++){
				SetupParameter setupParameter= setupDoc.getSensorSetup().getSetupParameterArray(i);
				InitParameter initParameter = new InitParameter(setupParameter.getName(),setupParameter.getDescription(),setupParameter.getValue());
				initParameters[i] = initParameter;
			}
		}
		return initParameters;
	}

}
