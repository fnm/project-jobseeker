
angular.module('app').directive('paging', function (initially,postRequestsService) {

    // Assign null-able scope values from settings
    function setScopeValues(scope, attrs) {
        scope.List = [];
        scope.Hide = false;
        scope.page = parseInt(scope.page) || 1;
        scope.newsFeedLength = parseInt(scope.newsFeedLength) || 0;
        scope.newsFeed= scope.newsFeed;
        scope.pageSize=parseInt(scope.pageSize) || 10;
        scope.dots = scope.dots || '...';
        scope.ulClass = scope.ulClass || 'pagination';
        scope.adjacent = parseInt(scope.adjacent) || 2;
        scope.activeClass = scope.activeClass || 'active';
		scope.disabledClass = scope.disabledClass || 'disabled';
	//	scope.newsFeedd=1;
        scope.scrollTop = scope.$eval(attrs.scrollTop);
        scope.hideIfEmpty = scope.$eval(attrs.hideIfEmpty);
        scope.showPrevNext = scope.$eval(attrs.showPrevNext);

    }


    // Validate and clean up any scope values
    // This happens after we have set the
    // scope values
    function validateScopeValues(scope, pageCount) {

        // Block where the page is larger than the pageCount
        if (scope.page > pageCount) {
            scope.page = pageCount;
        }

        // Block where the page is less than 0
        if (scope.page <= 0) {
            scope.page = 1;
        }

        // Block where adjacent value is 0 or below
        if (scope.adjacent <= 0) {
            scope.adjacent = 2;
        }

        // Hide from page if we have 1 or less pages
        // if directed to hide empty
        if (pageCount <= 1) {
            scope.Hide = scope.hideIfEmpty;
        }
    }



    // Internal Paging Click Action
    function internalAction(scope, page) {
        var arr=[];
    	
        // Block clicks we try to load the active page
        if (scope.page == page) {
            return;
        }

        // Update the page in scope and fire any paging actions
        scope.page = page;
		
        scope.pagingAction({
		
            page: page
        });

        // If allowed scroll up to the top of the page
        if (scope.scrollTop) {
            scrollTo(0, 0);
        }
    }


    // Add Range of Numbers
    function addRange(start, finish, scope) {

        var i = 0;
        for (i = start; i <= finish; i++) {

            var item = {
                value: i,
                title: 'Page ' + i,
                liClass: scope.page == i ? scope.activeClass : '',
                action: function () {
                    internalAction(scope, this.value);
                }
            };
          
           // scope.List.push(item);
        }
    }


    // Add Dots ie: 1 2 [...] 10 11 12 [...] 56 57
    function addDots(scope) {
        scope.List.push({
            value: scope.dots
        });
    }


    // Add First Pages
    function addFirst(scope) {
        addRange(1, 2, scope);
        addDots(scope);
    }


    // Add Last Pages
    function addLast(pageCount, scope) {
        addDots(scope);
        addRange(pageCount - 1, pageCount, scope);
    }


    // Adds the first, previous text if desired   
    function addPrev(scope, pageCount) {

        // Ignore if we are not showing
		// or there are no pages to display
        if (!scope.showPrevNext || pageCount < 1) {
            return;
        }

        // Calculate the previous page and if the click actions are allowed
        // blocking and disabling where page <= 0
        var disabled = scope.page - 1 <= 0;
        var prevPage = scope.page - 1 <= 0 ? 1 : scope.page - 1;

        var first = {
            value: '<<',
            title: 'First Page',
            liClass: disabled ? scope.disabledClass : '',
            action: function () {
                if(!disabled) {
                    internalAction(scope, 1);
                }
            }
        };
      
        var prev = {
            value: '<',
            title: 'Previous Page',
            liClass: disabled ? scope.disabledClass : '',
            action: function () {
                if(!disabled) {
                	var postId;
                	
                	
                    postId=scope.newsFeed[20].postId+10; 
                	postRequestsService.getListOfPosts(postId,10,"rev").then(
                            function(d)
                            {
                            	
                            
                            	  for(i=0;i<=9;i++)
                          	    {
                          			scope.newsFeed[i+10]=scope.newsFeed[i];
                          			scope.newsFeed[i]=scope.newsFeed[i+20];
                          			scope.newsFeed[i+20]=d.data.posts[i];
                          			// console.log(d.data.posts[i]);
                          			
                          			
                          	    }	
                            	  scope.pageSize=10;
 	
                            }
                            ,function(d)
                            {alert("error next newsFeed!")});
                       
                	
                	
                	//alert(angular.toJson(scope.newsFeed))
                	
                    internalAction(scope, prevPage);
                }
            }
        };

      //  scope.List.push(first);
        scope.List.push(prev);
    }


    // Adds the next, last text if desired
    function addNext(scope, pageCount) {

        // Ignore if we are not showing 
		// or there are no pages to display
        if (!scope.showPrevNext || pageCount < 1) {
            return;
        }

        // Calculate the next page number and if the click actions are allowed
        // blocking where page is >= pageCount
        var disabled = scope.page + 1 > pageCount;
        var nextPage = scope.page + 1 >= pageCount ? pageCount : scope.page + 1;

        var last = {
            value: '>>',
            title: 'Last Page',
            liClass: disabled ? scope.disabledClass : '',
            action: function () {
                if(!disabled){
                    internalAction(scope, pageCount);
                }
            }
        };

        var next = {
            value: '>',
            title: 'Next Page',
            liClass: disabled ? scope.disabledClass : '',
         		
            action: function () {
                if(!disabled){
                	//scope.pageSize=10;
                	var postId;
                	var counter=0;
                	if(scope.newsFeed[19]!=undefined){
                    postId=scope.newsFeed[19].postId; 
              /*      for(j=0;j<=9;j++)
              	    {
              			scope.newsFeed[j+20]=scope.newsFeed[j];
              			scope.newsFeed[j]=scope.newsFeed[j+10];
              			console.log(scope.newsFeed[j+10]);
              			
              	    }*/
                	postRequestsService.getListOfPosts(postId-1,10,"rev").then(
                            function(d)
                            {
                            	
                            
                            	  for(i=0;i<=9;i++)
                          	    {
                          			scope.newsFeed[i+20]=scope.newsFeed[i];
                          			scope.newsFeed[i]=scope.newsFeed[i+10];
                          			scope.newsFeed[i+10]=d.data.posts[i];
                          		  // console.log(d.data.posts[i]);
                          			
                          	    }	
 	
                            }
                            ,function(d)
                            {alert("error next newsFeed!")});
                       
                	}
                	else
                		{
                		
                		  for(i=0;i<=9;i++)
                    	    {
                			 // alert("aa")
                			  if(scope.newsFeed[i+10]==undefined){counter++}
                		scope.newsFeed[i+20]=scope.newsFeed[i];
              			scope.newsFeed[i]=scope.newsFeed[i+10];
              			
                    	    }
                		 // alert("cont"+counter)
                		  scope.pageSize=10-counter;
                		} 
                	//alert(angular.toJson(scope.newsFeed))
                    internalAction(scope, nextPage);
                }
            }
        };

        scope.List.push(next);
       // scope.List.push(last);
    }


    // Main build function
    function build(scope, attrs) {
//alert(scope.newsFeed)

        // Block divide by 0 and empty page size
        if (!scope.pageSize || scope.pageSize < 0) {
            return;
        }

        // Assign scope values
        setScopeValues(scope, attrs);

        // local variables
        var start,
            size = scope.adjacent * 2,pageCount;
           
            pageCount = Math.ceil(scope.newsFeedLength / 10);
              
        // Validate Scope
        validateScopeValues(scope, pageCount);

        // Calculate Counts and display
        addPrev(scope, pageCount);
        if (pageCount < (5 + size)) {

            start = 1;
            addRange(start, pageCount, scope);

        } else {

            var finish;

            if (scope.page <= (1 + size)) {

                start = 1;
                finish = 2 + size + (scope.adjacent - 1);

                addRange(start, finish, scope);
                addLast(pageCount, scope);

            } else if (pageCount - size > scope.page && scope.page > size) {

                start = scope.page - scope.adjacent;
                finish = scope.page + scope.adjacent;

                addFirst(scope);
                addRange(start, finish, scope);
                addLast(pageCount, scope);

            } else {

                start = pageCount - (1 + size + (scope.adjacent - 1));
                finish = pageCount;

                addFirst(scope);
                addRange(start, finish, scope);

            }
        }
        addNext(scope, pageCount);

    }


    // The actual angular directive return
    return {
        restrict: 'EA',
        scope: {
		    List: '@',
            page: '=',
            pageSize: '=',
            newsFeedLength: '=',
            newsFeed: '=',
            dots: '@',
            hideIfEmpty: '@',
            ulClass: '@',
			activeClass: '@',
			disabledClass: '@',
            adjacent: '@',
            scrollTop: '@',
            showPrevNext: '@',
            pagingAction: '&'
          
        },
        template: 
			'<ul ng-hide="Hide" ng-class="ulClass"> ' +
				'<li ' +
				' ng-repeat="Item in List"' +
				'ng-class="Item.liClass" ' +
				'style="cursor:pointer" ' +
				'ng-click="Item.action()" ' +
				'title="{{Item.title}}"> ' +
				'<span ng-bind="Item.value"></span> ' +
            '</ul>',
        link: function (scope, element, attrs) {
            scope.$watch('page', function () {
                build(scope, attrs,initially.getCurrentMaxPostId.CurrentMaxPostId);
           /* alert(initially.getCurrentMaxPostId.CurrentMaxPostId)*/
            });
            //authenticationService.userProfile.data=result.values[0];
           // scope.newsFeed=authenticationService.userProfile;
           
        }
    };
});
