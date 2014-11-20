//LinkedIn functions
//Execute on load profile 



function onLinkedInLoad() {

	IN.Event.on(IN, "auth", function() {
		onLinkedInLogin();	return;
	});
	IN.Event.on(IN, "logout", function() {
		onLinkedInLogout(); return;
	});
}

//execute on logout event
function onLinkedInLogout() {
    IN.parse();
}

//execute on login event
function onLinkedInLogin() {

	// pass user info to angular
	angular.element(document.getElementById("appBody")).scope().$apply(
		function($scope) {
		
			$scope.getUserProfile();
		}
	);

}

function onLinkedInNoLogin() {
	angular.element(document.getElementById("appBody")).scope().$apply(
		function($scope) {
		
			$scope.userNotConnected();
		}
	);

}


