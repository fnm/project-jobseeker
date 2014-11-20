

angular.module('servicesModule').factory('entitiesService', function() {

	return {
		postEntity: function(title,content,userId,tagsId) {
			var post = {};     // new object
			//auto Increment

			post.tags = tagsId;
			post.content = content;
			post.title = title;
			post.userId = userId;
			return post;
		},
		deletePostEntity: function(postId,userId) {
			var post = {};     // new object
			//auto Increment

			post.postId = postId;
			post.userId = userId;
			return post;
		},
		deleteCommentEntity: function(commentId,userId) {
			var post = {};     // new object
			//auto Increment

			post.commentId = commentId;
			post.userId = userId;
			return post;
		},
		applyForPuplicJobEntity: function(jobId,userId) { //muhammad daqqa 
			var publicJob = {};     // new object

			publicJob.jobId = jobId;
			publicJob.userId = userId;
			return publicJob;
		},
		suggestPuplicJobEntity: function(jobId,userId,jobseekersList) { //muhammad daqqa 
			var publicJob = {};     // new object

			publicJob.jobId = jobId;
			publicJob.userId = userId;
			publicJob.jobseekersList = jobseekersList;
			return publicJob;
		}
		,                 
		jobEntity: function(tag,userId,jobId,jobTitle,companyName,address,contact,description,isPrivate,isActive) {
            var job = {};     // new object
           	//auto Increment
            job.tags=tag;
            job.userId=userId;
            job.jobId=jobId;
            job.jobTitle=jobTitle;
            job.companyName=companyName;
            job.address=address;
            job.contact=contact;
            job.description=description;
            job.isPrivate=isPrivate;
            job.isActive=isActive;
            return job;
        },


		userEntity: function(firstName,lastName,email,linkedinId,profileUrl,pictureUrl) {

			var user = {};     // new object

			user.firstName=firstName;
			user.lastName = lastName;
			user.email = email;
			user.linkedinId = linkedinId;
			user.profileUrl = profileUrl;
			user.pictureUrl = pictureUrl;
			return user;
		},
		allowedUserEntity:function(email1,type){
			var user={};

			user.email=email1;
			user.userType=type;
			user.userId=123;
			return user;
		},

		commentEntity: function(userId,postId,content) {
			// commentId,postId,userId,content
			var comment = {};     // new object
			//auto Increment
			comment.commentId=100;
			comment.postId = postId;
			comment.userId = userId;
			comment.content = content;
			return comment;
		},

		notificationEntity: function(userId) {
			// userId
			var notification = {};     // new object
			//auto Increment
			notification.userId = userId;
			return notification;
		},

		eventEntity: function(userId,jobId,title, description, notes, address, dueData, notification) {
			var event = {};

			event.userId = userId;
			event.jobId = jobId;
			event.title = title;
			event.description = description;
			event.notes = notes;
			event.address = address;
			event.dueDate = dueData;
			event.notification = notification;
			return event;
		},
		
		closeJobEntity: function(userId,jobId,title, description, notes, address, dueDate, notification,flag) {
			var event = {};

			event.userId = userId;
			event.jobId = jobId;
			event.title = title;
			event.description = description;
			event.notes = notes;
			event.address = address;
			event.dueDate = dueDate;
			event.notification = notification;
			event.isAccepted = flag;
			return event;
		},
		
		assignmentEntity: function(jobId,userId) {
			var event = {};
			event.userId = userId;
			event.jobId = jobId;
			return event;
		},searchPostEntity:function(tags,freeText,userId,offset,dir,numberOfItems){
        	var data={};
        	data.tagIds=tags;
        	data.freeText=freeText;
        	data.userId=userId;
        	data.offset=offset;
        	data.dir=dir;
        	data.numberOfItems=numberOfItems;
        	
        	return data;
        },searchJobEntity:function(tags,freeText,userId,offset,dir,numberOfItems){
        	var data={};
        	data.tagIds=tags;
        	data.freeText=freeText;
        	data.userId=userId;
        	data.offset=offset;
        	data.dir=dir;
        	data.numberOfItems=numberOfItems;
        	
        	return data;
        },singlePost : {}

	};

});
