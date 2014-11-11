/*global define:false */

/**
 * @file Datagrid controller for dauily view mode.
 * @module mandy/datagrid/controller/day
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define(
    [ 'angular', 'angular-route', 'blockui', 'mandy-common',
        'mandy-datagrid-service' ],
    function(angular) {
      'use strict';

      var module = angular.module('mdDatagridControllerViewDay', [ 'ngRoute',
          'mdCommon', 'mdDatagridService' ]);

      module
          .controller(
              'DatagridDayViewController',
              [
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
                  'IGNORE_COMMENT',
                  function($rootScope, $scope, $location, $log, $timeout,
                      $route, $q, globals, utils, imputationService,
                      IGNORE_COMMENT) {

                    var year = $route.current.params.year;
                    var month = $route.current.params.month;
                    var day = $route.current.params.day;

                    var strDate = year + "-" + month + "-" + day;

                    $scope.currentDay = utils.formatDate(strDate);

                    /**
                     * Registers the save event handler when the user switch
                     * day/month view mode.
                     * 
                     * @callback
                     * @event 'md.datagrid.imputation.save'
                     */
                    var saveHandlerOff = $scope
                        .$on(
                            'md.datagrid.imputation.save',
                            function(event, imputationId, activityId, newQuota,
                                newComment) {
                              var item = findImputationPmItem(imputationId);
                              if (item) {
                                $scope.updateImputation(item, newQuota,
                                    newComment);
                              } else {
                                $log
                                    .warn("No presentation model item found to perform save event");
                              }
                            });

                    /**
                     * @binding
                     */
                    $scope.quotas = [ {
                      floatValue : 0.0,
                      printValue : "0.00"
                    }, {
                      floatValue : 0.25,
                      printValue : "0.25"
                    }, {
                      floatValue : 0.5,
                      printValue : "0.50"
                    }, {
                      floatValue : 0.75,
                      printValue : "0.75"
                    }, {
                      floatValue : 1.0,
                      printValue : "1.00"
                    } ];

                    /**
                     * TODO: services should directly return promises (that way
                     * angular will resolve them before instantiate the
                     * controller)
                     */
                    $q
                        .all(
                            [ $scope.datagrid.$promise,
                                $scope.activities.$promise,
                                $scope.imputationsMap.$promise ])
                        .then(
                            function() {
                              $scope.workingDay = utils.isWorkingDay(strDate,
                                  $scope.datagrid);

                              $scope
                                  .$watch(
                                      'workingDay',
                                      function(newValue, oldValue) {
                                        if (newValue) {
                                          // déblocage de la saisie
                                          $('.datagrid-body-view-day')
                                              .unblock();
                                          // réactivation du scroll
                                          $('html, body').css({
                                            'overflow' : 'auto',
                                            'height' : 'auto'
                                          });
                                        } else {
                                          $('.datagrid-body-view-day')
                                              .block(
                                                  {
                                                    message : '<h1>Saisie impossible</h1><span class="glyphicon glyphicon-ban-circle"></span><h2>(il faut être sur un jour ouvré pour pouvoir imputer)</h2>',
                                                    // disable vertical
                                                    // centering
                                                    centerY : false,
                                                    // apply css props as
                                                    // desired
                                                    css : {
                                                      top : '20px',
                                                      cursor : 'not-allowed',
                                                      width : '60%',
                                                      padding : '10px 10px',
                                                      'border-width' : '2px',
                                                      'background-color' : 'rgb(221, 221, 221)'
                                                    },

                                                    overlayCSS : {
                                                      cursor : 'not-allowed'
                                                    }

                                                  });

                                          // désactivation du scroll (sinon pb
                                          // avec le blockui qui un z-index très
                                          // élevé)
                                          $('html, body').css({
                                            'overflow' : 'hidden',
                                            'height' : '100%'
                                          });
                                        }
                                      });

                              $scope.preparedDataForDayView = preparePresentationModel(strDate);
                            });

                    /**
                     * Shows dialog for imputation details with commment.
                     * 
                     * @action
                     */
                    $scope.showImputationDetails = function(item, quota) {
                      var imputationId = item.imputation.imputationId;
                      var activityId = item.activity.id;
                      var date = strDate;
                      var quota = item.imputation.quota;

                      $scope.showModal(imputationId, activityId, date, quota);
                    };

                    /**
                     * @action
                     */
                    $scope.displayNextDay = function() {
                      $location.path("/datagrid/"
                          + utils.buildPathForNextDay(strDate));
                    };

                    /**
                     * @action
                     */
                    $scope.displayPreviousDay = function() {
                      $location.path("/datagrid/"
                          + utils.buildPathForPreviousDay(strDate));
                    };

                    /**
                     * @action
                     */
                    $scope.displayToday = function() {
                      $location.path("/datagrid/" + utils.buildPathForToday());
                    };

                    /**
                     * Updates imputation.
                     * 
                     * @action
                     */
                    $scope.updateImputation = function(pmItem, newQuotaValue,
                        newComment) {
                      var comment = newComment ? newComment : IGNORE_COMMENT;
                      if (isSyncRequired(pmItem, newQuotaValue, newComment)) {
                        // first synchronize backend using parent scope function
                        var imputationId = pmItem.imputation ? pmItem.imputation.imputationId
                            : null;
                        var asyncToken = $scope.synchronizeBackend(
                            imputationId, pmItem.activity.id, utils
                                .getDate(strDate), newQuotaValue, comment);
                        asyncToken.promise
                            .then(function(imputation) {
                              switch (asyncToken.type) {
                              case 'D':
                                // update pm
                                delete pmItem.imputation;
                                pmItem.printQuota = utils.formatQuota(0);
                                // remove imputation entry from flobal model
                                var index = $scope.findImputationIndex(
                                    pmItem.activity.id,
                                    pmItem.imputation.imputationId);
                                $scope.imputationsMap[pmItem.activity.id]
                                    .splice(index, 1);
                                break;

                              case 'C':
                                // update global model
                                var imputations = $scope.imputationsMap[pmItem.activity.id];
                                if (!imputations) {
                                  imputations = $scope.imputationsMap[pmItem.activity.id] = [];
                                }
                                imputations.push(imputation);
                                // update pm
                                pmItem.imputation = imputation;
                                pmItem.printQuota = utils
                                    .formatQuota(imputation.quota);
                                break;

                              case 'U':
                                pmItem.imputation.imputationId = imputation.imputationId;
                                pmItem.imputation.quota = imputation.quota;
                                pmItem.imputation.comment = imputation.comment;
                                pmItem.printQuota = utils
                                    .formatQuota(imputation.quota);
                                break;

                              default:
                                break;
                              }
                            });
                      }
                    };

                    /**
                     * @private
                     */
                    var isSyncRequired = function(pmItem, newQuotaValue,
                        newComment) {
                      return !pmItem.imputation
                          || pmItem.imputation.quota != newQuotaValue
                          || pmItem.imputation.comment != newComment
                    };

                    /**
                     * @binding
                     */
                    $scope.getQuotaClasses = function(item, quota) {
                      var status = item.imputation ? item.imputation.quota == quota.floatValue
                          : quota.floatValue == 0;

                      return {
                        'btn-radio-quota' : 'true',
                        checked : status
                      };
                    };

                    /**
                     * Returns false if the comment button should be disabled,
                     * true if not.
                     * 
                     * @binding
                     */
                    $scope.isCommentButtonDisabled = function(item) {
                      return !$scope.workingDay || item.printQuota == "0.00";
                    };

                    var preparePresentationModel = function(strDate) {
                      var data = [];
                      angular
                          .forEach(
                              $scope.activities,
                              function(activity, index) {
                                var item = {};
                                item.activity = activity;
                                // default quota value set to zero
                                item.printQuota = utils.formatQuota(0);
                                var activityImputations = $scope.imputationsMap[activity.id];
                                if (activityImputations) {
                                  var imputation = utils.findImputation(
                                      activityImputations, strDate);
                                  if (imputation) {
                                    item.imputation = imputation;
                                    item.printQuota = utils
                                        .formatQuota(imputation.quota);
                                  }
                                }
                                data.push(item);
                              });

                      return data;
                    };

                    /**
                     * Retrieves an imputation PM in scope from imputation id.
                     * 
                     * @private
                     */
                    var findImputationPmItem = function(imputationId) {
                      for (var i = 0; i < $scope.preparedDataForDayView.length; i++) {
                        var item = $scope.preparedDataForDayView[i];
                        // skip null imputation corresponding to empty
                        // imputation for an activity
                        if (item.imputation
                            && item.imputation.imputationId == imputationId) {
                          return item;
                        }
                      }
                      return null;
                    };

                  } ]);
    });