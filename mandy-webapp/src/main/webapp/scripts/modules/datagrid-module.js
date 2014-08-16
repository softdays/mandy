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
    		
    		module.service('ImputationService', ["$resource", "CONTEXT_ROOT", function ($resource, CONTEXT_ROOT) {
            	
            	var Imputations = $resource(CONTEXT_ROOT+"/api/imputations/:id",
					{ id: '@id' },
					{
						'query': {
		    				method:'GET', 
		    				url:CONTEXT_ROOT+"/api/imputations/:year/:month"
		    			},
		    			'save': {
		    				method:'POST'
		    			},
		    			'update': {
		    				method:'PUT'
		    			},
		    			'remove': {
		    				method:'DELETE'
		    			}
					}
            	);
            	
                /**
                 * @alias mandy/imputation/service
                 */
                return {
                    /**
                     * Retourne les imputations pour l'utilisateur et le mois donnés.
                     */
                    findImputations: function(userId, year, month) {
                    	
                        return Imputations.query({user:userId, year:year, month:month}, 
                    		function(data){
                        		console.log(data);
                        	},
                			function (err) 
                			{ 
                				// cas d'erreur
	                			var message = "Erreur technique récupération imputations : "+err;
	                        	console.log(message);
	                        	alert(message);
                			}
            		   );
                    },
                    
                    createImputation: function(userId, activityId, date, quota, callback) {
                    	var imputations = new Imputations();
                    	imputations.resourceId = userId;
                    	imputations.activityId = activityId;
                    	imputations.date = date;
                    	imputations.quota = quota;
                    	return imputations.$save(
                    		imputations,
                			angular.noop,
                			function (err) 
                			{ 
                				// cas d'erreur
	                			var message = "Erreur technique creation imputation : "+err;
	                        	console.log(message);
	                        	alert(message);
                			}
                    	);
                    	
                    	
//                    	return Imputations.save(
//                    			{},
//                    			{
//	                				resourceId: userId, 
//	                				activityId: activityId,
//	                				date: date, 
//	                				quota: quota
//                    			}, 
//                    			angular.noop,
//	                			function (err) 
//	                			{ 
//                    				// cas d'erreur
//		                			var message = "Erreur technique reation imputation : "+err;
//		                        	console.log(message);
//		                        	alert(message);
//	                			}
//                    	);
                    },
                    
                    updateImputation: function(imputation) {
                    	Imputations.update( { id:imputation.imputationId }, 
                    			imputation, 
                    			angular.noop, 
                    			function (err) { 
	                				// cas d'erreur
		                			var message = "Erreur technique update imputation : "+err;
		                        	console.log(message);
		                        	alert(message);
	                			}
                    	);
                    },
                    
                    deleteImputation: function(imputationId) {
                    	return Imputations.remove({ id:imputationId }, 
                    			{},
                    			angular.noop, 
                    			function (err) { 
	                				// cas d'erreur
		                			var message = "Erreur technique delete imputation : "+err;
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
		                	 datagrid: ['$route', 'CalendarService', function ($route, calendarService) {
		                		 var year = $route.current.params.year;
		                		 var month = $route.current.params.month;
				                 //console.log("resolve datagrid for year="+year+" and month="+month);
		                		 return calendarService.loadDataGrid(year, month);
		                	 }],
		                	 activities: ['ActivityService', function (activityService) {
		                         return activityService.getUserActivities();
		                	 }],
		                	 imputations: ['$rootScope', '$route', 'ImputationService', function ($rootScope, $route, imputationService) {
		                		 var userId = $rootScope.user.id;
		                		 var year = $route.current.params.year;
		                		 var month = $route.current.params.month;
		                         return imputationService.findImputations(userId, year, month);
		                	 }]
		                 }
		             }
	        	 );	            
	        }]);

	        // Configuration of the controller
	        module.controller('CalendarController', ['$rootScope', '$scope', '$location', 'Globals', 'Utils', 'ImputationService', 'datagrid', 'activities', 'imputations',
	            function ($rootScope, $scope, $location, globals, utils, imputationService, datagrid, activities, imputations)  {
	        	// global model
	        	$rootScope.selectedTab = 1;
	        	if (!$rootScope.contextRoot || !$rootScope.user) {
	        		$location.path("/");
	        	}
	            // Definition of the model
	        	$scope.currentDate = globals.getCurrentDate();	        	
	            $scope.datagrid = datagrid;	     
	            $scope.datagrid.$promise.then(function() {
	            	$scope.formattedMonth = utils.formatMonthLabel(datagrid.month);
	            	$scope.shortCurrentMonthIndicator = utils.formatShortMonthLabel(datagrid.month, datagrid.year);
	            });	    
	            $scope.activities = activities;
	            $scope.imputationsMap = imputations;
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
	            
	            $scope.tableClickHandler = function(event) {
	            	var ngCell = angular.element(event.target);
	            	if (ngCell.hasClass("imputation")) {
	            		processClickEventOnImputationCell(ngCell);
 	            	}
	            };
	            
	            // private methods
	            
	            /** Process an imputation cell */
	            var processClickEventOnImputationCell = function(ngCell) {
            		var activityId = ngCell.attr('data-activity-id');
            		console.log("activity id="+activityId);
            		var date = ngCell.attr('data-date');
            		console.log("date="+date);
            		var imputationId = ngCell.attr('data-imputation-id');
            		// mettre à jour le modèle
            		var quota = ngCell.attr('data-quota');
            		var newQuota = utils.getNewQuota(quota);
            		ngCell.attr('data-quota', newQuota); 
            		// mettre à jour la vue
            		ngCell.text(utils.formatQuota(newQuota));
            		// synchroniser le backend
            		if (imputationId) {
            			if (newQuota == 0) {
            				var delRes = imputationService.deleteImputation(+imputationId);
            				delRes.$promise.then(function(data){
            					ngCell.removeAttr('data-imputation-id');
            				});
            			} else {
            				var imputation = {};
            				imputation.imputationId= (+imputationId);
	            			imputation.activityId= (+activityId);
	            			imputation.resourceId= $rootScope.user.id;
	            			imputation.date = date;
	            			imputation.quota = newQuota;
	            			imputationService.updateImputation(imputation);
            			}
            		} else {
            			var res = imputationService.createImputation(
        					+$rootScope.user.resourceId, 
        					+activityId, 
        					date, 
        					newQuota
            		    );
            			res.then(function(createdImputation){
            				ngCell.attr('data-imputation-id', createdImputation.imputationId);
            			});
            		}
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
		                        	elemDay.attr("data-date", day.date);
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
	        
	        module.directive('mandyDatagridBody', ["$q", "Utils", function($q, utils) {
	        	
	            return {
	                restrict: 'A',
	                scope:false,
	                replace: false,
	                transclude:false,
	                link: function (scope, element) {	
	                	$q.all([scope.datagrid.$promise, scope.activities.$promise, scope.imputationsMap.$promise]).then(function() {
	                		var tbody = angular.element('<tbody></tbody>');
		                    angular.forEach(scope.activities, function(activity, index) {
		                    	var row = angular.element('<tr></tr>');
		                    	row.addClass('activity');
		                    	row.attr("title", activity.longLabel);
		                    	row.attr('data-activity-id', activity.id);
		                    	if (index == scope.activities.length-1) {
		                    		row.attr("data-placement", "top");
		                    	} else {
		                    		row.attr("data-placement", "bottom");
		                    	}		                    	
		                    	row.tooltip({container: 'body'});
		                    	tbody.append(row);
		                    	var firstCell = angular.element('<td></td>');
		                    	firstCell.text(activity.shortLabel);
		                    	row.append(firstCell);
			                    angular.forEach(scope.datagrid.weeks, function(week, index) {
			                        angular.forEach(week.days, function(day, dIndex) {
			                        	var elemDay = angular.element('<td class="imputation"></td>');	
			                        	elemDay.attr('data-activity-id', activity.id);
			                        	elemDay.attr('data-date', day.date);
			                        	row.append(elemDay);
			                        	console.log('this: '+this);
			                        	var activityImputations = scope.imputationsMap[activity.id];
			                        	if (activityImputations) {
			                        		var imputation = utils.findImputation(activityImputations, day.date);
				                        	if (imputation) {
				                        		elemDay.attr('data-imputation-id', imputation.imputationId);
					                        	elemDay.attr('data-quota', imputation.quota);
					                        	elemDay.text(utils.formatQuota(imputation.quota));
				                        	};
			                        	};
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