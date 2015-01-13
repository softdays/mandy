/*global define:false */

/**
 * @file Datagrid slider controller
 * @module mandy/datagrid/controller/slider
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define([ 'angular', 'mandy-common', 'mandy-datagrid-service' ], function(
    angular) {
  'use strict';

  var module = angular.module('mdDatagridControllerSlider', [ 'mdCommon',
      'mdDatagridService' ]);

  module.controller('DatagridModalController', [
      '$rootScope',
      '$scope',
      '$log',
      'Globals',
      'Utils',
      function($rootScope, $scope, $log, globals, utils) {

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
          var value = utils.getNewQuota($scope.imputationValue,
              $rootScope.preferences.granularity, minus);
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

      } ]);
});
