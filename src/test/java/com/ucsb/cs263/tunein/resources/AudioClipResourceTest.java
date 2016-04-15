package com.ucsb.cs263.tunein.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.glassfish.jersey.client.ClientConfig;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import java.net.URI;

import com.ucsb.cs263.tunein.model.AudioClip;
import com.ucsb.cs263.tunein.model.User;


public class AudioClipResourceTest {
	
	private static String BASE_URI="https://tune-in-1205.appspot.com/";
	private static WebTarget service;

	@BeforeClass
	public static void setUp(){
		ClientConfig config = new ClientConfig();
	    Client client = ClientBuilder.newClient(config);
	    service = client.target(getBaseURI());
	}

//    @Test
//    public void newAudioClipTest(){
//    	//create a new user first
//    	User user1=new User("1","Nimisha", "Srinivasa", "Nimisha Srinivasa", "nimisha_srinivasa@cs.ucsb.edu");
//		Response response = service.path("rest/users").request(
//				MediaType.APPLICATION_JSON).post(Entity.entity(user1,MediaType.APPLICATION_JSON),Response.class);
//		assertEquals(200, response.getStatus());
//		String user_key = response.readEntity(String.class);
//		
//    	AudioClip audioClip = new AudioClip("sampleTitle", user_key, "audioId1", "imageId1");
//    	response = service.path("rest/users/"+user_key+"/audioClips/").request(MediaType.TEXT_PLAIN).post(Entity.entity(audioClip,MediaType.APPLICATION_JSON),Response.class);
//		assertEquals(200, response.getStatus());
//    }
//    
//    @Test
//    public void getAudioClipByIdTest(){
//    	//create a new user first to make sure they exist
//    	User user1=new User("1","Nimisha", "Srinivasa", "Nimisha Srinivasa", "nimisha_srinivasa@cs.ucsb.edu");
//		Response response = service.path("rest/users").request(
//				MediaType.APPLICATION_JSON).post(Entity.entity(user1,MediaType.APPLICATION_JSON),Response.class);
//		assertEquals(200, response.getStatus());	
//		String user_key = response.readEntity(String.class);
//    	
//    	AudioClip audioClip = new AudioClip("sampleTitle", user_key, "audioId1", "imageId1");
//    	response = service.path("rest/users/"+user_key+"/audioClips/").request(MediaType.TEXT_PLAIN).post(Entity.entity(audioClip,MediaType.APPLICATION_JSON),Response.class);
//		assertEquals(200,response.getStatus()); //all ok
//		assertEquals(true,response.hasEntity());
//		String audio_key = response.readEntity(String.class);
//		
//		//test get method
//		response=service.path("rest/users/1/audioClips/"+audio_key).request(MediaType.APPLICATION_JSON).get(Response.class);
//		assertEquals(200, response.getStatus());
//		assertNotNull(response.getEntity()); 
//    }
//    
//    @Test
//    public void getAudioClipByIdTest_badRequest(){
//    	Response response;
//		String audio_key = "INCORRECT_KEY";
//		//test get method
//		response=service.path("rest/users/1/audioClips/"+audio_key).request(MediaType.APPLICATION_JSON).get(Response.class);
//		assertEquals(400, response.getStatus());
//    }
//    
//    //tests for memcache
//    @Test
//    public void newAudioClipInMemcache(){
//    	//create a new user first
//    	User user1=new User("1","Nimisha", "Srinivasa", "Nimisha Srinivasa", "nimisha_srinivasa@cs.ucsb.edu");
//		Response response = service.path("rest/users").request(
//				MediaType.APPLICATION_JSON).post(Entity.entity(user1,MediaType.APPLICATION_JSON),Response.class);
//		assertEquals(200, response.getStatus());
//		String user_key = response.readEntity(String.class);
//		
//    	AudioClip audioClip = new AudioClip("sampleTitle", user_key, "audioId1", "imageId1");
//    	response = service.path("rest/users/"+user_key+"/audioClips/memcache").request(MediaType.TEXT_PLAIN).post(Entity.entity(audioClip,MediaType.APPLICATION_JSON),Response.class);
//		assertEquals(204, response.getStatus());
//    }
//    
//    @Test
//    public void getAudioClipsInMemcache(){
//    	//create a new user first
//    	User user1=new User("1","Nimisha", "Srinivasa", "Nimisha Srinivasa", "nimisha_srinivasa@cs.ucsb.edu");
//		Response response = service.path("rest/users").request(
//				MediaType.APPLICATION_JSON).post(Entity.entity(user1,MediaType.APPLICATION_JSON),Response.class);
//		assertEquals(200, response.getStatus());
//		String user_key = response.readEntity(String.class);
//		
//    	AudioClip audioClip = new AudioClip("sampleTitle", user_key, "audioId1", "imageId1");
//    	response = service.path("rest/users/"+user_key+"/audioClips/memcache").request(MediaType.TEXT_PLAIN).post(Entity.entity(audioClip,MediaType.APPLICATION_JSON),Response.class);
//		assertEquals(204, response.getStatus());
//		
//		//test get method
//		response=service.path("rest/users/"+user_key+"/audioClips/memcache").request(MediaType.APPLICATION_JSON).get(Response.class);
//		assertEquals(200, response.getStatus());
//		
//    }
//    
//    //test task queue
//    @Test
//    public void deleteAudioClip(){
//    	User user1=new User("1","Nimisha", "Srinivasa", "Nimisha Srinivasa", "nimisha_srinivasa@cs.ucsb.edu");
//		Response response = service.path("rest/users").request(
//				MediaType.APPLICATION_JSON).post(Entity.entity(user1,MediaType.APPLICATION_JSON),Response.class);
//		assertEquals(200, response.getStatus());
//		String user_key = response.readEntity(String.class);
//		
//    	AudioClip audioClip = new AudioClip("sampleTitle", user_key, "audioId1", "imageId1");
//    	response = service.path("rest/users/"+user_key+"/audioClips/").request(MediaType.TEXT_PLAIN).post(Entity.entity(audioClip,MediaType.APPLICATION_JSON),Response.class);
//		assertEquals(200, response.getStatus());
//		String audio_key = response.readEntity(String.class);
//		
//		response=service.path("rest/users/1/audioClips/"+audio_key).request(MediaType.APPLICATION_JSON).delete(Response.class);
//		assertEquals(204, response.getStatus());
//		
//    }
//    
//    @Test
//    public void deleteAudioClip_badRequest(){
//    	String audio_key="NON_EXISTANT_KEY";
//    	User user1=new User("1","Nimisha", "Srinivasa", "Nimisha Srinivasa", "nimisha_srinivasa@cs.ucsb.edu");
//		Response response = service.path("rest/users").request(
//				MediaType.APPLICATION_JSON).post(Entity.entity(user1,MediaType.APPLICATION_JSON),Response.class);
//		assertEquals(200, response.getStatus());
//		String user_key = response.readEntity(String.class);
//		
//    	response=service.path("rest/users/"+user_key+"/audioClips/"+audio_key).request(MediaType.APPLICATION_JSON).delete(Response.class);
//    	assertEquals(400, response.getStatus());
//    }
    
     
    public static URI getBaseURI(){
    	return UriBuilder.fromUri(BASE_URI).build();
    }
   

}