package mySqlPackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import entitiesPackage.Comment;
import entitiesPackage.Notification;
import entitiesPackage.Post;
import exceptionPackage.IllegalOperation;

public class CommentDatabase extends MySqlConnection {

	public Comment addCommentToDB(Comment newComment) throws IllegalOperation {
		PreparedStatement ps = null;
		String query = null;
		Connection conn = null;
		try {
			NotificationDatabase NDB = new NotificationDatabase();
			PostDatabase PDB = new PostDatabase();
			conn = setConnection();
			query = "INSERT INTO comments"
					+ "(postId,userId,content,publishDate) VALUES"
					+ "(?,?,?,?)";
			ps = conn.prepareStatement(query);
			ps.setLong(1, newComment.getPostId());
			ps.setLong(2, newComment.getUserId());
			ps.setString(3, newComment.getContent());
			ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			ps.execute();
			newComment.setCommentId((new PostDatabase()).getNewestIdInTable(
					"comments", "commentId"));

			Notification newNotification = new Notification();
			newNotification.setNotificationTypeId(NDB
					.getNotificationTypeId("comment"));
			newNotification.setText("New comment: "+ newComment.getContent());
			newNotification.setDue(new Timestamp(System.currentTimeMillis()));
			newNotification.setNotificationForeignId(newComment.getCommentId());
			Post post = PDB.getPostByIdFromDB(newComment.getPostId());
			newNotification.setUserId(post.getUserId());
			newNotification.setTypeId(post.getPostId());
			newNotification.setNotificationAlertTime(newNotification.getDue());
			NDB.addNotification(newNotification);

			return newComment;

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

	public Comment deleteCommentFromDB(Long commentId, Long userId)
			throws IllegalOperation {

		Statement stmt = null;
		String query = null;
		Connection conn = null;
		try {

			Comment toDeleteComment = getCommentByIdFromDB(commentId);
		
			(new NotificationDatabase()).deleteCommentNotifications(commentId);
			
			
			
			conn = setConnection();
			query = "DELETE FROM comments WHERE commentId = " + commentId;
			stmt = conn.createStatement();
			stmt.executeUpdate(query);

			return toDeleteComment;

		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {
				stmt.close();
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

	public Comment getCommentByIdFromDB(Long commentId) throws IllegalOperation {

		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
		try {
			conn = setConnection();
			String query = "SELECT * FROM comments WHERE commentId = "
					+ commentId;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			if (rs.next())
				return getCommentFromResultSet(rs);
			
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
				stmt.close();
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

	public ArrayList<Comment> getCommentsByPostIdFromDB(Long postId, Long userId)
			throws IllegalOperation {

		ArrayList<Comment> commentsList = new ArrayList<Comment>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		Connection conn = null;
		
		try {
			conn = setConnection();
			query = "SELECT * FROM comments WHERE postId = " + postId
					+ " ORDER BY commentId";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery(query);
			
			while (rs.next())
				commentsList.add(getCommentFromResultSet(rs));
			
			return commentsList;
			
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

	public ArrayList<Comment> deleteCommentsByPostIdFromDB(Long postId,
			Long userId) throws IllegalOperation {

		try {
			ArrayList<Comment> commentsList = getCommentsByPostIdFromDB(postId,
					userId);

			if (commentsList != null)
				for (Comment curr : commentsList) {
					deleteCommentFromDB(curr.getCommentId(), userId);
				}
			return commentsList;
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		}
	}

	private Comment getCommentFromResultSet(ResultSet rs) {

		try {
			
			Comment found = new Comment();
			found.setCommentId(rs.getLong("commentId"));
			found.setPostId(rs.getLong("postId"));
			found.setUserId(rs.getLong("userId"));
			found.setContent(rs.getString("content"));
			found.setPublishDate(rs.getTimestamp("publishDate"));
			
			return found;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
