package com.alerts.src.service.impl;

import java.util.Date;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alerts.src.mongo.model.EventModel;
import com.alerts.src.mongo.repository.EventRepository;
import com.alerts.src.request.EventRequest;
import com.alerts.src.request.EventRequest.Alert;
import com.alerts.src.response.EventResponse;
import com.alerts.src.service.MyraEventService;

@Component
public class MyraEventServiceImpl implements MyraEventService {

	@Autowired
	private EventRepository eventRepository;
	
	@Override
	public EventResponse getAlerts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response addAlert(EventRequest eventRequest) {
		EventModel model = new EventModel();
		Alert alert = eventRequest.getAlert();
		model.setDelay(alert.getDelay());
		model.setDescription(alert.getDescription());
		model.setReferneceId(alert.getReferenceId());
		model.setCreatedAt(new Date().toString());
		eventRepository.save(model);
		return null;
	}

	@Override
	public Response revokeAlert(String refernceId) {
		// TODO Auto-generated method stub
		return null;
	}

}
