/*global define:false */

/**
 * @file Recap module
 * @module mandy/recap
 * @author rpatriarche
 */
define([ 'angular', 'angular-route', 'angular-resource', 'mandy-common' ], function(angular) {
    'use strict';

    var module = angular.module('mdRecap', [ 'ngRoute', 'ngResource', 'mdCommon' ]);

    module.service('RecapService', [
        '$rootScope',
        '$resource',
        '$log',
        'CONTEXT_ROOT',
        'ErrorService',
        function($rootScope, $resource, $log, CONTEXT_ROOT, errorService) {

            var RecapResource = $resource(CONTEXT_ROOT + "/api/recap");

            /**
             * Recap API
             */
            return {
                /**
                 * Returns the recap from given parameters.
                 */
                getRecap : function() {
                    return RecapResource.get({}, angular.noop, function(httpResponse) {
                        errorService.errorHandler(httpResponse,
                            "Erreur technique récupération récapitulatif");
                    }).$promise;
                }
            }
        } ]);

    /**
     * Configuration of the router.
     */
    module.config([ '$routeProvider', function($routeProvider) {

        /**
         * Navigation
         */
        $routeProvider.when('/recap', {
            templateUrl : "scripts/modules/recap/recap.html",
            controller : 'RecapController'
        });

    } ]);

    /**
     * Recap controller.
     */
    module.controller('RecapController', [ '$rootScope', '$scope', '$log', '$timeout', 'Utils',
        'RecapService', function($rootScope, $scope, $log, $timeout, utils, recapService) {
            $rootScope.selectedTab = 2;
        } ]);

    return module;
});
