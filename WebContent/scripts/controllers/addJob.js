'use strict';
angular.module('app')
  .controller('AddjobCtrl', function ($scope ,jobIdService,entitiesService,jobRequestsService,initially,$q,$location) {
	  var jobId=1;
	  $scope.job={}
  		$scope.isActive = true;
  		$scope.job.isPrivate=true;
  	  $scope.log = [];
        $scope.addJob = function() {
        	
			
	        var jobEntity=entitiesService.jobEntity($scope.log,$scope.userProfile.data.userId,jobId,$scope.job.jobTitle,$scope.job.companyName,$scope.job.address,$scope.job.contact,$scope.job.description,$scope.job.isPrivate,$scope.isActive);
	      
	        var jobPromise=jobRequestsService.addJob(jobEntity);
	        //jobIdService.setJobEntity(jobEntity);
	        jobPromise.then(
	        	function(d) {
	        		swal({
						title : "Success!",
						text : "Job added successfully!",
						type : "success",
						timer: 2000
					});
		       		$scope.job.jobTitle = "";
		       		$scope.job.companyName = "";
		       		$scope.job.address = "";
		       		$scope.job.contact = "";
		       		$scope.job.description = "";
		       		$scope.job.isPrivate = "true";
		       		$scope.job.tags = "";
		       		if ($scope.userProfile.data.userType==1){
		       			$location.path("/myJobList/");
		       		} else {
		       		$location.path("/newsFeed/");
		       		}
		       		//$route.reload();
		
	        	},
	        	
	        	function(d) {
	        		swal({
						title : "Error!",
						text : "Something went wrong, please try again later",
						type : "error"
						
					});
	        		
	        	});
        },
        
        $scope.loadTags = function(query) {
  	      var deferred = $q.defer();
  	      var i;
  	      //here we do filter with the current text
  	      var result=new Array();
  	      
  	      var a = initially.getTagsArr.resultTagsArr;
  	      for(i=0;i<a.length;i++){
  	      	
  	      	//var jsonStr=a[i];
  	          //var json_parsed = JSON.parse(jsonStr);

  	         var items = a[i].text; // an array
  	               var item= items.toLowerCase();
  	                 var quer=query.toLowerCase();
  	                  if( item.indexOf(quer) > -1){
  	                  	result.push(a[i]);
  	                  }// is the respective value

  	               }

  	      deferred.resolve(result);
  	      return deferred.promise;
  	  };

	  $scope.tagAdded = function(tag) {
	    $scope.log.push(tag.tagId);
	  };
	  
	  $scope.tagRemoved = function(tag) {
		  var x=$scope.log.indexOf(tag.tagId);
		  $scope.log.splice(x, tag.tagId); 
	  };
 });




