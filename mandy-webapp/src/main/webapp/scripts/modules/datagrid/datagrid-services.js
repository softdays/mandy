/*global define:false */

/**
 * @file Datagrid Service Layer
 * @module mandy/datagrid/service
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define(['angular', 
        'angular-resource',
        'mandy-common'], 
    function (angular) {
		'use strict';
		
		var module = angular.module('mdDatagridService', ['ngResource', 'mdCommon']);
		
		module.service('CalendarService', ['$resource', 'CONTEXT_ROOT', 'ErrorService', 'Utils', function ($resource, CONTEXT_ROOT, errorService, utils) {
        	
        	var Calendar = $resource(CONTEXT_ROOT+"/api/datagrid/:year/:month");
        	
            /**
             * @alias mandy/calendar/service
             */
            return {
                /**
                 * Retourne la datagrid pour l'année et le mois indiqué.
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
        	//TODO: tous les services devraient systématiquement retourner directement la promise de la resource
        	// de manière à ce que les routes synchronise les données avant d'invoquer leur contrôleur
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
                	
                	
//                	return Imputations.save(
//                			{},
//                			{
//                				resourceId: userId, 
//                				activityId: activityId,
//                				date: date, 
//                				quota: quota
//                			}, 
//                			angular.noop,
//                			function (httpResponse) 
//                			{ 
//                				// cas d'erreur
//                			}
//                	);
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
		
		return module;
		
    }
);