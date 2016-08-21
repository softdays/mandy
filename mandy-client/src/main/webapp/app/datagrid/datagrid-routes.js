( function () {
  'use strict';

  var app = angular.module( 'mandyApp' );

  // Configuration of the router
  // You need to add the "Provider" suffix to our custom provider, if not
  // Angular doesn't recognize it
  app.config( [
    '$routeProvider',
    function ( $routeProvider ) {

      /**
       * Accès au menu
       */
      $routeProvider.when( '/datagrid', {
        redirectTo: '/'
      } );

      // create single route config
      var route = {
        templateUrl: "app/datagrid/datagrid.html",

        controller: 'DatagridController',

        resolve: {
          year: [ '$route', function ( $route ) {
            return $route.current.params.year;
          } ],

          month: [ '$route', function ( $route ) {
            return $route.current.params.month;
          } ],

          day: [ '$route', function ( $route ) {
            return $route.current.params.day;
          } ],

          datagrid: [ '$route', 'CalendarService',
            function ( $route, calendarService ) {
              var year = $route.current.params.year;
              var month = $route.current.params.month;

              return calendarService.loadDataGrid( year, month );
            }
          ],

          activities: [ 'ActivityService', function ( activityService ) {
            return activityService.getUserRelevantActivities();
          } ],

          imputations: [ '$rootScope', '$route', '$cookieStore',
            'ImputationService',
            function ( $rootScope, $route, $cookieStore, imputationService ) {
              // $cookieStore permet de gérer le refresh
              // sauvage
              var userId = $cookieStore.get( 'user' )
                .resourceId;
              var year = $route.current.params.year;
              var month = $route.current.params.month;
              return imputationService.findImputations( userId, year, month );
            }
          ]
        }
      };

      /**
       * Navigation
       */
      $routeProvider.when( '/datagrid/year/:year/month/:month', route );
      $routeProvider.when( '/datagrid/year/:year/month/:month/day/:day', route );

    }
  ] );
} )();
