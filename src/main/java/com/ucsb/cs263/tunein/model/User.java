package com.ucsb.cs263.tunein.model;

import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;


@XmlRootElement
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private String userId;
	private String firstName;
	private String lastName;
	private String displayName;
	private String emailId;
	
	public User(){
	}

	public User(String userId, String firstName, String lastName, String displayName, String emailId){
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.displayName  = displayName;
		this.emailId = emailId;
	}
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	@Override
    public boolean equals(Object o) {
 
        // If the object is compared with itself then return true  
        if (o == this) {
            return true;
        }
 
        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof User)) {
            return false;
        }
         
        // typecast o to Complex so that we can compare data members 
        User u = (User) o;
         
        // Compare the data members and return accordingly 
        return userId.equals(u.userId)
        		&& firstName.equals(u.firstName)
        		&& lastName.equals(u.lastName)
        		&& displayName.equals(u.displayName)
        		&& emailId.equals(u.emailId);
        		
    }
}