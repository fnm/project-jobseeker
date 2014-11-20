angular.module("app").controller(
		'publicJobCtrl',
		function($scope, entitiesService, publicJobRequestsService,
				publicJobIdService, initially, $q, $log) {

			$scope.log = [];
			$scope.userId = $scope.userProfile.data.userId;
			$scope.userType = $scope.userProfile.data.userType;// 1 -
																// Jobseeker, 2
																// - placement
																// manager

			$scope.publicJobEntity = publicJobIdService.getPublicJobEntity();

			$scope.suggestJob = function() {
				// var jobId = publicJobIdService.getClickedJobId() ;
				var jobId = $scope.publicJobEntity.jobId;
				// var jobseekersList = publicJobIdService.getJobseekerList();
				var jobseekersList = []; // --------------------------------------------------------------------------------------------
											// --do later

				var suggestJobEntity = entitiesService.suggestPuplicJobEntity(
						jobId, $scope.userId, $scope.log);

				var suggestJobPromise = publicJobRequestsService
						.suggestJob(suggestJobEntity);

				suggestJobPromise.then(function(d) {
					swal({
						title : "Success!",
						text : "Job successfully suggested to users!",
						type : "success",
						timer:2000
					});
				},

				function(d) {
					swal({
						title : "Error!",
						text : "Something went wrong, please try again later",
						type : "error"
					});
				});
			};

			$scope.deleteJob = function() {
				var jobId = $scope.publicJobEntity.jobId;
				var deleteJobPromise = publicJobRequestsService.deleteJob(
						$scope.publicJobEntity.jobId,
						$scope.publicJobEntity.submitterUserId);
				
				deleteJobPromise.then(function(d) {
					swal({
						title : "Success!",
						text : "Job successfully deleted!",
						type : "success",
						timer:2000
					});
				},

				function(d) {
					swal({
						title : "Error!",
						text : "Something went wrong, please try again later",
						type : "error"
					});
				});
			}

			// var a = initially.getUsersArr.userList;
			// for(i=0;i<a.length;i++){
			//      	
			// //var jsonStr=a[i];
			// //var json_parsed = JSON.parse(jsonStr);
			//
			// var items = a[i].firstName+" "+a[i].lastName; // an array
			//           

			// /Users
			$scope.loadTags = function(query) {
				var deferred = $q.defer();
				var i;
				// here we do filter with the current text
				var result = new Array();

				var a = initially.getUsersArr.userList;
				for (i = 0; i < a.length; i++) {

					// var jsonStr=a[i];
					// var json_parsed = JSON.parse(jsonStr);

					var items = a[i].text; // an array
					var item = items.toLowerCase();
					var quer = query.toLowerCase();
					if (item.indexOf(quer) > -1) {
						result.push(a[i]);
					}// is the respective value

				}

				deferred.resolve(result);
				return deferred.promise;
			};

			$scope.tagAdded = function(tag) {
				// alert(tag.userId)
				$scope.log.push(tag.userId);
			};

			$scope.tagRemoved = function(tag) {
				var x = $scope.log.indexOf(tag.tagId);
				$scope.log.splice(x, tag.tagId);
			};

			$scope.applyForJob = function() {

				var jobId = $scope.publicJobEntity.jobId;

				var applyJobEntity = entitiesService.applyForPuplicJobEntity(
						jobId, $scope.userId);
                    
				var applyForJobPromise = publicJobRequestsService
						.applyForJob(applyJobEntity);

				applyForJobPromise.then(function(d) {
					swal({
						title : "Success!",
						text : "Job has been successfully added to your jobs list!",
						type : "success",
						timer:2000
					});
				},

				function(d) {
					swal({
						title : "Error!",
						text : "Something went wrong, please try again later",
						type : "error"
					});
				});
			}

		});
