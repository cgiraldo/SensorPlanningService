package org.n52.sps.service.rest.resources;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.n52.ows.exception.OwsException;
import org.n52.ows.exception.OwsExceptionReport;
//SPS imports
import org.n52.sps.service.InternalServiceException;
import org.n52.sps.service.rest.TaskService;
import org.n52.sps.service.rest.TaskingConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//json POJOs imports

import org.n52.sps.sensor.model.Task;
import org.n52.sps.sensor.model.TaskingConfiguration;
//import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Consumes("application/json")
@Produces("application/json")
@Path("/tasks/")

/**
 * Componente Rest para gestionar los sensores registrados en el módulo de tasking.
 * Para que un sensor esté registrado requiere una configuración 
 * {@link es.itg.sensorweb.sps.model.TaskingConfiguration} asociada
 * a su identificador (procedureId).
 * 
 * @author cgiraldo
 */
public class TaskResource{

	@Autowired    
	protected TaskService taskService;

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskResource.class);

    @GET
	public Response getTasks(@Context UriInfo uriInfo) {
		List<URI> taskIds = new ArrayList<URI>();
		for (String taskId:taskService.getTaskIds()){
			taskIds.add(setPrefix(uriInfo.getAbsolutePath().toString(),taskId));
		}
		return Response.ok(taskIds).build();
	}
    
    @GET
	@Path("/{taskId}")
	public Response getTask(@PathParam("taskId") String taskId) {
    	Task task = taskService.getTask(taskId);
    	if (task!=null){
    		return Response.ok(task).build();	
    	}
    	else {
    		return Response.status(Status.NOT_FOUND).entity("The task "+taskId+" does not exist in the server.").build();
    	}
	}

	@POST
	public Response postTask(Task task, @Context UriInfo uriInfo) {
		try {
			Task createdTask = taskService.submitTask(task);
			return Response.created(
					setPrefix(uriInfo.getAbsolutePath().toString(),createdTask.getId()))
					.entity(createdTask).build();
			
		} catch (OwsException e) {
			return Response.status(e.getHttpStatusCode()).entity(e.getExceptionTexts()).build();
		} catch (OwsExceptionReport e) {
			return Response.serverError().entity(e.getMessage()+e.getStackTrace()).build();
		}
	}
	
	@Path("/{id}")
	@PUT
	@Consumes("application/json")
	public Response updateTask(@PathParam("id") String task_id,Task task) {
		try{
			task.setId(task_id);
		    taskService.updateTask(task);
			return Response.ok().build();
		}
		catch (OwsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(e.getHttpStatusCode()).entity(e.getExceptionTexts()).build();
		} 
	}
	
	@Path("/{id}")
	@DELETE
	@Produces("application/json")
	public Response deleteTask(@PathParam("id") String task_id) {
		try{
		    taskService.deleteTask(task_id);
			return Response.ok().build();
		}
		catch (OwsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(e.getHttpStatusCode()).entity(e.getExceptionTexts()).build();
		} 
	}
	
	private URI setPrefix(String prefix,String resourceId){
		String resourceURI;
		try {
		// We add a backslash in case the last char of the prefix it is not
		if (prefix.length() > 0 && prefix.charAt(prefix.length()-1)=='/') {
				resourceURI = prefix+URLEncoder.encode(resourceId,"UTF-8");		}
		else{
			resourceURI = prefix+"/"+URLEncoder.encode(resourceId,"UTF-8");
		}
		URI uri = URI.create(resourceURI);
		LOGGER.debug(resourceURI);
		return uri;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}


