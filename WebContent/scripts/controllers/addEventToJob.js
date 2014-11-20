'use strict';

angular.module('app')
.controller('AddEventToJobCtrl', function ($scope ,entitiesService,eventRequestService,$location,jobIdService,$filter,dateFilter) {
	//date
	
	
	$scope.event={};
	$scope.event.eventNotificationDateStatus=false;
	 $scope.dateTimeNow = function() {
		    $scope.event.tmpeventDuedate = new Date();
		  };
		  $scope.dateTimeNow();
		  
		  $scope.toggleMinDate = function() {
		    $scope.minDate = $scope.minDate ? null : new Date();
		  };
		   
		  $scope.maxDate = new Date('2014-06-22');
		  $scope.toggleMinDate();

		  $scope.dateOptions = {
		    startingDay: 1
		  };
		  
		  // Disable weekend selection
		  $scope.disabled = function(calendarDate, mode) {
		    return mode === 'day' && ( calendarDate.getDay() === 0 || calendarDate.getDay() === 7 );
		  };
		  
		  $scope.hourStep = 1;
		  $scope.minuteStep = 1;

		  $scope.timeOptions = {
		    hourStep: [1, 2, 3],
		    minuteStep: [1, 5, 10, 15, 25, 30]
		  };

		  $scope.showMeridian = true;
		  $scope.timeToggleMode = function() {
		    $scope.showMeridian = !$scope.showMeridian;
		  };
	//date	
			 
	$scope.jobEntity = jobIdService.getJobEntity();
	
	$scope.addEventToJob = function() {
		$scope.event.eventDuedate=dateFilter($scope.event.tmpeventDuedate, 'yyyy-MM-dd HH:mm:ss')
		if($scope.event.eventNotificationDateStatus){
		 $scope.event.eventNotificationDate=dateFilter($scope.event.tmpeventNotificationDate, 'yyyy-MM-dd HH:mm:ss')
		}else{$scope.event.eventNotificationDate=""}
		var eventJobEntity = entitiesService.eventEntity($scope.userProfile.data.userId, $scope.jobEntity.jobId, $scope.event.eventTitle, $scope.event.eventDescription,$scope.event.eventNotes,$scope.event.eventAddress,$scope.event.eventDuedate,$scope.event.eventNotificationDate);
		var eventJobPromise = eventRequestService.addEvent(eventJobEntity);
         
		eventJobPromise.then(
				function(d) {
					swal({
						title : "Success!",
						text : "Event added successfully!",
						type : "success",
						timer: 2000
					});
					$location.path('/showMore/');
				},

				function(d) {
					swal({
						title : "Error!",
						text : "Something went wrong, please try again later",
						type : "error",
						
					});
				});
	};   
	
	$scope.changeNotificationDateStatus=function(){
		 $scope.event.tmpeventNotificationDate = $scope.event.tmpeventDuedate;
		$scope.event.eventNotificationDateStatus=!$scope.event.eventNotificationDateStatus;
	}

	$scope.validatmpeventDuedate=function(){
		//alert(Date.parse($scope.event.tmpeventDuedate))
		if(Date.parse($scope.event.tmpeventDuedate) < Date.parse($scope.event.tmpeventNotificationDate)){
			alert("Duedate need to be bigger than NotificationDate")
			$scope.event.tmpeventDuedate=$scope.event.tmpeventNotificationDate;
		}
			
	}

	$scope.validatmpeventNotificationDate=function(){
		//alert($scope.event.tmpeventNotificationDate)
	if(Date.parse($scope.event.tmpeventDuedate) < Date.parse($scope.event.tmpeventNotificationDate)){
			alert("NotificationDate need to be smaller than Duedate")
			$scope.event.tmpeventNotificationDate=$scope.event.tmpeventDuedate;	
	}		
	}
	
	
});
