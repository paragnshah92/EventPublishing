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
		private String userId;
		private boolean userFlag;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public boolean getUserFlag() {
			return userFlag;
		}

		public void setUserFlag(boolean userFlag) {
			this.userFlag = userFlag;
		}

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
