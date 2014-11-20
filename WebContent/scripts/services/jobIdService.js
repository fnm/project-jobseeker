angular.module('app').factory('jobIdService',function() {
var jobEntity = {};
var eventId={};
return {
	
    setJobEntity: function(newJobEntity) {
        jobEntity = newJobEntity;
    },
    getJobEntity: function() {
        return jobEntity;
    },
    
    getEventId:function(){
    	return eventId;
    },
    setEventId:function(eventId){
     eventId=evnetId;	
    }
	};
});