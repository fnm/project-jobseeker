'use strict';

angular.module('app').controller(
		'AddcommentCtrl',
		function($scope, entitiesService, commentRequestsService) {
			var commentId = 1;
			$scope.getListOfComments = function() {
				var postId = parseInt($scope.postId);
				var commentEntity = entitiesService.commentEntity("", postId,
						"", "");
				;
				var commentPromise = commentRequestsService
						.getListOfComments(commentEntity);
				commentPromise.then(function(d) {
					$("#newsfeed").prepend(
							"<h2 style='cursor:pointer;' id='"
									+ d.data.commentId + "' click='showId("
									+ d.data.commentId + ")'>" + $scope.title
									+ "</h2>");
					var inVar = document.getElementById(d.data.commentId);
					inVar.addEventListener("click", showId);

				}, function(d) {
					swal({
						title : "Error!",
						text : "Cannot load comments, please try again later",
						type : "error",
						timer: 2000
					});
				});
			};
		});

function showId(val) {
	var id = val.target.id;
	
}
