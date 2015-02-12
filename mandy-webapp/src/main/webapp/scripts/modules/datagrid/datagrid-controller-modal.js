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
      function($rootScope, $scope, $log, globals, utils) {

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
            if ($scope.activities[i].id === +activityId) {
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

          angular.element("#imputation-details").modal();
          angular.element("#imputation-details").on('shown.bs.modal',
              function() {
                $("#imputation-details textarea").focus();
              });
        };

        /**
         * @callback
         */
        $scope.$on('md.datagrid.modal.show', function(event, imputationId,
            activityId, date, quota) {
          showImputationDetailsDialog(imputationId, activityId, date, quota);
        });

        /**
         * Save imputation details from modal data model.
         * 
         * @borrows saveModalChanges (parent scope action)
         */
        $scope.saveImputationFromDetails = function() {
          angular.element('#imputation-details').modal('hide');

          $scope.saveModalChanges($scope.imputationId, $scope.activityId,
              +$scope.quota, $scope.imputationComment);
        };

        /**
         * Allows to update quota of the imputation 'copy' displayed in modal.
         * 
         * @public
         * 
         * $scope.minusQuota = function() { updateCurrentValue(true); };
         */

        /**
         * Allows to update quota of the imputation 'copy' displayed in modal.
         * 
         * @public
         * 
         * $scope.plusQuota = function() { updateCurrentValue(); };
         */

        /**
         * TODO: Prevents to save if the imputation quota is empty in creation
         * case. Be careful to not prevent deletion!
         * 
         * @public
         */
        $scope.isButtonSaveDisabled = function() {
          var imputation = $scope.findImputation($scope.activityId,
              $scope.imputationId);
          var disabled = false;
          if (imputation) {
            // existing imputation
            disabled = imputation.quota === $scope.quota
                && $scope.imputation.comment === $scope.imputationComment;
          } else {
            // not yet existing
            disabled = $scope.quota === 0;
          }

          return disabled;
        };

      } ]);
});
