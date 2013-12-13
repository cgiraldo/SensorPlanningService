/**
 * Copyright (C) 2012
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
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


import org.apache.xmlbeans.XmlException;
import org.n52.sps.sensor.model.SensorConfiguration;
import org.n52.sps.sensor.model.TaskingConfiguration;

import org.n52.sps.service.InternalServiceException;
import org.n52.sps.service.core.SensorInstanceProvider;
import org.n52.sps.store.SensorConfigurationRepository;
import org.n52.sps.util.convert.TaskingConfigurationHelperService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

//TODO Create Exceptions and manage them
public class TaskingConfigurationServiceImpl implements TaskingConfigurationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskingConfigurationServiceImpl.class);

	private SensorInstanceProvider sensorInstanceProvider;
	private SensorConfigurationRepository sensorConfigurationRepository;
	
	@Autowired
	private TaskingConfigurationHelperService taskingConfigurationHelper;
	
	public void setSensorInstanceProvider(
			SensorInstanceProvider sensorInstanceProvider) {
		this.sensorInstanceProvider = sensorInstanceProvider;
	}

	public void setSensorConfigurationRepository(
			SensorConfigurationRepository sensorConfigurationRepository) {
		this.sensorConfigurationRepository = sensorConfigurationRepository;
	}
	
	public void setTaskingConfigurationHelper(
			TaskingConfigurationHelperService taskingConfigurationHelper) {
		this.taskingConfigurationHelper = taskingConfigurationHelper;
	}
	
	public void insertTaskingConfiguration(TaskingConfiguration taskingConfiguration) throws InternalServiceException{	
		SensorConfiguration sensorConfiguration;
		try {
			sensorConfiguration = taskingConfigurationHelper.getSensorConfiguration(taskingConfiguration);
		} catch (XmlException e) {
			e.printStackTrace();
			//TODO throw custom exception
			return;
		}
		sensorInstanceProvider.handleInsertSensorConfiguration(sensorConfiguration);
	}

    public List<TaskingConfiguration> getTaskingConfigurations(){
    	List<TaskingConfiguration> taskingConfigurations = new ArrayList<TaskingConfiguration>();
    	for (SensorConfiguration sensorConfiguration:sensorConfigurationRepository.getSensorConfigurations()){
    		try {
				taskingConfigurations.add(taskingConfigurationHelper.getTaskingConfiguration(sensorConfiguration));
			} catch (XmlException e) {
				e.printStackTrace();
				//TODO throw custom Exception
				return null;
			}
    	}
    	return taskingConfigurations;
    }
    
	public TaskingConfiguration getTaskingConfiguration(String procedureId){
		TaskingConfiguration taskingConfiguration = null;
		SensorConfiguration sensorConfiguration = sensorConfigurationRepository.getSensorConfiguration(procedureId);
		if (sensorConfiguration !=null) {
			try {
				taskingConfiguration = taskingConfigurationHelper.getTaskingConfiguration(sensorConfiguration);
			} catch (XmlException e) {
				e.printStackTrace();
				//TODO throw custom Exception
				return null;
			}
		}
		return taskingConfiguration;
	}
	
	public void removeTaskingConfiguration(String procedureId) throws InternalServiceException{
		//remove Sensor Instance
		
		sensorInstanceProvider.removeSensorInstance(procedureId);
		//remove SensorConfiguration
		SensorConfiguration sensorConfiguration = sensorConfigurationRepository.getSensorConfiguration(procedureId);
		sensorConfigurationRepository.removeSensorConfiguration(sensorConfiguration);
		//TODO remove associated Tasks
		//Iterable<String> iterableSensorTasksIds = sensorTaskRepository.getSensorTaskIds(procedureId);
		//for (String taskId:iterableSensorTasksIds) {
		//	SensorTask sensorTask = sensorTaskRepository.getTask(taskId);
		//	sensorTaskRepository.removeSensorTask(sensorTask);
		//}
	}
}

