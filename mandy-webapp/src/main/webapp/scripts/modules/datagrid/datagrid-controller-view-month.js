/*global define:false */

/**
 * @file Datagrid controller
 * @module mandy/datagrid/controller/month
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define(
    [ 'angular', 'mandy-common', 'mandy-datagrid-service' ],
    function(angular) {
      'use strict';

      var module = angular.module('mdDatagridControllerViewMonth', [
          'mdCommon', 'mdDatagridService' ]);

      module
          .controller(
              'DatagridMonthViewController',
              [
                  '$rootScope',
                  '$scope',
                  '$location',
                  '$log',
                  '$timeout',
                  '$route',
                  '$q',
                  'Utils',
                  'ImputationService',
                  'IGNORE_COMMENT',
                  function($rootScope, $scope, $location, $log, $timeout,
                      $route, $q, utils, imputationService, IGNORE_COMMENT) {

                    /**
                     * Stores the current selected cell for further use.
                     * 
                     * @private
                     */
                    var currentCellSelected = undefined;

                    /**
                     * @private
                     */
                    var findTdCell = function(ngElem) {
                      if (ngElem === undefined) {
                        return undefined;
                      }
                      if (ngElem.prop("tagName") === 'TD') {
                        return ngElem;
                      } else {
                        return findTdCell(ngElem.parent());
                      }
                    };

                    /**
                     * Synchronizes view elements binding quota data.
                     * 
                     * @private
                     */
                    var synchronizeCellData = function(ngCell, newQuota,
                        createdId) {
                      ngCell.attr('data-quota', newQuota);
                      ngCell.find(".value").text(
                          utils.formatQuotaForGrid(newQuota));
                      if (createdId) {
                        ngCell.attr('data-imputation-id', createdId);
                      }
                    };

                    /**
                     * Updates completeness indicator of the given date column.
                     * 
                     * @private
                     */
                    var updateCompletenessMarker = function(strDate) {
                      // retrieve element to update
                      var elem = angular.element("th.day[data-date='" + strDate
                          + "'] .completeness");
                      if (elem) {
                        // remove previous glyphicon class name
                        var cssClasses = 'glyphicon-ok glyphicon-arrow-up glyphicon-arrow-down';
                        elem.removeClass(cssClasses);
                        // add new glyphicon class name
                        var glyphiconClass = utils
                            .getCompletenessGlyphiconClass(
                                $scope.imputationsMap, strDate);
                        elem.addClass(glyphiconClass);
                      } else {
                        $log.error("Completeness cell not found");
                      }
                    };

                    /**
                     * Returns true if at least one of new data differs from its
                     * previous value, false if not.
                     * 
                     * @private
                     */
                    var isSyncRequired = function(activityId, imputationId,
                        newQuota, newComment) {
                      var syncRequired = true;
                      var imputation = $scope.findImputation(activityId,
                          imputationId);
                      if (imputation) {
                        if (newComment === IGNORE_COMMENT) {
                          // on ne doit pas prendre en compte le commentaire
                          syncRequired = imputation.quota !== newQuota;
                        } else {
                          syncRequired = imputation.comment !== newComment
                              || imputation.quota !== newQuota;
                        }
                      } else if (newQuota === 0) {
                        syncRequired = false;
                      }
                      // sinon il s'agit d'une création et le modèle sera mis à
                      // jour au retour de la requête
                      return syncRequired;
                    };

                    /**
                     * Process new data entries on imputation cell.
                     * 
                     * @private
                     */
                    var updateImputation = function(ngCell, newQuota,
                        newComment) {
                      var activityId = ngCell.attr('data-activity-id');
                      $log.debug("activity id=" + activityId);
                      var date = ngCell.attr('data-date');
                      $log.debug("date=" + date);
                      var imputationId = ngCell.attr('data-imputation-id');
                      // est-ce qu'une modification doit être synchronisée ?
                      var syncBackendRequired = isSyncRequired(activityId,
                          imputationId, newQuota, newComment);
                      if (syncBackendRequired) {
                        var asyncToken = $scope.synchronizeBackend(
                            +imputationId, +activityId, date, newQuota,
                            newComment);
                        switch (asyncToken.type) {
                        case 'D':
                          asyncToken.promise.then(function() {
                            // mise à jour du DOM
                            ngCell.removeAttr('data-imputation-id');
                            ngCell.removeAttr('data-quota');
                            // synchronisation du modèle client
                            var index = $scope.findImputationIndex(activityId,
                                imputationId);
                            $scope.imputationsMap[activityId].splice(index, 1);
                            // synchroniser les données de la cellule
                            synchronizeCellData(ngCell, newQuota);
                            // mettre à jour l'indicateur de complétude
                            updateCompletenessMarker(date);
                          });
                          break;

                        case 'U':
                          asyncToken.promise.then(function(updatedImputation) {
                            // synchroniser le modèle client
                            var i = $scope.findImputation(activityId,
                                imputationId);
                            i.quota = updatedImputation.quota;
                            i.comment = updatedImputation.comment;
                            // synchroniser les données de la cellule
                            synchronizeCellData(ngCell, newQuota);
                            // mettre à jour l'indicateur de complétude
                            updateCompletenessMarker(date);
                          });
                          break;

                        case 'C':
                          asyncToken.promise
                              .then(function(createdImputation) {
                                // synchroniser le modèle client
                                var imputations = $scope.imputationsMap[activityId];
                                if (!imputations) {
                                  imputations = $scope.imputationsMap[activityId] = [];
                                }
                                imputations.push(createdImputation);
                                // synchroniser les données de la
                                // cellule
                                synchronizeCellData(ngCell, newQuota,
                                    createdImputation.imputationId);
                                // mettre à jour l'indicateur de
                                // complétude
                                updateCompletenessMarker(date);
                              });
                          break;

                        default:
                          break;
                        }
                      }
                    };

                    /**
                     * @callback
                     * @event 'md.datagrid.imputation.save'
                     * 
                     * @return A deregistration function for this event handler.
                     */
                    $scope
                        .$on(
                            'md.datagrid.imputation.save',
                            function(event, imputationId, activityId, newQuota,
                                newComment) {
                              if (currentCellSelected) {
                                updateImputation(currentCellSelected, newQuota,
                                    newComment);
                              } else {
                                $log
                                    .warn("No current selected cell to perform save event");
                              }
                            });

                    /**
                     * TODO: service should return promise
                     */
                    $scope.datagrid.$promise.then(function() {
                      $scope.formattedMonth = utils
                          .formatMonthLabel($scope.datagrid.month);
                    });

                    /**
                     * @action
                     */
                    $scope.displayNextMonth = function(year, month) {
                      $log.debug("displayNextMonth, year=" + year + ", month="
                          + month);
                      if (year && month) {
                        // prevent multi-submits problem
                        $location.path("/datagrid/"
                            + utils.buildPathForNextMonth(year, month));
                      }
                    };

                    /**
                     * @action
                     */
                    $scope.displayPreviousMonth = function(year, month) {
                      if (year && month) {
                        // prevent multi-submits problem
                        $location.path("/datagrid/"
                            + utils.buildPathForPreviousMonth(year, month));
                      }
                    };

                    /**
                     * @action
                     */
                    $scope.displayCurrentMonth = function() {
                      $location.path("/datagrid/"
                          + utils.buildPathForCurrentMonth());
                    };

                    /**
                     * Table simple-click handler.
                     * 
                     * @action
                     */
                    $scope.tableClickHandler = function(event) {
                      var ngCell = findTdCell(angular.element(event.target));
                      if (ngCell.hasClass("imputation")) {
                        if (currentCellSelected) {
                          currentCellSelected.toggleClass("selected");
                        }
                        ngCell.toggleClass('selected');
                        currentCellSelected = ngCell;
                      }
                    };

                    /**
                     * Table right-click handler.
                     * 
                     * @action
                     */
                    $scope.tableRightClickHandler = function(event) {
                      var ngCell = findTdCell(angular.element(event.target));
                      if (ngCell.hasClass("selected")) {
                        var oldQuota = ngCell.attr("data-quota");
                        var newQuota = utils.getNewQuota(oldQuota,
                            $rootScope.preferences.granularity);
                        updateImputation(ngCell, newQuota, IGNORE_COMMENT);
                      }
                    };

                    /**
                     * Table double-click handler.
                     * 
                     * @action
                     */
                    $scope.tableDoubleClickHandler = function() {
                      if (!currentCellSelected) {
                        return;
                      }

                      var ngCell = currentCellSelected;
                      var imputationId = ngCell.attr('data-imputation-id');
                      var activityId = ngCell.attr('data-activity-id');
                      var date = ngCell.attr('data-date');
                      var quota = ngCell.find(".value").text();
                      if (quota === '-') {
                        quota = 0;
                      }

                      $scope.showModal(imputationId, activityId, date, +quota);
                    };

                    /**
                     * @binding
                     */
                    $scope.countHiddenActivities = $rootScope.preferences.activitiesFilter.length;

                  } ]);
    });
