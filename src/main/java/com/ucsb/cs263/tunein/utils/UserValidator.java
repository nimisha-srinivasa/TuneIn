package com.ucsb.cs263.tunein.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ucsb.cs263.tunein.model.User;

public class UserValidator {
	
	User user;
	String error;
	private Pattern emailPattern;
	private Matcher emailMatcher;
	
 
	private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
 
	
	public UserValidator(User user){
		this.user = user;
		emailPattern = Pattern.compile(EMAIL_PATTERN);
	}
	
	public String validate(){
		//validate email
		String email = user.getEmailId();
		if (email == null || email.equals("")){
			error = "Email cannot be empty";
		}
		else{
			emailMatcher = emailPattern.matcher(email);
			if(!(email == null || email.equals("")) && !emailMatcher.matches()){
				error = "Invalid email address";
			}
		}
		return this.error;
	}

}