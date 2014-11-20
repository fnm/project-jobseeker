package dispatcherPackage;

import java.sql.Timestamp;
import java.util.ArrayList;

import mySqlPackage.*;
import entitiesPackage.*;
import exceptionPackage.IllegalOperation;

public class HandleRequest {

	public HandleRequestStatus HandleMessage(ClientRequest msg) {

		switch (msg.getOpCode()) {
		case initializeRequest:
			return initialize(msg.getOpCode(), (User) msg.getEntity());

		case addPostRequest:
			return addPost(msg.getOpCode(), (Post) msg.getEntity());

		case deletePostRequest:
			return deletePost(msg.getOpCode(), (Post) msg.getEntity());

		case viewNewsFeedRequest:
			return getListOfPosts(msg.getOpCode(),
					(NewsFeedEntity) msg.getEntity());

		case singlePostRequest:
			return getPostById(msg.getOpCode(), (Post) msg.getEntity());

		case addCommentRequest:
			return addComment(msg.getOpCode(), (Comment) msg.getEntity());

		case deleteCommentRequest:
			return deleteComment(msg.getOpCode(), (Comment) msg.getEntity());

		case commentListRequest:
			return getCommentList(msg.getOpCode(), (Comment) msg.getEntity());

		case validateJobSeekerRequest:
			return validateJobSeeker(msg.getOpCode(), (User) msg.getEntity());

		case addAllowedUsersRequest:
			return addAllowedUser(msg.getOpCode(), (User) msg.getEntity());

		case addTagRequest:
			return addTag(msg.getOpCode(), (Tag) msg.getEntity());

		case deleteTagRequest:
			return deleteTag(msg.getOpCode(), (Tag) msg.getEntity());

		case jobLifeCycleRequest:
			return getEventList(msg.getOpCode(), (Assignment) msg.getEntity());

		case addEventRequest:
			return addEvent(msg.getOpCode(), (AddEventEntity) msg.getEntity());

		case updateEventRequest:
			return updateEvent(msg.getOpCode(), (Event) msg.getEntity());

		case deleteEventRequest:
			return deleteEvent(msg.getOpCode(), (Event) msg.getEntity());

		case addNewJobRequest:
			return addJob(msg.getOpCode(), (Job) msg.getEntity());

		case myJobsListRequest:
			return getMyJobsList(msg.getOpCode(), (User) msg.getEntity());

		case publicJobDeleteRequest:
			return deleteJob(msg.getOpCode(), (Job) msg.getEntity());

		case publicJobUpdateRequest:
			return updateJob(msg.getOpCode(), (Job) msg.getEntity());

		case suggestJobRequest:
			return suggestJob(msg.getOpCode(),
					(SuggestJobEntity) msg.getEntity());

		case applyForJobRequest:
			return applyForJob(msg.getOpCode(), (Job) msg.getEntity());

		case searchForJobRequest:
			return searchForJobs(msg.getOpCode(),
					(SearchEntity) msg.getEntity());

		case searchForPostRequest:
			return searchForPosts(msg.getOpCode(),
					(SearchEntity) msg.getEntity());

		case listOfNotificationsRequest:
			return getListOfNotifications(msg.getOpCode(),
					(Notification) msg.getEntity());

		case markAsReadRequest:
			return markNotificationAsRead(msg.getOpCode(),
					(Notification) msg.getEntity());
		
		case acceptSuggestedJobRequest:
			return HandleAcceptSuggestedJob(msg.getOpCode(),
					(Event) msg.getEntity());

		case rejectSuggestedJobRequest:
			return HandleRejectSuggestedJob(msg.getOpCode(),
					(Assignment) msg.getEntity());

		case closeMyJobLifeCycleRequest:
			return HandleCloseMyJobLifeCycle(msg.getOpCode(),
					(CloseJobEvent) msg.getEntity());

		case singleNotificationEntityRequest:
			return handleSingleNotification(msg.getOpCode(),(SingleNotificationEntity) msg.getEntity());
			
			
			
		default:
			return null;
		}
	}

