/*global define:false */

/**
 * @file Datagrid module
 * @module mandy/datagrid
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define(['angular', 
        'angular-route',
        'angular-resource',
        'mandy-common',
        'mandy-user'], 
        function (angular) 
        {
    		'use strict';

    		var module = angular.module('DatagridModule', ['ngRoute', 'ngResource', 'CommonModule', 'UserModule']);
    		
    		module.service('CalendarService', ["$resource", "CONTEXT_ROOT", "Utils", function ($resource, CONTEXT_ROOT, utils) {
            	
            	var Calendar = $resource(CONTEXT_ROOT+"/api/datagrid/:year/:month");
            	
                /**
                 * @alias mandy/calendar/service
                 */
                return {
                    /**
                     * Retourne la datagrid pour l'année et le mois indiqué
                     */
                    loadDataGrid: function (year, month) {
                    	
                    	//var fdate = utils.formatDate(date);
                    	
                        return Calendar.get({year:year, month:month}, 
	                        		angular.noop,
		                			function (err) 
		                			{ 
                        				// cas d'erreur
			                			var message = "Erreur technique récupération grille de données : "+err;
			                        	console.log(message);
			                        	alert(message);
		                			}
	                		   );
                    }
                };
            }]);
    		
    		module.service('ActivityService', ["$resource", "CONTEXT_ROOT", function ($resource, CONTEXT_ROOT) {
            	
            	var Activities = $resource(CONTEXT_ROOT+"/api/activities");
            	
                /**
                 * @alias mandy/activity/service
                 */
                return {
                    /**
                     * Retourne la datagrid pour la date indiquée
                     */
                    getUserActivities: function() {
                    	
                        return Activities.query({}, 
	                        		angular.noop,
		                			function (err) 
		                			{ 
                        				// cas d'erreur
			                			var message = "Erreur technique récupération activités : "+err;
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
	        	 $routeProvider.when(
        			 '/datagrid/year/:year/month/:month', 
        			 {
		                 templateUrl: "partials/datagrid.html",
		                 controller: 'CalendarController',
		                 resolve: {
		                	 datagrid: ['$route', 'Globals', 'Utils', 'CalendarService', function ($route, globals, utils, calendarService) {
		                		 var year = $route.current.params.year;
		                		 var month = $route.current.params.month;
				                 console.log("resolve datagrid for year="+year+" and month="+month);
		                		 //var dateRef = utils.resolveDateWithOffset(globals.getCurrentDate(), offset);
		                		 //console.log("dateRef= "+dateRef);
		                         return calendarService.loadDataGrid(year, month);
		                	 }],
		                	 activities: ['ActivityService', function (activityService) {
		                         return activityService.getUserActivities();
		                	 }]
		                	 /*,
		                	 imputations: ['ImputationService', function (imputationService) {
		                         return imputationService.loadExistingImputations(commonData.getUserId());
		                	 }]*/
		                 }
		             }
	        	 );	            
	        }]);

	        // Configuration of the controller
	        module.controller('CalendarController', ['$rootScope', '$scope', '$location', 'Globals', 'Utils', 'datagrid', 'activities',
	            function ($rootScope, $scope, $location, globals, utils, datagrid, activities) {
	        	// global model
	        	$rootScope.selectedTab = 1;
	            // Definition of the model
	        	$scope.currentDate = globals.getCurrentDate();	        	
	            $scope.datagrid = datagrid;	     
	            $scope.datagrid.$promise.then(function() {
	            	$scope.formattedMonth = utils.formatMonthLabel(datagrid.month);
	            });	    
	            $scope.activities = activities;
	            // Actions
	            $scope.displayNextMonth = function(year, month){
	            	$location.path("/datagrid/"+utils.buildPathForNextMonth(year, month));
	            };
	            
	            $scope.displayPreviousMonth = function(year, month){
	            	$location.path("/datagrid/"+utils.buildPathForPreviousMonth(year, month));
	            };
	            
	            $scope.displayCurrentMonth = function(){
	            	$location.path("/datagrid/"+utils.buildPathForCurrentMonth());
	            };
	        }]);
	        
	        module.directive('mandyDatagridHeader', ["Utils", function(utils) {
	            return {
	                restrict: 'A',
	                scope:false,
	                replace: false,
	                transclude:false,
	                link: function (scope, element) {	                   
	                    scope.datagrid.$promise.then(function(resolvedDatagrid){
	                    	var weeks = resolvedDatagrid.weeks;
	                    	var elemDays = angular.element('<tr class="days"></tr>');
	                    	var activities = angular.element('<th class="header-activities">Activités</th>');
	                    	elemDays.append(activities);	
		                    angular.forEach(weeks, function(week, index) {
		                        angular.forEach(week.days, function(day, dIndex) {
		                        	var elemDay = angular.element('<th class="day"></th>');		                        	
		                        	elemDay.text(utils.getDay(day.date));
		                        	if (utils.areMonthsEqual(resolvedDatagrid.month, day.date)) {
		                        		elemDay.addClass("current-month");
		                        	}
		                        	elemDays.append(elemDay);
		                        });
		                    });
		                    element.replaceWith(elemDays);		                    
	                    });	                    
	                }
	            }
	        }]);
	        
	        module.directive('mandyDatagridBody', ["$q", function($q) {
	            return {
	                restrict: 'A',
	                scope:false,
	                replace: false,
	                transclude:false,
	                link: function (scope, element) {	
	                	$q.all([scope.datagrid.$promise, scope.activities.$promise]).then(function() {
	                		var tbody = angular.element('<tbody></tbody>');
		                    angular.forEach(scope.activities, function(activity, index) {
		                    	var row = angular.element('<tr class="activity"></tr>');
		                    	row.attr("title", activity.longLabel);
		                    	row.attr("data-placement", "left");
		                    	row.tooltip({container: 'body'});
		                    	tbody.append(row);
		                    	var firstCell = angular.element('<td></td>');
		                    	firstCell.text(activity.shortLabel);
		                    	row.append(firstCell);
			                    angular.forEach(scope.datagrid.weeks, function(week, index) {
			                        angular.forEach(week.days, function(day, dIndex) {
			                        	var elemDay = angular.element('<td class="imputation"></td>');		                        	
			                        	row.append(elemDay);
			                        });
			                    });
		                    });
		                    element.replaceWith(tbody);
	                	});
	                }
	            }
	        }]);
	        
	        return module;
        }
);