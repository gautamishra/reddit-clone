package com.learning.redit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.learning.redit.exception.RedditException;
import com.learning.redit.modal.NotificationEmail;

@Service
public class MailService {
	
	@Autowired
	private  JavaMailSender  mailSender;
	
	@Autowired
	private  MailContentBuilder mailContentBuilder;
	
	@Async
	 public void sendMail(NotificationEmail notificationEmail) {
	        MimeMessagePreparator messagePreparator = mimeMessage -> {
	            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
	            messageHelper.setFrom("springreddit@email.com");
	            messageHelper.setTo(notificationEmail.getRecipient());
	            messageHelper.setSubject(notificationEmail.getSubject());
	            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
	        };
	        try {
	            mailSender.send(messagePreparator);
	          System.out.println("mail sent");
	        } catch (MailException e) {
	            throw new RedditException("Exception occurred when sending mail to " + notificationEmail.getRecipient());
	        }
	    }
	
}
