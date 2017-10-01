package com.alerts.src.mongo.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="event")
public class EventModel implements Serializable {

	private static final long serialVersionUID = 1053584296703078051L;

	@Id
	private String id;
	
	@Field("referneceId")
	private String referneceId;
	
	@Field("delay")
	private int delay;
	
	@Field("description")
	private String description;
	
	@Field("status")
	private String status;
	
	@Field("created_at")
	private String createdAt;

	public String getReferneceId() {
		return referneceId;
	}

	public void setReferneceId(String referneceId) {
		this.referneceId = referneceId;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
