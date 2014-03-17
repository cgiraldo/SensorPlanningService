package org.n52.sps.service.rest.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

//SPS imports
import org.n52.sps.service.InternalServiceException;
import org.n52.sps.service.rest.TaskingConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//json POJOs imports

import org.n52.sps.sensor.model.TaskingConfiguration;
//import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Consumes("application/json")
@Produces("application/json")
@Path("/processes/")

/**
 * Componente Rest para gestionar los sensores registrados en el módulo de tasking.
 * Para que un sensor esté registrado requiere una configuración 
 * {@link es.itg.sensorweb.sps.model.TaskingConfiguration} asociada
 * a su identificador (procedureId).
 * 
 * @author cgiraldo
 */
public class TaskingConfigurationResource{

  @Autowired    
  protected TaskingConfigurationService taskingConfigurationService;


  private static final Logger LOGGER = LoggerFactory.getLogger(TaskingConfigurationResource.class);

	@POST
	/**
	 *  Añade una nueva {@link es.itg.sensorweb.sps.model.TaskingConfiguration} al módulo de tasking (registro).
	 * @param taskingConfiguration. TaskingConfiguration a añadir.
	 * @param uriInfo
	 * @return
	 */
	public Response insertTaskingConfiguration(TaskingConfiguration taskingConfiguration, @Context UriInfo uriInfo) {

		//String result_string = taskingConfiguration.isValid();
		//if (result_string.equals("OK")){
		try{
		taskingConfigurationService.insertTaskingConfiguration(taskingConfiguration);
		URI uri = new URI(uriInfo.getAbsolutePath().toString()+"/"+taskingConfiguration.getProcedureId());
		return Response.created(uri).build(); 
		} 
		catch (InternalServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().entity(e.toString()).build();
		}
		catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.ok().entity(e.toString()).build();
		}
		//}
		//else {
		//	return Response.serverError().entity(result_string).build();
		//}
	}

	/**
	 *  Obtiene la lista de sensores registrados en el módulo de tasking (su procedureId).
	 * 
	 * @return Response Http con String[] con los procedureIds.
	 */
	@GET
	public Response getTaskingConfigurations(@Context UriInfo uriInfo) {
		List<TaskingConfiguration> taskingConfigurations = taskingConfigurationService.getTaskingConfigurations();
		List<URI> taskingConfigurationIds = new ArrayList<URI>();
		for (TaskingConfiguration taskingConfiguration:taskingConfigurations){
			taskingConfigurationIds.add(setPrefix(uriInfo.getAbsolutePath().toString(),taskingConfiguration.getProcedureId()));
		}
		return Response.ok(taskingConfigurationIds).build();
	}
	
	@Path("/{procedureId}")
	@GET
	public Response getTaskingConfiguration(@PathParam("procedureId") String procedureId) {
	
		TaskingConfiguration taskingConfiguration = taskingConfigurationService.getTaskingConfiguration(procedureId);
		if (taskingConfiguration == null){
			return Response.status(Response.Status.NOT_FOUND).entity("No TaskingConfiguration for procedure "+procedureId).build();
		}
		else{
			return Response.ok().entity(taskingConfiguration).build();
		}
	}

	@Path("/{procedureId}")
	@DELETE
	public Response removeTaskingConfiguration(@PathParam("procedureId") String procedureId) {

		try {
			taskingConfigurationService.removeTaskingConfiguration(procedureId);
		} catch (InternalServiceException e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}
	
	private URI setPrefix(String prefix,String resourceId){
		String resourceURI;
		// We add a backslash in case the last char of the prefix it is not
		if (prefix.length() > 0 && prefix.charAt(prefix.length()-1)=='/') {
				resourceURI = prefix+resourceId;
		}
		else{
			resourceURI = prefix+"/"+resourceId;
		}
		URI uri = URI.create(resourceURI);
		return uri;
	}
}

