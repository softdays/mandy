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
    		
   		
    		module.service('UserService', ['$resource', '$cookieStore', '$log', 'CONTEXT_ROOT', 'ErrorService', 
    		    function ($resource, $cookieStore, $log, CONTEXT_ROOT, errorService) {
            	
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
			                        	$log.debug("user id retrieved from cookie: "+$cookieStore.get('user').resourceId);
			                        },
		                			function (httpResponse) 
		                			{ 
			                			errorService.errorHandler(httpResponse, 
        									"Erreur technique récupération infos utilisateur");
		                			}
	                		   );
                    }
                };
            }]);
	        
	        return module;
        }
);