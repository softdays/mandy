( function () {
  'use strict';

  var app = angular.module( 'mandyApp' );

  app.service( 'RecapService', [
    '$rootScope',
    '$resource',
    '$log',
    'CONTEXT_ROOT',
    'ErrorService',
    function ( $rootScope, $resource, $log, CONTEXT_ROOT, errorService ) {

      var RecapResource = $resource( CONTEXT_ROOT + "/api/recap" );

      /**
       * Recap API
       */
      return {
        /**
         * Returns the recap from given parameters.
         */
        getRecap: function () {
          return RecapResource.get( {}, angular.noop, function ( httpResponse ) {
              errorService.errorHandler( httpResponse,
                "Erreur technique récupération récapitulatif" );
            } )
            .$promise;
        }
      }
    }
  ] );

  /**
   * Configuration of the router.
   */
  app.config( [ '$routeProvider', function ( $routeProvider ) {

    /**
     * Navigation
     */
    $routeProvider.when( '/recap', {
      templateUrl: "app/recap/recap.html",
      controller: 'RecapController'
    } );

  } ] );

  /**
   * Recap controller.
   */
  app.controller( 'RecapController', [ '$rootScope', '$scope', '$log', '$timeout', 'Utils',
    'RecapService',
    function ( $rootScope, $scope, $log, $timeout, utils, recapService ) {
      $rootScope.selectedTab = 2;
    }
  ] );

} )();
