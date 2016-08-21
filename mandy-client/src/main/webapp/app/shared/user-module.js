( function () {
  'use strict';

  var app = angular.module( 'mandyApp' );

  app.service( 'UserService', [
    '$resource',
    '$cookieStore',
    '$log',
    'CONTEXT_ROOT',
    'ErrorService',
    function ( $resource, $cookieStore, $log, CONTEXT_ROOT, errorService ) {

      var UserResource = $resource( CONTEXT_ROOT + "/api/user" );

      /**
       * @alias mandy/user/service
       */
      return {
        /**
         * Returns the current user data.
         */
        getUserData: function () {
          return UserResource.get( {}, function ( data ) {
              $cookieStore.put( 'user', data );
              $log.debug( "user id retrieved from cookie: " +
                $cookieStore.get( 'user' )
                .resourceId );
            }, function ( httpResponse ) {
              errorService.errorHandler( httpResponse,
                "Erreur technique récupération infos utilisateur" );
            } )
            .$promise;
        }
      };
    }
  ] );

} )();
