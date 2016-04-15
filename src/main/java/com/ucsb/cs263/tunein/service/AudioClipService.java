package com.ucsb.cs263.tunein.service;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import javax.ws.rs.BadRequestException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ucsb.cs263.tunein.model.AudioClip;
import com.ucsb.cs263.tunein.model.AudioClipInstance;
import com.ucsb.cs263.tunein.model.User;
import com.ucsb.cs263.tunein.utils.TuneInConstants;

public class AudioClipService{
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
  	UserService userService = new UserService();

  	public List<AudioClipInstance> getOtherUsersAudioClips(String userId) throws BadRequestException{
  		List<AudioClipInstance> audioClipInstanceList;
  		
  		//no need to check for user existance here! if UserId is null, then all audioClips are returned
		Filter userFilter = new FilterPredicate(TuneInConstants.AUDIO_CLIP_OWNER_ID, FilterOperator.NOT_EQUAL, userId);
	    Query q = new Query(TuneInConstants.AUDIO_CLIP_TYPE).setFilter(userFilter);
	    PreparedQuery pq = datastore.prepare(q);
	    
	 // check mem-cache for the results first
	  	String CACHE_KEY = TuneInConstants.OTHERS_WORK_KEY+userId;
	  	audioClipInstanceList =  (ArrayList<AudioClipInstance>) syncCache.get(CACHE_KEY);
  	    
  	    if(audioClipInstanceList==null){
  	    	audioClipInstanceList = new ArrayList<AudioClipInstance>();
  	    	User user;
  	  	    AudioClipInstance audioClipInstance;
  	  	    
  	    	for (Entity result : pq.asIterable()) {
  	  	    	user = userService.getUserById((String)result.getProperty(TuneInConstants.AUDIO_CLIP_OWNER_ID));
  	  	    	audioClipInstance = new AudioClipInstance(KeyFactory.keyToString(result.getKey()),
  	  	    						(String)result.getProperty(TuneInConstants.AUDIO_CLIP_TITLE), 
  	  	    						user, 
  	  	    						(String)result.getProperty(TuneInConstants.AUDIO_CLIP_AUDIO_ID), 
  	  	    						(String)result.getProperty(TuneInConstants.AUDIO_CLIP_IMAGE_ID), 
  	  	    						(Date)result.getProperty(TuneInConstants.AUDIO_CLIP_DATE));
  	  	    	audioClipInstanceList.add(audioClipInstance); 
  	  	    }
  	    	syncCache.put(CACHE_KEY, audioClipInstanceList, Expiration.byDeltaSeconds(300));
  	    }
  	    
	    return audioClipInstanceList;
	}

	public AudioClip getAudioClipById(String id) throws BadRequestException{
		AudioClip audioClip;
		try{
	      Key audio_key=KeyFactory.stringToKey(id);
	      Entity result = datastore.get(audio_key);
	      audioClip = new AudioClip( id,
	    		  		(String)result.getProperty(TuneInConstants.AUDIO_CLIP_TITLE), 
	    		  		(String)result.getProperty(TuneInConstants.AUDIO_CLIP_OWNER_ID), 
	    		  		(String)result.getProperty(TuneInConstants.AUDIO_CLIP_AUDIO_ID), 
	    		  		(String)result.getProperty(TuneInConstants.AUDIO_CLIP_IMAGE_ID), 
	    		  		(Date)result.getProperty(TuneInConstants.AUDIO_CLIP_DATE));
	      return audioClip;
	    }
	    catch(Exception e){
	    	throw new BadRequestException();
	    }
	}

