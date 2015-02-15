/*global define:false */

/**
 * @file Common service
 * @module mandy/common
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define(
    [ 'angular', 'moment' ],
    function(angular, moment) {
      'use strict';

      // configuration de la librairie Moment.js
      moment.locale('fr');

      var module = angular.module('mdCommon', []);

      var UtilsService = function() {
        var DATAGRID_PARAMS = "[year]/YYYY/[month]/MM";
        var DATAGRID_PARAMS_FOR_VIEW_DAY = DATAGRID_PARAMS + "/[day]/DD";

        this.getDate = function(strDate) {
          return moment(strDate).toDate();
        };

        this.formatDate = function(strDate) {
          return moment(strDate).format("LL");
        };

        this.getDay = function(strDate) {
          return moment(strDate).format("DD");
        };

        this.getCurrentDay = function() {
          return moment().format("DD");
        };

        this.areMonthsEqual = function(currentFormattedMonth, date) {
          var dateMonth = moment(date).format("MM");
          return currentFormattedMonth === dateMonth;
        };

        this.formatMonthLabel = function(month) {
          var nb = (+month - 1);
          return moment().month(nb).format("MMMM");
        };

        this.getYear = function(strDate) {
          return new Date(strDate).getFullYear();
        };

        this.buildPathForCurrentMonth = function() {
          return moment().format(DATAGRID_PARAMS);
        };

        this.buildPathForNextMonth = function(year, month) {
          return buildPath(year, month);
        };

        this.buildPathForPreviousMonth = function(year, month) {
          return buildPath(year, month, true);
        };

        this.buildPathForToday = function() {
          return moment().format(DATAGRID_PARAMS_FOR_VIEW_DAY);
        };

        this.buildPathForNextDay = function(strDate) {
          return moment(strDate).add(1, 'days').format(
              DATAGRID_PARAMS_FOR_VIEW_DAY);
        };

        /**
         * 
         */
        this.buildPathForPreviousDay = function(strDate) {
          return moment(strDate).subtract(1, 'days').format(
              DATAGRID_PARAMS_FOR_VIEW_DAY);
        };

        /**
         * Retourne vrai si la date dénotée par la chaîne passée en paramètre
         * appartient à la datagrid passée également en paramètre, faux sinon.
         * 
         * @public
         */
        this.isWorkingDay = function(strDate, datagrid) {
          // je n'utilise pas un forEach angular car on ne peut pas
          // utiliser break ou return à l'intérieur de la boucle
          // see: https://github.com/angular/angular.js/issues/263
          for (var w = 0; w < datagrid.weeks.length; w++) {
            var days = datagrid.weeks[w].days;
            for (var d = 0; d < days.length; d++) {
              if (days[d].date === strDate) {
                return true;
              }
            }
          }

          return false;
        };

        /**
         * @private
         */
        function buildPath(year, month, previous) {
          var strDate = year + "-" + month + "-01";
          var m = moment(strDate);
          if (previous) {
            m.subtract(1, 'M');
          } else {
            m.add(1, 'M');
          }

          return m.format(DATAGRID_PARAMS);
        }
        ;

        /**
         * Returns the imputation matching the given date if exists, undefined
         * if not.
         * 
         * @public
         */
        this.findImputation = function(imputations, strDate) {
          // je n'utilise pas un forEach angular car on ne peut pas
          // utiliser break ou return à l'intérieur de la boucle
          // see: https://github.com/angular/angular.js/issues/263
          for (var i = 0; i < imputations.length; i++) {
            if (imputations[i].date === strDate) {
              return imputations[i];
            }
          }

          return undefined;
        };

        /**
         * Returns the quota number as formatted string.
         */
        this.formatQuota = function(quota) {
          quota = quota || 0;
          quota = quota === "-" ? 0 : quota;

          return (+quota).toFixed(2).toString();
        };

        /**
         * Returns a special quota format for zero-value.
         */
        this.formatQuotaForGrid = function(quota) {
          var q = this.formatQuota(quota);
          return q === "0.00" ? "-" : q;
        };

        /**
         * Returns the next quota value from an existing quota value. This
         * function use the given granularity value to determine the correct
         * value. If the next value is not compliant with the current
         * granularity, the closest compliant value is used instead.
         */
        this.getNewQuota = function(oldQuota, granularity, reverse) {
          var q = (oldQuota && oldQuota !== '-') ? (+oldQuota) : 0;
          var values = this.getQuotasModel(granularity);
          var newQuota = undefined;
          if (reverse) {
            if (q === 0) {
              newQuota = 1;
            } else if (q < granularity) {
              newQuota = 0;
            } else {
              newQuota = q - granularity;
              if (!isNewQuotaInPossibleValues(newQuota, values)) {
                // if new value not in values takes the closest lower value
                newQuota = getClosestLowerValue(newQuota, values);
              }
            }
          } else {
            if (q === 1 || (q + granularity) > 1) {
              newQuota = 0;
            } else {
              newQuota = q + granularity;
              if (!isNewQuotaInPossibleValues(newQuota, values)) {
                // if new value not in values takes the closest higher value
                newQuota = getClosestHigherValue(newQuota, values);
              }
            }
          }
          return newQuota;
        };

        /**
         * @private
         */
        var isNewQuotaInPossibleValues = function(floatValue, values) {
          for (var i = 0; i < values.length; i++) {
            if (values[i].floatValue === floatValue) {
              return true;
            }
          }

          return false;
        };

        // /**
        // * Returns the quotas values from granularity.
        // */
        // this.determineQuotaValues = function(granularity) {
        // var values = [];
        // values.push(0);
        // var current = granularity;
        // var nbSteps = (1 / granularity) - 1;
        // for (var i = 0; i < nbSteps; i++) {
        // values.push(current);
        // current = current + granularity;
        // }
        // values.push(1);
        //
        // return values;
        // };

        /**
         * 
         */
        this.getQuotasModel = function(granularity) {
          var values = [];
          values.push(createQuotaModelValue(0.0, this));
          var current = granularity;
          var nbSteps = (1 / granularity) - 1;
          for (var i = 0; i < nbSteps; i++) {
            values.push(createQuotaModelValue(current, this));
            current = current + granularity;
          }
          values.push(createQuotaModelValue(1.0, this));

          return values;
        };

        /**
         * 
         */
        var createQuotaModelValue = function(floatValue, context) {
          var value = {};
          value.floatValue = floatValue;
          value.printValue = context.formatQuota(floatValue);

          return value;
        };

        /**
         * @private
         */
        var getClosestHigherValue = function(newQuota, values) {
          for (var i = 0; i < values.length; i++) {
            if (values[i].floatValue > newQuota) {
              return values[i].floatValue;
            }
          }
          return undefined;
        };

        /**
         * @private
         */
        var getClosestLowerValue = function(newQuota, values) {
          for (var i = values.length - 1; i > 0; i--) {
            if (values[i].floatValue < newQuota) {
              return values[i].floatValue;
            }
          }
          return undefined;
        };

        /**
         * 
         */
        this.getCompletenessGlyphiconClass = function(imputationsMap, strDate) {
          // on additionne l'ensemble des quotas à la date indiquée
          var allQuotasAtDate = 0;
          angular.forEach(imputationsMap, function(imputations) {
            // don't use angular.forEach if you want to break
            for (var i = 0; i < imputations.length; i++) {
              if (imputations[i].date === strDate) {
                allQuotasAtDate += imputations[i].quota;
                break;
              }
            }
          });
          // On détermine l'icône à utiliser
          var glyphicon = 'glyphicon-ok';
          if (allQuotasAtDate < 1) {
            glyphicon = 'glyphicon-arrow-down';
          } else if (allQuotasAtDate > 1) {
            glyphicon = 'glyphicon-arrow-up';
          }

          return glyphicon;
        };

      };

      module.service('Utils', UtilsService);

      var GlobalsService = function() {
        var currentDate = new Date();

        this.getCurrentDate = function() {
          return currentDate;
        };

        this.setCurrentDate = function(date) {
          currentDate = date;
        };
      };

      module.service('Globals', GlobalsService);

      var ErrorService = [
          '$rootScope',
          '$location',
          '$log',
          '$timeout',
          function($rootScope, $location, $log, $timeout) {

            this.errorHandler = function(httpResponse, context) {
              var status, details, stack, title;
              status = httpResponse.status === undefined ? "???"
                  : httpResponse.status;
              if (status === 0) {
                // socket error (server is down)
                title = "Serveur inaccessible";
                details = "Le serveur ne répond pas, veuillez contacter l'administrateur de l'application pour qu'il redémarre le bouzin.";
              } else {
                details = httpResponse.statusText ? httpResponse.statusText
                    : "aucune information récupérable";
                stack = httpResponse.data;
                stack = stack.substring((stack.indexOf("<body>") + 6), stack
                    .indexOf("</body>"));
                title = "Code HTTP " + status;
                details = "Détails : " + details;
              }
              $log.debug("[" + title + "] " + context + ", " + details);
              $rootScope.alert = {
                title : title,
                details : details,
                stack : stack,
                context : context
              };
              $rootScope.selectedTab = 0;
              // delay to be sure the modal (if opened) is closed before
              // redirection
              $timeout(function() {
                $location.path("/error");
              }, 200);
            };

          } ];

      module.service('ErrorService', ErrorService);

      module.controller('ErrorController', [ '$rootScope', '$scope', '$sce',

      function($rootScope, $scope, $sce) {
        $scope.stack = $sce.trustAsHtml($rootScope.alert.stack);
      } ]);

      module
          .service(
              'ActivityService',
              [
                  '$resource',
                  'CONTEXT_ROOT',
                  function($resource, CONTEXT_ROOT) {

                    var Activities = $resource(CONTEXT_ROOT + "/api/activities");

                    /**
                     * @alias mandy/activity/service
                     */
                    return {
                      /**
                       * Returns user team activities.
                       */
                      getUserTeamActivities : function() {

                        return Activities
                            .query(
                                {},
                                angular.noop,
                                function(httpResponse) {
                                  errorService
                                      .errorHandler(httpResponse,
                                          "Erreur technique récupération activités de l'équipe de l'utilisateur");
                                }).$promise;
                      },
                      /**
                       * Returns user relevant activities.
                       */
                      getUserRelevantActivities : function() {

                        return Activities
                            .query(
                                {
                                  filter : 'relevant'
                                },
                                angular.noop,
                                function(httpResponse) {
                                  errorService
                                      .errorHandler(httpResponse,
                                          "Erreur technique récupération activités pertinentes");
                                }).$promise;
                      }
                    };
                  } ]);

      return module;
    });
