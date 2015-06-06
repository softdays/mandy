/* global define:false */

/**
 * 
 */
define([ 'angular', 'angular-route', 'angular-resource', 'mandy-common', 'bootstrap-toogle' ],
    function(angular) {
        'use strict';

        var module = angular.module('mdPreferences', [ 'ngRoute', 'ngResource', 'mdCommon' ]);

        module.service('PreferencesService', [
            '$rootScope',
            '$resource',
            '$log',
            'CONTEXT_ROOT',
            'ErrorService',
            function($rootScope, $resource, $log, CONTEXT_ROOT, errorService) {

                var PreferencesResource = $resource(CONTEXT_ROOT + "/api/preferences", {}, {
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
                        return PreferencesResource.get({}, angular.noop, function(httpResponse) {
                            errorService.errorHandler(httpResponse,
                                "Erreur technique récupération preferences utilisateur");
                        }).$promise;
                    },

                    /**
                     * Updates the user preferences.
                     */
                    updateUserPreferences : function(preferences) {
                        return PreferencesResource.update({}, preferences,
                            function(newPreferences) {
                                // notify
                                // that user
                                // preferences
                                // have
                                // changed
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
            function($rootScope, $scope, $log, $timeout, utils, prefService, preferences,
                activities) {

                $rootScope.selectedTab = 3;
                $rootScope.preferences = preferences;
                $scope.useDetailedWay = preferences.enableSubActivities;
                $scope.useFineGranularity = (preferences.granularity === 0.25);
                $scope.activities = activities;

                /**
                 * @binding
                 */
                $scope.isActivityVisible = function(activityId) {
                    return $.inArray(+activityId, $rootScope.preferences.activitiesFilter) === -1;
                };

                /**
                 * @binding
                 */
                $scope.getActivityLabelCssClasses = function(activityId) {
                    return {
                        'pref-activity-label' : true,
                        strike : !$scope.isActivityVisible(activityId)
                    };
                };

                /**
                 * Handles user preferences change event.
                 * @event
                 */
                $scope.$on('md.preferences.changed', function(event, prefs) {
                    $scope.useFineGranularity = (prefs.granularity === 0.25);
                    $scope.useDetailedWay = prefs.enableSubActivities;
                });

                /**
                 * @event granularity toggle-button change.
                 */
                angular.element('.pref-granularity input[data-toggle="toggle"]').change(
                    function(event) {
                        var status = angular.element(this).prop('checked');
                        $log.debug('checked: ' + status);
                        $rootScope.preferences.granularity = status ? 0.25 : 0.5;
                        prefService.updateUserPreferences($rootScope.preferences);
                    });

                /**
                 * @event detailedWay toggle-button change.
                 */
                angular.element('.pref-detailed-way input[data-toggle="toggle"]').change(
                    function(event) {
                        var status = angular.element(this).prop('checked');
                        $log.debug('checked: ' + status);
                        $rootScope.preferences.enableSubActivities = status;
                        prefService.updateUserPreferences($rootScope.preferences);
                    });

                /**
                 * @private
                 * @callback
                 */
                function activityVisibilityChangeEventHandler(event) {
                    var activityId = +angular.element(this).attr('data-activity-id');
                    var visible = angular.element(this).prop('checked');
                    if (visible) {
                        // remove if exists in filter
                        // list
                        $rootScope.preferences.activitiesFilter = $.grep(
                            $rootScope.preferences.activitiesFilter, function(currentActivityId) {
                                // the item
                                // will be
                                // in the
                                // result
                                // array
                                // only if
                                // the test
                                // returns
                                // true
                                return currentActivityId !== activityId;
                            });
                    } else {
                        // add if not exists in filter
                        // list
                        if ($.inArray(activityId, $rootScope.preferences.activitiesFilter) === -1) {
                            $rootScope.preferences.activitiesFilter.push(activityId);
                        }
                    }
                    prefService.updateUserPreferences($rootScope.preferences);
                }

                /**
                 * Initializing
                 */
                $timeout(function() {
                    // Initializes toggle-buttons.
                    angular.element('input[data-toggle="toggle"]').bootstrapToggle();
                    // Registers change listener (must
                    // be called when dom is rendered)
                    angular.element('.pref-activities input[data-toggle="toggle"]').change(
                        activityVisibilityChangeEventHandler);
                });

            } ]);

        return module;
    });
