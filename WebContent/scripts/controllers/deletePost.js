'use strict';

angular.module('app').controller(
		'DeletepostCtrl',
		function($scope, entitiesService, postRequestsService) {

			// $scope.id;
			// $scope.title;
			// $scope.body;

			$scope.deletePost = function() {
				var userId = parseInt($scope.userId);
				var postId = parseInt($scope.postId);

				var postEntity = entitiesService.postEntity(postId, "", "", "",
						userId);
				// alert(tagsIds);
				var postPromise = postRequestsService.deletePost(postEntity);

				postPromise.then(function(d) {
					swal({
						title : "Success!",
						text : "Your post has been deleted!",
						type : "info",
						timer: 2000
					});
				}, function(d) {
					swal({
						title : "Error!",
						text : "Something went wrong, please try again later",
						type : "error",
						timer: 2000
					});
				});
			};
		});
