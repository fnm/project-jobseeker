angular.module("app").controller("MyJobsListCtrl",
		function($scope, jobRequestsService, jobIdService) {

			$scope.oneAtATime = true;
			var userId = $scope.userProfile.data.userId;
			jobRequestsService.getMyListOfJobs(userId).then(function(d) {
				$scope.jobs = (d.data.jobs);
			}, function(d) {
				swal({
					title : "Error!",
					text : "Something went wrong, please try again later",
					type : "error",

				});
			});

			$scope.showMore = function(jobEntity) {
				jobIdService.setJobEntity(jobEntity);

			};

		});