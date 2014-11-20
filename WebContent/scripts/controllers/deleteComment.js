'use strict';

angular.module('app')
  .controller('DeletecommentCtrl', function ($scope ,entitiesService,commentRequestsService) {
	  $scope.deleteComment = function() {
            var userId=parseInt($scope.userId);
            var commentId=parseInt($scope.commentId);  
            var commentEntity=entitiesService.commentEntity(commentId,"",userId,"");
            var commentPromise=commentRequestsService.deleteComment(postEntity);        
            commentPromise.then(
           	function(d)
           		{alert(angular.toJson(d.data)); alert("success")}
           	,function(d)
           		{alert("error!")});
     };          
  });
