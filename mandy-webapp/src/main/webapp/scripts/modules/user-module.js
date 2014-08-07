/*global define:false */

/**
 * @file Login service
 * @module mandy/login
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define(['angular', 
        'angular-route',
        'angular-resource',
        'mandy-common'], 
        function (angular) 
        {
    		'use strict';

    		var module = angular.module('UserModule', ['ngRoute', 'ngResource', 'CommonModule']);
    		
    		module.service('LoginService', ["$resource", "CONTEXT_ROOT", "Utils", function ($resource, CONTEXT_ROOT) {
            	
            	var LoginResource = $resource(CONTEXT_ROOT+"/api/login");
            	
                /**
                 * @alias mandy/login/service
                 */
                return {
                    /**
                     * Try to authenticate the user.
                     */
                    login: function (login, password) {
                    	
                        return LoginResource.save({login:login, password:password}, 
	                        		angular.noop,
		                			function (err) 
		                			{ 
                        				// cas d'erreur
			                			var message = "Erreur technique login : "+err;
			                        	console.log(message);
			                        	alert(message);
		                			}
	                		   );
                    }
                };
            }]);
    		
    		module.service('UserService', ["$resource", "CONTEXT_ROOT", "Utils", function ($resource, CONTEXT_ROOT) {
            	
            	var UserResource = $resource(CONTEXT_ROOT+"/api/user/session");
            	
                /**
                 * @alias mandy/user/service
                 */
                return {
                    /**
                     * Returns the current user data.
                     */
                    getUserData: function () {                    	
                        return UserResource.get({}, 
	                        		function(data){
			                        	console.log(data);
			                        },
		                			function (err) 
		                			{ 
                        				// cas d'erreur
			                			var message = "Technical error on get user info : "+err;
			                        	console.log(message);
			                        	alert(message);
		                			}
	                		   );
                    }
                };
            }]);

    		// Configuration of the router
    		// You need to add the "Provider" suffix to our custom provider, if not Angular doesn't recognize it
    		module.config(['$routeProvider', function ($routeProvider) {
	            // Declare module routes
	        	 $routeProvider.when('/login', 
        			 {
		                 templateUrl: "partials/login.html",
		                 controller: 'LoginController'
		             }
	        	 );	            
	        }]);

	        // Configuration of the controller
	        module.controller('LoginController', ['$scope', 'LoginService', function ($scope, loginService) {
	            $scope.login = function() {
	            	loginService.login($scope.login, $scope.password);
	            };
	        }]);
	        
	        return module;
        }
);