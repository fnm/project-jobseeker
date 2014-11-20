package entitiesPackage;

import java.sql.Timestamp;

public class Notification implements Entity {

	private Long notificationId;
	private Long notificationTypeId;
	private Long notificationForeignId;
	private Long userId;
	private Long typeId;
	private boolean isRead;
	private String text;
	private Timestamp due;
	private Timestamp notificationAlertTime;

	public Timestamp getNotificationAlertTime() {
		return notificationAlertTime;
	}

	public void setNotificationAlertTime(Timestamp notificationAlertTime) {
		this.notificationAlertTime = notificationAlertTime;
	}

	public Long getNotificationForeignId() {
		return notificationForeignId;
	}

	public void setNotificationForeignId(Long notificationForeignId) {
		this.notificationForeignId = notificationForeignId;
	}

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public Long getNotificationTypeId() {
		return notificationTypeId;
	}

	public void setNotificationTypeId(Long notificationTypeId) {
		this.notificationTypeId = notificationTypeId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public Timestamp getDue() {
		return due;
	}

	public void setDue(Timestamp due) {
		this.due = due;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

}
