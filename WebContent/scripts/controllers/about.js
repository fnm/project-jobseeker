
angular.module('app')
  .controller('AboutCtrl', function ($scope,serviceEx) {
	  
	  $scope.serviceFun=function(){alert(serviceEx.myFunction())};

  });
