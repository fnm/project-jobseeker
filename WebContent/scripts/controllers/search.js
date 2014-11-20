angular.module('app').controller(
		'searchController',
		function($location, $scope, $q, entitiesService, authenticationService,
				postRequestsService, jobRequestsService, $route, initially,
				publicJobIdService) {
			$scope.log = [];
			$scope.offset = 0;
			$scope.status = {
				isFirst : true
			};
			$scope.dir = "";
			$scope.numberOfItems = 20;

			$scope.loadTags = function(query) {
				var deferred = $q.defer();
				var i;
				// here we do filter with the current text

				var result = [];

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

			$scope.search = function() {

				var entity = entitiesService.searchPostEntity($scope.log, "",
						$scope.userProfile.data.userId, $scope.offset,
						$scope.dir, $scope.numberOfItems);

				var promiss = postRequestsService.searchPost(entity);
				promiss.then(function(d) {
					// alert(d.data);
					authenticationService.posts = (d.data.posts);

				}, function(d) {

					swal({
						title : "Error!",
						text : "Something went wrong, please try again later",
						type : "error"
					});
				});
				var entity = entitiesService.searchJobEntity($scope.log, "",
						$scope.userProfile.data.userId, $scope.offset,
						$scope.dir, $scope.numberOfItems);

				var promiss = jobRequestsService.searchJob(entity);
				promiss.then(function(d) {
					authenticationService.jobs = (d.data.jobs);
					$location.path("/searchResult");
					$route.reload();

				}, function(d) {
					swal({
						title : "Error!",
						text : "Something went wrong, please try again later",
						type : "error"
					});
				});

				$scope.log = [];
				// $scope.tags="";

			};

			$scope.extendJob = function(clickedJob) {
				publicJobIdService.setPublicJobEntity(clickedJob); // save the
				// clicked
				// job in
				// other
				// service
				// for use
				// it when
				// showing
				// it in
				// single
				// page
			};

		});
