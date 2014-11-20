package entitiesPackage;

import java.sql.Timestamp;

public class AddEventEntity implements Entity{

	private Timestamp notificationAlertTime;
	private Event event;
	
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Timestamp getNotificationAlertTime() {
		return notificationAlertTime;
	}

	public void setNotificationAlertTime(Timestamp notificationAlertTime) {
		this.notificationAlertTime = notificationAlertTime;
	}

}
