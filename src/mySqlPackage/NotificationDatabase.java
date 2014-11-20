package mySqlPackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import entitiesPackage.Notification;
import entitiesPackage.Post;
import exceptionPackage.IllegalOperation;
public class NotificationDatabase extends MySqlConnection {

	public void markNotificationAsRead(Long notificationId)throws IllegalOperation
	{
		PreparedStatement ps = null;
		String query = null;
		Connection conn=null;
		try {
			
			conn = setConnection();
			query = "UPDATE notifications SET isRead=true WHERE notificationId=?";
			ps = conn.prepareStatement(query);
			ps.setLong(1, notificationId);
			ps.execute();

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
	
	
	public ArrayList<Notification> getNotificationList(Long userId) throws IllegalOperation {

		ArrayList<Notification> notificationList = new ArrayList<Notification>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		Connection conn=null;
		Timestamp notificationAlertTime=new Timestamp(System.currentTimeMillis());
		try {
			conn = setConnection();
			
				query = "SELECT * FROM notifications  where userId=? and isRead = false AND notificationAlertTime <=?  ORDER BY notificationId DESC";			
			ps = conn.prepareStatement(query);
			ps.setLong(1, userId);
			ps.setTimestamp(2, notificationAlertTime);
			System.out.println(query);
			rs = ps.executeQuery();

			while (rs.next())
				notificationList.add(getNotificationtFromResultSet(rs));

			return notificationList;

		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {ps.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {rs.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
		}
	}
	
	
	public Notification addNotification(Notification newNotification)
			throws IllegalOperation {
		PreparedStatement ps = null;
		String query = null;
		Connection conn=null;
		try {
		
			conn = setConnection();
			query = "INSERT INTO notifications"
					+ "(notificationTypeId,userId,notificationForeignId,typeId,text,isRead,notificationAlertTime,due) VALUES"
					+ "(?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(query);
			ps.setLong(1, newNotification.getNotificationTypeId());
			ps.setLong(2, newNotification.getUserId());
			ps.setLong(3, newNotification.getNotificationForeignId());
			ps.setLong(4, newNotification.getTypeId());
			ps.setString(5, newNotification.getText());
			ps.setBoolean(6, false);
			if(newNotification.getNotificationAlertTime()==null)			
			ps.setTimestamp(7,Timestamp.valueOf("5000-10-27 16:28:45"));
			else
			ps.setTimestamp(7,newNotification.getNotificationAlertTime());
			ps.setTimestamp(8, newNotification.getDue());
			ps.execute();
			
			newNotification.setNotificationId((new PostDatabase()).getNewestIdInTable("notifications", "notificationId"));
			
			return newNotification;

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

	public Notification getNotificationtByIdFromDB(Long notificationId)
			throws IllegalOperation {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		Connection conn=null;
		try {
			conn = setConnection();
			query = "SELECT * FROM notifications WHERE notificationId = ?";
			ps = conn.prepareStatement(query);
			ps.setLong(1, notificationId);
			rs = ps.executeQuery();
			if (rs.next()) {
				return getNotificationtFromResultSet(rs);
			}
			
			query = "update notifications set isRead=? where notificationId= ?";
			ps = conn.prepareStatement(query);
			ps.setLong(1, 1);
			ps.setLong(2, notificationId);
			rs = ps.executeQuery();
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
	public Notification deleteNotificationFromDB(Long notitId, Long userId)
			throws IllegalOperation {
		Statement stmt = null;
		String query = null;
		Connection conn=null;
		try {
			// case 1 - this user has no permission to delete this Notification
			Notification toDeleteNoti = getNotificationtByIdFromDB(notitId);

			// deleting this comment
			conn = setConnection();
			query = "DELETE FROM notifications WHERE notificationId = " + notitId;
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			return toDeleteNoti;
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {stmt.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
		}
	}

	private Notification getNotificationtFromResultSet(ResultSet rs) {
		Notification found = new Notification();
		try {
			found.setNotificationId(rs.getLong("notificationId"));
			found.setNotificationTypeId(rs.getLong("notificationTypeId"));
			found.setNotificationAlertTime(rs.getTimestamp("notificationAlertTime"));
			found.setTypeId(rs.getLong("typeId"));
			found.setUserId(rs.getLong("userId"));
			found.setRead(rs.getBoolean("isRead"));
			found.setDue(rs.getTimestamp("due"));
			found.setText(rs.getString("text"));
			found.setNotificationForeignId(rs.getLong("notificationForeignId"));

			return found;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Notification getNewestNotificationFromDB() throws IllegalOperation {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long maxId = null;
		Connection conn=null;
		try {
			conn = setConnection();
			ps = conn
					.prepareStatement("SELECT MAX(notificationId ) FROM notifications");
			rs = ps.executeQuery();
			if (rs.next()) {
				maxId = rs.getLong(1);
			}
			return getNotificationtByIdFromDB(maxId);
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

	public void deleteCommentNotifications(Long commentId) throws IllegalOperation{
		
		PreparedStatement ps = null;
		String query = null;
		Connection conn=null;
		try {
		
			conn = setConnection();
			query = "DELETE FROM notifications WHERE notificationForeignId=? AND notificationTypeId=?";
			ps = conn.prepareStatement(query);
			ps.setLong(1, commentId);
			ps.setLong(2, (long)1);
			ps.execute();
			
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {ps.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
		}
		
		
	}


	public Long getNotificationTypeId(String notTypeName) throws IllegalOperation {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long notTypeId=null;
		Connection conn=null;
		try {
			conn = setConnection();
			ps = conn.prepareStatement("SELECT * FROM notificationTypes WHERE notificationTypeName=?");
			ps.setString(1, notTypeName);
			rs = ps.executeQuery();
			if (rs.next()) {
				notTypeId = rs.getLong("notificationTypeId");
			}
			return notTypeId; 
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

}
