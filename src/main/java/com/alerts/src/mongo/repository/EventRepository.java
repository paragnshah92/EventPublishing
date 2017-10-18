package com.alerts.src.mongo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.alerts.src.mongo.model.EventModel;

public interface EventRepository extends CrudRepository<EventModel, String>{
	
	Long deleteByReferneceId(String referneceId);
	
	List<EventModel> findByStatusAndUserId(int skip,int limit,String status);
	
}
