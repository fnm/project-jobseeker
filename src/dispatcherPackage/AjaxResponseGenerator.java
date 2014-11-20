package dispatcherPackage;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import entitiesPackage.*;

public class AjaxResponseGenerator {

	public JSONObject HandleResponse(HandleRequestStatus HRS){
	
		switch (HRS.getOpCode()) {
		
		case validateJobSeekerRequest:
			return validateJobSeekerResponse(HRS);
			
		case addPostRequest:
			return addPostResponse(HRS);
			
		case deletePostRequest:
			return deletePostResponse(HRS);
			
		case viewNewsFeedRequest:
			return viewNewsFeedResponse(HRS);
			
		case singlePostRequest:
			return singlePostResponse(HRS);
			
		case initializeRequest:
			return initializeResponse(HRS);
			
		case addCommentRequest:
			return addCommentResponse(HRS);
			
		case deleteCommentRequest:
			return deleteCommentResponse(HRS);
			
		case commentListRequest:
			return commentListResponse(HRS);
			
		case addAllowedUsersRequest:
			return addAllowedUserResponse(HRS);
			
		case addTagRequest:
			return addTagResponse(HRS);
			
		case deleteTagRequest:
			return deleteTagResponse(HRS);
			
		case jobLifeCycleRequest:
			return jobLifeCycleResponse(HRS);
			
		case searchForJobRequest:
			return searchForJobResponse(HRS);
			
		case searchForPostRequest:
			return searchForPostResponse(HRS);
			
		case addNewJobRequest:
			return addNewJobResponse(HRS);
			
		case myJobsListRequest:
			return myJobsListResponse(HRS);
			
		case publicJobDeleteRequest:
			return publicJobDeleteResponse(HRS);
			
		case publicJobUpdateRequest:
			return publicJobUpdateResponse(HRS);
			
		case suggestJobRequest:
			return suggestJobResponse(HRS);
			
		case applyForJobRequest:
			return applyForJobResponse(HRS);
			
		case addEventRequest:
			return addEventResponse(HRS);
			
		case deleteEventRequest:
			return deleteEventResponse(HRS);
			
		case updateEventRequest:
			return updateEventResponse(HRS);
			
		case listOfNotificationsRequest:
			return notificationListResponse(HRS);
			
		case markAsReadRequest:
			return markAsReadResponse(HRS);
			
		case acceptSuggestedJobRequest:
			return acceptSuggestedJobResponse(HRS);
			
		case rejectSuggestedJobRequest:
			return rejectSuggestedJobResponse(HRS);
			
		case closeMyJobLifeCycleRequest:
			return closeMyJobLifeCycleResponse(HRS);
			
		case singleNotificationEntityRequest:
			return singleNotificationEntityResponse(HRS);
			
		default:
			return null;
		}
	}

	
	
	public JSONObject rejectSuggestedJobResponse(HandleRequestStatus HRS){
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "rejectSuggestedJobResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());
		
		Assignment ass = (Assignment)HRS.getEntity();
		jsonObj.put("jobId", ass.getJobId());
		jsonObj.put("userId",ass.getUserId());
		
		return jsonObj;
	}
	
	public JSONObject singleNotificationEntityResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "singleNotificationEntityResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());
		
