'use strict';
		angular.module('app')
		  .controller('UpdatejobCtrl', function ($scope ,entitiesService,jobRequestService,initially,$q) {
			  		
			  $scope.log = [];

			  var jobId=1;
		            $scope.updateJob = function() {
	                  var jobEntity=entitiesService.jobEntity($scope.jobId,$scope.description,$scope.JobTitle,$scope.companyName,$scope.address,$scope.contact,$scope.jobType,$scope.userProfile.data.userId);
	               
                      var jobPromise=jobRequestsService.updateJob(jobEntity);
	                 
                      jobPromise.then(
	                   	function(d)
	                   	{alert("success!")
	                   	}
	                   	,function(d)
	                   	{alert("error!")});
		               };  
 });




