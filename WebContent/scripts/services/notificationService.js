angular.module('app').factory('notificationService',function($http, authenticationService){
	
	return{
		
		getNotification: function(userId)
       {		
			var userId=userId;
		var requestPromise=$http({
            method : 'POST',
            url : authenticationService.deploymentLink.link,
            data: {"opcode":"listOfNotificationsRequest","Entity":{"userId":userId}}
        });
                return requestPromise;   
        },
	getNotificationEntity: function(notificationTypeId,notificationForeignId,notificationId)
    {		
	var requestPromise=$http({
        method : 'POST',
        url : authenticationService.deploymentLink.link,
        data: {"opcode":"singleNotificationEntityRequest","Entity":{"entityId":notificationForeignId,"typeId":notificationTypeId,"notificationId":notificationId}}
    });
	return requestPromise;
	}
	}
	

});