(function() {
    'use strict';

    var app = angular.module('mandyApp');

    app.controller('DatagridViewDayDetailedController', [
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
})();
