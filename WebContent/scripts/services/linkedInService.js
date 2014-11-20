


angular.module('servicesModule').
    factory('linkedinService', function($rootScope,$http,authenticationService){
    	
    	var profileData;
        return {
        	
            //basic profile
            getProfile : function (callback){
                IN.API.Profile("me")
                .fields("firstName", "lastName", "id", "pictureUrl","publicProfileUrl","email-address")
                .result(function (result){
                
                	//profileData=result.values[0];
                    $rootScope.$apply(function() {
                        callback(null, result);
                    });
                })
                .error(function error(error) {
                        callback(error,null)
                    });
            },
            getProfileData : {resultData:""},
                
            //returns true if user is authorized
            isAuthorized : function(){
                return IN.User.isAuthorized();
            },
            logout : function(){
                return IN.User.logout();
            },
            loginRequest : function(userEntity){
                var msg = {};
                msg.opcode = "validateJobSeekerRequest";
                msg.Entity =userEntity;
                var jobPromise=$http({
         method : 'POST',
            url : authenticationService.deploymentLink.link,
            data: msg
            });
                return jobPromise;
            }
            
    
            

        }
    });
