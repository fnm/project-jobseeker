package mySqlPackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import entitiesPackage.User;
import entitiesPackage.AllowedUser;
import entitiesPackage.UserTypes;
import entitiesPackage.ValidateUserEntity;
import exceptionPackage.IllegalOperation;

public class UserDatabase extends MySqlConnection {
	
	public User addAllowedUserToDB(User newUser) throws IllegalOperation {
	
		PreparedStatement ps = null;
		String query = null;
		Connection conn=null;
		try {

			if(getAllowedUserByIdFromDB(newUser.getEmail()) != null)
				throw new IllegalOperation("User exists already"); 

			conn = setConnection();
			 query = "INSERT INTO allowedUsers"
					+ " (email,userTypeId) VALUES" + " (?,?)";
			ps = conn.prepareStatement(query);
			System.out.println(query);
			ps.setString(1, newUser.getEmail());
			ps.setLong(2, newUser.getUserTypeId());
			ps.execute();
			
			return newUser;

		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace()); 
		}finally {
			try {ps.close();} catch (Exception e) {}
			try {conn.close();} catch (Exception e) {}
		}
	}
	/*
	 * return the active user with this id
	 * null if not found
	 */
	public User getUserByIdFromDB(Long userId) throws IllegalOperation {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn=null;
		try {
			conn = setConnection();
			String query = "SELECT * FROM activeUsers WHERE userId =?";
			ps=conn.prepareStatement(query);
			ps.setLong(1, userId);
			rs=ps.executeQuery();
			if (rs.next())
				return getUserFromResultSet(rs); 
			return null;
				
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace()); 
		}finally {
			try {rs.close();} catch (Exception e) {}
			try {ps.close();} catch (Exception e) {}
			try {conn.close();} catch (Exception e) {}
		}
	}
	
	/*
	 * return the user or an exception with a message
	 */
	public ValidateUserEntity validateUser(User user) throws IllegalOperation{
	
		ValidateUserEntity validateEntity=new ValidateUserEntity();
		
		User found=null;
		try {
			AllowedUser allowedUser = getAllowedUserByIdFromDB(user.getEmail());
			// case 1 - the user is not in the allowedUser table
			if(allowedUser == null)
					validateEntity.setStatus("UnauthorizedUser");	
			else	
				{
				// case 2 - the user is in the active users table
					found = getUserByEmailFromDB(user.getEmail());
					
					
					if (found == null) {
						// case 3 - the user is not in the active users table
						user.setStatus(true);
						user.setUserTypeId(allowedUser.getUserTypeId());
						found = addUserToDB(user);
						validateEntity.setStatus("NewUser");
					}else{
						validateEntity.setStatus("ExistingUser");
						updateUserInfoURL(user);			
					}
					
					
					validateEntity.setUser(found);
				}
			return validateEntity;
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace()); 
		}		
		
	}

	private void updateUserInfoURL(User user) throws IllegalOperation{
		PreparedStatement ps = null;
		String query = null;
		Connection conn = null;
		try {
			conn = setConnection();
			query = "update activeUsers set firstName=?,lastName=?,pictureUrl=?,profileUrl=?,lastLogin=? where email = ?";
			
			
			ps = conn.prepareStatement(query);
			ps.setString(1, user.getFirstName());
			ps.setString(2, user.getLastName());
			ps.setString(3, user.getPictureUrl());
			ps.setString(4, user.getProfileUrl());
			ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			ps.setString(6, user.getEmail()); // added by fareed
			ps.executeUpdate();

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
	private User getUserByEmailFromDB(String email) throws IllegalOperation {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn=null;
		try {
			conn = setConnection();
			String query = "SELECT * FROM activeUsers WHERE email = ?";
			ps=conn.prepareStatement(query);
			ps.setString(1, email);
			rs=ps.executeQuery();
			if (rs.next())
				return getUserFromResultSet(rs);
	
			return null;
		
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace()); 
		}finally {
			try {rs.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {ps.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
		}
	}
	
	public User addUserToDB(User newUser) throws IllegalOperation {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		Connection conn=null;
		try {	
			conn = setConnection();
			 query = "INSERT INTO activeUsers"
					+ "(userTypeId,firstName,lastName,email,status,pictureUrl,profileUrl,lastLogin) VALUES" + "(?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(query);
			ps.setLong(1, newUser.getUserTypeId());
			ps.setString(2, newUser.getFirstName());
			ps.setString(3, newUser.getLastName());
			ps.setString(4, newUser.getEmail());
			ps.setBoolean(5, newUser.isStatus());
			ps.setString(6, newUser.getPictureUrl());
			ps.setString(7, newUser.getProfileUrl());
			ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));

			ps.execute();

			rs = ps.executeQuery("select last_insert_id() as userId from activeUsers");
			if (rs.next())
				newUser.setUserId(rs.getLong("userId")); //

			newUser = getUserByIdFromDB(newUser.getUserId());
			return newUser;

		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace()); 
		}finally {
			try {rs.close();} catch (Exception e) {}
			try {ps.close();} catch (Exception e) {}
			try {conn.close();} catch (Exception e) {}
		}
	}
	public User getNewestUserFromDB() throws IllegalOperation {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Long maxId = null;
		Connection conn=null;
		try {
			conn = setConnection();
			ps = conn.prepareStatement("SELECT MAX(userId) FROM activeUsers");
			rs = ps.executeQuery();
			if (rs.next())
				maxId = rs.getLong(1);
			return getUserByIdFromDB(maxId);
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {rs.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {ps.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
		}
	}


	private User getUserFromResultSet(ResultSet rs) {
		try {
			User found = new User();
			found.setUserId(rs.getLong("userId"));
			found.setUserTypeId(rs.getLong("userTypeId"));
			found.setFirstName(rs.getString("firstName"));
			found.setLastName(rs.getString("lastName"));
			found.setEmail(rs.getString("email"));
			found.setStatus(rs.getBoolean("status"));	
			found.setLastLogin(rs.getTimestamp("lastLogin"));
			found.setPictureUrl(rs.getString("pictureUrl"));
			found.setProfileUrl(rs.getString("profileUrl"));
			return found;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/*
	 * return the allowed user with this email
	 * null if not found
	 */
	private AllowedUser getAllowedUserByIdFromDB(String email) throws IllegalOperation {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn=null;
		try {
			conn = setConnection();
			String query = "SELECT * FROM allowedUsers WHERE email = ?";
			ps=conn.prepareStatement(query);
			ps.setString(1, email);
			rs=ps.executeQuery();
			if (rs.next()) {
				AllowedUser found = new AllowedUser();
				found.setUserTypeId(rs.getLong("userTypeId"));
				found.setEmail(rs.getString("email"));		
				return found;
			}
			else return null;
				
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace()); 
			
		}finally {
			
			try {rs.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {ps.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
		}
	}


	public ArrayList getUsersList() throws IllegalOperation {
		ArrayList<User> usersList = new ArrayList<User>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query=null;
		Connection conn=null;
		try{
			conn = setConnection();
			query = "SELECT * FROM activeUsers";
			ps=conn.prepareStatement(query);
			rs=ps.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setUserId(rs.getLong("userId"));
				user.setUserTypeId(rs.getLong("userId"));
				user.setEmail(rs.getString("email"));
				user.setPictureUrl(rs.getString("pictureUrl"));
				user.setFirstName(rs.getString("firstName"));
				user.setLastName(rs.getString("lastName"));
				user.setProfileUrl(rs.getString("profileUrl"));
				user.setStatus(rs.getBoolean("status"));
				user.setLastLogin(rs.getTimestamp("lastLogin"));
				
				usersList.add(user);
			}
			return usersList;
			
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		}finally {
			try {rs.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}	
			try {ps.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
		}
	}
	
	public ArrayList getUserTypes() throws IllegalOperation {
		ArrayList<UserTypes> userTypes = new ArrayList<UserTypes>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query=null;
		Connection conn=null;
		try{
			conn = setConnection();
			query = "SELECT * FROM userTypes";
			ps=conn.prepareStatement(query);
			rs=ps.executeQuery();
			while (rs.next()) {
				UserTypes usertype = new UserTypes();	
				usertype.setUserTypeId(rs.getLong("userTypeId"));
				usertype.setUserTypeName(rs.getString("userTypeName"));
				userTypes.add(usertype);
			}
			return userTypes;
			
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		}finally {
			try {rs.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}	
			try {ps.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
		}
	}
	
}
