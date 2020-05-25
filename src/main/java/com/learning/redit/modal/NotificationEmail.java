package com.learning.redit.modal;


public class NotificationEmail {
	 private String subject;
	 private String recipient;
	 private String body;
	 
	 public NotificationEmail(String sub , String rec , String mess){
		 this.subject = sub;
		 this.recipient = rec;
		 this.body = mess;
	 }
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	 
	 
}
