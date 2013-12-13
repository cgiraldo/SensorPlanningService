/**
 * Copyright (C) 2013
 * by Instituto Tecnológico de Galicia
 *
 * Contact: Carlos Giraldo
 * 52 Instituto Tecnológico de Galicia
 * PO.CO.MA.CO. Sector i, Portal 5 15190, A Coruña
 * A Coruña, Spain
 * cgiraldo@5itg.es
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */

package org.n52.sps.service.rest;

import java.util.ArrayList;
import java.util.List;

import net.opengis.sps.x20.SubmitResponseDocument;

import org.n52.ows.exception.InvalidParameterValueException;
import org.n52.ows.exception.OwsException;
import org.n52.ows.exception.OwsExceptionReport;
import org.n52.sps.sensor.model.SensorTask;
import org.n52.sps.sensor.model.Task;
import org.n52.sps.service.SensorPlanningService;
import org.n52.sps.service.core.BasicSensorPlanner;
import org.n52.sps.store.SensorTaskRepository;
import org.n52.sps.util.convert.TaskHelperService;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskServiceImpl implements TaskService {

    private SensorTaskRepository sensorTaskRepository;
    
    private SensorPlanningService sensorPlanningService;
    @Autowired
	private TaskHelperService taskHelper;

    public SensorTaskRepository getSensorTaskRepository() {
        return sensorTaskRepository;
    }

    public void setSensorTaskRepository(SensorTaskRepository sensorTaskRepository) {
        this.sensorTaskRepository = sensorTaskRepository;
    }
    
    public void setSensorPlanningService(SensorPlanningService sensorPlanningService) {
		this.sensorPlanningService = sensorPlanningService;
	}
    
    public void setTaskHelper(TaskHelperService taskHelper) {
		this.taskHelper = taskHelper;
	}

	private List<String> iterableToArray(Iterable<String> iterableString) {
        ArrayList<String> array = new ArrayList<String>();
        for (String iter : iterableString) {
            array.add(iter);
        }
        return array;
    }
	
	public List<String> getTaskIds() {
	        Iterable<String> iterableSensorTasks = sensorTaskRepository.getSensorTaskIds();
	        return iterableToArray(iterableSensorTasks);
	   }
	
    public List<String> getTaskIds(String procedure) {
        Iterable<String> iterableSensorTasks = sensorTaskRepository.getSensorTaskIds(procedure);
        return iterableToArray(iterableSensorTasks);
    }
    
    public Task getTask(String taskId){
    	SensorTask sensorTask = sensorTaskRepository.getTask(taskId);
    	if (sensorTask!=null){
    		return taskHelper.getTask(sensorTaskRepository.getTask(taskId));
    	}
    	else{
    		return null;
    	}
    }
    
    public Task submitTask(Task task) throws OwsException, OwsExceptionReport{
    	BasicSensorPlanner basicSensorPlanner = sensorPlanningService.getBasicSensorPlanner();
    	SubmitResponseDocument submitResponseDoc=(SubmitResponseDocument) basicSensorPlanner.submit(taskHelper.getSubmitDocument(task));
    	task = taskHelper.getTask(submitResponseDoc);
    	return task;

    }
    
    public void deleteTask(String task_id) throws OwsException{
    	SensorTask sensorTask = sensorTaskRepository.getTask(task_id);
    	if (sensorTask==null) {
    		InvalidParameterValueException e = new InvalidParameterValueException("Delete Task");
    		e.addExceptionText("No task with id "+task_id);
    		throw e;
    	}
    	//if (sensorTask.getRequestStatus() == TaskingRequestStatus.REJECTED){
    	sensorTaskRepository.removeSensorTask(sensorTask);
    	/*}
    	else{ 
    		if (sensorTask.getRequestStatus() == TaskingRequestStatus.ACCEPTED){
    			if (sensorTask.getTaskStatus() == SensorTaskStatus.COMPLETED ||
    					sensorTask.getTaskStatus() == SensorTaskStatus.FAILED ||
    					sensorTask.getTaskStatus() == SensorTaskStatus.EXPIRED ||
    					sensorTask.getTaskStatus() == SensorTaskStatus.CANCELLED){
    				sensorTaskRepository.removeSensorTask(sensorTask);	
    			}
    			else{
    				OperationNotSupportedException e = new OperationNotSupportedException("Delete Task");
    				e.addExceptionText("The Status of the Task does not allow the Delete Operation");
    				throw e;
    			}
    		}
    		else{
    			OperationNotSupportedException e = new OperationNotSupportedException("Delete Task");
				e.addExceptionText("The Status of the Task Request does not allow the Delete Operation");
				throw e;
    		}
    	}*/
    }
    
    public void updateTask(Task task) throws OwsException{
    	SensorTask sensorTask = sensorTaskRepository.getSensorTask(task.getProcedureId(),task.getId());
    	sensorTaskRepository.updateSensorTask(taskHelper.updateSensorTask(task,sensorTask));
    }
}
