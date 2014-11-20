angular.module('servicesModule').factory('jobRequestsService', function($http,authenticationService) {
        return {
        	
            addJob: function(jobEntity) {
                var msg = {};
                msg.opcode = "addNewJobRequest";
                msg.Entity =jobEntity;
                var jobPromise=$http({
         method : 'POST',
            url : authenticationService.deploymentLink.link,
            data: msg
        });
                return jobPromise;
            },
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

            deleteJob: function(jobEntity) {
            	var msg = {};
            	msg.opcode = "publicJobDeleteRequest";
            	msg.Entity =jobEntity;
			   
            	var jobPromise=$http({
            		method : 'POST',
            		url : authenticationService.deploymentLink.link,
            		data: msg
            	});

			   return jobPromise;
            },
           
            updateJob: function(jobEntity) {
            	var msg = {};
            	msg.opcode = "publicJobUpdateRequest";
            	msg.Entity = jobEntity;
            	
            	var jobPromise = $http({
            		method : 'POST',
            		url : authenticationService.deploymentLink.link,
            		data : msg
            	});
            	
            	return jobPromise;
            },
            acceptSuggestedJob: function(jobEntity) {
            	var msg = {};
            	msg.opcode = "acceptSuggestedJobRequest";
            	msg.Entity = jobEntity;
            	
            	var jobPromise = $http({
            		method : 'POST',
            		url : authenticationService.deploymentLink.link,
            		data : msg
            	});
            	
            	return jobPromise;
            },
            
            rejectSuggestedJob: function(jobEntity) {
            	var msg = {};
            	msg.opcode = "rejectSuggestedJobRequest";
            	msg.Entity = jobEntity;
            	
            	var jobPromise = $http({
            		method : 'POST',
            		url : authenticationService.deploymentLink.link,
            		data : msg
            	});
            	
            	return jobPromise;
            },
            
            closeMyJobLifeCycle: function(jobEntity) {
            	var msg = {};
            	msg.opcode = "closeMyJobLifeCycleRequest";
            	msg.Entity = jobEntity;
            	
            	var jobPromise = $http({
            		method : 'POST',
            		url : authenticationService.deploymentLink.link,
            		data : msg
            	});
            	
            	return jobPromise;
            },
          //return all jobs
			getMyListOfJobs: function(userId){
				var postPromise=$http({
					method : 'POST',
					//Will be replaced with the url of the required servlet
					url : authenticationService.deploymentLink.link,
					data : {"opcode":"myJobsListRequest","Entity":{"userId":userId}}
				});
				
				return postPromise;
			}   ,  searchJob:function(dataToSearch){
            	var msg = {};
            	msg.opcode = "searchForJobRequest";
            	msg.Entity = dataToSearch;
            	
            	var jobPromise = $http({
            		method : 'POST',
            		url : authenticationService.deploymentLink.link,
            		data : msg
            	});
            	
            	return jobPromise;
            }
        }
    });
