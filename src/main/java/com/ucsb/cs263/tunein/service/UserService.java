package com.ucsb.cs263.tunein.service;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import javax.ws.rs.BadRequestException;

import com.ucsb.cs263.tunein.model.User;
import com.ucsb.cs263.tunein.utils.TuneInConstants;


public class UserService{
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	public String addNewUser(User user){
		
		Entity userEntity = new Entity(TuneInConstants.USER_TYPE, user.getUserId());
		userEntity.setProperty(TuneInConstants.USER_ID, user.getUserId());
		userEntity.setProperty(TuneInConstants.USER_FIRST_NAME, user.getFirstName());
		userEntity.setProperty(TuneInConstants.USER_LAST_NAME, user.getLastName());
		userEntity.setProperty(TuneInConstants.USER_DISPLAY_NAME, user.getDisplayName());
		userEntity.setProperty(TuneInConstants.USER_EMAIL_ID, user.getEmailId());
		datastore.put(userEntity);
		return KeyFactory.keyToString(userEntity.getKey());
	}
	
	public User getUserById(String userId) throws BadRequestException{
		User user= null;
		try{
			Key userKey = KeyFactory.stringToKey(userId);
			Filter userFilter = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, userKey);
		    Query q = new Query(TuneInConstants.USER_TYPE).setFilter(userFilter);
		    PreparedQuery pq = datastore.prepare(q);
		    
		    
		    for (Entity result : pq.asIterable()) {
		    	user = new User(KeyFactory.keyToString(result.getKey()),
		    			(String)result.getProperty(TuneInConstants.USER_FIRST_NAME), 
		    			(String)result.getProperty(TuneInConstants.USER_LAST_NAME), 
		    			(String)result.getProperty(TuneInConstants.USER_DISPLAY_NAME), 
		    			(String)result.getProperty(TuneInConstants.USER_EMAIL_ID));
		    	break;
		    }
		}catch(IllegalArgumentException e){
			throw new BadRequestException();
		}
	    return user;
	}
}