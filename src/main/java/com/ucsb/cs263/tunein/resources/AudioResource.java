package com.ucsb.cs263.tunein.resources;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ucsb.cs263.tunein.model.AudioClip;
import com.ucsb.cs263.tunein.model.AudioClipInstance;
import com.ucsb.cs263.tunein.model.User;
import com.ucsb.cs263.tunein.service.AudioClipService;
import com.ucsb.cs263.tunein.service.UserService;

@Path("/users/{userId}/audioClips")
public class AudioResource {
  @Context
  UriInfo uriInfo;
  @Context
  Request request;

  private AudioClipService audioClipService = new AudioClipService();
  private UserService userService = new UserService();
  private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  

  @GET
  @Path("/others")
  @Produces(MediaType.APPLICATION_JSON )
  public List<AudioClipInstance> getAllAudioClips(@PathParam("userId") String userId) throws IOException, BadRequestException{
	  List<AudioClipInstance> audioClipList = new ArrayList<AudioClipInstance>();
	  audioClipList = audioClipService.getOtherUsersAudioClips(userId);
    return audioClipList;
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON )
  public AudioClip getAudioClipById(@PathParam("id") String id) throws IOException{
	  AudioClip audioClip = null;
	  try{
		  audioClip  = audioClipService.getAudioClipById(id);
	  }catch(Exception e){
		  throw new BadRequestException();
	  }
      return audioClip;
  }
  
  @GET
  @Produces(MediaType.APPLICATION_JSON )
  public List<AudioClip> getAudioClipsByUser(@PathParam("userId") String userId) throws IOException, BadRequestException{
      return audioClipService.getAudioClipsByUser(userId);
    
  }

  @GET
  @Path("/audio")
  public Response getAudio(
    @QueryParam("blobkey") String blobkey,
    @Context HttpServletResponse response) throws IOException{
      BlobKey blob_key=new BlobKey(blobkey);
      blobstoreService.serve(blob_key,response);
      return Response.ok().build();
  }

  @GET
  @Path("/blob")
  @Produces(MediaType.TEXT_PLAIN )
  public String getBlobUploadURL(
    @PathParam("userId") String userId,
    @Context HttpServletResponse response) throws IOException{
	  //verify user exists
	  User user  = userService.getUserById(userId); //will throw bad request if user  doesnt exists
	  String callbackURL="/rest/users/"+userId+"/audioClips";
	  return blobstoreService.createUploadUrl(callbackURL);
  }

  @GET
  @Path("/image")
  public Response getImage(
    @QueryParam("blobkey") String blobkey,
    @Context HttpServletResponse response) throws IOException{
      BlobKey blob_key=new BlobKey(blobkey);
      blobstoreService.serve(blob_key,response);
      return Response.ok().build();
  }
  
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response newAudioClipFromForm(@Context HttpServletRequest request,
    @Context HttpServletResponse response) throws IOException, URISyntaxException{
      Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(request);
      BlobKey audio_blobKey = blobs.get("myAudio");
      BlobKey image_blobKey = blobs.get("myImage");
      String userId = request.getParameter("userId");
      String audioclip_key = audioClipService.newAudioClip(userId, request.getParameter("title"), 
    		  audio_blobKey.getKeyString(), image_blobKey.getKeyString());
      return Response.seeOther(new URI("/success.html?audioclip_key="+audioclip_key+"&userId="+userId)).build();
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public String newAudioClipFromJson(AudioClip audioClip, @PathParam("userId") String userId) throws IOException, URISyntaxException{
	  String newKey  = audioClipService.newAudioClip(userId, audioClip.getTitle(), 
			  audioClip.getAudioId(), audioClip.getImageId());
	  return newKey;
  }

  @DELETE
  @Path("/{id}")
  public void deleteAudioClip(@PathParam("id") String id) {
	  audioClipService.deleteAudioClip(id);
  }
  
  @DELETE
  @Path("/audio")
  public void deleteAudioBlob(
    @QueryParam("blobkey") String blobkey) throws IOException{
	  audioClipService.deleteBlob(blobkey); 
  }
  
  @DELETE
  @Path("/image")
  public void deleteImageBlob(
    @QueryParam("blobkey") String blobkey) throws IOException{
	  if(blobkey!=null){
		  audioClipService.deleteBlob(blobkey);
	  }
  }

  @PUT 
  public void updateAudioClip() {
	  /* TODO */
  }
  
  //memcache only endpoints
  @POST
  @Path("/memcache")
  @Consumes(MediaType.APPLICATION_JSON)
  public String newAudioClipInMemcache(@PathParam("userId") String ownerId, AudioClip audioClip) throws IOException, URISyntaxException{
	  String key = audioClipService.newAudioClipInMemcache(ownerId, audioClip);
	  return key;
  }
  
  @GET
  @Path("/memcache")
  @Produces(MediaType.APPLICATION_JSON )
  public List<AudioClip> getAudioClipInMemcache() throws IOException, BadRequestException{
      return audioClipService.getAudioClipInMemcache();
  }

} 