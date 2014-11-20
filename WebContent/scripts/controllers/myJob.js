angular
		.module("app")
		.controller(
				'MyJobCtrl',
				function($route, dateFilter ,$location, $scope, jobIdService,
						eventRequestService, entitiesService,
						jobRequestsService) {

					$scope.eventReason = "";
					$scope.jobEntity = jobIdService.getJobEntity();
					$scope.getEventId = jobIdService.getEventId();
					$scope.eventClose = 0;
					$scope.eventStatus = false;
					console.log("here")
					console.log($scope.jobEntity)
					console.log($scope.jobEntity.status)
					eventRequestService
							.getListOfEventsByJobId(
									entitiesService.assignmentEntity(
											$scope.jobEntity.jobId,
											$scope.userProfile.data.userId))
							.then(
									function(d) {

										$scope.events = (d.data.events);

									},
									function(d) {
										swal({
											title : "Error!",
											text : "Something went wrong, please try again later",
											type : "error",
											timer : 2000
										});
									});

					// accept
					$scope.accept = function(jobEntity) {
						$scope.jobEntity.status = status + 1;
						jobIdService.setJobEntity($scope.jobEntity);

						var eventJobEntity = entitiesService.eventEntity(
								$scope.userProfile.data.userId,
								$scope.jobEntity.jobId, 'First Event', "", "",
								"", "", "");

						jobRequestsService
								.acceptSuggestedJob(eventJobEntity)
								.then(
										function(d) {
											$scope.events = ([ eventJobEntity ]);
											swal({
												title : "Success!",
												text : "Your application has successfully started!",
												type : "success"
											});
											$location.path('/myJobList/');
											$scope.event.push(eventJobEntity);
										},
										function(d) {
											swal({
												title : "Error!",
												text : "Something went wrong, please try again later",
												type : "error",
											});
										});
					};

					// reject
					$scope.reject = function(jobEntity) {

						var jobEntity = entitiesService.assignmentEntity(
								$scope.jobEntity.jobId,
								$scope.userProfile.data.userId);

						jobRequestsService
								.rejectSuggestedJob(jobEntity)
								.then(
										function(d) {
											swal({
												title : "Success!",
												text : "job suggestion removed from your list!",
												type : "success",
												timer : 2000
											});
											$location.path('/myJobList/');
										},
										function(d) {
											swal({
												title : "Error!",
												text : "Something went wrong, please try again later",
												type : "error",
											});
										});
					};

					// close
					$scope.eventClose = function() {

						$scope.eventClose = 1;

					};

					// close
					$scope.CloseMyJobLifeCycle = function() {

						if ($scope.eventStatus == true) {
							$scope.status = "closed";
							$scope.jobEntity.status = 3;
						} else {
							$scope.status = "accepted";
							$scope.jobEntity.status = 4;
						}
						
						var closeJobEntity = entitiesService.closeJobEntity(
								
								$scope.userProfile.data.userId,
								$scope.jobEntity.jobId, $scope.status,
								$scope.eventReason, " ", " ", " ", " ",
								$scope.eventStatus);
						var eventJobEntity = entitiesService.eventEntity(
								$scope.userProfile.data.userId,
								$scope.jobEntity.jobId, $scope.status,
								$scope.eventReason, " ", " ", " ", " ");
						var eventJobPromise = eventRequestService
								.closeMyJobLifeCycle(closeJobEntity);
						 $scope.tmpPublish = new Date();
						 $scope.tmpPublish=dateFilter( $scope.tmpPublish, 'yyyy-MM-dd HH:mm:ss')
						eventJobPromise
								.then(
										function(d) {
											eventJobEntity.publishDate=$scope.tmpPublish;
											$scope.events.push(eventJobEntity);
											$scope.eventClose = 0;
										},

										function(d) {
											swal({
												title : "Error!",
												text : "Something went wrong, please try again later",
												type : "error",
											});
										});
					};

				});
