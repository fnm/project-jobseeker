angular.module('app')
		  .controller('searchResultController', function ($scope,authenticationService,publicJobIdService) { 
			  
			   $scope.posts=authenticationService.posts; 
			   $scope.jobs= authenticationService.jobs;
			   
			   $scope.extendJob = function(clickedJob){
				   publicJobIdService.setPublicJobEntity(clickedJob);
				   
			   };
		  });