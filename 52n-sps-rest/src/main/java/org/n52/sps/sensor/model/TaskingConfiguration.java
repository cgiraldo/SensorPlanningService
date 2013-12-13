package org.n52.sps.sensor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import es.itg.sensorweb.model.swecommon.AbstractDataComponent;



@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskingConfiguration {
	
	String procedureId;

	String plugin;
	
	InitParameter[] init;

	AbstractDataComponent taskingParameters;
	
	String dataResultAccess;

	public String getProcedureId() {
		return procedureId;
	}
	public void setProcedureId(String procedureId) {
		this.procedureId = procedureId;
	}
	public String getPlugin() {
		return plugin;
	}
	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}
	public String getDataResultAccess() {
		return dataResultAccess;
	}
	public void setDataResultAccess(String dataResultAccess) {
		this.dataResultAccess = dataResultAccess;
	}
	public InitParameter[] getInit() {
		return init;
	}
	public void setInit(InitParameter[] init) {
		this.init = init;
	}
	public AbstractDataComponent getTaskingParameters() {
		return taskingParameters;
	}
	public void setTaskingParameters(AbstractDataComponent taskingParameters) {
		this.taskingParameters = taskingParameters;
	}
}