		ArrayList<Long> TypeIdForSwitch=(ArrayList<Long>)HRS.getEntityArr();
		long SwitchCaseInt=(Long)TypeIdForSwitch.get(0);
		switch((int)SwitchCaseInt)
		{
				case 1: Post post = (Post)HRS.getEntity();
				JSONObject jsonPost = new JSONObject();
				jsonPost.put("postId", post.getPostId());
				jsonPost.put("userId", post.getUserId());
				jsonPost.put("title", post.getTitle());
				jsonPost.put("content", post.getContent());
				jsonPost.put("tags", post.getTags());
				jsonPost.put("publishDate", post.getPublishDate().toString());
				jsonPost.put("firstName", post.getFirstName());
				jsonPost.put("lastName", post.getLastName());
				jsonPost.put("pictureUrl", post.getPictureUrl());				
				jsonObj.put("post", jsonPost);
				break;
				
		case 2:
		case 3: MyJob myJob = (MyJob)HRS.getEntity();
				JSONObject jsonJob2 = new JSONObject();
				jsonJob2.put("jobId", myJob.getJob().getJobId());
				jsonJob2.put("postId", myJob.getJob().getPostId());
				jsonJob2.put("submitterUserId", myJob.getJob().getSubmitterUserId());
				jsonJob2.put("jobTitle", myJob.getJob().getJobTitle());
				jsonJob2.put("companyName", myJob.getJob().getCompanyName());
				jsonJob2.put("address", myJob.getJob().getAddress());
				jsonJob2.put("contact", myJob.getJob().getContact());
				jsonJob2.put("description", myJob.getJob().getDescription());
				jsonJob2.put("isPrivate", myJob.getJob().getIsPrivate());
				jsonJob2.put("isActive", myJob.getJob().getIsActive());
				jsonJob2.put("tags", myJob.getJob().getTags());
				jsonJob2.put("publishDate", myJob.getJob().getPublishDate().toString());
				
				JSONObject jsonAssignment = new JSONObject();
				jsonAssignment.put("assignmentId", myJob.getAss().getAssignmentId());
				jsonAssignment.put("userId", myJob.getAss().getUserId());
				jsonAssignment.put("jobId", myJob.getAss().getJobId());
				jsonAssignment.put("status", myJob.getAss().getStatus());
				jsonAssignment.put("isPrivate", myJob.getAss().isPrivate());
				
				jsonObj.put("job", jsonJob2);
				jsonObj.put("assignment", jsonAssignment);
				break;
		default:
				jsonObj.put("entity", null);
		}
		
