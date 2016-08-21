(function() {
    'use strict';

    var app = angular.module('mandyApp');

    app.service('CalendarService', [
        '$resource',
        'CONTEXT_ROOT',
        'ErrorService',
        function($resource, CONTEXT_ROOT, errorService) {

            var Calendar = $resource(CONTEXT_ROOT + "/api/datagrid/:year/:month");

            /**
             * @alias mandy/calendar/service
             */
            return {
                /**
                 * Retourne la datagrid pour l'année et le mois indiqué.
                 */
                loadDataGrid : function(year, month) {

                    return Calendar.get({
                        year : year,
                        month : month
                    }, angular.noop, function(httpResponse) {
                        errorService.errorHandler(httpResponse,
                            "Erreur technique récupération grille de données");
                    });
                }
            };
        } ]);

    app.service('ImputationService', [
        '$resource',
        'CONTEXT_ROOT',
        '$log',
        'ErrorService',
        function($resource, CONTEXT_ROOT, $log, errorService) {

            var Imputations = $resource(CONTEXT_ROOT + "/api/imputations/:id", {
                id : '@id'
            }, {
                'query' : {
                    method : 'GET',
                    url : CONTEXT_ROOT + "/api/imputations/:year/:month"
                },
                'save' : {
                    method : 'POST'
                },
                'update' : {
                    method : 'PUT'
                },
                'remove' : {
                    method : 'DELETE'
                }
            });

            /**
             * @alias mandy/imputation/service
             */
            // TODO: tous les services devraient systématiquement retourner
            // directement la promise de la resource
            // de manière à ce que les routes synchronise les données avant
            // d'invoquer leur contrôleur
            return {
                /**
                 * Retourne les imputations pour l'utilisateur et le mois donnés.
                 */
                findImputations : function(userId, year, month) {

                    return Imputations.query({
                        user : userId,
                        year : year,
                        month : month
                    }, function(data) {
                        $log.debug("imputations = " + data);
                    },

                    function(httpResponse) {
                        errorService.errorHandler(httpResponse,
                            "Erreur technique récupération imputations");
                    });
                },

                createImputation : function(userId, activityId, date, quota, comment) {
                    var imputations = new Imputations();
                    imputations.resourceId = userId;
                    imputations.activityId = activityId;
                    imputations.date = date;
                    imputations.quota = quota;
                    imputations.comment = comment;
                    // attention les méthodes d'instances Angular#Resource
                    // retournent directement la promise
                    return imputations.$save(imputations, angular.noop, function(httpResponse) {
                        errorService.errorHandler(httpResponse,
                            "Erreur technique creation imputation");
                    });
                },

                updateImputation : function(imputation) {
                    return Imputations.update({
                        id : imputation.imputationId
                    }, imputation, angular.noop, function(httpResponse) {
                        errorService.errorHandler(httpResponse,
                            "Erreur technique update imputation");
                    });
                },

                deleteImputation : function(imputationId) {
                    // attention les méthodes de classe retournent une
                    // référence vide sur l'objet résultat qui sera peuplé
                    // au retour de la requête
                    // cet objet possède également une propriété '$promise'
                    // depuis angular 1.2
                    return Imputations.remove({
                        id : imputationId
                    }, {}, angular.noop, function(httpResponse) {
                        errorService.errorHandler(httpResponse,
                            "Erreur technique delete imputation");
                    });
                }
            };
        } ]);

})();
