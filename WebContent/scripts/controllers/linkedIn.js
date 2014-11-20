angular.module('app').controller('LinkedinCtrl', 
		function AppCtrl($scope,entitiesService,authenticationService, $location, $rootScope, $http, linkedinService) {
    
	$scope.getUserProfile = function () {
    	
        linkedinService.getProfile(function(err, result){
            if(err){
            	swal({
					title : "Error!",
					text : "Something went wrong, please try again later!",
					type : "error"
				});
                
            }else{
            	var userEntity=entitiesService.userEntity(result.values[0].firstName,result.values[0].lastName,
            		result.values[0].emailAddress,result.values[0].id,result.values[0].publicProfileUrl,result.values[0].pictureUrl);
            	var userPromise=linkedinService.loginRequest(userEntity);
            	
            	userPromise.then(
	                   	function(d){
	                   		if(d.data.userStatus!="UnauthorizedUser")
	                   			{
	                		result.values[0].userId=d.data.userId;
	                   		result.values[0].userType=d.data.userType;
	                   		result.values[0].loggedIn=true;
	                   		authenticationService.userProfile.data=result.values[0];
	                   		authenticationService.userLoggedIn.status=true;
	                   	  //alert(angular.toJson(authenticationService.userProfile.data))
	                   	  //alert("chekLogin");
	                   		
	                   		$location.path("/newsFeed");
	                   	}
	                   	else{
	                   		swal({
	    						title : "Error!",
	    						text : "We are sorry. You are an unauthorized user!",
	    						type : "error"
	    					});
	                   		$scope.logoutLinkedIn();
	                   	$location.path("/login");}
	                   	},
	                	function(d){
	                   		swal({
	    						title : "Error!",
	    						text : "Something went wrong, please try again later!",
	    						type : "error"
	    					});
	                   	}
            );
            	
            }
        });
    };
 
    //logout and go to login screen
	$scope.logoutLinkedIn = function() {
		linkedinService.logout();
	
        delete linkedinService.getProfileData.resultData;
        delete authenticationService.userProfile.data
	//	$rootScope.loggedUser = false;
		
        $location.path("/login");
	};



});