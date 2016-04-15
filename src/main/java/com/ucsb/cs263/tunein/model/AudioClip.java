package com.ucsb.cs263.tunein.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Date;

import com.ucsb.cs263.tunein.utils.JsonDateSerializer;

// JAX-RS supports an automatic mapping from JAXB annotated class to XML and JSON
@XmlRootElement
public class AudioClip implements Serializable{

	  private static final long serialVersionUID = 1L;
	  private String keyname;	
	  private String title;
	  private String ownerId;	//actually refers to a Key
	  private String audioId; 	//actually refers to a BlobKey
	  private String imageId;		//actually refers to a BlobKey
	  private Date date;

	  public AudioClip(){

	  }

	  public AudioClip(String keyname, String title, String ownerId, String audioId, String imageId, Date date){
	  	this.keyname = keyname;
	  	this.title = title;
	  	this.ownerId = ownerId;
	  	this.audioId = audioId;
	  	this.imageId = imageId;
	  	this.date = date;
	  }

	  public AudioClip(String title, String ownerId, String audioId, String imageId, Date date){
	  	this.title = title;
	  	this.ownerId = ownerId;
	  	this.audioId = audioId;
	  	this.imageId = imageId;
	  	this.date = date;
	  }
	  
	  public AudioClip(String title, String ownerId, String audioId, String imageId){
		  	this.title = title;
		  	this.ownerId = ownerId;
		  	this.audioId = audioId;
		  	this.imageId = imageId;
		  }

		public String getKeyname() {
			return keyname;
		}
		
		public void setKeyname(String keyname) {
			this.keyname = keyname;
		}

		public String getTitle() {
			return title;
		}
		
		public void setTitle(String title) {
			this.title = title;
		}
		
		public String getOwnerId() {
			return ownerId;
		}
		
		public void setOwnerId(String ownerId) {
			this.ownerId = ownerId;
		}
		
		public String getAudioId() {
			return audioId;
		}
		
		public void setAudioId(String audioId) {
			this.audioId = audioId;
		}
		
		public String getImageId() {
			return imageId;
		}
		
		public void setImageId(String imageId) {
			this.imageId = imageId;
		}
		
		@JsonSerialize(using=JsonDateSerializer.class)
		public Date getDate() {
			return date;
		}
		
		public void setDate(Date date) {
			this.date = date;
		}
		
		@Override
	    public boolean equals(Object o) {
	 
	        // If the object is compared with itself then return true  
	        if (o == this) {
	            return true;
	        }
	 
	        /* Check if o is an instance of Complex or not
	          "null instanceof [type]" also returns false */
	        if (!(o instanceof AudioClip)) {
	            return false;
	        }
	         
	        // typecast o to Complex so that we can compare data members 
	        AudioClip u = (AudioClip) o;
	         
	        // Compare the data members and return accordingly 
	        return keyname.equals(u.keyname)
	        		&& title.equals(u.title)
	        		&& ownerId.equals(u.ownerId)
	        		&& audioId.equals(u.audioId)
	        		&& imageId.equals(u.imageId)
	        		&& date.equals(u.date);
	        		
	    }
  
} 