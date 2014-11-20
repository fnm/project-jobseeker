
angular.module('app').factory('initially',function($http,authenticationService){
	
	
	return{
		getTags:function(){
			var promise=$http({
				method : 'POST',
				url : authenticationService.deploymentLink.link,
				data :{"opcode":"initializeRequest","Entity":{"userId":1}} 
			});
			return promise;
		},
		
		getUserTypes : {userTypes : ""},
		
		pagingNewsFeed: {newsFeedLength:""},
	
		getTagsArr : {resultTagsArr:""},
	


		getUsersArr : {userList : ""},

		getCurrentMaxPostId : {CurrentMaxPostId:""}
}	
});
