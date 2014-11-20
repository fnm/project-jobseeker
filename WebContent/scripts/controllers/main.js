
angular.module('app')
		  .controller('MainviewCtrl', function ($scope,linkedinService,initially) {
			  $scope.userprofile=linkedinService.getProfileData;  
			  
			  
		  });




