package org.n52.sps.control.rest;
import org.glassfish.jersey.server.ResourceConfig;

import org.n52.sps.service.rest.resources.TaskResource;
import org.n52.sps.service.rest.resources.TaskingConfigurationResource;

public class JerseyApplication extends ResourceConfig {

	/**
	 * Register JAX-RS application components.
	 */

	public JerseyApplication() {
		register(TaskingConfigurationResource.class);
		register(TaskResource.class);
	}
}

