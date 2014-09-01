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
                    
                    createImputation: function(userId, activityId, date, quota, comment) {
                    	var imputations = new Imputations();
                    	imputations.resourceId = userId;
                    	imputations.activityId = activityId;
                    	imputations.date = date;
                    	imputations.quota = quota;
                    	imputations.comment = comment;
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
                    	return Imputations.update( { id:imputation.imputationId }, 
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
	            		if ($scope.currentCellSelected) {
	            			$scope.currentCellSelected.toggleClass("selected");
	            		}
	            		ngCell.toggleClass('selected');
	            		$scope.currentCellSelected = ngCell;
 	            	} 
	            };
	            
	            /**
	             * Table right-click handler;
	             */
	            $scope.tableRightClickHandler = function(event) {
	            	var ngCell = findTdCell(angular.element(event.target));
	            	if (ngCell.hasClass("selected")) {
	            		var oldQuota = ngCell.attr("data-quota");
	            		var newQuota = utils.getNewQuota(oldQuota);
	            		updateImputation(ngCell, newQuota, -1);
	            	}
	            };
	            
	            /**
	             * Table double-click handler.
	             */
	            $scope.tableDoubleClickHandler = function(event) {
	            	//var ngCell = findTdCell(angular.element(event.target));
	            	if (!$scope.currentCellSelected) return;
	            	
	            	var ngCell = $scope.currentCellSelected;
	            	var activityId = ngCell.attr('data-activity-id');
	            	$scope.detailsDialogImputationActivity = findLabel(activityId);
	            	var imputationId = ngCell.attr('data-imputation-id');
	            	if (imputationId) {
	            		$scope.detailsDialogImputationComment = findComment(activityId, imputationId);
	            	} else {
	            		$scope.detailsDialogImputationComment = "";
	            	}          		
            		$scope.detailsDialogImputationDate = ngCell.attr('data-date');
            		var value = ngCell.find(".value").text();
            		updateDetailsDialogImputationValue(value);
	            	angular.element("#imputation-details").modal();
	            };
	            
	            /**
	             * Allows to update quota of the imputation 'copy' displayed in modal.
	             */
	            $scope.updateCurrentValue = function(minus) {
	            	var value = utils.getNewQuota(+$scope.detailsDialogImputationValue, minus);
	            	updateDetailsDialogImputationValue(value);
	            };
	            
	            // private methods
	            
	            var updateDetailsDialogImputationValue = function(newValue) {
	            	var value = utils.formatQuota(newValue);
	            	$scope.progressValue = value*100;
	            	$scope.progressValueStyle = {width:($scope.progressValue+'%')};
	            	$scope.detailsDialogImputationValue = value;
	            };
	            
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
	            
	            var findComment = function(activityId, imputationId) {	            	
	            	var comment = "";
                	var imputation = findImputation(activityId, imputationId);
					if (imputation) {
						comment = imputation.comment;
					} 
	            	
	            	return comment;
	            };
	            
	            var findImputation = function(activityId, imputationId) {	
	            	// je n'utilise pas un forEach angular car on ne peut pas 
					// utiliser break ou return à l'intérieur de la boucle
					// see: https://github.com/angular/angular.js/issues/263
	            	var imputations = $scope.imputationsMap[activityId] || [];
	            	for (var i = 0; i < imputations.length; i++) {
						 if (imputations[i].imputationId == imputationId) {
		            			return imputations[i];
		            	 }
					 }
	            	
	            	return undefined;
	            };
	            
	            var findImputationIndex = function(activityId, imputationId) {	
	            	// je n'utilise pas un forEach angular car on ne peut pas 
					// utiliser break ou return à l'intérieur de la boucle
					// see: https://github.com/angular/angular.js/issues/263
	            	var imputations = $scope.imputationsMap[activityId] || [];
	            	for (var i = 0; i < imputations.length; i++) {
						 if (imputations[i].imputationId == imputationId) {
		            			return i;
		            	 }
					 }
	            	
	            	return -1;
	            };
	            
	            /**
	             * Save from modal data.
	             */
	            $scope.saveImputationFromDetails = function() {	            	
	            	updateImputation($scope.currentCellSelected, 
	            			+$scope.detailsDialogImputationValue, 
	            			$scope.detailsDialogImputationComment);
	            	
	            	angular.element('#imputation-details').modal('hide');
	            };
	            
	            
	            /** 
	             * Process new data entries on imputation cell.
	             */
	            var updateImputation = function(ngCell, newQuota, newComment) {
            		var activityId = ngCell.attr('data-activity-id');
            		$log.debug("activity id="+activityId);
            		var date = ngCell.attr('data-date');
            		$log.debug("date="+date);
            		var imputationId = ngCell.attr('data-imputation-id');
            		// est-ce qu'une modification doit être synchronisée ?
            		var syncBackendRequired = isSyncRequired(activityId, imputationId, newQuota, newComment);
            		if (syncBackendRequired) {
                		// synchroniser le backend
                		if (imputationId) {
                			// DELETE CASE
                			if (newQuota == 0) {
                				var delRes = imputationService.deleteImputation(+imputationId);
                				delRes.$promise.then(function(data){
                					ngCell.removeAttr('data-imputation-id');
                					ngCell.removeAttr('data-quota');
                					// synchroniser le modèle client
                					var index = findImputationIndex(activityId, imputationId);
                					$scope.imputationsMap[activityId].splice(index, 1);
                					// synchroniser les données de la cellule
    	            				synchronizeCellData(ngCell, newQuota);
    	            				// mettre à jour l'indicateur de complètude
    	            				updateCompletenessMarker(date);
                				});
                			} else {
                				// UPDATE CASE
                				var oldImputation = findImputation(activityId, imputationId);
                				var imputation = {};
                				imputation.imputationId= (+imputationId);
    	            			imputation.activityId= (+activityId);
    	            			imputation.resourceId= $rootScope.user.id;
    	            			imputation.date = date;
    	            			imputation.quota = newQuota;
    	            			if (typeof newComment === 'number' && newComment==-1) {
    	            				imputation.comment = oldImputation.comment;
    	            			} else {
    	            				imputation.comment = newComment;
    	            			}
    	            			var updateRes = imputationService.updateImputation(imputation);
    	            			updateRes.$promise.then(function(){
    	            				// synchroniser le modèle client
    	            				var i = findImputation(activityId, imputationId);
    	            				i.quota = imputation.quota;
    	            				i.comment = imputation.comment;
    	            				// synchroniser les données de la cellule
    	            				synchronizeCellData(ngCell, newQuota);
    	            				// mettre à jour l'indicateur de complètude
    	            				updateCompletenessMarker(date);
    	            			});
                			}
                		} else {
                			// CREATE CASE
                			var createRes = imputationService.createImputation(
            					+$rootScope.user.resourceId, 
            					+activityId, 
            					date, 
            					newQuota,
            					newComment
                		    );
                			createRes.then(function(createdImputation){
                				// synchroniser le modèle client
                				$scope.imputationsMap[activityId].push(createdImputation);
                				// synchroniser les données de la cellule
                				synchronizeCellData(ngCell, newQuota, createdImputation.imputationId);
                				// mettre à jour l'indicateur de complètude
	            				updateCompletenessMarker(date);
                			});
                		}
            		}
	            };
	            
	            /**
	             * Synchronize les éléments de vue stockant les informations de quota.
	             */
	            var synchronizeCellData = function(ngCell, newQuota, createdId) {
	            	ngCell.attr('data-quota', newQuota); 
            		ngCell.find(".value").text(utils.formatQuotaForGrid(newQuota));
            		if (createdId) {
            			ngCell.attr('data-imputation-id', createdId);
            		}
	            };
	            
	            /**
	             * Mets à jour l'indicateur de complètude de la colonne correspondant à la date donnée.
	             */
	            var updateCompletenessMarker = function(strDate){
	            	// retrieve element to update
	            	var elem = angular.element("th.day[data-date='"+strDate+"'] .completeness");
	            	if (elem) {
	            		// remove previous glyphicon class name
	            		//var regex = /(\s)*(glyphicon-.*?)(?=\s)/g; 
	            		//elem.className =  elem.className.replace(regex, '');
	            		elem.removeClass('glyphicon-ok glyphicon-arrow-up glyphicon-arrow-down');
	            		// add new glyphicon class name
	            		var glyphiconClass = utils.getCompletenessGlyphiconClass($scope.imputationsMap, strDate);
	            		elem.addClass(glyphiconClass);
	            	} else {
	            		$log.error("Completeness cell not found");
	            	}
	            };
	            
	            /**
	             * Retourne vrai si au moins une des nouvelles données diffère de son ancienne valeur, faux sinon.
	             */
	            var isSyncRequired = function(activityId, imputationId, newQuota, newComment) {
	            	var syncRequired = true;
	            	var imputation = findImputation(activityId, imputationId);
	            	if (imputation) {
	            		if (typeof newComment === 'number' && newComment==-1) {
	            			// on ne doit pas prendre en compte le commentaire
	            			syncRequired = imputation.quota != newQuota;
	            		} else {
	            			syncRequired = imputation.comment != newComment || imputation.quota != newQuota;
	            		}
	            		
	            	} 
	            	// sinon il s'agit d'une création et le modèle sera mis à jour au retour de la requête
	            	return syncRequired;
	            };
            
	        }]);
	        
	        module.directive('mandyDatagridHeader', ["$q", "Utils", function($q, utils) {
	            return {
	                restrict: 'A',
	                scope:false,
	                replace: false,
	                transclude:false,
	                link: function (scope, element) {
	                	// on doit disposer de la datagrid et des imputations
	                	// pour pouvoir réaliser le traitement
	                	$q.all([scope.datagrid.$promise, 
	                	        scope.imputationsMap.$promise]).then(function(){
	                    	var weeks = scope.datagrid.weeks;
	                    	var elemDays = angular.element('<tr class="days"></tr>');
	                    	var activities = angular.element('<th class="header-activities">Activités</th>');
	                    	elemDays.append(activities);	
		                    angular.forEach(weeks, function(week, index) {
		                        angular.forEach(week.days, function(day, dIndex) {
		                        	var elemDay = angular.element('<th class="day"></th>');	
		                        	var elemDayContentWrapper = angular.element('<div></div>');	
		                        	var spanCompleteness = angular.element('<span class="completeness glyphicon"></span>');	
		                        	var completenessGlyphiconIcon = utils.getCompletenessGlyphiconClass(scope.imputationsMap, day.date); 
		                        	spanCompleteness.addClass(completenessGlyphiconIcon);
		                        	var spanDayValue = angular.element('<span class="day-value"></span>');	
		                        	spanDayValue.text(utils.getDay(day.date));
		                        	elemDay.attr("data-date", day.date);		                        	
		                        	elemDayContentWrapper.append(spanDayValue);	
		                        	elemDayContentWrapper.append(spanCompleteness);
		                        	elemDay.append(elemDayContentWrapper);
		                        	if (utils.areMonthsEqual(scope.datagrid.month, day.date)) {
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
	        
	        module.directive('ngRightClick', function($parse) {
	            return function(scope, element, attrs) {
	                var fn = $parse(attrs.ngRightClick);
	                element.bind('contextmenu', function(event) {
	                    scope.$apply(function() {
	                        event.preventDefault();
	                        fn(scope, {$event:event});
	                    });
	                });
	            };
	        });
	        
	        return module;
        }
);