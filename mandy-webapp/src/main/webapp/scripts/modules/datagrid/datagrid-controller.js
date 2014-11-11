/*global define:false */

/**
 * @file Datagrid controller
 * @module mandy/datagrid/controller
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define([ 'angular', 'mandy-common', 'mandy-datagrid-service' ], function(
    angular) {
  'use strict';

  var module = angular.module('mdDatagridController', [ 'mdCommon',
      'mdDatagridService' ]);

  /**
   * @constant
   */
  module.constant('IGNORE_COMMENT', "$_IGNORE_COMMENT_$");

  module.controller('DatagridController', [
      '$rootScope',
      '$scope',
      '$location',
      '$log',
      '$timeout',
      'Globals',
      'Utils',
      'ImputationService',
      'datagrid',
      'activities',
      'imputations',
      'year',
      'month',
      'day',
      'IGNORE_COMMENT',
      function($rootScope, $scope, $location, $log, $timeout, globals, utils,
          imputationService, datagrid, activities, imputations, year, month,
          day, IGNORE_COMMENT) {

        // détection du wild wild refresh
        if ($rootScope.user == undefined) {
          var currentPath = $location.path();
          // $location.path('/').search('redirect', currentPath);
          $location.url('/?redirect=' + currentPath);
        }

        /**
         * Performs save imputation details from modal view. This action enables
         * communication between modal controller and current active view
         * controller.
         * 
         * @fires 'md.datagrid.imputation.save'
         */
        $scope.saveModalChanges = function(imputationId, activityId, newQuota,
            newComment) {
          $scope.$broadcast('md.datagrid.imputation.save', imputationId,
              activityId, newQuota, newComment);
        };

        /**
         * Updates the location when user resizes its screen in a manner that
         * switches the view mode.
         * 
         * @callback
         * @event 'md.datagrid.input.mode.changed'
         */
        var off = $scope.$on('md.datagrid.input.mode.changed', function(event,
            arg) {
          // console.log("I've been notified with: "+arg);
          if (arg == 'day') {
            // il faut ajouter le jour au path
            $location.path("/datagrid/year/" + year + "/month/" + month
                + "/day/" + utils.getDay(datagrid.weeks[0].days[0].date));
          } else {
            // retirer la partie du path correspondant au
            // jour
            $location.path("/datagrid/year/" + year + "/month/" + month);
          }
        });

        // model
        $rootScope.selectedTab = 1;
        $scope.currentDate = globals.getCurrentDate();
        $scope.datagrid = datagrid;
        $scope.activities = activities;
        $scope.imputationsMap = imputations;

        /**
         * Shows modal popup dialog.
         * 
         * @action
         */
        $scope.showModal = function(imputationId, activityId, date, quota) {
          $scope.$broadcast('md.datagrid.modal.show', imputationId, activityId,
              date, quota);
        };

        /**
         * Finds imputation from activity and imputation ids.
         * 
         * @public
         */
        $scope.findImputation = function(activityId, imputationId) {
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

        /**
         * Permet de synchroniser la base de données avec la saisie effectuée.
         * Returns an object (async token) containing two properties: - promise=
         * called resource $promise - type= the event type
         * (C=create,D=delete,U=update)
         * 
         * @public
         */
        $scope.synchronizeBackend = function(imputationId, activityId, date,
            newQuota, newComment) {
          var resource = null;
          var asyncToken = {
            type : null,
            promise : null
          };
          if (imputationId) {
            // Imputation already exists, we have to change it.
            if (newQuota == 0) {
              // DELETE CASE
              asyncToken.type = 'D';
              resource = imputationService.deleteImputation(imputationId);
              // set $promise on token
              asyncToken.promise = resource.$promise;
            } else {
              // UPDATE CASE
              asyncToken.type = 'U';
              var oldImputation = $scope.findImputation(activityId,
                  imputationId);
              var imputation = {};
              imputation.imputationId = imputationId;
              imputation.activityId = activityId;
              imputation.resourceId = $rootScope.user.id;
              imputation.date = date;
              imputation.quota = newQuota;
              if (newComment === IGNORE_COMMENT) {
                imputation.comment = oldImputation.comment;
              } else {
                imputation.comment = newComment;
              }
              resource = imputationService.updateImputation(imputation);
              // set $promise on token
              asyncToken.promise = resource.$promise;
            }
          } else {
            // CREATE CASE
            asyncToken.type = 'C';
            if (newComment === IGNORE_COMMENT) {
              newComment = null;
            }
            // attention ce service utilise une méthode d'instance
            // qui retourne directement la promise
            asyncToken.promise = imputationService.createImputation(
                +$rootScope.user.resourceId, activityId, date, newQuota,
                newComment);
          }

          return asyncToken;
        };

        /**
         * Returns imputation index in the global model map.
         * 
         * @param activityId
         *          The activity id of the imputation to find.
         * @param imputationId
         *          The imputation id of the imputation index to find.
         * 
         * @public
         */
        $scope.findImputationIndex = function(activityId, imputationId) {
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

      } ]);
});
