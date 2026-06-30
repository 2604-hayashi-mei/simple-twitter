package chapter6.beans;

import java.io.Serializable;
import java.util.Date;

public class Follow implements Serializable {

	private int id;
	private int followerId;
	private int followingId;
	private Date createdDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFollowerId() {
		return followerId;
	}

	public void setFollowerId(int followerId) {
		this.followerId = followerId;
	}

	public int getFollowingId() {
		return followingId;
	}

	public void setFollowingId(int followingId) {
		this.followingId = followingId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}