	public List<AudioClip> getAudioClipsByUser(String userId) throws BadRequestException{
		List<AudioClip> audioClipList=new ArrayList<AudioClip>();
		
		//verify userId
		validateUserExistance(userId); //throws bad request exception of user doesn't exist
		Filter userFilter = new FilterPredicate(TuneInConstants.AUDIO_CLIP_OWNER_ID, FilterOperator.EQUAL, userId);
	    Query q = new Query(TuneInConstants.AUDIO_CLIP_TYPE).setFilter(userFilter).addSort(TuneInConstants.AUDIO_CLIP_DATE, 
	    			SortDirection.ASCENDING);
	    PreparedQuery pq = datastore.prepare(q);
		AudioClip audioClip;
	    for (Entity result : pq.asIterable()) {
	      audioClip = new AudioClip(KeyFactory.keyToString(result.getKey()),
  	  		    		(String)result.getProperty(TuneInConstants.AUDIO_CLIP_TITLE), 
	    		  		(String)result.getProperty(TuneInConstants.AUDIO_CLIP_OWNER_ID), 
	    		  		(String)result.getProperty(TuneInConstants.AUDIO_CLIP_AUDIO_ID), 
	    		  		(String)result.getProperty(TuneInConstants.AUDIO_CLIP_IMAGE_ID), 
	    		  		(Date)result.getProperty(TuneInConstants.AUDIO_CLIP_DATE));
	      audioClipList.add(audioClip);
	    }
	    return audioClipList;
	}

	public String newAudioClip(String userId, String title, String audio, String image)throws BadRequestException{
		//validate existence of the user
		validateUserExistance(userId);	//throws bad request exception of user doesn't exist
  		
		Entity audioClip = new Entity(TuneInConstants.AUDIO_CLIP_TYPE);
	    audioClip.setProperty(TuneInConstants.AUDIO_CLIP_TITLE,title);
	    audioClip.setProperty(TuneInConstants.AUDIO_CLIP_AUDIO_ID, audio);
	    audioClip.setProperty(TuneInConstants.AUDIO_CLIP_IMAGE_ID, image);
	    audioClip.setProperty(TuneInConstants.AUDIO_CLIP_OWNER_ID, userId);
	    audioClip.setProperty(TuneInConstants.AUDIO_CLIP_DATE,new Date());
	    datastore.put(audioClip);
	    return KeyFactory.keyToString(audioClip.getKey());
	}
	
	public void deleteAudioClip(String audioClipId)throws BadRequestException{
		AudioClip audioClip = getAudioClipById(audioClipId);
		datastore.delete(KeyFactory.stringToKey(audioClipId));
		
		//task queue to delete the associated audio and image blobs
        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(TaskOptions.Builder.withUrl("/rest/users/"+audioClip.getOwnerId()+"/audioClips/audio").method(TaskOptions.Method.DELETE).param("blobkey", audioClip.getAudioId()));
        queue.add(TaskOptions.Builder.withUrl("/rest/users/"+audioClip.getOwnerId()+"/audioClips/image").method(TaskOptions.Method.DELETE).param("blobkey", audioClip.getImageId()));
	}
	
	public void deleteBlob(String blobkey){
		BlobKey blob_key=new BlobKey(blobkey);
	    blobstoreService.delete(blob_key);  
	}
	
	//memcache only endpoints
	public List<AudioClip> getAudioClipInMemcache() throws BadRequestException{
		ArrayList<AudioClip> audioClipList = (ArrayList<AudioClip>)syncCache.get(TuneInConstants.ALL_AUDIO_CLIPS);
		return audioClipList;
	}
	
	public String newAudioClipInMemcache(String ownerId, AudioClip audioClip)throws BadRequestException{
		validateUserExistance(ownerId);
		ArrayList<AudioClip> audioCLipList = (ArrayList<AudioClip>)syncCache.get(TuneInConstants.ALL_AUDIO_CLIPS);
		if(audioCLipList==null){
			audioCLipList = new ArrayList<AudioClip>();
		}
		String key  = newAudioClip(ownerId, audioClip.getTitle(), audioClip.getAudioId(),audioClip.getImageId());
		audioClip.setKeyname(key);
		audioCLipList.add(audioClip);
		syncCache.put(TuneInConstants.ALL_AUDIO_CLIPS, audioCLipList, Expiration.byDeltaSeconds(300));
	    return key;
	}
	
	private void validateUserExistance(String userId)throws BadRequestException{
		//validate existence of the user
		userService.getUserById(userId);	//throws bad request exception of user doesn't exist
	}
}