	private HandleRequestStatus handleSingleNotification(RequestCodeEnum OpCode, SingleNotificationEntity singleNotificationEntity){
		
		HandleRequestStatus HRS = new HandleRequestStatus();
		Entity resultEntity=null;
		
		ArrayList<Long> resultCaseForSwitch=new ArrayList<Long>();
		switch ((int)singleNotificationEntity.getNotificationTypeId()){
		case 1://comment
			PostDatabase PDB=new PostDatabase();	
			CommentDatabase CDB=new CommentDatabase();
			NotificationDatabase NDB=new NotificationDatabase();
			try {	
				NDB.markNotificationAsRead(singleNotificationEntity.getNotificationId());
				Comment comment=CDB.getCommentByIdFromDB(singleNotificationEntity.getEntityId());
				resultEntity=PDB.getPostByIdFromDB(comment.getPostId());
				HRS.setStatus("Success");
				HRS.setErrorMsg(null);

			} catch (IllegalOperation e) {
				HRS.setStatus("Fail");
				HRS.setErrorMsg(e.getStackTraceMessage());
			}
			break;
		case 2://event
			JobDatabase JDB=new JobDatabase();	
			EventDatabase EDB=new EventDatabase();
			MyJob myJob=new MyJob();
			try {	
				Event event=EDB.getEventByIdFromDB(singleNotificationEntity.getEntityId());
				myJob.setJob(JDB.getJobByIdFromDB(event.getJobId()));
				myJob.setAss(JDB.getAssignmentByUserAndJobFromDB(event.getUserId(),event.getJobId()));
				resultEntity=myJob;
				HRS.setStatus("Success");
				HRS.setErrorMsg(null);
			} catch (IllegalOperation e) {
				HRS.setStatus("Fail");
				HRS.setErrorMsg(e.getStackTraceMessage());
			}
			break;
		case 3://assignment
			JobDatabase JDB2=new JobDatabase();
			MyJob myJob2=new MyJob();
			try {
				myJob2.setAss(JDB2.getAssignmentByIdFromDB(singleNotificationEntity.getEntityId()));
				myJob2.setJob(JDB2.getJobByIdFromDB(myJob2.getAss().getJobId()));
				resultEntity=myJob2;
				HRS.setStatus("Success");
				HRS.setErrorMsg(null);
			} catch (IllegalOperation e) {
				HRS.setStatus("Fail");
				HRS.setErrorMsg(e.getStackTraceMessage());
			}
			break;
		default:
			return null;
		}
		
		HRS.setOpCode(OpCode);
		HRS.setEntity(resultEntity);
		resultCaseForSwitch.add(singleNotificationEntity.getNotificationTypeId());
		HRS.setEntityArr(resultCaseForSwitch);
		
		return HRS;
	}
	private HandleRequestStatus HandleCloseMyJobLifeCycle(
			RequestCodeEnum OpCode, CloseJobEvent CloseJobEvent) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		JobDatabase JDB = new JobDatabase();
		Event resultEvent = null;
		try {
			resultEvent=JDB.CloseMyJobCycle(CloseJobEvent);
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);

		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(resultEvent);
		HRS.setEntityArr(null);
		return HRS;
	}

	private HandleRequestStatus HandleRejectSuggestedJob(
			RequestCodeEnum OpCode, Assignment assignment) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		JobDatabase JDB = new JobDatabase();
		Assignment deletedAssignment = new Assignment();
		Long deletedJobId = (long) 0;
		try {
			deletedJobId = JDB.deleteAssignmentbyUserIdJobId(assignment.getUserId(), assignment.getJobId());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		deletedAssignment.setJobId(deletedJobId);
		deletedAssignment.setUserId(assignment.getUserId());
		HRS.setEntity(deletedAssignment);
		HRS.setEntityArr(null);
		return HRS;

	}

	private HandleRequestStatus HandleAcceptSuggestedJob(
			RequestCodeEnum OpCode, Event event) {
		
		HandleRequestStatus HRS = new HandleRequestStatus();
		JobDatabase JDB = new JobDatabase();
		EventDatabase EDB = new EventDatabase();
	
		Event resultEvent = null;
		
		try {
			JDB.updateAssignmentStatus(event.getUserId(), event.getJobId(), 2L);
			resultEvent = EDB.addEventToDB(event,null);
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);

		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(resultEvent);
		HRS.setEntityArr(null);
		
		return HRS;
	}

	private HandleRequestStatus getListOfNotifications(RequestCodeEnum OpCode,
			Notification notification) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		NotificationDatabase NDB = new NotificationDatabase();
		ArrayList notificationList = null;
		try {
			notificationList = NDB
					.getNotificationList(notification.getUserId());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(null);
		HRS.setEntityArr(notificationList);

		return HRS;
	}

	private HandleRequestStatus markNotificationAsRead(RequestCodeEnum OpCode,
			Notification notification) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		NotificationDatabase NDB = new NotificationDatabase();

		try {
			NDB.markNotificationAsRead(notification.getNotificationId());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(null);
		HRS.setEntityArr(null);

		return HRS;
	}

	private HandleRequestStatus searchForPosts(RequestCodeEnum OpCode,
			SearchEntity SE) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		PostDatabase PDB = new PostDatabase();
		ArrayList postList = null;
		try {
			postList = PDB.SearchPostsByTags(SE.getTagsIds(),
					SE.getNumberOfItems(), SE.getOffset(), SE.getUserId(),
					SE.isForward());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(null);
		HRS.setEntityArr(postList);
		return HRS;
	}

	private HandleRequestStatus searchForJobs(RequestCodeEnum OpCode,
			SearchEntity SE) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		JobDatabase JDB = new JobDatabase();
		ArrayList jobList = null;
		try {
			jobList = JDB.SearchJobsByTags(SE.getTagsIds(),
					SE.getNumberOfItems(), SE.getOffset(), SE.getUserId(),
					SE.isForward());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(null);
		HRS.setEntityArr(jobList);
		return HRS;
	}

	private HandleRequestStatus applyForJob(RequestCodeEnum OpCode, Job job) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		JobDatabase JDB = new JobDatabase();
		
		
		MyJob myJob=new MyJob();
		try {
			myJob=JDB.applyToJob(job.getSubmitterUserId(), job.getJobId());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(myJob);
		HRS.setEntityArr(null);
		
		return HRS;
	}
	
	public static void main(String args[]){
	/*	SuggestJobEntity SJE=new SuggestJobEntity();
		SJE.setJobId(jobId);
		SJE.setJobseekersList(jobseekersList);
		SJE.setUserId(userId);
		*/
		
	} 
	
	private HandleRequestStatus suggestJob(RequestCodeEnum OpCode,
			SuggestJobEntity SJE) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		JobDatabase JDB = new JobDatabase();
		try {
			JDB.suggestJobToJobSeekers(SJE.getJobId(), SJE.getUserId(),(ArrayList<Long>)SJE.getJobseekersList());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		Job job=new Job();
		job.setJobId(SJE.getJobId());
		job.setSubmitterUserId(SJE.getUserId());
		HRS.setEntity(job);
		HRS.setEntityArr(null);
		return HRS;
	}

	private HandleRequestStatus deleteEvent(RequestCodeEnum OpCode, Event event) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		EventDatabase EDB = new EventDatabase();
		try {
			EDB.deleteEventFromDB(event.getEventId(),event.getUserId());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(event);
		HRS.setEntityArr(null);
		return HRS;
	}

	private HandleRequestStatus updateEvent(RequestCodeEnum OpCode, Event event) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		EventDatabase EDB = new EventDatabase();
		Event resultevent = null;
		try {
			resultevent = EDB.updateEventFromDB(event);
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(resultevent);
		HRS.setEntityArr(null);
		return HRS;
	}

	private HandleRequestStatus addEvent(RequestCodeEnum OpCode, AddEventEntity addEventEntity) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		EventDatabase EDB = new EventDatabase();
		Event resultevent = null;
		try {
			resultevent = EDB.addEventToDB(addEventEntity.getEvent(),addEventEntity.getNotificationAlertTime());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(resultevent);
		HRS.setEntityArr(null);
		return HRS;
	}

	
	private HandleRequestStatus addJob(RequestCodeEnum OpCode, Job job) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		JobDatabase JDB = new JobDatabase();
		Job resultJob = null;
		try {
			resultJob = JDB.addJobToDB(job);
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(resultJob);
		HRS.setEntityArr(null);
		return HRS;
	}


	private HandleRequestStatus updateJob(RequestCodeEnum OpCode, Job job) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		JobDatabase JDB = new JobDatabase();
		Job resultJob = null;
		try {

			resultJob = JDB.updateJob(job);
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(resultJob);
		HRS.setEntityArr(null);
		return HRS;
	}

	private HandleRequestStatus deleteJob(RequestCodeEnum OpCode, Job job) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		JobDatabase JDB = new JobDatabase();
		try {
			JDB.deleteJobFromDB(job.getJobId(), job.getSubmitterUserId());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(job);
		HRS.setEntityArr(null);
		return HRS;
	}

	private HandleRequestStatus getEventList(RequestCodeEnum OpCode,
			Assignment ass) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		EventDatabase EDB = new EventDatabase();
		ArrayList<Event> resultEventList = null;
		try {
			resultEventList = EDB.getListOfEventsByJobId(ass.getJobId(),ass.getUserId());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(null);
		HRS.setEntityArr(resultEventList);

		return HRS;
	}

	private HandleRequestStatus getMyJobsList(RequestCodeEnum OpCode, User user) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		JobDatabase JDB = new JobDatabase();
		ArrayList<MyJob> resultJobList = null;
		try {
			resultJobList = JDB.getJobsListByUserFromDB(user.getUserId());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(null);
		HRS.setEntityArr(resultJobList);

		return HRS;
	}

	private HandleRequestStatus deleteTag(RequestCodeEnum OpCode, Tag tag) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		TagDatabase TDB = new TagDatabase();
		try {
			TDB.deleteTagFromDB(tag.getTagId());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(null);
		HRS.setEntityArr(null);
		return HRS;
	}

	private HandleRequestStatus addTag(RequestCodeEnum OpCode, Tag tag) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		TagDatabase TDB = new TagDatabase();
		Tag resultTag = null;
		try {
			resultTag = TDB.addTagToDataBase(tag);
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(resultTag);
		HRS.setEntityArr(null);
		return HRS;
	}

	private HandleRequestStatus validateJobSeeker(RequestCodeEnum OpCode,
			User user) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		UserDatabase UDB = new UserDatabase();
		ValidateUserEntity resultValidateUser = null;
		try {
			resultValidateUser = UDB.validateUser(user);
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(resultValidateUser);
		HRS.setEntityArr(null);

		return HRS;

	}

	private HandleRequestStatus addAllowedUser(RequestCodeEnum OpCode, User user) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		UserDatabase UDB = new UserDatabase();
		User resultUser = null;
		try {
			resultUser = UDB.addAllowedUserToDB(user);
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(resultUser);
		HRS.setEntityArr(null);

		return HRS;
	}

	private HandleRequestStatus getCommentList(RequestCodeEnum OpCode,
			Comment comment) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		CommentDatabase CDB = new CommentDatabase();
		ArrayList<Comment> resultCommentList = null;
		try {
			resultCommentList = CDB.getCommentsByPostIdFromDB(
					comment.getPostId(), comment.getUserId());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(null);
		HRS.setEntityArr(resultCommentList);

		return HRS;
	}

	private HandleRequestStatus deleteComment(RequestCodeEnum OpCode,
			Comment comment) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		CommentDatabase CDB = new CommentDatabase();
		Comment resultComment = null;

		try {
			resultComment = CDB.deleteCommentFromDB(comment.getCommentId(),
					comment.getUserId());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(resultComment);
		HRS.setEntityArr(null);

		return HRS;
	}

	private HandleRequestStatus addComment(RequestCodeEnum OpCode,
			Comment comment) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		CommentDatabase CDB = new CommentDatabase();
		Comment resultComment = null;

		try {
			resultComment = CDB.addCommentToDB(comment);
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(resultComment);
		HRS.setEntityArr(null);

		return HRS;
	}

	private HandleRequestStatus initialize(RequestCodeEnum OpCode, User user) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		InitializeDatabase IDB = new InitializeDatabase();

		InitializeEntity resultIE = null;

		try {
			resultIE = IDB.Initialize(user.getUserId());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setOpCode(OpCode);
		HRS.setEntity(resultIE);
		HRS.setEntityArr(null);
		return HRS;
	}

	private HandleRequestStatus addPost(RequestCodeEnum OpCode, Post post) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		PostDatabase PDB = new PostDatabase();
		Post resultPost = null;

		try {
			resultPost = PDB.addPostToDB(post);
			if (resultPost != null) {
				HRS.setStatus("Success");
				HRS.setErrorMsg(null);
			}
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}

		HRS.setOpCode(OpCode);
		HRS.setEntity(resultPost);
		HRS.setEntityArr(null);
		return HRS;
	}

	private HandleRequestStatus deletePost(RequestCodeEnum OpCode, Post post) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		PostDatabase PDB = new PostDatabase();
		Post resultPost = null;

		try {
			resultPost = PDB.deletePostFromDB(post.getPostId(),
					post.getUserId());
			if (resultPost != null) {
				HRS.setStatus("Success");
				HRS.setErrorMsg(null);
			}
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}

		HRS.setOpCode(OpCode);
		HRS.setEntity(resultPost);
		HRS.setEntityArr(null);

		return HRS;
	}

	private HandleRequestStatus getListOfPosts(RequestCodeEnum OpCode,
			NewsFeedEntity newsFeedEntity) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		PostDatabase PDB = new PostDatabase();
		ArrayList<Post> ResultPostsList = null;
		boolean forward;
		
		if (newsFeedEntity.getDir().equals("forward"))
			forward = true;
		else
			forward = false;
		
		try {
			ResultPostsList = PDB.getNewsfeedFromDB(
					newsFeedEntity.getNewsFeedLength(),
					newsFeedEntity.getUserId(), newsFeedEntity.getCurrentId(),
					forward);
			newsFeedEntity.setNewestPostId(PDB.getNewestIdInTable("posts",
					"postId"));

			newsFeedEntity.setNewsFeedLength(PDB.getNumberOfPosts());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
			HRS.setEntity(newsFeedEntity);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTrace().toString());
			HRS.setEntity(null);
		}
		HRS.setOpCode(OpCode);
		HRS.setEntityArr(ResultPostsList);

		return HRS;
	}

	private HandleRequestStatus getPostById(RequestCodeEnum OpCode, Post post) {

		HandleRequestStatus HRS = new HandleRequestStatus();
		PostDatabase PDB = new PostDatabase();
		Post resultPost = null;

		try {
			resultPost = PDB.getPostByIdFromDB(post.getPostId());
			HRS.setStatus("Success");
			HRS.setErrorMsg(null);
		} catch (IllegalOperation e) {
			HRS.setStatus("Fail");
			HRS.setErrorMsg(e.getStackTraceMessage());
		}
		HRS.setEntity(resultPost);
		HRS.setOpCode(OpCode);
		HRS.setEntity(resultPost);
		HRS.setEntityArr(null);

		return HRS;
	}
}
