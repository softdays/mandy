/*global define:false */

/**
 * @file We configure here the main module of our Angular Js application
 * @module mandy/app
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define(
    ['angular', 'angular-route', 'mandy-common', 'mandy-user',
        'mandy-datagrid'],
    function(angular){
        'use strict';

        var app = angular.module('mandyApp', ['ngRoute', 'mdCommon', 'mdUser',
            'mdDatagrid']);

        app.run([
            "$rootScope",
            function($rootScope){
                // définition des variables globales
                // il est nécessaire de les déclarer dès
                // l'initialisation car
                // sinon elles ne sont pas partagées par
                // l'ensemble des
                // controllers
                // Achtung: ne pas généraliser
                $rootScope.user = undefined;
                $rootScope.selectedTab = undefined;
                $rootScope.contextRoot = undefined;
                $rootScope.admin = false;

                //TODO: move this code to datagrid module
                var updateMode = function(width){
                    if (width < 992 && $rootScope.datagridInputMode != "day") {
                        $rootScope.datagridInputMode = "day";
                        $rootScope.$broadcast('md.datagrid.input.mode.changed',
                            $rootScope.datagridInputMode);
                    } else if (width >= 992
                        && $rootScope.datagridInputMode != "month") {
                        $rootScope.datagridInputMode = "month";
                        $rootScope.$broadcast('md.datagrid.input.mode.changed',
                            $rootScope.datagridInputMode);
                    }
                };

                updateMode($(window).width());

                $(window).resize(function(){
                    // console.log($(window).width());

                    $rootScope.$apply(function(){
                        updateMode($(window).width());
                        // $rootScope.$broadcast('myEventName');
                        // console.log("reload");
                        // $route.reload();
                    });
                });

                // $rootScope.$watch('datagridInputMode',
                // function() {
                //
                // });
            }]);

        // Configuration of the router
        app.config([
            '$httpProvider',
            '$routeProvider',
            '$locationProvider',
            '$logProvider',
            function($httpProvider, $routeProvider, $locationProvider,
                     $logProvider){
                $logProvider.debugEnabled(true);

                $httpProvider.defaults.cache = true;

                // Declare the basic routes
                $routeProvider.when('/', {
                    // redirectTo: '/datagrid',
                    templateUrl: "partials/loading.html",
                    controller: 'MainController',
                    resolve: {
                        user: ['UserService', function(userService){
                            return userService.getUserData();
                        }]
                    }
                });

                $routeProvider.when('/error', {
                    templateUrl: "partials/error.html"
                });

                $routeProvider.otherwise({
                    redirectTo: '/error'
                });

                $routeProvider.when('/logout', {
                    redirectTo: '/'
                });

            }]);

        // Definition of the main controller
        app
            .controller(
            'MainController',
            [
                '$rootScope',
                '$scope',
                '$location',
                '$route',
                'CONTEXT_ROOT',
                'user',
                "Utils",
                function($rootScope, $scope, $location, $route, CONTEXT_ROOT,
                         user, utils){

                    $rootScope.contextRoot = CONTEXT_ROOT;
                    $rootScope.user = user;
                    user.$promise
                        .then(function(resolvedUser){
                            $rootScope.admin = (resolvedUser.role == 'ROLE_ADMIN' || resolvedUser.role == 'ROLE_MANAGER');
                            $rootScope.role = resolvedUser.role.substring(5,
                                resolvedUser.role.length);

                            if ($location.search().redirect) {
                                // pour supprimer la
                                // query string devenue
                                // inutile
                                // il faut utiliser la
                                // méthode
                                // $location.url()
                                // see:
                                // http://stackoverflow.com/questions/17376416/angularjs-how-to-clear-query-parameters-in-the-url
                                $location.url($location.search().redirect);
                            } else {
                                // param format =
                                // yyyy/MM[01-12]
                                var params = null;
                                if ($rootScope.datagridInputMode == 'day') {
                                    params = utils.buildPathForToday();
                                } else {
                                    params = utils.buildPathForCurrentMonth();
                                }
                                $location.path("/datagrid/" + params);
                            }

                        });
                }]);

        var contextRootValue = window.location.pathname;
        contextRootValue = contextRootValue.substring(0, contextRootValue
            .indexOf("/", 1));

        app.constant('CONTEXT_ROOT', contextRootValue);

        // app.provider("ContextRoot", function() {
        // this.$get = function() {
        // return {
        // getValue: function() {
        // return "/mandy";
        // }
        // };
        // };
        // });

        return app;
    });
