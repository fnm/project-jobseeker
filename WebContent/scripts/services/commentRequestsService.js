
angular.module('servicesModule').factory('commentRequestsService', function($http,authenticationService) {
        return {
        	
            addComment: function(commentEntity) {
                var msg = {};
                msg.opcode = "addCommentRequest";
                msg.Entity =commentEntity;
                var commentPromise=$http({
                method : 'POST',
                url : authenticationService.deploymentLink.link,
                data: msg
        });
                return commentPromise;
            },

            deleteComment: function(commentEntity) {
            	
                var msg = {};
			   msg.opcode = "deleteCommentRequest";
			   msg.Entity =commentEntity;
			   var commentPromise=$http({
			method : 'POST',
			url : authenticationService.deploymentLink.link,
			data: msg
			});
			
			   return commentPromise;
			},
			
			getListOfComments: function(userId,postId ){
				var commentPromise=$http({
					method : 'POST',
					//Will be replaced with the url of the required servlet
					url : authenticationService.deploymentLink.link,
					data : {"opcode":"commentListRequest","Entity":{"userId":userId,"postId":postId}}
				});
				
				return commentPromise;
			}
        }
    });