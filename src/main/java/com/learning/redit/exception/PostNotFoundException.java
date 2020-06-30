package com.learning.redit.exception;

public class PostNotFoundException extends RuntimeException{
	
	public PostNotFoundException (String message){
		 super(message);
	}
}
