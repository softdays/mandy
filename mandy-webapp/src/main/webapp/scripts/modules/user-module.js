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
        'angular-cookies',
        'mandy-common'], 
        function (angular) 
        {
    		'use strict';

    		var module = angular.module('UserModule', ['ngRoute', 'ngResource', 'ngCookies', 'CommonModule']);
    		
   		
    		module.service('UserService', ['$resource', '$cookieStore', 'CONTEXT_ROOT', function ($resource, $cookieStore, CONTEXT_ROOT) {
            	
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
			                        	$cookieStore.put('user', data);
			                        	console.log("user resource id from cookie: "+$cookieStore.get('user').resourceId);
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
	        
	        return module;
        }
);