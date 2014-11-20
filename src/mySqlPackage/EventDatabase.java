package mySqlPackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import exceptionPackage.IllegalOperation;
import entitiesPackage.Event;
import entitiesPackage.Notification;

public class EventDatabase extends MySqlConnection {
	public Event addEventToDB(Event newEvent,Timestamp notificationAlertTime) throws IllegalOperation {
		PreparedStatement ps = null;
		String query = null;
		Connection conn=null;
		
		try {
			NotificationDatabase NDB=new NotificationDatabase();
			
			conn = setConnection();
			query = "INSERT INTO events"
					+ "(userId,jobId,title,address,description,notes,due,publishDate) VALUES"
					+ "(?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(query);
			ps.setLong(1, newEvent.getUserId());
			ps.setLong(2, newEvent.getJobId());
			ps.setString(3, newEvent.getTitle());
			ps.setString(4, newEvent.getAddress());
			ps.setString(5, newEvent.getDescription());
			ps.setString(6, newEvent.getNotes());
			ps.setTimestamp(7, newEvent.getDue());
			ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
			ps.execute();
	
			newEvent.setEventId((new PostDatabase()).getNewestIdInTable("events", "eventId"));
			
			if (notificationAlertTime!=null){
			Notification newNotification=new Notification();
			newNotification.setNotificationTypeId(NDB.getNotificationTypeId("event"));
			newNotification.setText("Reminder: "+ newEvent.getTitle());
			newNotification.setDue(new Timestamp(System.currentTimeMillis()));	
			newNotification.setNotificationForeignId(newEvent.getEventId());
			newNotification.setUserId(newEvent.getUserId());
			newNotification.setTypeId(newEvent.getJobId());
			newNotification.setNotificationAlertTime(notificationAlertTime);
			NDB.addNotification(newNotification);
			}
			return newEvent;

		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
		

	}

	public Event getEventByIdFromDB(Long eventId) throws IllegalOperation {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		Connection conn=null;
		try {
			conn = setConnection();
			query = "SELECT * FROM events WHERE eventId = ?";
			ps = conn.prepareStatement(query);
			ps.setLong(1, eventId);
			rs = ps.executeQuery();
			if (rs.next()) {
				return getEventFromResultSet(rs);
			}
			return null;

		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				throw new IllegalOperation(e.getStackTrace());
			}
			try {
				ps.close();
			} catch (Exception e) {
				throw new IllegalOperation(e.getStackTrace());
			}
			try {
				conn.close();
			} catch (Exception e) {
				throw new IllegalOperation(e.getStackTrace());
			}
		}
	}

	
	/*public Event updateEventFromDB(Long eventId, String newNotes) throws IllegalOperation {
		PreparedStatement ps = null;
		String query = null;
		Connection conn=null;
		try {
			conn = setConnection();
			query = "update events"
					+ " set  notes = ? "
					+ "where eventId= ? ";

			ps = conn.prepareStatement(query);
			ps.setString(1, newNotes);
			ps.setLong(2, eventId);
			ps.execute();

			return getEventByIdFromDB(eventId);

		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
				throw new IllegalOperation(e.getStackTrace());
			}
			try {
				conn.close();
			} catch (Exception e) {
				throw new IllegalOperation(e.getStackTrace());
			}
		}
		
	}*/
	public Event deleteEventFromDB(Long eventId, Long userId) throws IllegalOperation {
		Statement stmt = null;
		String query = null;
		Connection conn=null;
		try {
			// case 1 - this user has no permission to delete this event
			Event toDeleteEvent = getEventByIdFromDB(eventId);
			if (userId != toDeleteEvent.getUserId())
				throw new IllegalOperation(
						"this user has no permission to delete this event");

			// deleting this event
			conn = setConnection();
			query = "DELETE FROM events WHERE eventId = " + eventId;
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			return toDeleteEvent;
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {stmt.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
		}
	}
	public ArrayList<Event> getListOfEventsByJobId(Long jobId, Long userId) throws IllegalOperation {
		ArrayList<Event> eventsList = new ArrayList<Event>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		Connection conn=null;
		try {
			conn = setConnection();
			query = "SELECT * FROM events WHERE jobId = " + jobId + " AND userId = " + userId
					+ " ORDER BY eventId";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			while (rs.next())	
		{
			eventsList.add(getEventFromResultSet(rs));	
		}
		return eventsList;
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {rs.close();} catch (Exception e) { throw new IllegalOperation(e.getStackTrace());}
			try {ps.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
		}
	}

	private Event getEventFromResultSet(ResultSet rs) {
		Event found = new Event();
		try {
			found.setEventId(rs.getLong("eventId"));
			found.setUserId(rs.getLong("userId"));
			found.setJobId(rs.getLong("jobId"));
			found.setTitle(rs.getString("title"));	
			found.setAddress(rs.getString("address"));
			found.setDescription(rs.getString("description"));
			found.setNotes(rs.getString("notes"));
			found.setDue(rs.getTimestamp("due"));
			found.setPublishDate(rs.getTimestamp("publishDate"));
			return found;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("hhh");
			return null;
		}
	}

	private Event getNewestEventFromDB() throws IllegalOperation {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long maxId = null;
		Connection conn=null;
		try {
			conn = setConnection();
			ps = conn.prepareStatement("SELECT MAX(eventId) FROM events");
			rs = ps.executeQuery();
			if (rs.next()) {
				maxId = rs.getLong(1);
			}
			return getEventByIdFromDB(maxId);
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				throw new IllegalOperation(e.getStackTrace());
			}
			try {
				ps.close();
			} catch (Exception e) {
				throw new IllegalOperation(e.getStackTrace());
			}
			try {
				conn.close();
			} catch (Exception e) {
				throw new IllegalOperation(e.getStackTrace());
			}
		}
	}

	public Event updateEventFromDB(Event event) throws IllegalOperation {

		PreparedStatement ps = null;
		String query = null;
		Connection conn=null;
		try {
			
			
			conn = setConnection();
			query = "update events set title=?,description=?,notes=?,address=?,due=? where eventId= ?";
			
			ps = conn.prepareStatement(query);
			//ps.setLong(1, event.getUserId());
			//ps.setLong(2, event.getJobId());
			ps.setString(1, event.getTitle());
			ps.setString(2, event.getDescription());
			ps.setString(3, event.getNotes());
			ps.setString(4, event.getAddress());
			ps.setTimestamp(5, event.getDue());
			ps.setLong(6, event.getEventId());
			ps.execute();

			return getEventByIdFromDB(event.getEventId());

		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
				throw new IllegalOperation(e.getStackTrace());
			}
			try {
				conn.close();
			} catch (Exception e) {
				throw new IllegalOperation(e.getStackTrace());
			}
		}
	}
	
	


}
