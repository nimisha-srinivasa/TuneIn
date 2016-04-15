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

import com.ucsb.cs263.tunein.model.User;

public class UserResourceTest {
	private static String BASE_URI="https://tune-in-1205.appspot.com/";
	private static WebTarget service;

	@BeforeClass
	public static void setUp(){
		ClientConfig config = new ClientConfig();
	    Client client = ClientBuilder.newClient(config);
	    service = client.target(getBaseURI());
	}
	
	public static URI getBaseURI(){
    	return UriBuilder.fromUri(BASE_URI).build();
    }
	
//	@Test
//	public void newUserTest(){
//		User user1=new User("1","Nimisha", "Srinivasa", "Nimisha Srinivasa", "nimisha_srinivasa@cs.ucsb.edu");
//		Response response = service.path("rest/users").request(
//				MediaType.APPLICATION_JSON).post(Entity.entity(user1,MediaType.APPLICATION_JSON),Response.class);
//		assertEquals(200, response.getStatus());
//	}
//	
//	@Test
//	public void newUserTest_badRequest(){
//		User user1=new User("2","Nimisha", "Srinivasa", "Nimisha Srinivasa", "nimisha_srinivasa");
//		Response response = service.path("rest/users").request(
//				MediaType.APPLICATION_JSON).post(Entity.entity(user1,MediaType.APPLICATION_JSON),Response.class);
//		assertEquals(400, response.getStatus());
//	}
//	
//	@Test
//	public void getUserByIdTest(){
//		//POST first and then get!
//		User user1=new User("1","Nimisha", "Srinivasa", "Nimisha Srinivasa", "nimisha_srinivasa@cs.ucsb.edu");
//		Response response = service.path("rest/users").request(
//				MediaType.APPLICATION_JSON).post(Entity.entity(user1,MediaType.APPLICATION_JSON),Response.class);
//		String userKey = response.readEntity(String.class);
//		user1.setUserId(userKey);
//		response=service.path("rest/users/"+userKey).request(MediaType.APPLICATION_JSON).get(Response.class);
//		assertEquals(200,response.getStatus());
//		assertNotNull(response.getEntity());
//		assertEquals(response.readEntity(User.class),user1);
//	}
//	
//	@Test
//	public void getUserByIdTest_badRequest(){
//		//POST first and then get!
//		String userId="NON_EXISTANT_USER_KEY";
//		
//		 Response response=service.path("rest/users/"+userId).request(MediaType.APPLICATION_JSON).get(Response.class);
//		assertEquals(400,response.getStatus());
//	}

}
