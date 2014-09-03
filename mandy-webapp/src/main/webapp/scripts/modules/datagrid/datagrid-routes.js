/*global define:false */

/**
 * @file Datagrid router
 * @module mandy/datagrid
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define(['angular', 
        'angular-route', 
        'angular-cookies',
        'mandy-datagrid-service'], 
    function (angular) {
		'use strict';

		var module = angular.module('mdDatagridRoute', ['ngRoute', 'ngCookies', 'mdDatagridService']);


		// Configuration of the router
		// You need to add the "Provider" suffix to our custom provider, if not Angular doesn't recognize it
		module.config(['$routeProvider', function ($routeProvider) {
			
			 /**
			  * Accès au menu
			  */ 
			 $routeProvider.when('/datagrid', 
				{
        			redirectTo: '/'
				}
        	 );
			
			 /**
			  * Navigation
			  */
        	 $routeProvider.when(
    			 '/datagrid/year/:year/month/:month', 
    			 {
	                 templateUrl: "partials/datagrid.html",
	                 
	                 controller: 'DatagridController',
	                 
	                 resolve: {
	                	 datagrid: ['$route', 'CalendarService', function ($route, calendarService) {
	                		 var year = $route.current.params.year;
	                		 var month = $route.current.params.month;
	                		 
	                		 return calendarService.loadDataGrid(year, month);
	                	 }],
	                	 
	                	 activities: ['ActivityService', function (activityService) {
	                         return activityService.getUserActivities();
	                	 }],
	                	 
	                	 imputations: ['$rootScope', '$route', '$cookieStore', 'ImputationService',
	                	   function ($rootScope, $route, $cookieStore, imputationService) {
	                		 // $cookieStore permet de gérer le refresh sauvage
	                		 var userId = $cookieStore.get('user').resourceId;
	                		 var year = $route.current.params.year;
	                		 var month = $route.current.params.month;
	                         return imputationService.findImputations(userId, year, month);
	                	 }]
	                 }
	             }
        	 );	            
        }]); 
});
