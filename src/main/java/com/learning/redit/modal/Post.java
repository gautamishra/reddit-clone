package com.learning.redit.modal;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import com.sun.istack.internal.Nullable;

@Entity
public class Post {

		
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long postId;
	
	@NotBlank(message = "name Can not be blank")
	private String name;
	
	@Nullable
    private String url;
    @Nullable
    @Lob
    private String description;
    private Integer voteCount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
    
    private Instant createdDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Subreddit subreddit;
    
}
