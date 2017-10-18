package com.alerts.src.service.manager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alerts.src.mongo.model.EventModel;
import com.alerts.src.mongo.repository.EventRepository;
import com.alerts.src.mongo.repository.impl.EventRepositoryImpl;
import com.alerts.src.response.EventResponse;
import com.alerts.src.response.EventResponse.Alerts;

@Component
public class EventServiceManager {
	
	Logger logger = LoggerFactory.getLogger(EventServiceManager.class);
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	EventRepositoryImpl eventRepositoryImpl;
	
	private static final String INITSTATUS = "INITIATED";
	private static final String REVOKESTATUS = "REVOKED";
	private static final int FLAGTIME = 300000;
	private static final int FLAGCOUNT = 3;

	public EventResponse getAlerts() {
		EventResponse response = new EventResponse();
		List<Alerts> alertList = new ArrayList<>();
		Map<String,Integer> flagMap = new HashMap<>();
		try {
			List<EventModel> eventModelList = (List<EventModel>) eventRepository.findAll();
			if (eventModelList != null && !eventModelList.isEmpty()) {
				for (EventModel model : eventModelList) {
					TimeZone zone = TimeZone.getTimeZone("IST");
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.sss"); 
					format.setTimeZone(zone);
					Date date = format.parse(model.getCreatedAt());
					Calendar cal = Calendar.getInstance(zone);
					cal.setTime(date);
					cal.add(Calendar.SECOND, model.getDelay());
					Calendar current = Calendar.getInstance(zone);
					int delay = (int) ((current.getTimeInMillis() - cal.getTimeInMillis()) / 1000);
					long flagTime = current.getTimeInMillis() - FLAGTIME;
					if (delay > 0) {
						if (StringUtils.equalsIgnoreCase(INITSTATUS, model.getStatus())) {
							Alerts alert = new Alerts();
							alert.setDelay(delay);
							alert.setDescription(model.getDescription());
							alert.setReferenceId(model.getReferneceId());
							alert.setUserId(model.getUserId());
							alertList.add(alert);
						}
						int count;
						if (cal.getTimeInMillis() >= flagTime) {
							if (flagMap.containsKey(model.getUserId())) {
								count = flagMap.get(model.getUserId()) + 1;
							} else {
								count = 1;
							}
							flagMap.put(model.getUserId(), count);
						}
					}
				}
				if (!flagMap.isEmpty()) {
					for (String key : flagMap.keySet()) {
						int val = flagMap.get(key);
						if (val >= FLAGCOUNT) {
							Alerts alert = new Alerts();
							alert.setUserId(key);
							alert.setUserFlag(true);
							alertList.add(alert);
						}
					}
				}
			}
			response.setAlerts(alertList);
		} catch (Exception e) {
			logger.error("Issue while getting all the alerts : " , e.getMessage());
		}
		return response;
	}
	
	public int revokeAlert(String refernceId) {
		int status;
		try {
			int count = eventRepositoryImpl.updateStatusByReferneceId(REVOKESTATUS, refernceId);
			if (count == 1) {
				status = 204;
			} else {
				status = 304;
			}
		}catch (Exception e) {
			logger.error("Issue while revoking the alert : " , e.getMessage());
			status = 304; 
		}
		return status;
	}
	
	public void test() {
		Map<String,Object> keyValueMap = new HashMap<>();
		keyValueMap.put("status", INITSTATUS);
		keyValueMap.put("referneceId", "1");
		Map<String,Map<String,Object>> topMap = new HashMap<>();
		topMap.put("$and", keyValueMap);
		int count = eventRepositoryImpl.updateStatusByRefernece(topMap);
		keyValueMap.clear();
		topMap.clear();
		List<String> refId = new ArrayList<>();
		refId.add("1");
		refId.add("2");
		keyValueMap.put("$in", refId);
		topMap.put("referneceId", keyValueMap);
		count = eventRepositoryImpl.updateStatusByRefernece(topMap);
	}
	
}
