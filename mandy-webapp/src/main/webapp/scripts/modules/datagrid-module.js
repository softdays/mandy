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
        'angular-cookies',
        'mandy-common',
        'mandy-user'], 
        function (angular) 
        {
    		'use strict';

    		var module = angular.module('DatagridModule', ['ngRoute', 'ngResource', 'ngCookies', 'CommonModule', 'UserModule']);
    		
    		module.service('CalendarService', ['$resource', 'CONTEXT_ROOT', 'ErrorService', 'Utils', function ($resource, CONTEXT_ROOT, errorService, utils) {
            	
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
		                			function (httpResponse) 
		                			{ 
                        				errorService.errorHandler(httpResponse, 
                        						"Erreur technique récupération grille de données");
		                			}
	                		   );
                    }
                };
            }]);
    		
    		module.service('ActivityService', ['$resource', 'CONTEXT_ROOT', function ($resource, CONTEXT_ROOT) {
            	
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
		                			function (httpResponse) 
		                			{ 
                        				errorService.errorHandler(httpResponse, 
                        						"Erreur technique récupération activités");
		                			}
	                		   );
                    }
                };
            }]);
    		
    		module.service('ImputationService', ['$resource', 'CONTEXT_ROOT', '$log', 'ErrorService', function ($resource, CONTEXT_ROOT, $log, errorService) {
            	
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
                        		$log.debug("imputations = "+data);
                        	},
                        	
                			function (httpResponse) 
                			{ 
                        		errorService.errorHandler(httpResponse, 
                        				"Erreur technique récupération imputations");
                			}
            		   );
                    },
                    
                    createImputation: function(userId, activityId, date, quota, callback) {
                    	var imputations = new Imputations();
                    	imputations.resourceId = userId;
                    	imputations.activityId = activityId;
                    	imputations.date = date;
                    	imputations.quota = quota;
                    	// attention les méthodes d'instances Angular#Resource 
                    	// retournent directement la promise
                    	return imputations.$save(
                    		imputations,
                			angular.noop,
                			function (httpResponse) 
                			{ 
	                			errorService.errorHandler(httpResponse, 
        							"Erreur technique creation imputation");
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
//	                			function (httpResponse) 
//	                			{ 
//                    				// cas d'erreur
//	                			}
//                    	);
                    },
                    
                    updateImputation: function(imputation) {
                    	Imputations.update( { id:imputation.imputationId }, 
                    			imputation, 
                    			angular.noop, 
                    			function (httpResponse) { 
		                			errorService.errorHandler(httpResponse, 
        								"Erreur technique update imputation");
	                			}
                    	);
                    },
                    
                    deleteImputation: function(imputationId) {
                    	// attention les méthodes de classe retournent une 
                    	// référence vide sur l'objet résultat qui sera peuplé
                    	// au retour de la requête 
                    	// cet objet possède également une propriété '$promise'
                    	// depuis angular 1.2
                    	return Imputations.remove({ id:imputationId }, 
                    			{},
                    			angular.noop, 
                    			function (httpResponse) { 
		                        	errorService.errorHandler(httpResponse, 
		                        			"Erreur technique delete imputation");
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

	        // Configuration of the controller
	        module.controller('CalendarController', ['$rootScope', '$scope', 
	            	'$location', '$log', 'Globals', 'Utils', 'ImputationService', 
	            	'datagrid', 'activities', 'imputations', '$timeout', 
	            function ($rootScope, $scope, $location, $log, globals, utils, 
	            		imputationService, datagrid, activities, imputations, $timeout) {
	        	// détection du wild wild refresh
	        	if ($rootScope.user == undefined) {
	        		var currentPath = $location.path();
        			//$location.path('/').search('redirect', currentPath); 
        			$location.url('/?redirect='+currentPath);
	        	}
	        	
	        	// Definition of the model
	        	$rootScope.selectedTab = 1;
	        	
	        	$scope.currentDate = globals.getCurrentDate();	        	
	            $scope.datagrid = datagrid;	     
	            $scope.datagrid.$promise.then(function() {
	            	$scope.formattedMonth = utils.formatMonthLabel(datagrid.month);
	            	$scope.shortCurrentMonthIndicator = utils.formatShortMonthLabel(datagrid.month, datagrid.year);
	            });	    
	            $scope.activities = activities;
	            $scope.imputationsMap = imputations;
	            // Actions
	            $scope.displayNextMonth = function(year, month) {
	            	$log.debug("displayNextMonth, year="+year+", month="+month);
	            	if (year && month) { // prevent multi-submits problem
	            		$location.path("/datagrid/"+utils.buildPathForNextMonth(year, month));
	            	}
	            };
	            
	            $scope.displayPreviousMonth = function(year, month) {
	            	if (year && month) { // prevent multi-submits problem
	            		$location.path("/datagrid/"+utils.buildPathForPreviousMonth(year, month));
	            	}
	            };
	            
	            $scope.displayCurrentMonth = function() {
	            	$location.path("/datagrid/"+utils.buildPathForCurrentMonth());
	            };
	            
	            $scope.currentCellSelected = undefined;
	            
	            $scope.tableClickHandler = function(event) {
	            	var ngCell = findTdCell(angular.element(event.target));
	            	if (ngCell.hasClass("imputation")) {
	            		//processClickEventOnImputationCell(ngCell);
	            		//angular.element('#myModal').modal();
	            		if ($scope.currentCellSelected) {
	            			$scope.currentCellSelected.toggleClass("selected");
		            		//$scope.currentCellSelected.find('.marker').toggleClass('invisible');
	            		}
	            		ngCell.toggleClass('selected');
	            		//ngCell.find('.marker').toggleClass('invisible');
	            		$scope.currentCellSelected = ngCell;
 	            	} 
	            };
	            
	            $scope.tableDoubleClickHandler = function(event) {
	            	var ngCell = findTdCell(angular.element(event.target));
	            	$scope.detailsDialogImputationActivity = findLabel(ngCell.attr('data-activity-id'));
            		$scope.detailsDialogImputationDate = ngCell.attr('data-date');
            		var value = ngCell.find(".value").text();
            		value = (value == "-") ? "0.00" : value;
            		$scope.detailsDialogImputationValue = value;
	            	angular.element("#imputation-details").modal();
	            };
	            
	            $scope.updateCurrentValue = function(minus) {
	            	var value = utils.getNewQuota(+$scope.detailsDialogImputationValue, minus);
	            	$scope.detailsDialogImputationValue = utils.formatQuota(value);
	            };
	            
	            // private methods
	            
	            var findTdCell = function(ngElem) {
	            	if (ngElem == undefined) return undefined;
	            	if (ngElem.prop("tagName") == 'TD') {
	            		return ngElem;
	            	} else {
	            		return findTdCell(ngElem.parent());
	            	}
	            };
	            
	            
	            var findLabel = function(activityId) {
	            	 // je n'utilise pas un forEach angular car on ne peut pas 
					 // utiliser break ou return à l'intérieur de la boucle
					 // see: https://github.com/angular/angular.js/issues/263
					 for (var i = 0; i < $scope.activities.length; i++) {
						 if ($scope.activities[i].id == activityId) {
		            			return $scope.activities[i].longLabel;
		            	 }
					 }
	            	
	            	return "Activité non retrouvée";
	            };
	            
	            
	            /** Process an imputation cell */
	            var processClickEventOnImputationCell = function(ngCell) {
            		var activityId = ngCell.attr('data-activity-id');
            		$log.debug("activity id="+activityId);
            		var date = ngCell.attr('data-date');
            		$log.debug("date="+date);
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
            					ngCell.removeAttr ('data-imputation-id');
            					ngCell.removeAttr ('data-quota');
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
		                        	var elemDayContentWrapper = angular.element('<div></div>');	
		                        	//var spanCompleteness = angular.element('<span class="completeness glyphicon"></span>');	
		                        	//var completeness = utils.getCompleteness(); 
		                        	//spanCompleteness.addClass();
		                        	var spanDayValue = angular.element('<span class="day-value"></span>');	
		                        	spanDayValue.text(utils.getDay(day.date));
		                        	elemDay.attr("data-date", day.date);		                        	
		                        	//elemDayContentWrapper.append(spanCompleteness);
		                        	elemDayContentWrapper.append(spanDayValue);		                        	
		                        	elemDay.append(elemDayContentWrapper);
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
	                	$q.all([scope.datagrid.$promise, 
	                	        scope.activities.$promise, 
	                	        scope.imputationsMap.$promise])
	                	.then(function() {
	                		var tbody = angular.element('<tbody></tbody>');
		                    angular.forEach(scope.activities, function(activity, index) {
		                    	var row = angular.element('<tr></tr>');
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
		                    	firstCell.addClass('activity');
		                    	firstCell.text(activity.shortLabel);
		                    	row.append(firstCell);
			                    angular.forEach(scope.datagrid.weeks, function(week, index) {
			                        angular.forEach(week.days, function(day, dIndex) {
			                        	var cellDay = angular.element('<td class="imputation"></td>');	
			                        	//var marker = angular.element('<span class="marker invisible" />');
			                        	var value = angular.element('<span class="value">-</span>');
			                        	var elemDay = angular.element('<div></div>');
			                        	//elemDay.append(marker);
			                        	elemDay.append(value);
			                        	cellDay.append(elemDay);
			                        	cellDay.attr('data-activity-id', activity.id);
			                        	cellDay.attr('data-date', day.date);
			                        	row.append(cellDay);
			                        	var activityImputations = scope.imputationsMap[activity.id];
			                        	if (activityImputations) {
			                        		var imputation = utils.findImputation(activityImputations, day.date);
				                        	if (imputation) {
				                        		cellDay.attr('data-imputation-id', imputation.imputationId);
					                        	cellDay.attr('data-quota', imputation.quota);
					                        	value.text(utils.formatQuota(imputation.quota));
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