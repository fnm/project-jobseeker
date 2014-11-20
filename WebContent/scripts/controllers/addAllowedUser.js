		//'use strict';

		angular.module('app')
		  .controller('AddAllowedUser', function ($scope ,allowedUserService,entitiesService,initially) {
			  
			  //we can save more information about type if we want
			  // and we can add another kind of user 

			  $scope.user={}
			  $scope.types = initially.getUserTypes.userTypes;
			  $scope.user.userType = $scope.types[0].userTypeName; //default is jobseeker
			  $scope.user.userEmail="";
			
			  	$scope.addUser = function() {       
			  	
			  		var userEntity=entitiesService.allowedUserEntity($scope.user.userEmail,$scope.user.userType.userTypeId);
			  		var userPromise=allowedUserService.addAllowedUser(userEntity);
			  		userPromise.then(
	            	    function(d) {
	            	    	swal({
								title : "Success!",
								text : "New user successfully added!",
								type : "success",
								timer : 2000
							});
	            	   		$scope.user.userEmail="";
	            	   	},
	            	    		
	            	   	function(d) {
	            	   		swal({
								title : "Error!",
								text : "Something went wrong, please try again later!",
								type : "error"
							
							});
	            	   	}
	            	);	            	    	
			  	};          
		  });
			
				
		function showId(val){
			var id=val.target.id;
			alert(id);
		}
