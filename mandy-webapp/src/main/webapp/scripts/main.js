/*jshint expr:true */
/*global require:false, alert:false */

/**
 * @file This file contain the RequireJs configuration and will start the
 *       application when all dependencies are loaded
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
window.require
    && (function(requirejs) {
      'use strict';

      // use "requirejs" variable name instead of "require":
      // see:
      // http://stackoverflow.com/questions/19472500/using-require-config-in-a-vows-file

      // Sets the require.js configuration for your application.
      requirejs
          .config({
            // 3rd party script alias names (Easier to type 'jquery' than
            // 'libs/jquery-1.10.1.min')
            paths : {
              // 'Package' based for our application
              'mandy' : '.',
              'mandy-common' : './modules/common-module',
              'mandy-datagrid' : './modules/datagrid/datagrid-module',
              'mandy-datagrid-controller' : './modules/datagrid/datagrid-controller',
              'mandy-datagrid-controller-view-month' : './modules/datagrid/datagrid-controller-view-month',
              'mandy-datagrid-controller-view-day' : './modules/datagrid/datagrid-controller-view-day',
              'mandy-datagrid-controller-modal' : './modules/datagrid/datagrid-controller-modal',
              'mandy-datagrid-service' : './modules/datagrid/datagrid-services',
              'mandy-datagrid-directive' : './modules/datagrid/datagrid-directives',
              'mandy-datagrid-route' : './modules/datagrid/datagrid-routes',
              'mandy-user' : './modules/user-module',
              // Templates path
              'partials' : '../partials/',
              // Core Libraries
              'jquery' : '../frameworks/jquery/jquery',
              'blockui' : '../frameworks/jquery/jquery.blockUI.min',
              'bootstrap' : '../frameworks/bootstrap/js/bootstrap',
              'angular' : '../frameworks/angular/angular',
              'angular-resource' : '../frameworks/angular/angular-resource',
              'angular-route' : '../frameworks/angular/angular-route',
              'angular-cookies' : '../frameworks/angular/angular-cookies',
              'moment' : '../frameworks/moment/moment-with-locales.min'
            },

            // urlArgs: 'timestamp=' + Date.now(), // To prevent caching

            // Put in a shim your your third party scripts that are not AMD
            // compatible
            shim : {
              'jquery' : {
                'deps' : [],
                'exports' : 'jQuery' // attaches 'jQuery' to the window object
              },
              'bootstrap' : {
                'deps' : [ 'jquery' ]
              },
              'blockui' : {
                'deps' : [ 'jquery' ]
              },
              'angular' : {
                'deps' : [ 'jquery' ],
                'exports' : 'angular'
              },
              'angular-resource' : {
                'deps' : [ 'angular' ]
              },
              'angular-route' : {
                'deps' : [ 'angular' ]
              },
              'angular-cookies' : {
                'deps' : [ 'angular' ]
              }
            }
          });

      requirejs([ 'angular', 'bootstrap', 'mandy/app' ], function(angular) {
        // Startup manually the application
        angular.bootstrap(document, [ 'mandyApp' ]);
      });

    }(requirejs));
