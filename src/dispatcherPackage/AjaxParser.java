package dispatcherPackage;

import java.io.BufferedReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import entitiesPackage.*;

// This is a singleton that parses the request
public class AjaxParser {

	private static AjaxParser Instance;
	Map<String, RequestCodeEnum> OpCodesMap = new HashMap<String, RequestCodeEnum>();

	private AjaxParser() {

		OpCodesMap.put("deletePostRequest", RequestCodeEnum.deletePostRequest);
		OpCodesMap.put("addPostRequest", RequestCodeEnum.addPostRequest);
		OpCodesMap.put("viewNewsFeedRequest",RequestCodeEnum.viewNewsFeedRequest);
		OpCodesMap.put("initializeRequest", RequestCodeEnum.initializeRequest);
		OpCodesMap.put("addAllowedUsersRequest",RequestCodeEnum.addAllowedUsersRequest);
		OpCodesMap.put("singlePostRequest", RequestCodeEnum.singlePostRequest);
		OpCodesMap.put("addCommentRequest", RequestCodeEnum.addCommentRequest);
		OpCodesMap.put("deleteCommentRequest",RequestCodeEnum.deleteCommentRequest);
		OpCodesMap.put("validateJobSeekerRequest",RequestCodeEnum.validateJobSeekerRequest);
		OpCodesMap.put("addTagRequest", RequestCodeEnum.addTagRequest);
		OpCodesMap.put("deleteTagRequest", RequestCodeEnum.deleteTagRequest);
		OpCodesMap.put("commentListRequest", RequestCodeEnum.commentListRequest);
		OpCodesMap.put("myJobsListRequest", RequestCodeEnum.myJobsListRequest);
		OpCodesMap.put("jobLifeCycleRequest",RequestCodeEnum.jobLifeCycleRequest);
		OpCodesMap.put("addEventRequest", RequestCodeEnum.addEventRequest);
		OpCodesMap.put("updateEventRequest", RequestCodeEnum.updateEventRequest);
		OpCodesMap.put("deleteEventRequest", RequestCodeEnum.deleteEventRequest);
		OpCodesMap.put("addNewJobRequest", RequestCodeEnum.addNewJobRequest);
		OpCodesMap.put("publicJobDeleteRequest",RequestCodeEnum.publicJobDeleteRequest);
		OpCodesMap.put("publicJobUpdateRequest",RequestCodeEnum.publicJobUpdateRequest);
		OpCodesMap.put("suggestJobRequest", RequestCodeEnum.suggestJobRequest);
		OpCodesMap.put("applyForJobRequest", RequestCodeEnum.applyForJobRequest);
		OpCodesMap.put("searchForJobRequest",RequestCodeEnum.searchForJobRequest);
		OpCodesMap.put("searchForPostRequest",RequestCodeEnum.searchForPostRequest);
		OpCodesMap.put("listOfNotificationsRequest",RequestCodeEnum.listOfNotificationsRequest);
		OpCodesMap.put("markAsReadRequest", RequestCodeEnum.markAsReadRequest);
		OpCodesMap.put("acceptSuggestedJobRequest",RequestCodeEnum.acceptSuggestedJobRequest);
		OpCodesMap.put("rejectSuggestedJobRequest",RequestCodeEnum.rejectSuggestedJobRequest);
		OpCodesMap.put("closeMyJobLifeCycleRequest",RequestCodeEnum.closeMyJobLifeCycleRequest);
		OpCodesMap.put("singleNotificationEntityRequest",RequestCodeEnum.singleNotificationEntityRequest);

	}

	public static AjaxParser ClientRequestFactory() {
		if (Instance == null)
			Instance = new AjaxParser();
		return (Instance);
	}

	public ClientRequest ParseClientAjax(HttpServletRequest request) {

		ClientRequest msg = new ClientRequest();
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) { 
			e.printStackTrace();
		}

