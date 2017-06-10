package com.workshop.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_feedback")
public class UserFeedback {

	public enum ActivityType {
		BLOCK_DOMAIN(0),
		THUMBS_UP(1),
		THUMBS_DOWN(2);

		private final int value;

		ActivityType (int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	};

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER"))
	private User user;

	@Column(name = "timestamp")
	private Timestamp timestamp;

	@Column(name = "activity_type")
	private String activityType;

	@Column(name = "url")
	private String url;


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public ActivityType getActivityType() {
		return ActivityType.valueOf(activityType);
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType.name();
	}

}
