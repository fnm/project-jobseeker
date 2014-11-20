package mySqlPackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

import entitiesPackage.Tag;
import exceptionPackage.IllegalOperation;
public class TagDatabase extends MySqlConnection {

	public Tag addTagToDataBase(Tag newTag)  throws  IllegalOperation{
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		Connection conn=null;

		try {	
			conn = setConnection();
			query = "INSERT INTO tags" + "(tagName) VALUES" + "(?)";
			ps = conn.prepareStatement(query);
			ps.setString(1, newTag.getTagName());
			ps.executeUpdate();
			
			rs = ps.executeQuery("select last_insert_id() as tagId from tags");

			if (rs.next())
				newTag.setTagId(rs.getLong("tagId")); 
			
			return newTag;
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		}finally {
			try {rs.close();} catch (Exception e) {}
			try {ps.close();} catch (Exception e) {}
			try {conn.close();} catch (Exception e) {}
		}

	}

	public void deleteTagFromPostTags(Long postId) throws IllegalOperation {

		Statement stmt = null;
		String query = null;
		Connection conn=null;
		try {
			conn = setConnection();
			query = "DELETE FROM postTags WHERE postId = " + postId;
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		
		} catch (Exception e) {
			 throw new IllegalOperation(e.getStackTrace()); 
		} finally {
			try {stmt.close();} catch (Exception e) { }
			try {conn.close();} catch (Exception e) { }
		}
	}
	
	public void deleteTagFromDB(Long tagId) throws IllegalOperation {
		
		Statement stmt = null;
		String query = null;
		Connection conn=null;
		
		try {

			conn = setConnection();
			query = "DELETE FROM tags WHERE tagId = " + tagId;
			stmt = conn.createStatement();
			stmt.executeUpdate(query);

		} catch (Exception e) {
			 throw new IllegalOperation(e.getStackTrace()); 
		} finally {
			try {stmt.close();} catch (Exception e) { /* ignored */}
			try {conn.close();} catch (Exception e) { /* ignored */}
		}
	}
	
	public ArrayList<Tag> getTagTableFromDataBase() throws IllegalOperation {
		
		ArrayList<Tag> tagsList = new ArrayList<Tag>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query=null;
		Connection conn=null;
		try{
			conn = setConnection();
			query = "SELECT * FROM tags";
			ps=conn.prepareStatement(query);
			rs=ps.executeQuery();
			while (rs.next()) {
				Tag tag = new Tag();
				tag.setTagId(rs.getLong("tagId"));
				tag.setTagName(rs.getString("tagName"));	
				tagsList.add(tag);
			}
			return tagsList;
			
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		}finally {
			try {rs.close();} catch (Exception e) {}	
			try {ps.close();} catch (Exception e) {}
			try {conn.close();} catch (Exception e) {}
		}	
	}

public Tag getTagFromDataBase(Long tagId) throws IllegalOperation {
	
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn=null;
		try{
			conn = setConnection();
			String query = "SELECT * FROM tags WHERE tagId = ?";
			ps=conn.prepareStatement(query);
			ps.setLong(1, tagId);
			rs=ps.executeQuery();
			if (rs.next()) {
				Tag found = new Tag();
				found.setTagId(rs.getLong("tagId"));
				found.setTagName(rs.getString("tagName"));	
				return found;
			}
			else return null;
			
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		}finally {
			try {rs.close();} catch (Exception e) {}	
			try {ps.close();} catch (Exception e) {}
			try {conn.close();} catch (Exception e) {}
		}	
	}
}
