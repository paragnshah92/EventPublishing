package com.alerts.src.mongo.repository.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.alerts.src.mongo.model.EventModel;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.WriteResult;

@Component
public class EventRepositoryImpl {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public int updateStatusByReferneceId(String status,String referneceId) {
	
		Query query = new Query(Criteria.where("referneceId").is(referneceId));
        Update update = new Update();
        update.set("status", status);
        
        WriteResult result = mongoTemplate.updateFirst(query, update, EventModel.class);
        if(result!=null)
            return result.getN();
        else
            return 0;
		
	}
	
	public int updateStatusByRefernece(Map<String,Map<String,Object>> map){
		try {
			BasicDBObject obj = new BasicDBObject();
			for (String operator : map.keySet()) {			
		        Map<String,Object> keyValueMap = map.get(operator);	
		        BasicDBList list = new BasicDBList();
		        for (String key : keyValueMap.keySet()) {
		        		final BasicDBObject subQuery = obj.get(operator) != null ?
			                (BasicDBObject) obj.get(operator) : new BasicDBObject();
		        	 	subQuery.append(key, keyValueMap.get(key));		        	 	
		        	 	list.add(subQuery);
		        }
		        if (keyValueMap.size() > 1) {
		        		obj.put(operator,list);
    	 			} else {
    	 				obj.put(operator,list.get(0));
    	 			}		        
			}
			DBCollection collection = mongoTemplate.getDb().getCollection("event");
			DBCursor cursor = collection.find(obj).skip(10).limit(10);
			return cursor.count();
		} catch(Exception e) {
			return 0;
		}
	}
	
}
