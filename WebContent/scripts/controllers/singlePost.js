angular.module("app").controller(
    "SinglePostCtrl",
    function ($scope, $timeout, entitiesService, postRequestsService,
              linkedinService, authenticationService, initially) {
        $scope.singlePost = {};
        $scope.oneAtATime = true;
        $scope.newsFeed = {};
        $scope.status = {
            isFirstOpen: true,
            isFirstDisabled: false
        };

        $scope.userprofile = linkedinService.getProfileData;

        $scope.newsFeed = ([entitiesService.singlePost]);

        $scope.deletePost = function (postId, index) {
            var deletePostEntity = entitiesService.deletePostEntity(postId,
                $scope.userProfile.data.userId);
            var postPromise = postRequestsService
                .deletePost(deletePostEntity);

            postPromise.then(function (d) {
                    swal({
                        title: "Success!",
                        text: "post successfully deleted!",
                        type: "success",
                        timer: 2000
                    });

                },

                function (d) {
                    swal({
                        title: "Error!",
                        text: "Cannot delete post, please try again later",
                        type: "error"
                    });
                });
        }

    });

angular
    .module("app")
    .controller(
    "CommentCtrl",
    function ($scope, entitiesService, commentRequestsService,
              linkedinService) {
        $scope.comments = {};
        $scope.status = {
            isFirstOpen: true,
            isFirstDisabled: false
        };

        $scope.addComment = function (form, userId) {
            // $scope.singlePost.commentText ="";
            var comment = {
                content: $scope.userProfile.data.firstName + " "
                + $scope.userProfile.data.lastName + ": "
                + form.commentText
            };
            var commentEntity = entitiesService.commentEntity(
                userId, $scope.newsFeed[$scope.$index].postId,
                comment.content);
            var commentPromise = commentRequestsService
                .addComment(commentEntity);
            commentPromise
                .then(
                function (d) {

                    showComments();

                },
                function (d) {
                    swal({
                        title: "Error!",
                        text: "Cannot add comment, please try again later",
                        type: "error"
                    });
                });

        };

        function showComments() {
            var commentsPromise = commentRequestsService
                .getListOfComments(
                $scope.userProfile.data.userId,
                $scope.newsFeed[$scope.$index].postId);// i
            // have
            // to
            // send
            // postid
            // and
            // userid
            commentsPromise
                .then(
                function (d) {

                    $scope.comments = (d.data.comments);

                },
                function (d) {
                    swal({
                        title: "Error!",
                        text: "Cannot load comments, please try again later",
                        type: "error"
                    });
                });
        }

        $scope.deleteComment = function (commentId) {

            var deleteCommentEntity = entitiesService
                .deleteCommentEntity(commentId,
                $scope.userProfile.data.userId);
            var postPromise = commentRequestsService
                .deleteComment(deleteCommentEntity);

            postPromise
                .then(
                function (d) {
                    showComments();
                },

                function (d) {
                    swal({
                        title: "Error!",
                        text: "Cannot delete comment, please try again later",
                        type: "error"
                    });
                });
        };


        $scope.showComments = function () {
            showComments();
            $scope.isShowComments = true;
        };
    });
