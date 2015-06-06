/* global define:false */

/**
 * @file Datagrid controller for detailed view mode.
 * @module mandy/datagrid/controller/detailed
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define([ 'angular', 'angular-route', 'blockui', 'mandy-common', 'mandy-datagrid-service' ],
    function(angular) {
        'use strict';

        var module = angular.module('mdDatagridControllerViewDayDetailed', [ 'ngRoute', 'mdCommon',
            'mdDatagridService' ]);

        module.controller('DatagridViewDayDetailedController', [
            '$rootScope',
            '$scope',
            '$location',
            '$log',
            '$timeout',
            '$route',
            '$q',
            'Globals',
            'Utils',
            'ImputationService',
            function($rootScope, $scope, $location, $log, $timeout, $route, $q, globals, utils,
                imputationService) {
                var prefs = $rootScope.preferences;
            } ]);
    });
