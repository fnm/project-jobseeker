//angular.module('servicesModule',[]);

angular.module('app').factory('allowedUserService', function($http , authenticationService) {

        
            return {
                addAllowedUser: function(allowedUserEntity) {
                    var request = {};
                    request.opcode = "addAllowedUsersRequest";
                    request.Entity =allowedUserEntity;
                    var requestPromise=$http({
             method : 'POST',
             	url : authenticationService.deploymentLink.link,
                data: request
            });
                    return requestPromise;
                }
            }
            	
            	
            	
            	
            
        
    });