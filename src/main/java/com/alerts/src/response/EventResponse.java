package com.alerts.src.response;

import java.util.List;

public class EventResponse {

	private List<Alerts> alerts;

	public List<Alerts> getAlerts() {
		return alerts;
	}

	public void setAlerts(List<Alerts> alerts) {
		this.alerts = alerts;
	}

	public static class Alerts {

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
