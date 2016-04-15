package com.ucsb.cs263.tunein.resources;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import java.io.IOException;
import java.net.URISyntaxException;

import com.ucsb.cs263.tunein.model.User;
import com.ucsb.cs263.tunein.service.UserService;
import com.ucsb.cs263.tunein.utils.UserValidator;

@Path("/users")
public class UserResource {
  @Context
  UriInfo uriInfo;
  @Context
  Request request;

  private UserService userService = new UserService();

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public String newUser(User user) throws IOException, URISyntaxException{
	 UserValidator userValidator = new UserValidator(user);
	 String error = userValidator.validate();
	 if (error!=null && !error.isEmpty()){
		 throw new BadRequestException();
	}
	String key = userService.addNewUser(user);
    return key;
  }

  @GET
  @Path("/{userId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUserById(@PathParam("userId") String userId)throws IOException{
	  User user = null;
	  user = userService.getUserById(userId);
	  if(user==null){
		  return Response.status(Status.NOT_FOUND).build();
	  }
	  return Response.ok(user).build();
  }

}