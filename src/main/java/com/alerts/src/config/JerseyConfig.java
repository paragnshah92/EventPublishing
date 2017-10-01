package com.alerts.src.config;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.alerts.src.service.impl.EventServiceImpl;

@Component
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(EventServiceImpl.class);
	}

}