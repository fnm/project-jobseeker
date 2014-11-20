/*angular.module('myApp', [ 'ab-base64' ]).controller('myController', [

'$scope', 'base64', function($scope, base64) {

	$scope.encoded = base64.encode('a string');
	$scope.decoded = base64.decode('YSBzdHJpbmc=');
} ]);*/

angular
		.module('app')
		.controller('AddpostCtrl',function($scope, $q, entitiesService,postRequestsService, linkedinService,
authenticationService, initially, $location,$route) {
							$scope.log = [];
							$scope.post = {}
							var postId = 1;
							$scope.addPost = function() {
								var postEntity = entitiesService.postEntity(
										($scope.post.title),
										($scope.post.content),
										$scope.userProfile.data.userId,
										$scope.log);

								var postPromise = postRequestsService
										.addPost(postEntity);

								postPromise
										.then(
												function(d) {
													swal({
														title : "Success!",
														text : "Your Post Has been Published!",
														type : "success",
														timer : 2000
													});
													$scope.log = [];
													$scope.post.tags.value;
													$scope.post.tags = "";
													$scope.post.content = "";
													$scope.post.title = "";
													$location
															.path("/newsFeed/");
													$route.reload();

												},
												function(d) {
													swal({
														title : "Error!",
														text : "Something went wrong, please try again later",
														type : "error"
													});
												});
							};

							$scope.loadTags = function(query) {
								var deferred = $q.defer();
								var i;
								// here we do filter with the current text
								var result = new Array();

								var a = initially.getTagsArr.resultTagsArr;
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
								$scope.log.push(tag.tagId);
							};

							$scope.tagRemoved = function(tag) {
								var x = $scope.log.indexOf(tag.tagId);
								$scope.log.splice(x, tag.tagId);
							};
						});