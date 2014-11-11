/*global define:false */

/**
 * @file Datagrid modal controller
 * @module mandy/datagrid/controller/modal
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define([ 'angular', 'mandy-common', 'mandy-datagrid-service' ], function(
    angular) {
  'use strict';

  var module = angular.module('mdDatagridControllerModal', [ 'mdCommon',
      'mdDatagridService' ]);

  module.controller('DatagridModalController', [
      '$rootScope',
      '$scope',
      '$log',
      'Globals',
      'Utils',
      'ImputationService',
      'IGNORE_COMMENT',
      function($rootScope, $scope, $log, globals, utils, imputationService,
          IGNORE_COMMENT) {

        /**
         * @callback
         */
        $scope.$on('md.datagrid.modal.show', function(event, imputationId,
            activityId, date, quota) {
          showImputationDetailsDialog(imputationId, activityId, date, quota);
        });

        /**
         * Shows imputation details dialog.
         * 
         * @private
         */
        var showImputationDetailsDialog = function(imputationId, activityId,
            date, quota) {

          $scope.imputationId = imputationId;
          $scope.imputationDate = date;
          $scope.activityId = activityId;
          $scope.quota = quota;

          $scope.imputationActivity = findActivityLabel(activityId);

          if (imputationId) {
            $scope.imputationComment = findComment(activityId, imputationId);
          } else {
            $scope.imputationComment = "";
          }

          updateImputationValue(quota);
          angular.element("#imputation-details").modal();
          angular.element("#imputation-details").on('shown.bs.modal',
              function(e) {
                $("#imputation-details textarea").focus();
              });
        };

        /**
         * Save imputation details from modal data model.
         * 
         * @borrows saveModalChanges (parent scope action)
         */
        $scope.saveImputationFromDetails = function() {
          angular.element('#imputation-details').modal('hide');

          $scope.saveModalChanges($scope.imputationId, $scope.activityId,
              +$scope.imputationValue, $scope.imputationComment);
        };

        /**
         * Allows to update quota of the imputation 'copy' displayed in modal.
         * 
         * @public
         */
        $scope.minusQuota = function() {
          updateCurrentValue(true);
        };

        /**
         * Allows to update quota of the imputation 'copy' displayed in modal.
         * 
         * @public
         */
        $scope.plusQuota = function() {
          updateCurrentValue();
        };

        /**
         * Allows to update quota of the imputation 'copy' displayed in modal.
         * 
         * TODO: in progress (does not work yet)
         * 
         * @public
         */
        $scope.isButtonSaveDisabled = function() {
          var imputation = $scope.findImputation($scope.activityId,
              $scope.imputationId);
          var disabled = false;
          if (imputation) {
            // existing imputation
            disabled = imputation.quota == $scope.quota
                && $scope.imputation.comment == $scope.imputationComment;
          } else {
            // not yet existing
            disabled = $scope.quota == 0;
          }

          return disabled;
        };

        /**
         * Allows to update quota of the imputation 'copy' displayed in modal.
         * 
         * @param minus
         *          If true, the current value will be reduced, if null or false
         *          the value will be increased.
         * 
         * @private
         */
        var updateCurrentValue = function(minus) {
          var value = utils.getNewQuota(+$scope.imputationValue, minus);
          updateImputationValue(value);
        };

        /**
         * Prepare PM from given new value.
         * 
         * @private
         */
        var updateImputationValue = function(newValue) {
          var value = utils.formatQuota(newValue);
          $scope.progressValue = value * 100;
          $scope.progressValueStyle = {
            width : ($scope.progressValue + '%')
          };
          $scope.imputationValue = value;
        };

        /**
         * Find activity label from its is.
         * 
         * @private
         */
        var findActivityLabel = function(activityId) {
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

        /**
         * Find comment from imputation and activity ids.
         * 
         * @private
         */
        var findComment = function(activityId, imputationId) {
          var comment = "";
          var imputation = $scope.findImputation(activityId, imputationId);
          if (imputation) {
            comment = imputation.comment;
          }

          return comment;
        };

      } ]);
});
