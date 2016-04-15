package com.ucsb.cs263.tunein.utils;

public class TuneInConstants {
	
	//constants related to User
	public static final String USER_TYPE = "User";
	public static final String USER_ID = "userId";
	public static final String USER_FIRST_NAME = "firstName";
	public static final String USER_LAST_NAME = "lastName";
	public static final String USER_DISPLAY_NAME = "displayName";
	public static final String USER_EMAIL_ID = "emailId";
	
	//constants related to AudioClip
	public static final String AUDIO_CLIP_TYPE = "AudioClip";
	public static final String AUDIO_CLIP_OWNER_ID = "ownerId"; 
	public static final String AUDIO_CLIP_TITLE = "title"; 
	public static final String AUDIO_CLIP_AUDIO_ID = "audioId";
	public static final String AUDIO_CLIP_IMAGE_ID = "imageId"; 
	public static final String AUDIO_CLIP_DATE = "date"; 
	
	//memcache related keys
	public static final String ALL_AUDIO_CLIPS = "ALL_AUDIOCLIPS";
	public static final String OTHERS_WORK_KEY = "OTHERS_";
}
