
 // Main module of the application.
angular.module('servicesModule',[]);
angular.module('app', ['ngRoute','servicesModule','ui.bootstrap','ngTagsInput','angular-loading-bar','ui.bootstrap.datetimepicker'])
  .config(function ($routeProvider, $locationProvider,cfpLoadingBarProvider) {
	  cfpLoadingBarProvider.includeSpinner = false;
    $routeProvider.when('/login', {
		templateUrl : 'views/login.html',
		controller : 'LoginCtrl'
    }).when('/main/', {
		templateUrl : 'views/main.html',
		controller : 'LinkedinCtrl'
	}).when('/addPost/', {
          templateUrl: 'views/addPost.html',
          controller: 'AddpostCtrl',
   /*     	   resolve: {
               	
             	  tagsArry: function(initially){
                       return initially.getTags();
                   }
                  
                   
               }*/  
        	  
        }).when('/deletePost/', {
            templateUrl: 'views/deletePost.html',
            controller: 'DeletepostCtrl'
          }).when('/addAllowedUser/', {
              templateUrl: 'views/addAllowedUser.html',
              controller: 'AddAllowedUser'
            })
          .when('/addJob/', {
            templateUrl: 'views/addJob.html',
            controller: 'AddjobCtrl'
          }) .when('/newsFeed/', {
              templateUrl: 'views/newsFeed.html',
              controller: 'NewsfeedCtrl',
              resolve: {
            
            	/*  newsFeedLength: function(authenticationService){
                      return authenticationService.getNesFeedLength();
                  },*/ 	
                  newsFeedList: function(postRequestsService,initially,$timeout){
                      
                      return postRequestsService.getListOfPosts(0,30,"rev");
                	 
                	  }
                 
                  
              }
            }).when('/myJobList/', {
                templateUrl: 'views/myJobsList.html',
                controller: 'MyJobsListCtrl'
              }).when('/showMore/', {
                templateUrl: 'views/myJob.html',
                controller: 'MyJobCtrl'
              }).when('/addEvent/', {
                  templateUrl: 'views/addEventToJob.html',
                  controller: 'AddEventToJobCtrl'
                }).when('/extendJob/', {
                    templateUrl: 'views/publicJob.html',
                    controller: 'publicJobCtrl'
                  }).when('/extendPost/', {
                      templateUrl: 'views/publicPost.html',
                      controller: 'publicPostCtrl'
                    }).when('/searchResult/', {
                    templateUrl: 'views/searchResult.html',
                    controller: 'searchResultController'
                  }).when('/notification/', {
                      templateUrl: 'views/notification.html',
                     
                    }).when('/singlePost/', {
                      templateUrl: 'views/singlePost.html',
                       controller: 'SinglePostCtrl'
                    })
      .otherwise({
        redirectTo: '/login'
      
      })
    	      
  }).run( function($rootScope,$window, $location,authenticationService) {

	    // register listener to watch route changes
	    $rootScope.$on( "$routeChangeStart", function(event, next, current) {
	     if (authenticationService.userLoggedIn.status!=true) {
	        // no logged user, we should be going to #login
	    	// alert("run"+authenticationService.userProfile.data.loggedIn)
	          // not going to #login, we should redirect now
	    	 //$window.location.reload();
	    	
	    	 $location.url('/login')
	    	/* alert($location.url())
		    	if($location.url()!="/"){
		    	 console.log(next.$$route.originalPath)
		    	 if(next.$$route.originalPath=="/login"){
		    	 $location.url('/login')}
		    	 else{
		    		 $location.url('views/index.html');
		    		 
		    	 }
		    	}*/
	    	
	    		 
	    	 
	     }
	    	 /* alert("a")
	    	 $route.reload();*/
	    	// event.preventDefault();
	    	// $location.path("/login");
	    
	    	 
	    	 /*	}else{$location.path( "/login" );}*/
	      
	    });
  });

