
angular.module('servicesModule').
    factory('authenticationService', function($q, $timeout){
    	
    	
        return {
            userProfile : {data:""},
            userLoggedIn : {status:""},
			  jobs:{},
			 posts:{},
			 deploymentLink:{link:'http://ec2-54-69-86-150.us-west-2.compute.amazonaws.com:8080/jobseeker-tsofen/dispatch'},
			// deploymentLink:{link:'http://localhost:8080/jobseeker/dispatch'},
            getNesFeedLength: function(){
                var deferred = $q.defer();
                $timeout(function(){
                    deferred.resolve(50);
                },2000);
                return deferred.promise;
            }
          

        }
    });