		JSONParser parser = new JSONParser();
		try {
			JSONObject obj = (JSONObject) parser.parse(jb.toString());

			String opcodeString = (String) obj.get("opcode");
			RequestCodeEnum numericEnumCode = (RequestCodeEnum) OpCodesMap
					.get(opcodeString);
			msg.setOpCode(numericEnumCode);

			String entityStr = obj.get("Entity").toString();
			Entity entity = makeEntityFromString(numericEnumCode, entityStr);
			msg.setEntity(entity);

		} catch (ParseException pe) {
			System.out.println(pe);
		}
		return msg;
	}

	// ********************** Parsing AjaxString to Object
	private JSONObject ParseStringToObject(String AjaxString) {

		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(AjaxString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return obj;
	}

	// ********************** Parsing AjaxString to Array
	private JSONArray ParseStringToArray(String AjaxString) {

		JSONParser parser = new JSONParser();
		JSONArray jarr = null;
		try {
			jarr = (JSONArray) parser.parse(AjaxString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jarr;

	}

	private Timestamp ParseStringToTimestamp(String TimestampString) {
		Timestamp dueTimeStamp = Timestamp.valueOf(TimestampString);
		return dueTimeStamp;
	}

	// ***** Make Entity by opCode
	private Entity makeEntityFromString(RequestCodeEnum opCode, String entityStr)
			throws ParseException {

		Entity entity = null;
		switch ((RequestCodeEnum) opCode) {

		case validateJobSeekerRequest:
			return makeValidateJobSeekerEntity(entityStr);

		case initializeRequest:
			return makeInitializeEntity(entityStr);

		case addPostRequest:
			return makePostEntity(entityStr);

		case deletePostRequest:
			return makeDeletePostEntity(entityStr);

		case viewNewsFeedRequest:
			return makeViewNewsFeedEntity(entityStr);

		case singlePostRequest:
			return makeSinglePostEntity(entityStr);

		case addCommentRequest:
			return makeAddCommentEntity(entityStr);

		case deleteCommentRequest:
			return makeDeleteCommentEntity(entityStr);

		case commentListRequest:
			return makeCommentListEntity(entityStr);

		case addAllowedUsersRequest:
			return makeAddAllowedUserEntity(entityStr);

		case addTagRequest:
			return makeTagEntity(entityStr);

		case deleteTagRequest:
			return makeDeleteTagEntity(entityStr);

		case myJobsListRequest:
			return makeMyJobListEntity(entityStr);

		case jobLifeCycleRequest:
			return makeJobLifeCycleEntity(entityStr);

		case addEventRequest:
			return makeAddEventEntity(entityStr);

		case updateEventRequest:
			return makeUpdateEventEntity(entityStr);

		case deleteEventRequest:
			return makeDeleteEventEntity(entityStr);

		case addNewJobRequest:
			return makeJobEntity(entityStr);

		case publicJobDeleteRequest:
			return makeDeleteJobEntity(entityStr);

		case publicJobUpdateRequest:
			return makeUpdateJobEntity(entityStr);

		case suggestJobRequest:
			return makeSuggestJobEntity(entityStr);

		case applyForJobRequest:
			return makeApplyForJobEntity(entityStr);

		case searchForJobRequest:
			return MakeSearchEntity(entityStr);

		case searchForPostRequest:
			return MakeSearchEntity(entityStr);

		case listOfNotificationsRequest:
			return makeListOfNotificationsEntity(entityStr);

		case markAsReadRequest:
			return makeMarkAsReadEntity(entityStr);

		case acceptSuggestedJobRequest:
			return makeAcceptSuggestedJobEntity(entityStr);

		case rejectSuggestedJobRequest:
			return makeRejectSuggestedJobEntity(entityStr);

		case closeMyJobLifeCycleRequest:
			return makecloseMyJobLifeCycleEntity(entityStr);
		
		case singleNotificationEntityRequest:
			return makeSingleNotificationEntity(entityStr);
		default:
			return entity;
		}
	}

	// *********************************************************************

	// Receive Entity From Client Methods

	// *********************************************************************
	private SingleNotificationEntity makeSingleNotificationEntity(String AjaxString){
		
		SingleNotificationEntity notification=new SingleNotificationEntity();
		JSONObject obj = ParseStringToObject(AjaxString);
		
		long notificationTypeId=(Long) obj.get("typeId");
		notification.setNotificationTypeId(notificationTypeId);
		notification.setEntityId((Long) obj.get("entityId"));
		notification.setNotificationId((Long) obj.get("notificationId"));
		return notification;
	}
	private CloseJobEvent makecloseMyJobLifeCycleEntity(String AjaxString) {

		CloseJobEvent CloseJobEvent = new CloseJobEvent();
		JSONObject obj = ParseStringToObject(AjaxString);

		Event event=new Event();			
		event.setUserId((Long) obj.get("userId"));
		event.setJobId((Long) obj.get("jobId"));
		event.setAddress((String) obj.get("address"));
		event.setTitle((String) obj.get("title"));
		if((String) obj.get("description")!=null)
		event.setDescription((String) obj.get("description"));
		else 
		event.setDescription("");
		event.setNotes((String) obj.get("notes"));
		event.setDue(new Timestamp(System.currentTimeMillis()));
		
		CloseJobEvent.setEvent(event);
		CloseJobEvent.setIsAccepted((Boolean) obj.get("isAccepted"));

		return CloseJobEvent;
	}

	
	
	private Assignment makeRejectSuggestedJobEntity(String AjaxString) {

		Assignment assignment = new Assignment();
		JSONObject obj = ParseStringToObject(AjaxString);

		assignment.setJobId((Long) obj.get("jobId"));
		assignment.setUserId((Long) obj.get("userId"));

		return assignment;
	}

	private Notification makeListOfNotificationsEntity(String AjaxString) {

		Notification notification = new Notification();
		JSONObject obj = ParseStringToObject(AjaxString);

		notification.setUserId((Long) obj.get("userId"));

		return notification;
	}

	private Notification makeMarkAsReadEntity(String AjaxString) {

		Notification notification = new Notification();
		JSONObject obj = ParseStringToObject(AjaxString);

		notification.setUserId((Long) obj.get("userId"));
	

		return notification;
	}

	private SearchEntity MakeSearchEntity(String AjaxString) {

		SearchEntity SE = new SearchEntity();
		JSONObject obj = ParseStringToObject(AjaxString);

		SE.setUserId((Long) obj.get("userId"));
		SE.setNumberOfItems((Long) obj.get("numberOfItems"));
		SE.setOffset((Long) obj.get("offset"));

		String dirStr = (String) obj.get("dir");
		if (dirStr.equals("forward"))
			SE.setForward(true);
		else
			SE.setForward(false);

		JSONArray jsonarr = ParseStringToArray(obj.get("tagIds").toString());
		ArrayList<Long> tagsIds = new ArrayList<Long>();

		for (int i = 0; i < jsonarr.size(); i++) {
			tagsIds.add((Long) jsonarr.get(i));
		}
		SE.setTagsIds(tagsIds);

		return SE;
	}

	private SuggestJobEntity makeSuggestJobEntity(String AjaxString) {

		SuggestJobEntity SJE = new SuggestJobEntity();
		JSONObject obj = ParseStringToObject(AjaxString);

		SJE.setJobId((Long) obj.get("jobId"));
		SJE.setUserId((Long) obj.get("userId"));

		JSONArray jsonarr = ParseStringToArray(obj.get("jobseekersList")
				.toString());
		ArrayList<Long> jobseekersList = new ArrayList<Long>();

		for (int i = 0; i < jsonarr.size(); i++) {
			jobseekersList.add((Long) jsonarr.get(i));
		}
		SJE.setJobseekersList(jobseekersList);

		return SJE;
	}

	private Job makeApplyForJobEntity(String AjaxString) {

		Job job = new Job();
		JSONObject obj = ParseStringToObject(AjaxString);

		job.setJobId((Long) obj.get("jobId"));
		job.setSubmitterUserId((Long) obj.get("userId"));

		return job;
	}

	private Job makeUpdateJobEntity(String AjaxString) {

		Job job = new Job();
		JSONObject obj = ParseStringToObject(AjaxString);

		job.setJobId((Long) obj.get("jobId"));
		job.setSubmitterUserId((Long) obj.get("userId"));
		job.setJobTitle((String) obj.get("jobTitle"));
		job.setCompanyName((String) obj.get("companyName"));
		job.setAddress((String) obj.get("address"));
		job.setContact((String) obj.get("contact"));
		job.setDescription((String) obj.get("description"));
		job.setIsPrivate((Boolean) obj.get("isPrivate"));
		//job.setIsActive((Boolean) obj.get("isActive"));
		

		return job;
	}

	private Job makeDeleteJobEntity(String AjaxString) {

		Job job = new Job();
		JSONObject obj = ParseStringToObject(AjaxString);

		job.setJobId((Long) obj.get("jobId"));
		job.setSubmitterUserId((Long) obj.get("userId"));

		return job;
	}

	private Event makeDeleteEventEntity(String AjaxString) {

		Event event = new Event();
		JSONObject obj = ParseStringToObject(AjaxString);

		event.setEventId((Long) obj.get("eventId"));
		event.setUserId((Long) obj.get("userId"));

		return event;
	}



	private Event makeAcceptSuggestedJobEntity(String AjaxString) {

		Event event = new Event();
		JSONObject obj = ParseStringToObject(AjaxString);

		event.setUserId((Long) obj.get("userId"));
		event.setJobId((Long) obj.get("jobId"));
		event.setAddress((String) obj.get("address"));
		event.setTitle((String) obj.get("title"));
		event.setDescription((String) obj.get("description"));
		event.setNotes((String) obj.get("notes"));
		event.setDue(ParseStringToTimestamp("2011-11-11 11:11:11"));
		
		return event;
	}
	
	
	private AddEventEntity makeAddEventEntity(String AjaxString) {

		AddEventEntity addEvent = new AddEventEntity();
		JSONObject obj = ParseStringToObject(AjaxString);
		
		String notificationAlertTimeString=(String) obj.get("notification");
		if(notificationAlertTimeString.equals(""))
			addEvent.setNotificationAlertTime(null);
		else
			addEvent.setNotificationAlertTime(ParseStringToTimestamp(notificationAlertTimeString));
		
		Event event = new Event();
		event.setUserId((Long) obj.get("userId"));
		event.setJobId((Long) obj.get("jobId"));
		event.setAddress((String) obj.get("address"));
		event.setTitle((String) obj.get("title"));
		event.setDescription((String) obj.get("description"));
		event.setNotes((String) obj.get("notes"));
		event.setDue(ParseStringToTimestamp((String)obj.get("dueDate")));
		addEvent.setEvent(event);
		
		return addEvent;
	}
	
	
	
	
	private Event makeUpdateEventEntity(String AjaxString) {

		Event event = new Event();
		JSONObject obj = ParseStringToObject(AjaxString);

		event.setUserId((Long) obj.get("userId"));
		event.setEventId((Long) obj.get("eventId"));
		event.setJobId((Long) obj.get("jobId"));
		event.setAddress((String) obj.get("address"));
		event.setTitle((String) obj.get("title"));
		event.setDescription((String) obj.get("description"));
		event.setNotes((String) obj.get("notes"));
		event.setDue(ParseStringToTimestamp((String)obj.get("dueDate")));

		return event;
	}

	private Job makeJobEntity(String AjaxString) {

		Job job = new Job();
		JSONObject obj = ParseStringToObject(AjaxString);

		job.setSubmitterUserId((Long) obj.get("userId"));
		job.setJobTitle((String) obj.get("jobTitle"));
		job.setCompanyName((String) obj.get("companyName"));
		job.setAddress((String) obj.get("address"));
		job.setContact((String) obj.get("contact"));
		job.setDescription((String) obj.get("description"));
		job.setIsPrivate((Boolean) obj.get("isPrivate"));
		job.setIsActive((Boolean) obj.get("isActive"));
		JSONArray jsonarr = ParseStringToArray(obj.get("tags").toString());
		ArrayList<Long> tags = new ArrayList<Long>();

		for (int i = 0; i < jsonarr.size(); i++) {
			tags.add((Long) jsonarr.get(i));
		}
		job.setTags(tags);

		return job;
	}

	private Assignment makeJobLifeCycleEntity(String AjaxString) {

		Assignment ass = new Assignment();
		JSONObject obj = ParseStringToObject(AjaxString);

		ass.setJobId((Long) obj.get("jobId"));
		ass.setUserId((Long) obj.get("userId"));

		return ass;
	}

	// TODO
	private User makeMyJobListEntity(String AjaxString) {

		User user = new User();
		JSONObject obj = ParseStringToObject(AjaxString);

		user.setUserId((Long) obj.get("userId"));

		return user;
	}

	private User makeAddAllowedUserEntity(String AjaxString) {

		User user = new User();
		JSONObject obj = ParseStringToObject(AjaxString);

		user.setEmail((String) obj.get("email"));
		user.setUserTypeId((Long) obj.get("userType"));
		// user.setUserId((Long)obj.get("userId"));

		return user;
	}

	private Comment makeCommentListEntity(String AjaxString) {

		Comment comment = new Comment();
		JSONObject obj = ParseStringToObject(AjaxString);

		// comment.setCommentId((Long)obj.get("commentId"));
		comment.setUserId((Long) obj.get("userId"));
		comment.setPostId((Long) obj.get("postId"));

		return comment;
	}

	private Comment makeDeleteCommentEntity(String AjaxString) {

		Comment comment = new Comment();
		JSONObject obj = ParseStringToObject(AjaxString);

		comment.setCommentId((Long) obj.get("commentId"));
		comment.setUserId((Long) obj.get("userId"));

		return comment;
	}

	private Post makeSinglePostEntity(String AjaxString) {

		Post post = new Post();
		JSONObject obj = ParseStringToObject(AjaxString);

		post.setPostId((Long) obj.get("postId"));
		post.setUserId((Long) obj.get("userId"));

		return post;
	}

	private Comment makeAddCommentEntity(String AjaxString) {

		Comment comment = new Comment();
		JSONObject obj = ParseStringToObject(AjaxString);

		comment.setUserId((Long) obj.get("userId"));
		comment.setPostId((Long) obj.get("postId"));
		comment.setContent((String) obj.get("content"));

		return comment;
	}

	private User makeValidateJobSeekerEntity(String AjaxString) {

		User user = new User();
		JSONObject obj = ParseStringToObject(AjaxString);

		user.setFirstName((String) obj.get("firstName"));
		user.setLastName((String) obj.get("lastName"));
		user.setEmail((String) obj.get("email"));
		user.setProfileUrl((String) obj.get("profileUrl"));
		user.setPictureUrl((String) obj.get("pictureUrl"));
		return user;
	}

	private Post makeDeletePostEntity(String AjaxString) {
		Post post = new Post();
		JSONObject obj = ParseStringToObject(AjaxString);

		post.setPostId((Long) obj.get("postId"));
		post.setUserId((Long) obj.get("userId"));

		return post;
	}

	private Post makePostEntity(String AjaxString) {

		Post post = new Post();
		JSONObject obj = ParseStringToObject(AjaxString);

		post.setTitle((String) obj.get("title"));
		post.setContent((String) obj.get("content"));
		post.setUserId((Long) obj.get("userId"));

		JSONArray jsonarr = ParseStringToArray(obj.get("tags").toString());
		ArrayList<Long> tags = new ArrayList<Long>();

		for (int i = 0; i < jsonarr.size(); i++) {
			tags.add((Long) jsonarr.get(i));
		}
		post.setTags(tags);

		return post;
	}

	private NewsFeedEntity makeViewNewsFeedEntity(String AjaxString) {

		NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
		JSONObject obj = ParseStringToObject(AjaxString);

		newsFeedEntity.setUserId((Long) obj.get("userId"));
		newsFeedEntity.setCurrentId((Long) obj.get("currentId"));
		newsFeedEntity.setDir((String) obj.get("dir"));// forWARD AW BACK
		newsFeedEntity.setNewsFeedLength((Long) obj.get("newsFeedLength"));

		return newsFeedEntity;
	}

	private User makeInitializeEntity(String AjaxString) {

		User user = new User();
		JSONObject obj = ParseStringToObject(AjaxString);

		user.setUserId((Long) obj.get("userId"));

		return user;
	}

	private Tag makeTagEntity(String AjaxString) {
		Tag tag = new Tag();
		JSONObject obj = ParseStringToObject(AjaxString);

		tag.setTagName((String) obj.get("tagName"));

		return tag;
	}

	private Tag makeDeleteTagEntity(String AjaxString) {
		Tag tag = new Tag();
		JSONObject obj = ParseStringToObject(AjaxString);

		tag.setTagId((Long) obj.get("tagId"));

		return tag;
	}

}
