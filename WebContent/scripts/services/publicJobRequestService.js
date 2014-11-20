angular.module('servicesModule').factory('publicJobRequestsService', function($http,authenticationService) {
        return {
        	
            getSingleJob: function(jobEntity) {
                var msg = {};
                msg.opcode = "singleJobRequest";
                msg.Entity =jobEntity;
                
                var jobPromise=$http({
                	method : 'POST',
                	url : authenticationService.deploymentLink.link,
                	data: msg
                });

                return jobPromise;
            },

            deleteJob: function(jobId,submitterId) { 
            	var msg = {};
            	
            	
			   
            	var jobPromise=$http({
            		method : 'POST',
            		url : authenticationService.deploymentLink.link,
            		data : {"opcode":"publicJobDeleteRequest","Entity":{"jobId":jobId,"submitterUserId":submitterId}}
            	});

			   return jobPromise;
            },
            suggestJob: function(jobEntity) {
            	var msg = {};
            	msg.opcode = "suggestJobRequest";
            	msg.Entity = jobEntity;
            	
            	var jobPromise = $http({
            		method : 'POST',
            		url : authenticationService.deploymentLink.link,
            		data : msg
            	});
            	
            	return jobPromise;
            },
//            “opcode”:”applyForJobRequest”
//        	 “Entity”: {  	“jobId”:”123”
//                                           "userId":"456" // who is applying for the job”
//       			“event”: <event entity as described in 4.2>
//          }
            applyForJob: function(jobEntity) {
            	var msg = {};
            	msg.opcode = "applyForJobRequest";
            	msg.Entity = jobEntity;
            	
            	var jobPromise = $http({
            		method : 'POST',
            		url : authenticationService.deploymentLink.link,
            		data : msg
            	});
            	
            	return jobPromise;
            },
            updateJob: function(jobEntity) {
            	var msg = {};
            	msg.opcode = "publicJobUpdateRequest";
            	msg.Entity = jobEntity;
            	
            	var jobPromise = $http({
            		method : 'POST',
            		url :authenticationService.deploymentLink.link,
            		data : msg
            	});
            	
            	return jobPromise;
            },
            
          //return all jobs
			getPublicListOfJobs: function(userId){
				var postPromise=$http({
					method : 'POST',
					//Will be replaced with the url of the required servlet
					url : authenticationService.deploymentLink.link,
					data : {"opcode":"publicJobRequest","Entity":{"userId":userId}}
				});
				
				return postPromise;
			}
        }
    });
