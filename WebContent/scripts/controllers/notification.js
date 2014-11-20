angular.module('app').controller('notificationCtrl',function($scope,jobIdService,$location,notificationService,entitiesService,authenticationService,jobIdService){
	
//	$scope.items = [
//	                'The first choice!',
//	                'And another choice for you.',
//	                'but wait! A third!'
//	              ];

	              $scope.status = {
	                isopen: false
	              };

	              $scope.toggled = function(open) {
	              };

	              $scope.toggleDropdown = function($event) {
	                $event.preventDefault();
	                $event.stopPropagation();
	                $scope.status.isopen = !$scope.status.isopen;
	              };
	
	$scope.getNotofication=function(){
		var promise=notificationService.getNotification($scope.userProfile.data.userId);
		promise.then(
				 function(d){
					// alert("notification s7");
					 $scope.items=d.data.notificationList;
					 $scope.notification=d.data.notificationList;
				 },function(d){
					 swal({
							title : "Error!",
							text : "Cannot get notifications, please try again later",
							type : "error",
						});
				 }
						
				);
	}
		$scope.getNotificationnEntity=function(index){
			/*alert(angular.toJson($scope.notification))
			alert(index)*/
			//console.log($scope.notification[index])
			//alert($scope.notification[index].notificationTypeId+','+$scope.notification[index].notificationForeignId);
			var promise=notificationService.getNotificationEntity($scope.notification[index].notificationTypeId,$scope.notification[index].notificationForeignId,$scope.notification[index].notificationId);
			promise.then(
					 function(d){
						 //alert($scope.notification[index].notificationTypeId)
						if($scope.notification[index].notificationTypeId==1){
						 entitiesService.singlePost=d.data.post;
						 $location.path("/singlePost");
						 }
						 if($scope.notification[index].notificationTypeId==2){
							 d.data.job.status=2;
							 jobIdService.setJobEntity(d.data.job)
							 $location.path("/showMore/");
							 }
						 if($scope.notification[index].notificationTypeId==3){
							 
							 $location.path("/myJobList/");
							 }
						/* if(d[index].notificationTypeId==1)
							{ 
							  $scope.test=d[index].notificationTypeId;
//							 jobIdService.setJobEntity(d.data.entity);
//						     jobIdservice.setEventId(d[index].notificationForeignId);
//						 	$location.path("/myJob");
						 	}
							      
							
						 if(d[index].notificationTypeId==2)
							{
							 $scope.test=d[index].notificationTypeId;
//							     jobIdService.setJobEntity(d.data.entity);
//							     jobIdservice.setEventId(d[index].notificationForeignId);
//							 	$location.path("/myJob");
							 	}
							
						 if(d[index].notificationTypeId==3)
							{
							 $scope.test=d[index].notificationTypeId;
							}*/
						 
					 },function(d){
						 swal({
								title : "Error!",
								text : "Something went wrong, please try again later",
								type : "error",
							});
					 }
							
					);
		
			
			
		}
	
	
});