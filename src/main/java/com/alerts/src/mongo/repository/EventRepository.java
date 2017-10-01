package com.alerts.src.mongo.repository;

import org.springframework.data.repository.CrudRepository;

import com.alerts.src.mongo.model.EventModel;

public interface EventRepository extends CrudRepository<EventModel, String>{
	
	void deleteByReferneceId(String referneceId);
	
}
