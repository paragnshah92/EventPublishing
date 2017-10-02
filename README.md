# EventPublishing
Repository For publishing and storing events using embedded mongo


1. I have used Spring boot application for this so to run it import it to any IDE like eclipse and run application.java class as spring boot application or run maven package command and execute the jar file generated using java -jar command.
	The reason for choosing the spring boot app is that it is light weight, comes packaged with spring dependencies and can be run on any platform with java installed in windows or linux env.
	
2. For storing data i have used embedded mongo which creates local mongo db everytime you run the application it is configured to run on localhost with port 27000 and db name is 'embeded_db', you can check all this detail in MongoConfig.java class.
	Only issue with this is sometimes when you run the application, stop it and run it again it gives exception that port 27000 is already in use even though application was stopped. In that case just again run the application after refreshing.
	You can connect to mongo after running the application using command 'mongo localhost:27000/embeded_db'.
	This will connect to mongo db which was started by embedded mongo on port 27000.
	The collection name that i have created is 'event' , which will store all the alerts. Once u restart the application all the previous data will disappear.
	
3. For APIs I have decided to go with REST (JAX-RS) and have used jersey implementation for that.
	REST is easy to use has no extra overhead required as SOAP and has HTTP protocol at its core. jersey makes the development of RESTful services easier. Also it is very easy to create RESTful services that can be deployed to any java application server.
	Another reason is my comfort with it, as I use it almost on daily basis.	
	
4. For unit tests I have used junit.

5. API Details : 

	1. For Posting an Alert :
		Endpoint - http://localhost:8080/events
		Method - Post
		Body - {
					"alert" : {
						"reference_id" : "reference_start_3",
						"delay" : 10,
						"description" : "reference 3 not started"
					}
				} 
		Response Code - 201
		Message - Alert added successfully!!!		
					
		This API will store the posted alert in mongo with current timestamp which help in calculating if when listing alerts API 		is called whether this alert should be displayed or not.
	
	2 . For Revoking an Alert:
		Endpoint - http://localhost:8080/events?refernce_id=reference_start_3
		Method - Delete
		Response Code - 204
		
		This api will remove the corresponding entry from event collections based on reference_id passed on as Query Attribute. 
		
	3. For Listing Alerts : 
		Endpoint - http://localhost:8080/events
		Method - GET
		Response Code - 200
		Response - {
					    "alerts": [
					        {
					            "referenceId": "reference_start_2",
					            "delay": 35,
					            "description": "reference 2 not started"
					        },
					        {
					            "referenceId": "reference_start_3",
					            "delay": 10,
					            "description": "reference 3 not started"
					        }
					    ]
					}
					
		This API will list all the alerts who have actually exceeded their delay period since the time they were posted and will 		show the actual delay after passing their delay limit as obtained from delay field in posting API.
		For eg : 
		
		Alert with reference_id = reference_start_3 was posted at 10:00:30 AM and had delay of 15 seconds, than the listing api 		will display this alert if it is not revoked before 10:00:45 AM and after 10:00:45 AM its delay will start from 1 Second 		based on when the listing API is called, so if it is called at say 10:01:10 AM than the 'delay' field for 		'reference_start_3' will be 25 Seconds, indicating actual delay for the Alert in seconds.
