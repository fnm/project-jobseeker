
angular.module('app').factory('addTagService',function($http,authenticationService){
	  return {
          addTag: function(addTag) {
              var request = {};
              request.opcode = "addTagServiceRequest";
              request.Entity =addTag;
              var requestPromise=$http({
       method : 'POST',
          url : authenticationService.deploymentLink.link,
          data: request
      });
              return requestPromise;
          }
      }
	
});

