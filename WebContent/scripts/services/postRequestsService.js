angular.module('servicesModule').factory('postRequestsService', function($http,authenticationService) {
        return {
        	
            addPost: function(postEntity) {
                var msg = {};
                msg.opcode = "addPostRequest";
                msg.Entity =postEntity;
                var postPromise=$http({
         method : 'POST',
            url : authenticationService.deploymentLink.link,
            data: msg
        });
                return postPromise;
            },
            getSinglePost: function(postEntity) {

                             var msg = {};
                msg.opcode = "RequestGetPostOpCode";
                msg.Entity =postEntity;
                var postPromise=$http({
         method : 'POST',
            url : authenticationService.deploymentLink.link,
            data: msg
        });

                return postPromise;
            },
            deletePost: function(deletePostEntity) {

                var msg = {};
			   msg.opcode = "deletePostRequest";
			   msg.Entity =deletePostEntity;
			   var postPromise=$http({
				method : 'POST',
				url : authenticationService.deploymentLink.link,
				data: msg
			});
			
			   return postPromise;
			},
			
				//return all posts
				getListOfPosts: function(currentId,newsFeedLength,direction){
					var listOfPosts;
					var postPromise=$http({
						method : 'POST',
						//Will be replaced with the url of the required servlet
						url : authenticationService.deploymentLink.link,
						data : {"opcode":"viewNewsFeedRequest","Entity":{"userId":1,"currentId":currentId,"dir":direction,"newsFeedLength":newsFeedLength}}
					
					});
					
					return postPromise;
				},searchPost:function(entity){
					var msg = {};
	            	msg.opcode = "searchForPostRequest";
	            	msg.Entity = entity;
	            	
	            	var postPromise = $http({
	            		method : 'POST',
	            		url : authenticationService.deploymentLink.link,
	            		data : msg
	            	});
	            	
	            	return postPromise;
				},singlePost:{},
        }
    });