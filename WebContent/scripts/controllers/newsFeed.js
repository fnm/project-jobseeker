angular.module("app").controller(
		"NewsfeedCtrl",
		function($scope, $timeout, newsFeedList, entitiesService,
				postRequestsService, linkedinService, authenticationService,
				initially) {
			// var postPromise=postRequestsService.getListOfPosts();
			$scope.oneAtATime = true;
			// $scope.newsFeedLength = newsFeedLength;
			$scope.newsFeed = {};
			$scope.status = {
				isFirstOpen : true,
				isFirstDisabled : false
			};

			$scope.userprofile = linkedinService.getProfileData;
			/*
			 * postRequestsService.getListOfPosts().then( function(d){
			 * //alert(d.data); $scope.newsFeed=(d.data.posts); } ,function(d)
			 * {alert("error!")} );
			 */
			// $scope.newsFeedLength=newsFeedList.data.posts.length;
			$scope.newsFeedLength = newsFeedList.data.numberOfPostsInDB
			// $scope.currentCounterforPost=tagsArry.data.currentCounterforPost;
			// alert("curr"+$scope.currentCounterforPost)
			// alert($scope.newsFeedLength)
			/*
			 * initially.getTags().then(function(d){
			 * $scope.newsFeedLength=d.data.numberOfPosts; })
			 */

			$scope.newsFeed = newsFeedList.data.posts;

			// alert(angular.toJson($scope.newsFeed))
			// alert("length"+$scope.newsFeedLength.length);
			// alert(angular.toJson($scope.newsFeed))
			/*
			 * $scope.$watch('newsFeed',function(){
			 * 
			 * //$scope.newsFeedLength=30; });
			 */

			$scope.test = function(a) {
			}
			$scope.deletePost = function(postId, index) {

				var deletePostEntity = entitiesService.deletePostEntity(postId,
						$scope.userProfile.data.userId);
				var postPromise = postRequestsService
						.deletePost(deletePostEntity);

				postPromise.then(function(d) {
					// location.reload();
					var promise = postRequestsService.getListOfPosts(0, 30,
							"rev");
					promise.then(function(d) {
						$scope.newsFeed = d.data.posts;
						swal({
							title : "success!",
							text : "Post successfully deleted!",
							type : "success",
							timer:2000
						});

					})
				},

				function(d) {
					swal({
						title : "Error!",
						text : "Cannot delete comment, please try again later",
						type : "error"
					});
				});
			}

		});

angular.module("app").controller(
		"CommentCtrl",
		function($scope, entitiesService, commentRequestsService,
				linkedinService) {
			var x = 1;
			$scope.isShowComments = false;
			$scope.comments = {};
			$scope.comment.showDelete = true;
			$scope.status = {
				isFirstOpen : true,
				isFirstDisabled : false
			};

			$scope.hover = function(comment) {
				// Shows/hides the delete button on hover
				return $scope.comment.showDelete = !$scope.comment.showDelete;
			};

			$scope.addComment = function(form, userId) {
				var comment = {
					content : form.commentText
				};
				var commentEntity = entitiesService.commentEntity(userId,
						$scope.newsFeed[$scope.$index].postId,
						$scope.userProfile.data.firstName + " "
								+ $scope.userProfile.data.lastName
								+ comment.content);
				var commentPromise = commentRequestsService
						.addComment(commentEntity);
				commentPromise.then(function(d) { // $scope.commentAuthor= '';
					$scope.isShowComments = true;
					showComments();
					$scope.commentText = "";
				}, function(d) {
					swal({
						title : "Error!",
						text : "Something went wrong, please try again later",
						type : "error"
					});
				});

			};

			$scope.$watch('accordion.status', function(value) {
				if (value && x == 1) {
					x++;
					showComments();
				}
			});

			function showComments() {
				var commentsPromise = commentRequestsService.getListOfComments(
						$scope.userProfile.data.userId,
						$scope.newsFeed[$scope.$index].postId);// i have to
				// send postid
				// and userid
				commentsPromise.then(function(d) {
					$scope.comments = (d.data.comments);
				}, function(d) {
					swal({
						title : "Error!",
						text : "Cannot load comments, please try again later!",
						type : "error",
					});
				});
			}

			$scope.deleteComment = function(commentId) {

				var deleteCommentEntity = entitiesService.deleteCommentEntity(
						commentId, $scope.userProfile.data.userId);
				var postPromise = commentRequestsService
						.deleteComment(deleteCommentEntity);

				postPromise.then(function(d) {
					showComments();
				},

				function(d) {
					swal({
						title : "Error!",
						text : "Something went wrong, please try again later",
						type : "error",
						timer : 2000
					});
				});
			}

			/*
			 * $scope.showAllCommentForThisPost = function(userId,postId) {
			 * alert($scope.newsFeed[postId].postId);
			 * 
			 * var commentsPromise =
			 * commentRequestsService.getListOfComments(userId,$scope.newsFeed[postId].postId);//
			 * i have to send postid and userid commentsPromise.then(
			 * function(d){
			 * 
			 * $scope.comments =(d.data.comments); alert("success
			 * showAllCommentForThisPost!") } ,function(d) { alert("error!") } ); }
			 */

		});
