/*global define:false */

/**
 * @file Datagrid controller
 * @module mandy/datagrid
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define(['angular', 'mandy-common', 'mandy-datagrid-service'], 
    function (angular) {
		'use strict';

		var module = angular.module('mdDatagridController', ['mdCommon', 'mdDatagridService']);
		
        module.controller('DatagridController', ['$rootScope', '$scope', 
            	'$location', '$log', '$timeout', 'Globals', 'Utils', 
            	'ImputationService', 'datagrid', 'activities', 'imputations', 
            function ($rootScope, $scope, $location, $log, $timeout, globals, utils, 
            		imputationService, datagrid, activities, imputations) {
        	// détection du wild wild refresh
        	if ($rootScope.user == undefined) {
        		var currentPath = $location.path();
    			//$location.path('/').search('redirect', currentPath); 
    			$location.url('/?redirect='+currentPath);
        	}
        	
        	var IGNORE_COMMENT = "$_IGNORE_COMMENT_$";
        	
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
            		updateImputation(ngCell, newQuota, IGNORE_COMMENT);
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
            	$scope.detailsDialogImputationId = imputationId;
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
	            			if (newComment === IGNORE_COMMENT) {
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
            			if (newComment === IGNORE_COMMENT) {
            				newComment = null;
            			} 
            			var createRes = imputationService.createImputation(
        					+$rootScope.user.resourceId, 
        					+activityId, 
        					date, 
        					newQuota,
        					newComment
            		    );
            			createRes.then(function(createdImputation){
            				// synchroniser le modèle client
            				var imputations = $scope.imputationsMap[activityId];
            				if (!imputations) {
            					imputations = $scope.imputationsMap[activityId] = [];
            				}
            				imputations.push(createdImputation);
            				
            				// synchroniser les données de la cellule
            				synchronizeCellData(ngCell, newQuota, createdImputation.imputationId);
            				// mettre à jour l'indicateur de complètude
            				updateCompletenessMarker(date);
            			});
            		}
        		}
            };
            
            /**
             * Synchronise les éléments de vue stockant les informations de quota.
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
            		if (newComment === IGNORE_COMMENT) {
            			// on ne doit pas prendre en compte le commentaire
            			syncRequired = imputation.quota != newQuota;
            		} else {
            			syncRequired = imputation.comment != newComment || imputation.quota != newQuota;
            		}            		
            	} else if (newQuota==0) {
            		syncRequired = false;
            	}
            	// sinon il s'agit d'une création et le modèle sera mis à jour au retour de la requête
            	return syncRequired;
            };
        
        }]);
    }
);