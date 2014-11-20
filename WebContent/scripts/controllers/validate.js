angular.module('app').controller('validatecontroller',
		function($scope, initially) {

			$scope.contentType ={};
			$scope.contentType.maxLen=255;
			$scope.contentType.minLen=5;
			$scope.contentType.required=true;
			
			$scope.titleType ={};
			$scope.titleType.maxLen=Number(255);
			$scope.titleType.minLen=Number(5);
			$scope.titleType.required=true;
			
			
			$scope.companyNameType ={};
			$scope.companyNameType.maxLen=255;
			$scope.companyNameType.minLen=5;
			$scope.companyNameType.required=true;
			
			
			$scope.companyAddressType ={};
			$scope.companyAddressType.maxLen=255;
			$scope.companyAddressType.minLen=5;
			$scope.companyAddressType.required=true;
			
			$scope.infoForContactType ={};
			$scope.infoForContactType.maxLen=255;
			$scope.infoForContactType.minLen=5;
			$scope.infoForContactType.required=true;
			
			$scope.commentType ={};
			$scope.commentType.maxLen=255;
			$scope.commentType.minLen=5;
			$scope.commentType.required=true;
			
			$scope.descriptionType ={};
			$scope.descriptionType.maxLen=4095;
			$scope.descriptionType.minLen=5;
			$scope.descriptionType.required=true;
			
		


			
			
		});
