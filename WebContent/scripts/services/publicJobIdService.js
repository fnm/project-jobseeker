angular.module('app').factory('publicJobIdService',function() {
var publicJobEntity = {};

return {
	
    setPublicJobEntity: function(newPublicJobEntity) {
    	publicJobEntity = newPublicJobEntity;
    },
    getPublicJobEntity: function() {
        return publicJobEntity;
    }
	};
});