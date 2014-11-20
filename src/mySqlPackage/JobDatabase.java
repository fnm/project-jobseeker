package mySqlPackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import entitiesPackage.Assignment;
import entitiesPackage.CloseJobEvent;
import entitiesPackage.Event;
import entitiesPackage.Job;
import entitiesPackage.MyJob;
import entitiesPackage.Notification;
import entitiesPackage.Post;
import entitiesPackage.User;
import exceptionPackage.IllegalOperation;

/**
 * for updating Job database tables
 */
public class JobDatabase extends MySqlConnection {

	public Job addJobToDB(Job newJob) throws IllegalOperation {

		PreparedStatement ps = null;
		String query = null;
		Connection conn = null;
		PostDatabase PDB = new PostDatabase();

		try {

			if ((new UserDatabase()).getUserByIdFromDB(newJob
					.getSubmitterUserId()) == null)
				throw new IllegalOperation("user not available");
			conn = setConnection();

			if (!newJob.getIsPrivate())
				newJob.setPostId(addPostWithJobAutomatically(newJob));

			query = "INSERT INTO jobs"
					+ "(submitterUserId,postId,jobTitle,companyName,address,contact,description,isPrivate,isActive,publishDate) VALUES"
					+ "(?,?,?,?,?,?,?,?,?,?)";

			ps = conn.prepareStatement(query);
			ps.setLong(1, newJob.getSubmitterUserId());
			if (!newJob.getIsPrivate())
				ps.setLong(2, newJob.getPostId());
			else
				ps.setInt(2, 0);
			ps.setString(3, newJob.getJobTitle());
			ps.setString(4, newJob.getCompanyName());
			ps.setString(5, newJob.getAddress());
			ps.setString(6, newJob.getContact());
			ps.setString(7, newJob.getDescription());
			ps.setBoolean(8, newJob.getIsPrivate());
			ps.setBoolean(9, newJob.getIsActive());
			ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
			ps.execute();

			newJob.setJobId(PDB.getNewestIdInTable("jobs", "jobId")); //

			addTagsToJobTagsTable(newJob.getTags(), newJob.getJobId());

			User user = (new UserDatabase()).getUserByIdFromDB(newJob
					.getSubmitterUserId());

			if (user.getUserTypeId() == 1) {// 1=jobseeker
				Assignment newAss = new Assignment();
				newAss.setJobId(newJob.getJobId());
				newAss.setUserId(newJob.getSubmitterUserId());
				newAss.setPrivate(true);
				newAss.setStatus(2L);
				newAss.setJobTitle(newJob.getJobTitle());
				addAssignment(newAss);
				Event event = new Event();
				event.setUserId(newJob.getSubmitterUserId());
				event.setJobId(newJob.getJobId());
				event.setAddress("");
				event.setTitle("Application started");
				event.setDescription("");
				event.setNotes("");
				event.setDue(new Timestamp(System.currentTimeMillis()));
				(new EventDatabase()).addEventToDB(event, null);
			}

			return newJob;

		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				throw new IllegalOperation(e.getStackTrace());
			}
			try {
				ps.close();
			} catch (Exception e) {
				throw new IllegalOperation(e.getStackTrace());
			}
		}
	}

	private Long addPostWithJobAutomatically(Job newJob)
			throws IllegalOperation {

		PreparedStatement ps = null;
		String query = null;
		Connection conn = null;
		PostDatabase PDB = new PostDatabase();
		Post jobpost = new Post();
		try {
			conn = setConnection();
			jobpost.setTitle("A new job was added -" + newJob.getJobTitle());
			String str = newJob.getDescription();
			int max = (str.length() > 100) ? 100 : (str.length());
			jobpost.setContent(newJob.getDescription().substring(0, max)
					+ "...");
			jobpost.setUserId(newJob.getSubmitterUserId());

			query = "INSERT INTO posts"
					+ "(userId,title,content,publishDate) VALUES" + "(?,?,?,?)";
			ps = conn.prepareStatement(query);
			ps.setLong(1, jobpost.getUserId());
			ps.setString(2, jobpost.getTitle());
			ps.setString(3, jobpost.getContent());
			ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			ps.execute();

			return PDB.getNewestIdInTable("posts", "postId");
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				throw new IllegalOperation(e.getStackTrace());
			}
			try {
				ps.close();
			} catch (Exception e) {
				throw new IllegalOperation(e.getStackTrace());
			}
		}

	}

	public void addTagsToJobTagsTable(ArrayList arrayList, Long jobId)
			throws IllegalOperation {
		PreparedStatement ps = null;
		String query = null;
		Connection conn = null;
		if (arrayList != null)
			for (int i = 0; i < arrayList.size(); i++) {
				try {
					conn = setConnection();
					query = "INSERT INTO jobTags" + "(jobId,tagId) VALUES"
							+ "(?,?)";
					ps = conn.prepareStatement(query);
					ps.setLong(1, jobId);
					ps.setLong(2, (Long) arrayList.get(i));
					ps.execute();
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

	public Job getJobByIdFromDB(Long jobId) throws IllegalOperation {

		PreparedStatement ps = null;
		ResultSet rs = null;

		ArrayList<Long> tags = new ArrayList<Long>();
		Connection conn = null;
		try {
			Job found = null;
			conn = setConnection();
			String query = "SELECT * FROM jobs WHERE jobId = ?";
			ps = conn.prepareStatement(query);
			ps.setLong(1, jobId);
			rs = ps.executeQuery();
			if (rs.next()) {
				found = getJobFromResultSet(rs);
			} else
				return null;

			query = "SELECT * FROM jobTags WHERE jobId = ?";
			ps = conn.prepareStatement(query);
			ps.setLong(1, jobId);
			rs = ps.executeQuery();
			while (rs.next()) {
				tags.add(rs.getLong("tagId"));
			}

			found.setTags(tags);
			return found;

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

	// updates only isPrivate, isActive
	public Job updateJob(Long jobId, Boolean isPrivate, boolean isActive)
			throws IllegalOperation {
		// To DO : this function must return boolian... no need for a job
		PreparedStatement ps = null;
		String query = null;
		Connection conn = null;
		try {
			conn = setConnection();
			query = "update jobs" + " set  isPrivate = ? " + " ,isActive = ? "
					+ "where jobId = ? ";

			ps = conn.prepareStatement(query);
			ps.setBoolean(1, isPrivate);
			ps.setBoolean(2, isActive);
			ps.setLong(3, jobId); // added by fareed
			ps.execute();

			return getJobByIdFromDB(jobId);

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

	// deleting means only to change isActive to false

	public Job deleteJobFromDB(Long jobId, Long userId) throws IllegalOperation {
		try {
			Job toDeleteJob = getJobByIdFromDB(jobId);
			updateJob(jobId, toDeleteJob.getIsPrivate(), false);
			return toDeleteJob;
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		}
	}

	/*
	 * returns all the jobs+assignments info that are assigned to this user
	 */
	public ArrayList<MyJob> getJobsListByUserFromDB(Long userId)
			throws IllegalOperation {

		ArrayList<Assignment> ass = getAssignmentsListByUserFromDB(userId);
		ArrayList<MyJob> myJobs = new ArrayList<MyJob>();

		for (Assignment curr : ass) {
			Job currJob = getJobByIdFromDB(curr.getJobId());
			MyJob myJob = new MyJob();
			myJob.setAss(curr);
			myJob.setJob(currJob);
			myJobs.add(myJob);
		}
		return myJobs;
	}

	private Job getJobFromResultSet(ResultSet rs) {

		Job found = new Job();
		try {
			found.setJobId(rs.getLong("jobId"));
			found.setPostId(rs.getLong("postId"));
			found.setSubmitterUserId(rs.getLong("submitterUserId"));
			found.setJobTitle(rs.getString("jobTitle"));
			found.setCompanyName(rs.getString("companyName"));
			found.setAddress(rs.getString("address"));
			found.setContact(rs.getString("contact"));
			found.setDescription(rs.getString("description"));
			found.setIsPrivate(rs.getBoolean("isPrivate"));
			found.setIsActive(rs.getBoolean("isActive"));
			found.setPublishDate(rs.getTimestamp("publishDate"));
			return found;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Assignments Database
	 * 
	 **/
	public void suggestJobToJobSeekers(Long jobId, Long userId,
			ArrayList<Long> jobSeekerList) throws IllegalOperation {

		for (Long curr : jobSeekerList) {
			Assignment newAss = new Assignment();
			newAss.setJobId(jobId);
			newAss.setUserId(curr);
			newAss.setStatus(1L);
			newAss.setPrivate(true);
			Job job = new Job();
			job = getJobByIdFromDB(jobId);
			newAss.setJobTitle(job.getJobTitle());
			Assignment found = getAssignmentByUserAndJobFromDB(curr, jobId);
			if (found == null)
				addAssignment(newAss);
		}
	}

	public Job updateJob(Job job) throws IllegalOperation {
		PreparedStatement ps = null;
		String query = null;
		Connection conn = null;
		try {
			conn = setConnection();
			query = "update jobs"
					+ "set"
					+ "(submitterUserId,jobTitle,companyName,address,contact,description,isPrivate,isActive,publishDate) VALUES"
					+ "(?,?,?,?,?,?,?,?,?)" + "where jobId= ?";

			ps = conn.prepareStatement(query);
			ps.setLong(1, job.getSubmitterUserId());
			ps.setString(2, job.getJobTitle());
			ps.setString(3, job.getCompanyName());
			ps.setString(4, job.getAddress());
			ps.setString(5, job.getContact());
			ps.setString(6, job.getDescription());
			ps.setBoolean(7, job.getIsPrivate());
			ps.setBoolean(8, true);
			ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));

			ps.setLong(10, job.getJobId());

			ps.execute();

			return getJobByIdFromDB(job.getJobId());

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

	public Event CloseMyJobCycle(CloseJobEvent closeJobEvent)
			throws IllegalOperation {

		Event event = closeJobEvent.getEvent();
		JobDatabase JDB = new JobDatabase();
		EventDatabase EDB = new EventDatabase();
		if (closeJobEvent.getIsAccepted())
			JDB.updateAssignmentStatus(event.getUserId(), event.getJobId(), 4L);
		else
			JDB.updateAssignmentStatus(event.getUserId(), event.getJobId(), 3L);

		return EDB.addEventToDB(closeJobEvent.getEvent(), null);
	}

	public MyJob applyToJob(Long userId, Long jobId) throws IllegalOperation {

		Assignment newAss = new Assignment();
		newAss.setJobId(jobId);
		newAss.setUserId(userId);
		newAss.setStatus(2L);
		newAss.setPrivate(true);
		Assignment found = getAssignmentByUserAndJobFromDB(userId, jobId);
		if (found == null) {
			Job job = new Job();
			job = getJobByIdFromDB(jobId);
			newAss.setJobTitle(job.getJobTitle());
			addAssignment(newAss);
			MyJob myJob = new MyJob();
			myJob.setAss(newAss);
			myJob.setJob(job);
			Event event = new Event();
			event.setUserId(userId);
			event.setJobId(job.getJobId());
			event.setAddress("");
			event.setTitle("Application started");
			event.setDescription("");
			event.setNotes("");
			event.setDue(new Timestamp(System.currentTimeMillis()));
			(new EventDatabase()).addEventToDB(event, null);
			return myJob;
		} else
			return null;
	}

	/*
	 * public static void main(String args[]){
	 * 
	 * try { (new JobDatabase()).getAssignmentByUserAndJobFromDB(100L, 100L); }
	 * catch (IllegalOperation e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * }
	 */

	/**
	 * Assignments Database
	 * 
	 * @throws IllegalOperation
	 * 
	 **/
	public Assignment addAssignment(Assignment newAss) throws IllegalOperation {
		PreparedStatement ps = null;
		String query = null;
		Connection conn = null;
		try {
			NotificationDatabase NDB = new NotificationDatabase();

			conn = setConnection();
			query = "INSERT INTO assignments"
					+ "(userId,jobId,isPrivate,status) VALUES" + "(?,?,?,?)";

			ps = conn.prepareStatement(query);
			ps.setLong(1, newAss.getUserId());
			ps.setLong(2, newAss.getJobId());
			ps.setBoolean(3, newAss.isPrivate());
			ps.setLong(4, newAss.getStatus());

			ps.execute();
			if (newAss.getStatus() == 1L) {
				newAss.setAssignmentId((new PostDatabase()).getNewestIdInTable(
						"assignments", "assignmentId"));

				Notification newNotification = new Notification();
				newNotification.setNotificationTypeId(NDB
						.getNotificationTypeId("assignment"));
				newNotification.setText("Job suggestion: "
						+ newAss.getJobTitle());
				newNotification
						.setDue(new Timestamp(System.currentTimeMillis()));
				newNotification.setUserId(newAss.getUserId());
				newNotification.setTypeId(newAss.getJobId());
				newNotification.setNotificationForeignId(newAss
						.getAssignmentId());
				newNotification.setNotificationAlertTime(newNotification
						.getDue());
				NDB.addNotification(newNotification);
			}
			return newAss;

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

	public ArrayList<Job> SearchJobsByTags(ArrayList tags, Long numberOfItems,
			Long offset, Long userId, boolean forward) throws IllegalOperation {

		ArrayList<Long> jobIds = new ArrayList<Long>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		Connection conn = null;
		try {
			conn = setConnection();

			for (int i = 0; i < tags.size(); i++) {
				if (i == 0) {
					if (forward == false)
						query = "SELECT DISTINCT jobId FROM jobTags WHERE jobId <=? AND (tagId="
								+ tags.get(i);
					else
						query = "SELECT DISTINCT jobId FROM jobTags WHERE jobId >=? AND (tagId="
								+ tags.get(i);
				} else
					query = query + " or tagId=" + tags.get(i);
			}
			query = query + ")";
			ps = conn.prepareStatement(query);
			if (offset == 0)
				ps.setLong(1, (new PostDatabase()).getNewestIdInTable(
						"jobTags", "jobId"));
			else
				ps.setLong(1, offset);

			rs = ps.executeQuery();

			while (rs.next() && jobIds.size() < numberOfItems) {
				jobIds.add(rs.getLong("jobId"));
			}

			return getJobListByIds(jobIds);

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

	public ArrayList<Job> getJobListByIds(ArrayList<Long> jobIds)
			throws IllegalOperation {

		ArrayList<Job> jobsList = new ArrayList<Job>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		Connection conn = null;
		try {
			conn = setConnection();
			for (int i = 0; i < jobIds.size(); i++) {
				if (i == 0)
					query = "SELECT * FROM jobs WHERE isActive=? AND (jobId="
							+ jobIds.get(i);
				else
					query = query + " or jobId=" + jobIds.get(i);
			}
			query = query + ") ORDER BY jobId DESC";
			ps = conn.prepareStatement(query);
			ps.setBoolean(1, true);
			rs = ps.executeQuery();

			while (rs.next())
				jobsList.add(getJobFromResultSet(rs));

			return jobsList;
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

	public void deleteAssignment(Long assId, Long userId)
			throws IllegalOperation {

		PreparedStatement ps = null;
		String query = null;
		Connection conn = null;
		try {

			Assignment toDeleteAssignment = getAssignmentByIdFromDB(assId);
			// case 1 - this user has no permission to delete this job
			if (userId != toDeleteAssignment.getUserId())
				throw new IllegalOperation(
						"this user has no permission to delete this assignment");

			conn = setConnection();
			query = "DELETE FROM assignments WHERE assignmentId=?";
			ps = conn.prepareStatement(query);
			ps.setLong(1, assId);
			ps.executeQuery();

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

	public Assignment getAssignmentByIdFromDB(Long assId)
			throws IllegalOperation {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		Connection conn = null;
		try {
			conn = setConnection();
			query = "SELECT * FROM assignments WHERE assignmentId  = ?";
			ps = conn.prepareStatement(query);
			ps.setLong(1, assId);
			rs = ps.executeQuery();
			if (rs.next()) {
				return getAssignmentFromResultSet(rs);
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

	public ArrayList<Assignment> deleteAssignmentsByUser(Long userId)
			throws IllegalOperation {

		try {
			ArrayList<Assignment> assList = getAssignmentsListByUserFromDB(userId);

			if (assList != null)
				for (Assignment curr : assList) {
					deleteAssignment(curr.getAssignmentId(), userId);
				}
			return assList;
		} catch (Exception e) {
			throw new IllegalOperation(e.getStackTrace());
		}

	}

	public ArrayList<Assignment> getAssignmentsListByUserFromDB(Long userId)
			throws IllegalOperation {

		ArrayList<Assignment> jobsList = new ArrayList<Assignment>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		Connection conn = null;
		try {
			conn = setConnection();
			query = "SELECT * FROM assignments WHERE userId = " + userId
					+ " ORDER BY assignmentId";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery(query);
			while (rs.next())
				jobsList.add(getAssignmentFromResultSet(rs));
			return jobsList;
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

	private Assignment getAssignmentFromResultSet(ResultSet rs) {
		Assignment found = new Assignment();
		try {
			found.setAssignmentId(rs.getLong("assignmentId"));
			found.setUserId(rs.getLong("userId"));
			found.setJobId(rs.getLong("jobId"));
			found.setPrivate(rs.getBoolean("isPrivate"));
			found.setStatus(rs.getLong("status"));

			return found;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Boolean updateAssignmentStatus(Long userId, Long jobId, Long status)
			throws IllegalOperation {
		PreparedStatement ps = null;
		String query = null;
		Connection conn = null;
		try {
			conn = setConnection();
			query = "update assignments" + " set status = ? "
					+ "where jobId = ? AND userId = ?";
			System.out.println(query);
			ps = conn.prepareStatement(query);
			ps.setLong(1, status);
			ps.setLong(2, jobId);
			ps.setLong(3, userId);
			ps.execute();

			return true;

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

	public Assignment getAssignmentByUserAndJobFromDB(Long userId, Long jobId)
			throws IllegalOperation {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		Connection conn = null;
		Assignment ass = null;
		try {
			conn = setConnection();
			query = "SELECT * FROM assignments WHERE userId = ? AND jobId = ?";
			ps = conn.prepareStatement(query);
			ps.setLong(1, userId);
			ps.setLong(2, jobId);
			rs = ps.executeQuery();
			if (rs.next())
				ass = getAssignmentFromResultSet(rs);
			return ass;
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

	public Long deleteAssignmentbyUserIdJobId(Long userId, Long jobId)
			throws IllegalOperation {
		PreparedStatement ps = null;
		String query = null;
		Connection conn = null;
		try {
			conn = setConnection();
			query = "DELETE FROM assignments WHERE jobId = ? AND userId = ?";

			ps = conn.prepareStatement(query);
			ps.setLong(1, jobId);
			ps.setLong(2, userId);
			ps.execute();

			return jobId;

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
