package chapter6.beans;

import java.io.Serializable;
import java.util.Date;

public class Like implements Serializable {
	
	private int id;
    private int userId;
    private int messageId;
    private Date createdDate;
    
    public int getId() {
    	return id;
    }
    
    public void setId(int id) {
    	this.id = id;
    }
    
    public int getUserId() {
    	return userId;
    }
    
    public void setUserId(int userId) {
    	this.userId = userId;
    }
    
    public int getMessageId() {
		return messageId;
	}
    
    public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
    
    public Date getCreatedDate() {
    	return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
    	this.createdDate = createdDate;
    }
}
