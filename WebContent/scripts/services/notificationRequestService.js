angular.module('servicesModule').factory('notificationRequestsService', function($http,authenticationService) {
        return {
        	
        	lastNotification: function(notificationEntity) {
                var msg = {};
                msg.opcode = "lastNotificationRequest";
                msg.Entity =notificationEntity;
                var notificationPromise=$http({
         method : 'POST',
            url : authenticationService.deploymentLink.link,
            data: msg
        });
                return notificationPromise;
            }
        }
});