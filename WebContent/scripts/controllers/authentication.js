angular.module('app')
		  .controller('AuthenticationCtrl', function ($scope,authenticationService,initially,notificationService) {
			  $scope.userProfile=authenticationService.userProfile;
			  $scope.notificationsList=initially.notificationsList;
	  var promise=initially.getTags(); 
	 
			  promise.then(
						function(d){
						//	alert("successTags");
							
							initially.getUserTypes.userTypes = d.data.userTypes;
							initially.getTagsArr.resultTagsArr=d.data.tags;
							initially.getCurrentMaxPostId.CurrentMaxPostId=d.data.currentCounterforPost;
							initially.getUsersArr.userList=d.data.userList;
							},
						
						function(d){
								
								
								swal({
									title : "Error!",
									text : "Server currently unavailable, please try again later",
									type : "error",
								});
							}); 
			  
			
		  });
