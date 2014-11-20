package entitiesPackage;

public class SingleNotificationEntity implements Entity {
	long notificationTypeId;
	Long entityId;
	Long notificationId;
	
	public Long getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}
	public long getNotificationTypeId() {
		return notificationTypeId;
	}
	public void setNotificationTypeId(long notificationTypeId) {
		this.notificationTypeId = notificationTypeId;
	}
	public Long getEntityId() {
		return entityId;
	}
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	
}
