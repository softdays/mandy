/*global define:false */

/**
 * @file We configure here the main module of our Angular Js application
 * @module mandy/app
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define(['angular', 
        'angular-route', 
        'mandy-common',
        'mandy-user',
        'mandy-datagrid'], 
        function (angular) 
        {
    		'use strict';

    		var app = angular.module('mandyApp', ['ngRoute', 'CommonModule', 'UserModule', 'DatagridModule']);
    		
    		app.run(["$rootScope", function ($rootScope) {
					// définition des variables globales
				    // il est nécessaire de les déclarer dès l'initialisation car 
					// sinon elles ne sont pas partagées par l'ensemble des
					// controllers
				    // Achtung: ne pas généraliser
		        	$rootScope.user = undefined;
		        	$rootScope.selectedTab = undefined;
		        	$rootScope.contextRoot = undefined;
		        	$rootScope.admin=false;
				}		
			]);
	
	        // Configuration of the router
	        app.config(['$routeProvider', '$locationProvider', '$logProvider',
                function ($routeProvider, $locationProvider, $logProvider) 
                {
	        	 	$logProvider.debugEnabled(true);	        	 	
  
	        	 	// Declare the basic routes
	        	 	$routeProvider.when('/', 
						{
	        	 			//redirectTo: '/datagrid',
	        	 			templateUrl: "partials/loading.html",
	        	 		    controller: 'MainController',
	        	 			resolve: {
	        	 				user: ['UserService', function (userService) {
			                         return userService.getUserData();
			                	 }]
	        	 			}
						}
			        );	
	        	 	
	        	 	$routeProvider.when('/404', 
						{
							templateUrl: "partials/error.html"
						}
		        	);	       
		            
		            $routeProvider.otherwise(
		            	{
		            		redirectTo: '/404'
		            	}
		            );
		        }
	        ]);
	
	        // Definition of the main controller
	        app.controller('MainController', ['$rootScope', '$scope', '$location', 'CONTEXT_ROOT', 'user', "Utils", 
	            function ($rootScope, $scope, $location, CONTEXT_ROOT, user, utils) {
	        		$rootScope.contextRoot = CONTEXT_ROOT;
	        		$rootScope.user = user;
//	        		$rootScope.isAdmin = function() { 
//	        			return user.role == 'ROLE_ADMIN' || user.role == 'ROLE_MANAGER'; 
//	        		};
	        		user.$promise.then(function(resolvedUser) {
	        			$rootScope.admin = (resolvedUser.role == 'ROLE_ADMIN' || resolvedUser.role == 'ROLE_MANAGER');
	        	    });
	        		var param = utils.buildPathForCurrentMonth();
	        		// param format =  yyyy/MM[01-12]
	        		$location.path("/datagrid/"+param);
	        		
	        }]);
	        
	        var contextRootValue = window.location.pathname;
			contextRootValue = contextRootValue.substring(0, contextRootValue.indexOf("/", 1));
			
			app.constant('CONTEXT_ROOT', contextRootValue);
			
			app.provider("ContextRoot", function() {
				this.$get = function() {
				    return {
				      getValue: function() {
				        return "/mandy";
				      }
				    };
				 };
			});
	        
	        return app;
});