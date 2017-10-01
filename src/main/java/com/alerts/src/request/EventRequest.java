package com.alerts.src.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EventRequest {
	
	private Alert alert;

	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	public static class Alert {
		
		@JsonProperty(value="reference_id")
		private String referenceId;
		private int delay;
		private String description;

		public String getReferenceId() {
			return referenceId;
		}

		public void setReferenceId(String referenceId) {
			this.referenceId = referenceId;
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

	}
}
