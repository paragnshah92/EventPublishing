import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.ws.rs.core.Response;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alerts.src.mongo.model.EventModel;
import com.alerts.src.request.EventRequest;
import com.alerts.src.request.EventRequest.Alert;
import com.alerts.src.response.EventResponse;
import com.alerts.src.service.impl.EventServiceImpl;
import com.mongodb.MongoClient;
import org.junit.runners.MethodSorters;

import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:web-config.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventTest {
	
	@Autowired
	private EventServiceImpl eventServiceImpl;
	
	@Test
	public void testMongoConnectionAndFindAndSaveOperation() throws IOException {
		EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
		mongo.setBindIp("localhost");
		mongo.setPort(27001);
		MongoClient mongoClient = mongo.getObject();
		MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "embeded_db");
		EventModel model = new EventModel();
		TimeZone zone = TimeZone.getTimeZone("IST");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.sss"); 
		format.setTimeZone(zone);
		model.setCreatedAt(format.format(new Date()));
		model.setDelay(10);
		model.setReferneceId("first_test_reference");
		model.setDescription("First Test Alert");
		mongoTemplate.save(model);
		assertNotNull(mongoTemplate.findAll(EventModel.class));
		mongoClient.close();
	}
	
	@Test
	public void testPostAlertIfReferenceIdIsNull() throws IOException {
		EventRequest request = new EventRequest();
		Alert alert = new Alert();
		alert.setDelay(10);
		alert.setDescription("Test");
		request.setAlert(alert);
		Response response = eventServiceImpl.addAlert(request);
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void testPostAlertIfDelayIsLessThanEqualZero() throws IOException {
		EventRequest request = new EventRequest();
		Alert alert = new Alert();
		alert.setDelay(-10);
		alert.setDescription("Test");
		alert.setReferenceId("second_test_reference_id");
		request.setAlert(alert);
		Response response = eventServiceImpl.addAlert(request);
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void testPostAlertIfRequestOrAlertIsNull() throws IOException {
		EventRequest request = new EventRequest();
		Response response = eventServiceImpl.addAlert(request);
		assertEquals(400, response.getStatus());
		EventRequest eventRequest = null;
		Response response2 = eventServiceImpl.addAlert(eventRequest);
		assertEquals(400, response2.getStatus());
	}
	
	@Test
	public void testPostAlertIfRequestIsValid() throws IOException {
		EventRequest request = new EventRequest();
		Alert alert = new Alert();
		alert.setDelay(15);
		alert.setReferenceId("first_test_reference_id");
		alert.setDescription("Test");
		request.setAlert(alert);
		Response response = eventServiceImpl.addAlert(request);
		assertEquals(201, response.getStatus());
	}
	
	@Test
	public void testRetrieveAlertWithNoResult() throws IOException {
		EventResponse response = eventServiceImpl.getAlerts();
		assertTrue(response.getAlerts().isEmpty());
	}
	
	@Test
	public void testRetrieveAlertWithResult() throws IOException, InterruptedException {
		//Making thread sleep for 17 Seconds as first_test_reference_id alert has delay of 15 Seconds 
		Thread.sleep(17000);
		EventResponse response = eventServiceImpl.getAlerts();
		assertFalse(response.getAlerts().isEmpty());
	}
	
	@Test
	public void testRetrieveAlertWithResultDelayCheck() throws IOException {
		EventResponse response = eventServiceImpl.getAlerts();
		//This will confirm that computed delay started after delay that was given when posting alert exceeded the time from when it was created
		assertTrue(response.getAlerts().get(0).getDelay() >= 2 && response.getAlerts().get(0).getDelay() < 15);
	}
	
	@Test
	public void testRevokeAlertIfCorrectRefernceId() throws IOException {
		Response response = eventServiceImpl.revokeAlert("first_test_reference_id");
		assertEquals(204, response.getStatus());
	}
	
	@Test
	public void testRevokeAlertIfInCorrectRefernceId() throws IOException {
		EventRequest request = new EventRequest();
		Alert alert = new Alert();
		alert.setDelay(10);
		alert.setReferenceId("second_test_reference_id");
		alert.setDescription("Test");
		request.setAlert(alert);
		eventServiceImpl.addAlert(request);
		Response response = eventServiceImpl.revokeAlert("sec_test_reference_id");
		assertEquals(304, response.getStatus());
	}
	
}
