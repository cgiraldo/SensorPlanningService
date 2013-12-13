package org.n52.sps.util.convert;

import javax.xml.namespace.QName;

import net.opengis.ows.x11.LanguageStringType;
import net.opengis.sps.x20.StatusReportType;
import net.opengis.sps.x20.SubmitDocument;
import net.opengis.sps.x20.SubmitResponseDocument;
import net.opengis.sps.x20.SubmitType;
import net.opengis.sps.x20.TaskingRequestType.TaskingParameters;
import net.opengis.swe.x20.TextEncodingDocument;
import net.opengis.swe.x20.TextType;

import org.apache.xmlbeans.XmlCursor;
import org.n52.sps.sensor.model.SensorTask;
import org.n52.sps.sensor.model.Task;
import org.springframework.stereotype.Service;

@Service
public class TaskHelperService {
	
	private static final String SERVICE = "SPS";
	private static final String SERVICE_VERSION = "2.0.0";

	public TaskHelperService(){}
	
	public SubmitDocument getSubmitDocument(Task task){
		SubmitDocument submitDoc = SubmitDocument.Factory.newInstance();
		SubmitType submit = submitDoc.addNewSubmit();
		submit.setService(SERVICE);
		submit.setVersion(SERVICE_VERSION);
		submit.setProcedure(task.getProcedureId());
		// Setting the parameters encoding
		TaskingParameters taskingParameters = submit.addNewTaskingParameters();
		taskingParameters.addNewParameterData();
		TextEncodingDocument textEncodingDoc = TextEncodingDocument.Factory.newInstance();
		textEncodingDoc.addNewTextEncoding().setTokenSeparator(",");
		textEncodingDoc.getTextEncoding().setBlockSeparator("@@");
		taskingParameters.getParameterData().addNewEncoding().set(textEncodingDoc);
		// Setting the parameters values
		String parameters = task.getStatus().getParameters();

		TextType textType = TextType.Factory.newInstance();
		textType.setValue(parameters);
		XmlCursor cursor = taskingParameters.getParameterData().newCursor();
		cursor.toEndToken();
		cursor.insertElementWithText(new QName("http://www.opengis.net/sps/2.0","values"), parameters);
		return submitDoc;
		
	}

	public Task getTask(SubmitResponseDocument submitResponseDocument){
		Task task = new Task();
		System.out.println(submitResponseDocument.xmlText());
		StatusReportType statusReport = submitResponseDocument.getSubmitResponse().getResult().getStatusReport();
		task.setId(statusReport.getTask());
		task.setProcedureId(statusReport.getProcedure());
		task.getStatus().setRequestStatus(statusReport.getRequestStatus());
		task.getStatus().setTaskStatus(statusReport.getTaskStatus());
		task.getStatus().setUpdateTime(statusReport.getUpdateTime());
		task.getStatus().setEstimatedToC(statusReport.getEstimatedToC());
		if (statusReport.getStatusMessageArray()!=null){
			for (LanguageStringType statusMessage:statusReport.getStatusMessageArray()){
				task.getStatus().addStatusMessage(statusMessage.getStringValue());
			}
		}
		if (statusReport.getTaskingParameters() !=null){
			task.getStatus().setParameters(statusReport.getTaskingParameters().getParameterData().getValues().newCursor().getTextValue());
		}
		return task;
	}
	
	public SensorTask getSensorTask(Task task){
		SensorTask sensorTask = new SensorTask(task.getId(),task.getProcedureId());
		
		sensorTask.setPercentCompletion(task.getStatus().getPercentCompletion());
		sensorTask.setRequestStatusAsString(task.getStatus().getRequestStatus());
		sensorTask.setTaskStatusAsString(task.getStatus().getTaskStatus());
		sensorTask.setUpdateTime(task.getStatus().getUpdateTime());
		sensorTask.setEstimatedToC(task.getStatus().getEstimatedToC());
		sensorTask.setStatusMessages(task.getStatus().getStatusMessages());
		return sensorTask;
	}
	// 
	public SensorTask updateSensorTask(Task task,SensorTask sensorTask){
		sensorTask.setPercentCompletion(task.getStatus().getPercentCompletion());
		sensorTask.setRequestStatusAsString(task.getStatus().getRequestStatus());
		sensorTask.setTaskStatusAsString(task.getStatus().getTaskStatus());
		sensorTask.setUpdateTime(task.getStatus().getUpdateTime());
		sensorTask.setEstimatedToC(task.getStatus().getEstimatedToC());
		sensorTask.setStatusMessages(task.getStatus().getStatusMessages());
		return sensorTask;
	}
	
	public Task getTask(SensorTask sensorTask){
		Task task = new Task();
		task.setId(sensorTask.getTaskId());
		task.setProcedureId(sensorTask.getProcedure());
		task.getStatus().setPercentCompletion(sensorTask.getPercentCompletion());
		task.getStatus().setRequestStatus(sensorTask.getRequestStatusAsString());
		task.getStatus().setTaskStatus(sensorTask.getTaskStatusAsString());
		task.getStatus().setUpdateTime(sensorTask.getUpdateTime());
		task.getStatus().setEstimatedToC(sensorTask.getEstimatedToC());
		if (sensorTask.getParameterData()!=null){
			task.getStatus().setParameters(sensorTask.getParameterData().getValues().newCursor().getTextValue());
		}
		if (sensorTask.getStatusMessages()!= null){
			for (String statusMessage:sensorTask.getStatusMessages()){
				task.getStatus().addStatusMessage(statusMessage);
			}
		}
		return task;
	}

}
