package org.n52.sps.sensor.model;

import java.util.ArrayList;
import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Task {
	String id;
	String procedureId;
	String blockSeparator;
	String tokenSeparator;
	String parameters; 
	Status status;

	public Task(){
		status = new Status();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(String procedureId) {
		this.procedureId = procedureId;
	}
	
	public String getBlockSeparator() {
		return blockSeparator;
	}

	public void setBlockSeparator(String blockSeparator) {
		this.blockSeparator = blockSeparator;
	}

	public String getTokenSeparator() {
		return tokenSeparator;
	}

	public void setTokenSeparator(String tokenSeparator) {
		this.tokenSeparator = tokenSeparator;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public class Status{
		Calendar estimatedToC;
		double percentCompletion;
		String requestStatus;
		String taskStatus;
		ArrayList<String> statusMessages;
		Calendar updateTime;
		
		public Status(){
			statusMessages = new ArrayList<String>();
		}

		public Calendar getEstimatedToC() {
			return estimatedToC;
		}

		public void setEstimatedToC(Calendar estimatedToC) {
			this.estimatedToC = estimatedToC;
		}

		public double getPercentCompletion() {
			return percentCompletion;
		}
		public void setPercentCompletion(double percentCompletion) {
			this.percentCompletion = percentCompletion;
		}

		public String getRequestStatus() {
			return requestStatus;
		}

		public void setRequestStatus(String requestStatus) {
			this.requestStatus = requestStatus;
		}

		public String getTaskStatus() {
			return taskStatus;
		}

		public void setTaskStatus(String taskStatus) {
			this.taskStatus = taskStatus;
		}

		public ArrayList<String> getStatusMessages() {
			return statusMessages;
		}

		public void setStatusMessages(ArrayList<String> statusMessages) {
			this.statusMessages = statusMessages;
		}

		public void addStatusMessage(String statusMessage){
			this.statusMessages.add(statusMessage);
		}

		public Calendar getUpdateTime() {
			return updateTime;
		}
		public void setUpdateTime(Calendar updateTime) {
			this.updateTime = updateTime;
		}
	}
}