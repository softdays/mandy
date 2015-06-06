/* global define:false */

/**
 * @file We configure here the main module of our Angular Js application
 * @module mandy/app
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define([ 'angular', 'angular-route', 'mandy-common', 'mandy-user', 'mandy-datagrid',
    'mandy-preferences' ], function(angular) {
    'use strict';

    var app = angular.module('mandyApp', [ 'ngRoute', 'mdCommon', 'mdUser', 'mdDatagrid',
        'mdPreferences' ]);

    app.run([
        '$templateCache',
        '$http',
        '$rootScope',
        function($templateCache, $http, $rootScope) {
            // $http.defaults.cache = false; // pas la peine de le faire
            // ici, c'est géré par le httpProvider

            // put error partial in cache
            $http.get('partials/error.html', {
                cache : $templateCache
            });

            /*
             * Définition des variables globales : il est nécessaire de les déclarer dès
             * l'initialisation car sinon elles ne sont pas partagées par l'ensemble des
             * controllers. Attention: ne pas généraliser
             */
            $rootScope.user = undefined;
            $rootScope.selectedTab = undefined;
            $rootScope.contextRoot = undefined;
            $rootScope.admin = false;

            var MAX_WIDTH = 992;

            // TODO: move this code to datagrid module
            var updateMode = function(width) {
                /*
                 * if ($rootScope.preferences && $rootScope.preferences.enableSubActivities &&
                 * $rootScope.datagridInputMode !== "day") { $rootScope.datagridInputMode = "day";
                 * $rootScope.$broadcast('md.datagrid.input.mode.changed',
                 * $rootScope.datagridInputMode) } else
                 */
                if (width < MAX_WIDTH && $rootScope.datagridInputMode !== "day") {
                    $rootScope.datagridInputMode = "day";
                    $rootScope.$broadcast('md.datagrid.input.mode.changed',
                        $rootScope.datagridInputMode);
                } else if (width >= MAX_WIDTH && $rootScope.datagridInputMode !== "month") {
                    $rootScope.datagridInputMode = "month";
                    $rootScope.$broadcast('md.datagrid.input.mode.changed',
                        $rootScope.datagridInputMode);
                }
            };

            updateMode($(window).width());

            $(window).resize(function() {
                $rootScope.$apply(function() {
                    updateMode($(window).width());
                });
            });

        } ]);

    // Configuration of the router
    app.config([ '$httpProvider', '$routeProvider', '$locationProvider', '$logProvider',
        function($httpProvider, $routeProvider, $locationProvider, $logProvider) {
            $logProvider.debugEnabled(true);

            // active resource cache (not recommanded or you have to deal
            // with server sync)
            // $httpProvider.defaults.cache = false;

            // Put standard Ajax header to detect Ajax request server-side
            var headers = $httpProvider.defaults.headers;
            headers.common["X-Requested-With"] = 'XMLHttpRequest';

            // Declare the basic routes
            $routeProvider.when('/', {
                templateUrl : "partials/loading.html",
                controller : 'MainController',
                resolve : {
                    user : [ 'UserService', function(userService) {
                        return userService.getUserData();
                    } ],
                    preferences : [ 'PreferencesService', function(preferencesService) {
                        return preferencesService.getUserPreferences();
                    } ]
                }
            });

            $routeProvider.when('/error', {
                templateUrl : "partials/error.html",
                controller : 'ErrorController'
            });

            $routeProvider.otherwise({
                redirectTo : '/error',
                resolve : {
                    missingRoute : function() {
                        return true;
                    }
                }
            });

            $routeProvider.when('/logout', {
                redirectTo : '/'
            });

        } ]);

    // Definition of the main controller
    app.controller('MainController', [ '$rootScope', '$scope', '$location', '$route',
        'CONTEXT_ROOT', "Utils", 'user', 'preferences',
        function($rootScope, $scope, $location, $route, CONTEXT_ROOT, utils, user, preferences) {

            $rootScope.contextRoot = CONTEXT_ROOT;
            $rootScope.user = user;
            $rootScope.preferences = preferences;

            user.$promise.then(function(resolvedUser) {
                $rootScope.admin = isAdmin(resolvedUser);
                $rootScope.role = resolvedUser.role.substring(5, resolvedUser.role.length);

                if ($location.search().redirect) {
                    // pour supprimer la query string devenue inutile il faut
                    // utiliser
                    // la méthode $location.url()
                    // see:
                    // http://stackoverflow.com/questions/17376416/angularjs-how-to-clear-query-parameters-in-the-url
                    $location.url($location.search().redirect);
                } else {
                    // param format = yyyy/MM[01-12]
                    var params = null;
                    // if (preferences.enableSubActivities || $rootScope.datagridInputMode ===
                    // 'day') {
                    if ($rootScope.datagridInputMode === 'day') {
                        params = utils.buildPathForToday();
                    } else {
                        params = utils.buildPathForCurrentMonth();
                    }
                    $location.path("/datagrid/" + params);
                }

            });
        } ]);

    /**
     * Returns true is the user has admin role, false, if not.
     */
    function isAdmin(user) {
        return user.role === 'ROLE_ADMIN' || user.role === 'ROLE_MANAGER';
    }

    var contextRootValue = window.location.pathname;
    contextRootValue = contextRootValue.substring(0, contextRootValue.indexOf("/", 1));

    app.constant('CONTEXT_ROOT', contextRootValue);

    return app;
});
