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

package es.itg.sensorweb.sps.plugins.generic;

import javax.xml.namespace.QName;

import net.opengis.sps.x20.DescribeResultAccessResponseType.Availability;
import net.opengis.swe.x20.AbstractDataComponentType;

import org.n52.ows.exception.OwsException;
import org.n52.ows.exception.OwsExceptionReport;
import org.n52.oxf.xmlbeans.tools.XMLBeansTools;
import org.n52.sps.sensor.SensorPlugin;
import org.n52.sps.sensor.SensorTaskService;
import org.n52.sps.sensor.model.SensorConfiguration;
import org.n52.sps.sensor.model.SensorTask;
import org.n52.sps.service.InternalServiceException;
import org.n52.sps.tasking.SubmitTaskingRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericPlugin extends SensorPlugin {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenericPlugin.class);
    
    public GenericPlugin(SensorTaskService sensorTaskService, SensorConfiguration configuration) throws InternalServiceException {
        super(sensorTaskService, configuration);
    }

    @Override
    public SensorTask submit(SubmitTaskingRequest submit, OwsExceptionReport owsExceptionReport) throws OwsException {
    	LOGGER.debug("submit method called of procedure '{}'", getProcedure());
        SensorTask submitTask = sensorTaskService.createNewTask();
        submitTask.setParameterData(submit.getParameterData());
        return submitTask;
    }
    
	@Override
    public void qualifyDataComponent(AbstractDataComponentType componentToQualify) {
        QName qname = new QName("http://www.opengis.net/swe/2.0", "DataRecord");
        XMLBeansTools.qualifySubstitutionGroup(componentToQualify, qname);
    }
	
    @Override
    public Availability getResultAccessibilityFor(SensorTask sensorTask) {
    	//TODO
    	return null;
    }
    public boolean isDataAvailable(){
    	//TODO
    	return true;
    }
    public Availability getResultAccessibility(){
    	//TODO
    	return null;
    }
}

