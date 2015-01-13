/*global define:false */

/**
 * @file Login service
 * @module mandy/login
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define([ 'angular', 'angular-route', 'angular-resource', 'mandy-common',
    'bootstrap-toogle' ], function(angular) {
  'use strict';

  var module = angular.module('mdPreferences', [ 'ngRoute', 'ngResource',
      'mdCommon' ]);

  module.service('PreferencesService', [
      '$rootScope',
      '$resource',
      '$log',
      'CONTEXT_ROOT',
      'ErrorService',
      function($rootScope, $resource, $log, CONTEXT_ROOT, errorService) {

        var PreferencesResource = $resource(CONTEXT_ROOT + "/api/preferences",
            {}, {
              'update' : {
                method : 'PUT'
              }
            });

        /**
         * @alias mandy/user/service
         */
        return {
          /**
           * Returns the current user data.
           */
          getUserPreferences : function() {
            return PreferencesResource.get({}, angular.noop, function(
                httpResponse) {
              errorService.errorHandler(httpResponse,
                  "Erreur technique récupération preferences utilisateur");
            }).$promise;
          },

          /**
           * Updates the user preferences.
           */
          updateUserPreferences : function(preferences) {
            return PreferencesResource.update({}, preferences, function(
                newPreferences) {
              // notify that user preferences have changed
              $rootScope.$broadcast('md.preferences.changed', newPreferences);
            }, function(httpResponse) {
              errorService.errorHandler(httpResponse,
                  "Erreur technique mise à jour des preferences utilisateur");
            }).$promise;
          }

        };
      } ]);

  // Configuration of the router
  module.config([ '$routeProvider', function($routeProvider) {

    /**
     * Navigation
     */
    $routeProvider.when('/preferences', {
      templateUrl : "partials/preferences.html",

      controller : 'PreferencesController',

      resolve : {
        preferences : [ 'PreferencesService', function(preferencesService) {
          return preferencesService.getUserPreferences();
        } ],
        activities : [ 'ActivityService', function(activityService) {
          return activityService.getUserTeamActivities();
        } ]
      }
    });

  } ]);

  module.controller('PreferencesController', [
      '$rootScope',
      '$scope',
      '$log',
      '$timeout',
      'Utils',
      'PreferencesService',
      'preferences',
      'activities',
      function($rootScope, $scope, $log, $timeout, utils, prefService,
          preferences, activities) {

        $rootScope.selectedTab = 3;
        $scope.useFineGranularity = (preferences.granularity === 0.25);
        $scope.activities = activities;

        /**
         * Handles user preferences change event.
         * 
         * @event
         */
        $scope.$on('md.preferences.changed', function(event, prefs) {
          $scope.useFineGranularity = (prefs.granularity === 0.25);
        });

        /**
         * @event granularity toggle-button change.
         */
        angular.element('.pref-granularity input[data-toggle="toggle"]')
            .change(function(event) {
              var status = angular.element(this).prop('checked');
              $log.debug('checked: ' + status);
              $rootScope.preferences.granularity = status ? 0.25 : 0.5;
              prefService.updateUserPreferences($rootScope.preferences);
            });

        /**
         * Initializes toggle-buttons.
         */
        $timeout(function() {
          angular.element('input[data-toggle="toggle"]').bootstrapToggle();
        });

      } ]);

  return module;
});
