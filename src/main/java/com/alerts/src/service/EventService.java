package com.alerts.src.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.alerts.src.request.EventRequest;
import com.alerts.src.response.EventResponse;

@Path("events")
public interface EventService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public EventResponse getAlerts();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addAlert(EventRequest eventRequest);

	@DELETE
	public Response revokeAlert(@QueryParam("refernce_id") String refernceId);
}