		return jsonObj;
	}
	
	
	public JSONObject closeMyJobLifeCycleResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "closeMyJobLifeCycleResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());
		
		return jsonObj;
	}

	public JSONObject acceptSuggestedJobResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "acceptSuggestedJobResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());
		
	
		
		
		return jsonObj;
	}

	public JSONObject markAsReadResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "markAsReadResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		return jsonObj;
	}

	public JSONObject notificationListResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "notificationListResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		ArrayList<Notification> notificationList = (ArrayList<Notification>) HRS.getEntityArr();
		JSONArray jsonArr = new JSONArray();
		
		if (notificationList != null) {
			for (int i = 0; i < notificationList.size(); i++) {
				Notification notification = notificationList.get(i);
				JSONObject notificationObj = new JSONObject();
				notificationObj.put("notificationId",notification.getNotificationId());
				notificationObj.put("notificationTypeId",notification.getNotificationTypeId());
				notificationObj.put("notificationForeignId", notification.getNotificationForeignId());
				notificationObj.put("typeId", notification.getTypeId());
				notificationObj.put("userId", notification.getUserId());
				notificationObj.put("isRead", notification.isRead());
				notificationObj.put("text", notification.getText());
				notificationObj.put("due", notification.getDue().toString());
				jsonArr.add(notificationObj);
			}
			jsonObj.put("notificationList", jsonArr);
		} else {
			jsonObj.put("notificationList", null);
		}

		return jsonObj;
	}

	public JSONObject updateEventResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "updateEventResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		Event event = (Event) HRS.getEntity();
		jsonObj.put("jobId", event.getEventId());
		jsonObj.put("userId", event.getUserId());

		return jsonObj;
	}

	public JSONObject deleteEventResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "deleteEventResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		Event event = (Event) HRS.getEntity();
		jsonObj.put("jobId", event.getEventId());
		jsonObj.put("userId", event.getUserId());

		return jsonObj;
	}

	public JSONObject addEventResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "addEventResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		Event event = (Event) HRS.getEntity();
		jsonObj.put("jobId", event.getEventId());
		jsonObj.put("userId", event.getUserId());

		return jsonObj;
	}

	public JSONObject applyForJobResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "applyForJobResponse");
		jsonObj.put("status", HRS.getStatus());
		

		MyJob myJob=(MyJob)HRS.getEntity();
		if(myJob!=null){
			jsonObj.put("errorMessage", HRS.getErrorMsg());	
			
		JSONObject jsonJob = new JSONObject();
			jsonJob.put("jobId", myJob.getJob().getJobId());
			jsonJob.put("postId", myJob.getJob().getPostId());
			jsonJob.put("submitterUserId", myJob.getJob().getSubmitterUserId());
			jsonJob.put("jobTitle", myJob.getJob().getJobTitle());
			jsonJob.put("companyName", myJob.getJob().getCompanyName());
			jsonJob.put("address", myJob.getJob().getAddress());
			jsonJob.put("contact", myJob.getJob().getContact());
			jsonJob.put("description", myJob.getJob().getDescription());
			jsonJob.put("isPrivate", myJob.getJob().getIsPrivate());
			jsonJob.put("isActive", myJob.getJob().getIsActive());
			jsonJob.put("tags", myJob.getJob().getTags());
			jsonJob.put("publishDate", myJob.getJob().getPublishDate().toString());
			
		JSONObject jsonAssignment = new JSONObject();
			jsonAssignment.put("assignmentId", myJob.getAss().getAssignmentId());
			jsonAssignment.put("userId", myJob.getAss().getUserId());
			jsonAssignment.put("jobId", myJob.getAss().getJobId());
			jsonAssignment.put("status", myJob.getAss().getStatus());
			jsonAssignment.put("isPrivate", myJob.getAss().isPrivate());
			
			jsonObj.put("job", jsonJob);
			jsonObj.put("assignment", jsonAssignment);
		}
		else{
			jsonObj.put("errorMessage", "Already applied");
			jsonObj.put("job", null);
			jsonObj.put("assignment",null);
		}
		return jsonObj;
	}

	public JSONObject suggestJobResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "suggestJobResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		Job job = (Job) HRS.getEntity();
		jsonObj.put("jobId", job.getJobId());
		jsonObj.put("userId", job.getSubmitterUserId());

		return jsonObj;
	}

	public JSONObject publicJobUpdateResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "publicJobUpdateResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		Job job = (Job) HRS.getEntity();
		jsonObj.put("jobId", job.getJobId());
		jsonObj.put("userId", job.getSubmitterUserId());

		return jsonObj;
	}

	public JSONObject publicJobDeleteResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "publicJobDeleteResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		Job job = (Job) HRS.getEntity();
		jsonObj.put("jobId", job.getJobId());
		jsonObj.put("userId", job.getSubmitterUserId());

		return jsonObj;
	}

	public JSONObject myJobsDeleteJobResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "myJobsDeleteJobResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		Job job = (Job) HRS.getEntity();
		jsonObj.put("jobId", job.getJobId());
		jsonObj.put("userId", job.getSubmitterUserId());

		return jsonObj;
	}

	// TODO
	public JSONObject myJobsListResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "myJobsListResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		ArrayList<MyJob> jobList = (ArrayList<MyJob>) HRS.getEntityArr();
		JSONArray jsonArr = new JSONArray();
		
		if (jobList != null) {
			for (int i = 0; i < jobList.size(); i++) {
				MyJob myJob = jobList.get(i);
				JSONObject jobObj = new JSONObject();
				jobObj.put("jobId", myJob.getJob().getJobId());
				jobObj.put("submitterUserId", myJob.getJob().getSubmitterUserId());
				jobObj.put("jobTitle", myJob.getJob().getJobTitle());
				jobObj.put("companyName", myJob.getJob().getCompanyName());
				jobObj.put("address", myJob.getJob().getAddress());
				jobObj.put("contact", myJob.getJob().getContact());
				jobObj.put("description", myJob.getJob().getDescription());
				jobObj.put("isPrivate", myJob.getJob().getIsPrivate());
				jobObj.put("isActive", myJob.getJob().getIsActive());
				jobObj.put("publishDate", myJob.getJob().getPublishDate().toString());
				jobObj.put("status", myJob.getAss().getStatus().toString());

				jsonArr.add(jobObj);
			}
			jsonObj.put("jobs", jsonArr);
		} else
			jsonObj.put("jobs", null);

		return jsonObj;
	}

	public JSONObject addNewJobResponse(HandleRequestStatus HRS) {
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "addNewJobResponse");

		Job job = (Job) HRS.getEntity();

		jsonObj.put("jobId", job.getJobId());
		jsonObj.put("userId", job.getSubmitterUserId());

		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		return jsonObj;
	}

	public JSONObject searchForPostResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "searchForPostResponse");

		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		ArrayList<Post> postList = (ArrayList<Post>) HRS.getEntityArr();
		JSONArray jsonArr = new JSONArray();
		if (postList != null) {
			for (int i = 0; i < postList.size(); i++) {
				Post post = postList.get(i);
				JSONObject postObj = new JSONObject();
				postObj.put("postId", post.getPostId());
				postObj.put("userId", post.getUserId());
				postObj.put("title", post.getTitle());
				postObj.put("content", post.getContent());
				postObj.put("publishDate", post.getPublishDate().toString());
				postObj.put("firstName", post.getFirstName());
				postObj.put("lastName", post.getLastName());
				postObj.put("pictureUrl", post.getPictureUrl());
				
				
				jsonArr.add(postObj);
			}
			jsonObj.put("posts", jsonArr);
		} else {
			jsonObj.put("posts", null);
		}

		return jsonObj;
	}

	public JSONObject searchForJobResponse(HandleRequestStatus HRS){

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "searchForJobResponse");

		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		ArrayList<Job> jobList = (ArrayList<Job>) HRS.getEntityArr();
		JSONArray jsonArr = new JSONArray();
		if (jobList != null) {
			for (int i = 0; i < jobList.size(); i++) {
				Job job = jobList.get(i);
				JSONObject jobObj = new JSONObject();
				jobObj.put("jobId", job.getJobId());
				jobObj.put("submitterUserId", job.getSubmitterUserId());
				jobObj.put("jobTitle", job.getJobTitle());
				jobObj.put("companyName", job.getCompanyName());
				jobObj.put("address", job.getAddress());
				jobObj.put("contact", job.getContact());
				jobObj.put("description", job.getDescription());
				jobObj.put("isPrivate", job.getIsPrivate());
				jobObj.put("isActive", job.getIsActive());
				jobObj.put("publishDate", job.getPublishDate().toString());
				jsonArr.add(jobObj);
			}
			jsonObj.put("jobs", jsonArr);
		} else
			jsonObj.put("jobs", null);

		return jsonObj;
	}

	public JSONObject jobLifeCycleResponse(HandleRequestStatus HRS){

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "jobLifeCycleResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		ArrayList<Event> eventList = (ArrayList<Event>) HRS.getEntityArr();
		JSONArray jsonArr = new JSONArray();
		if (eventList != null) {
			for (int i = 0; i < eventList.size(); i++) {
				Event event = eventList.get(i);
				JSONObject eventObj = new JSONObject();
				eventObj.put("eventId", event.getEventId());
				eventObj.put("userId", event.getUserId());
				eventObj.put("jobId", event.getJobId());
				eventObj.put("title", event.getTitle());
				eventObj.put("description", event.getDescription());
				eventObj.put("notes", event.getNotes());
				eventObj.put("address", event.getAddress());
				eventObj.put("due", event.getDue().toString());
				eventObj.put("publishDate", event.getPublishDate().toString());
				jsonArr.add(eventObj);
			}
			jsonObj.put("events", jsonArr);
		} else
			jsonObj.put("events", null);
		return jsonObj;
	}

	public JSONObject addPostResponse(HandleRequestStatus HRS){

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "addPostResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		Post post = (Post) HRS.getEntity();
		jsonObj.put("postId", post.getPostId());
		jsonObj.put("userId", post.getUserId());

		return jsonObj;
	}

	public JSONObject deletePostResponse(HandleRequestStatus HRS){

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "deletePostResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		Post post = (Post) HRS.getEntity();
		jsonObj.put("postId", post.getPostId());
		jsonObj.put("userId", post.getUserId());

		return jsonObj;
	}

	public JSONObject viewNewsFeedResponse(HandleRequestStatus HRS){

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "viewNewsFeedResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		ArrayList<Post> postList = (ArrayList<Post>) HRS.getEntityArr();
		JSONArray jsonArr = new JSONArray();
		if (postList != null) {
			for (int i = 0; i < postList.size(); i++) {
				Post post = postList.get(i);
				JSONObject postObj = new JSONObject();
				postObj.put("postId", post.getPostId());
				postObj.put("userId", post.getUserId());
				postObj.put("title", post.getTitle());
				postObj.put("content", post.getContent());
				postObj.put("publishDate", post.getPublishDate().toString());
				postObj.put("firstName", post.getFirstName());
				postObj.put("lastName", post.getLastName());
				postObj.put("pictureUrl", post.getPictureUrl());
				postObj.put("text",
						post.getFirstName() + " " + post.getLastName());

				jsonArr.add(postObj);
			}
			jsonObj.put("posts", jsonArr);
		} else {
			jsonObj.put("posts", null);
		}

		NewsFeedEntity NFE = (NewsFeedEntity) HRS.getEntity();
		jsonObj.put("newestPostId", NFE.getNewestPostId());
		jsonObj.put("numberOfPostsInDB", NFE.getNewsFeedLength());

		return jsonObj;
	}

	public JSONObject singlePostResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "getPostByIdResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		Post post = (Post) HRS.getEntity();
		jsonObj.put("postId", post.getPostId());
		jsonObj.put("userId", post.getUserId());
		jsonObj.put("title", post.getTitle());
		jsonObj.put("content", post.getContent());
		jsonObj.put("publishDate", post.getPublishDate());
		
		return jsonObj;
	}

	public JSONObject initializeResponse(HandleRequestStatus HRS) {
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "initializeResponse");

		InitializeEntity IE = (InitializeEntity) HRS.getEntity();

		JSONArray jsonArr = new JSONArray();
		jsonObj.put("currentCounterforPost", IE.getCurrentCounterforPost());
		jsonObj.put("numberOfPosts", IE.getNumberOfPosts());

		ArrayList<Tag> tagList = (ArrayList<Tag>) IE.getTags();
		if (tagList != null) {
			for (int i = 0; i < tagList.size(); i++) {
				Tag tag = tagList.get(i);
				JSONObject tagObj = new JSONObject();
				tagObj.put("tagId", tag.getTagId());
				tagObj.put("text", tag.getTagName());
				jsonArr.add(tagObj);
			}
			jsonObj.put("tags", jsonArr);
		} else
			jsonObj.put("tags", null);

		JSONArray jsonArr2 = new JSONArray();
		ArrayList<User> users = (ArrayList<User>) IE.getUsers();
		if (users != null) {
			for (int i = 0; i < users.size(); i++) {
				User user = users.get(i);
				JSONObject userObj = new JSONObject();

				userObj.put("userId", user.getUserId());
				userObj.put("userTypeId", user.getUserTypeId());
				userObj.put("firstName", user.getFirstName());
				userObj.put("lastName", user.getLastName());
				userObj.put("text",user.getFirstName() + " " + user.getLastName());
				userObj.put("email", user.getEmail());
				userObj.put("status", user.isStatus());
				userObj.put("pictureUrl", user.getPictureUrl());
				userObj.put("profileUrl", user.getProfileUrl());
				userObj.put("lastLogin", user.getLastLogin().toString());

				jsonArr2.add(userObj);
			}
			jsonObj.put("userList", jsonArr2);
		} else
			jsonObj.put("userList", null);

		JSONArray jsonArr3 = new JSONArray();
		ArrayList<UserTypes> userTypes = (ArrayList<UserTypes>) IE.getUserTypes();
		
		if (userTypes != null) {
			for (int i = 0; i < userTypes.size(); i++) {
				UserTypes userType = userTypes.get(i);
				JSONObject userTypeObj = new JSONObject();

				userTypeObj.put("userTypeId", userType.getUserTypeId());
				userTypeObj.put("userTypeName", userType.getUserTypeName());

				jsonArr3.add(userTypeObj);
			}
			jsonObj.put("userTypes", jsonArr3);
		} else
			jsonObj.put("userType", null);

		return jsonObj;

	}

	public JSONObject addCommentResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "addCommentResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		Comment comment = (Comment) HRS.getEntity();
		jsonObj.put("commentId", comment.getCommentId());
		jsonObj.put("userId", comment.getUserId());

		return jsonObj;
	}

	public JSONObject deleteCommentResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "deleteCommentResponse");

		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		Comment comment = (Comment) HRS.getEntity();
		jsonObj.put("commentId", comment.getCommentId());

		return jsonObj;
	}

	public JSONObject commentListResponse(HandleRequestStatus HRS){

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "commentListResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		ArrayList<Comment> commentList = (ArrayList<Comment>) HRS.getEntityArr();
		JSONArray jsonArr = new JSONArray();
		
		if (commentList != null) {
			for (int i = 0; i < commentList.size(); i++) {
				Comment comment = commentList.get(i);
				JSONObject commentObj = new JSONObject();
				commentObj.put("postId", comment.getPostId());
				commentObj.put("commentId", comment.getCommentId());
				commentObj.put("userId", comment.getUserId());
				commentObj.put("content", comment.getContent());
				commentObj.put("publishDate", comment.getPublishDate()
						.toString());
				jsonArr.add(commentObj);
			}
			jsonObj.put("comments", jsonArr);
		} else
			jsonObj.put("comments", null);

		return jsonObj;
	}

	public JSONObject addAllowedUserResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "addAllowedUserResponse");
		jsonObj.put("status", HRS.getStatus());

		User user = (User) HRS.getEntity();
		jsonObj.put("userType", user.getUserTypeId());
		jsonObj.put("userId", user.getUserId());

		return jsonObj;
	}

	public JSONObject validateJobSeekerResponse(HandleRequestStatus HRS) {

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "validateJobSeekerResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		ValidateUserEntity VUE = (ValidateUserEntity) HRS.getEntity();
		if (!VUE.getStatus().equals("UnauthorizedUser")) {
			jsonObj.put("userId", VUE.getUser().getUserId());
			jsonObj.put("userType", VUE.getUser().getUserTypeId());
		}
		jsonObj.put("userStatus", VUE.getStatus());
		
		return jsonObj;
	}

	public JSONObject addTagResponse(HandleRequestStatus HRS) {
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "addTagResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		Tag tag = (Tag) HRS.getEntity();
		jsonObj.put("text", tag.getTagName());
		jsonObj.put("tagId", tag.getTagId());

		return jsonObj;

	}

	public JSONObject deleteTagResponse(HandleRequestStatus HRS) {
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("opcode", "deleteTagResponse");
		jsonObj.put("status", HRS.getStatus());
		jsonObj.put("errorMessage", HRS.getErrorMsg());

		return jsonObj;

	}

}
