/**
 * Created by Ahmad on 9/23/2014.
 */

angular.module('MainApplication')
    .controller('loginController', function ($scope, $rootScope) {
        $scope.getLinkedInData = function () {
            IN.API.Profile("me").fields(["id", "firstName", "lastName", "publicProfileUrl", "email-address"])
                .result(function (profiles) {
                    $rootScope.userProfile = profiles.values[0];
                    //...
                })

                .error(function (err) {
                    $scope.error = err;
                })
        }

        $scope.logoutLinkedIn = function () {
            IN.User.logout();
            delete $rootScope.userProfile;
            //...
        }
    });