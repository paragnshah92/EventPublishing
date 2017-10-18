package com.alerts.src.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alerts.src.mongo.model.EventModel;
import com.alerts.src.mongo.repository.EventRepository;
import com.alerts.src.mongo.repository.impl.EventRepositoryImpl;
import com.alerts.src.request.EventRequest;
import com.alerts.src.request.EventRequest.Alert;
import com.alerts.src.response.EventResponse;
import com.alerts.src.response.EventResponse.Alerts;
import com.alerts.src.service.EventService;
import com.alerts.src.service.manager.EventServiceManager;

@Component
public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private EventServiceManager eventServiceManager;

	Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
	
	private static final String INITSTATUS = "INITIATED";
	private static final String REVOKESTATUS = "REVOKED";

	@Override
	public EventResponse getAlerts() {
		eventServiceManager.test();
		return eventServiceManager.getAlerts();
	}

	@Override
	public Response addAlert(EventRequest eventRequest) {
		EventModel model = new EventModel();
		int status;
		String message;
		if (eventRequest != null && eventRequest.getAlert() != null
				&& StringUtils.isNotBlank(eventRequest.getAlert().getReferenceId())
				&& eventRequest.getAlert().getDelay() > 0) {
			Alert alert = eventRequest.getAlert();
			model.setDelay(alert.getDelay());
			model.setDescription(alert.getDescription());
			model.setReferneceId(alert.getReferenceId());
			model.setUserId(alert.getUserId());
			model.setStatus(INITSTATUS);
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.sss"); 
			format.setTimeZone(TimeZone.getTimeZone("IST"));
			model.setCreatedAt(format.format(new Date()));
			eventRepository.save(model);
			status = 201;
			message = "Alert added successfully!!!";
		} else {
			status = 400;
			message = "Alert Not added, missing required params...";
		}
		return Response.status(status).type("text/plain")
                .entity(message).build();
	}

	@Override
	public Response revokeAlert(String refernceId) {
		int status = eventServiceManager.revokeAlert(refernceId);
		return Response.status(status).build();
	}

}
