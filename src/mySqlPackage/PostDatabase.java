package mySqlPackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import exceptionPackage.IllegalOperation;
import entitiesPackage.Post;

public class PostDatabase extends MySqlConnection {
	  
	public Post addPostToDB(Post newPost) throws IllegalOperation {

		PreparedStatement ps = null;
		String query = null;
		Connection conn=null;
		try {
			conn = setConnection();
			query = "INSERT INTO posts"
					+ "(userId,title,content,publishDate) VALUES" + "(?,?,?,?)";
			ps = conn.prepareStatement(query);
			ps.setLong(1, newPost.getUserId());
			ps.setString(2, newPost.getTitle());
			ps.setString(3, newPost.getContent());
			ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			ps.execute();
			// update the post id
			newPost.setPostId(getNewestIdInTable("posts","postId"));
			//add tags
			if(!newPost.getTags().isEmpty()){
			    addTagsToPostTagsTable(newPost.getTags(), newPost.getPostId());
			   }
			
			//newPost = getNewestPostFromDB();
			
			return newPost;

		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {ps.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
		}
	}

	public Long getNumberOfPosts() throws IllegalOperation{
	       
        PreparedStatement ps = null;
        String query = null;
        ResultSet rs = null;
        Connection conn=null;
        try {
   
            conn = setConnection();  
            query = "SELECT count(postId) from posts";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery(query);
            rs.next();
            return rs.getLong(1);

        } catch (Exception e) {
            return 0L;
           
        }finally {
            try {ps.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
            try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
            try {rs.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}  
        }      
    }
	
	public Post deletePostFromDB(Long postId, Long userId)
			throws IllegalOperation {

		Statement stmt = null;
		String query = null;
		Connection conn=null;
		Post toDeletePost = getPostByIdFromDB(postId);
		try {
		
			//if(userId=AdminId); ignore checking user
			// case 3 - this user has no permission to delete this post
			if (userId != toDeletePost.getUserId())
				throw new IllegalOperation(
						"this user has no permission to delete this post");

			// delete all its comments
			(new CommentDatabase())
					.deleteCommentsByPostIdFromDB(postId, userId);

			// delete all its tags
			(new TagDatabase()).deleteTagFromPostTags(toDeletePost.getPostId());

		
		
			// delete the post itself
			conn = setConnection();
			query = "DELETE FROM posts WHERE postId = " + postId;
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			return toDeletePost;

		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {stmt.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
		}
	}

	/*
	 * N - number of posts returned - in our case 20 posts
	 * currId - the id for the current post to start our search from
	 * forward - true to look for newer posts - false to look for older posts
	 * 
	 */
	public ArrayList<Post> getNewsfeedFromDB(Long N, Long userId, Long currId,
			boolean forward) throws IllegalOperation {

		ArrayList<Post> postsList = new ArrayList<Post>();
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		String query = null;
		Connection conn=null;
		try {
			
			conn = setConnection();
			if (forward == true)
				query="SELECT * FROM posts Inner Join activeUsers On posts.userId=activeUsers.userId where postId >= ? Order By postId DESC";
			else
				query="SELECT * FROM posts Inner Join activeUsers On posts.userId=activeUsers.userId where postId <= ? Order By postId DESC";
			ps = conn.prepareStatement(query);
			
			if(currId==0)
				ps.setLong(1,getNewestIdInTable("posts", "postId"));
				else
				ps.setLong(1, currId);
			
			rs = ps.executeQuery();
			
			while (rs.next() && postsList.size() < N)
			{
				Post post=getPostFromResultSet(rs);		
				post.setPictureUrl(rs.getString("pictureUrl"));
				post.setFirstName(rs.getString("firstName"));
				post.setLastName(rs.getString("lastName"));
				postsList.add(post);
			}
			return postsList;

		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {ps.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {rs.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
		}
	}

	/*
	 * returns the found post or null if post doen't exist
	 */
	public Post getPostByIdFromDB(Long postId) throws IllegalOperation {

		PreparedStatement ps=null;
		ResultSet rs = null;
		Connection conn=null;
		try {
			Post found = null;
			conn = setConnection();
			String query = "SELECT * FROM posts WHERE postId = ?";
			ps = conn.prepareStatement(query);
			ps.setLong(1, postId);
			rs = ps.executeQuery();
			if (rs.next())
				found =  getPostFromResultSet(rs);
		
			ArrayList<Long> tags=new ArrayList<Long>();
			query = "SELECT * FROM postTags WHERE postId = ?";
			ps=conn.prepareStatement(query);
			ps.setLong(1, postId);
			rs=ps.executeQuery();	
		    while (rs.next()) {
		    	tags.add(rs.getLong("tagId"));
			}
		    
			found.setTags(tags);
			return found;
			
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace()); 
		}finally {
			try {rs.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {ps.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
		}
	}
	/*
	 * delete all the posts for this userId
	 */
	public ArrayList<Post> deletePostsByUserIdFromDB(Long userId)
			throws IllegalOperation {

		try {
			ArrayList<Post> postsList = getPostsByUserIdFromDB(userId);
			for (Post curr : postsList)
				deletePostFromDB(curr.getPostId(), userId);
			return postsList;
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		}
	}

	/**
	 * to use for testing
	 */
	public void deletePostsTable() {

		PreparedStatement ps = null;
		Connection conn=null;
		try {

			conn = setConnection();
			String query = "DELETE FROM posts WHERE postId > 0";
			ps = conn.prepareStatement(query);
			ps.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {ps.close();} catch (Exception e) {e.printStackTrace();}
			try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
	}

	/*
	 * get array list of post for this userId
	 */
	public ArrayList<Post> getPostsByUserIdFromDB(Long userId)
			throws IllegalOperation {

		ArrayList<Post> postsList = new ArrayList<Post>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		Connection conn=null;
		try {
			conn = setConnection();
			query = "SELECT * FROM posts WHERE userId =? ORDER BY postId";
			ps = conn.prepareStatement(query);
			ps.setLong(1, userId);
			rs = ps.executeQuery();

			rs = ps.executeQuery(query);

			while (rs.next())
				postsList.add(getPostFromResultSet(rs));

			return postsList;

		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {rs.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {ps.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
		}
	}

	public void addTagsToPostTagsTable(ArrayList tagsList, Long postId)
			throws IllegalOperation {
		PreparedStatement ps = null;
		String query = null;
		Connection conn = null;

		try {
			conn = setConnection();
			if (tagsList != null)
				for (int i = 0; i < tagsList.size(); i++) {
					query = "INSERT INTO postTags" + "(postId,tagId) VALUES"
							+ "(?,?)";
					ps = conn.prepareStatement(query);
					ps.setLong(1, postId);
					ps.setLong(2, (Long) tagsList.get(i));
					ps.execute();
				}

		}
		  catch (Exception e) {  
		 	throw new IllegalOperation(e.getStackTrace());
		 	
		  } finally {
			try {ps.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
		  }
		
	}
	
	

	public Post getNewestPostFromDB() throws IllegalOperation {
			return getPostByIdFromDB(getNewestIdInTable("posts","postId"));
	}

	private Post getPostFromResultSet(ResultSet rs) {

		try {
			Post found = new Post();
			found.setPostId(rs.getLong("postId"));
			found.setUserId(rs.getLong("userId"));
			found.setTitle(rs.getString("title"));
			found.setContent(rs.getString("content"));
			found.setPublishDate(rs.getTimestamp("publishDate"));

			return found;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Post getPostFromResultSetFull(ResultSet rs) {

		try {
			Post found = new Post();
			found.setPostId(rs.getLong("postId"));
			found.setUserId(rs.getLong("userId"));
			found.setTitle(rs.getString("title"));
			found.setContent(rs.getString("content"));
			found.setPublishDate(rs.getTimestamp("publishDate"));
			found.setPictureUrl(rs.getString("pictureUrl"));
			found.setFirstName(rs.getString("firstName"));
			found.setLastName(rs.getString("lastName"));
			System.out.println(found.getPostId()+found.getContent()+found.getFirstName());
			return found;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Long getNewestIdInTable(String tableName,String columnName) throws IllegalOperation {
			PreparedStatement ps = null;
			ResultSet rs = null;
			Long maxId = null;
			Connection conn=null;
			try {
				conn = setConnection();
				ps = conn.prepareStatement("SELECT MAX("+columnName+") FROM "+tableName);
				rs = ps.executeQuery();
				if (rs.next()) {
					maxId = rs.getLong(1);
				}
				return maxId;
			} catch (Exception e) {
				throw new IllegalOperation(e.getStackTrace());
			} finally {
				try {rs.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
				try {ps.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
				try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			}
		}
	
	public ArrayList<Post> SearchPostsByTags(ArrayList tags,Long numberOfItems,Long offset,Long userId,boolean forward)throws IllegalOperation {

			ArrayList<Post> postsList = new ArrayList<Post>();
			ArrayList<Long> postIds= new ArrayList<Long>();
			PreparedStatement ps = null;
			ResultSet rs = null;
			String query = null;
			Connection conn=null;
			try {
				conn = setConnection();			
				
				for(int i=0;i<tags.size();i++)
				{
					if(i==0){
						if(forward==false)
							
							query = "SELECT p.postId,p.userId,p.content,p.title,p.publishDate,u.firstName,u.lastName,u.pictureUrl FROM posts As p INNER JOIN postTags As tt On p.postId = tt.postId inner join activeUsers as u on u.userId=p.userId where  (tt.tagId="+ tags.get(i);
						else
							query = "SELECT p.postId,p.userId,p.content,p.title,p.publishDate,u.firstName,u.lastName,u.pictureUrl FROM posts As p INNER JOIN postTags As tt On p.postId = tt.postId inner join activeUsers as u on u.userId=p.userId AND (tt.tagId="+ tags.get(i);
					}else
					query=query+" or tagId="+tags.get(i);	
				}		
				query=query+")";
				ps = conn.prepareStatement(query);
/*				if(offset==0)
					ps.setLong(1,getNewestIdInTable("postTags","postId"));
				else
					ps.setLong(1, offset);*/
				
				rs = ps.executeQuery();
				
/*				while (rs.next() && postIds.size() < numberOfItems) {
					postIds.add(rs.getLong("postId"));
				}
				

				for(int i=0;i<postIds.size();i++)
				{
					if(i==0)
					query ="SELECT * FROM posts WHERE postId="+ postIds.get(i);
					else
					query=query+" or postId="+postIds.get(i);	
				}
				query=query+" ORDER BY postId DESC";
				ps = conn.prepareStatement(query);
				rs = ps.executeQuery();*/
			
				while (rs.next()){
					postsList.add(getPostFromResultSetFull(rs));
				}
				
				return postsList;

			} catch (Exception e) {
				throw new IllegalOperation(e.getStackTrace());
			} finally {
				try {rs.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
				try {ps.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
				try {conn.close();} catch (Exception e) {throw new IllegalOperation(e.getStackTrace());}
			}
		}
		
	
	
	
	
	

  
}
