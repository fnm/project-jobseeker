
angular.module('servicesModule').factory('eventRequestService', function($http,authenticationService) {
        return {

            
            addEvent: function(eventEntity) {
                var msg = {};
                msg.opcode = "addEventRequest";//change later
                msg.Entity = eventEntity;
                var eventPromise=$http({
                method : 'POST',
                url : authenticationService.deploymentLink.link,
                data: msg
                });
                
                return eventPromise;
            },

            deleteEvent: function(eventId, userId) {

                var msg = {};
			    msg.opcode = "deleteEventRequest";//change later
			    msg.Entity = eventEntity;
			    
			    var eventPromise=$http({
				method : 'POST',
				url : authenticationService.deploymentLink.link,
				data: msg
			});
			
			   return eventPromise;
			},
			
			updateEvent: function(eventEntity){
				var msg = {};
				msg.opcode = "updateEventRequest";//change later
            	msg.Entity = eventEntity;
            	
            	var eventPromise = $http({
            		method : 'POST',
            		url : authenticationService.deploymentLink.link,
            		data : msg
            	});
            	
            	return eventPromise;
			},
			
			getListOfEventsByJobId: function(eventEntity){
				
				var eventPromise=$http({
					method : 'POST',
					//Will be replaced with the url of the required servlet
					url : authenticationService.deploymentLink.link,
					data : {"opcode":"jobLifeCycleRequest","Entity":eventEntity}
				});
				
				return eventPromise;
			},
			
			closeMyJobLifeCycle: function(eventEntity){
				
				var eventPromise=$http({
					method : 'POST',
					//Will be replaced with the url of the required servlet
					url : authenticationService.deploymentLink.link,
					data : {"opcode":"closeMyJobLifeCycleRequest","Entity":eventEntity}
				});
				
				return eventPromise;
			}
        }
    });