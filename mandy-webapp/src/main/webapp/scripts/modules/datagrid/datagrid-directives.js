/*global define:false */

/**
 * @file Datagrid directives
 * @module mandy/datagrid/directive
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define([ 'angular', 'mandy-common' ], function(angular) {
    'use strict';

    var module = angular.module('mdDatagridDirective', [ 'mdCommon' ]);

    module.directive('mdDatagridHeader', [ '$q', 'Utils', function($q, utils) {
        return {
            restrict : 'A',
            scope : false,
            replace : false,
            transclude : false,
            link : function(scope, element) {
                performDatagridHeader($q, scope, element, utils);
            }
        };
    } ]);

    /**
     * @private
     */
    function performDatagridHeader($q, scope, element, utils) {
        // on doit disposer de la datagrid et des imputations
        // pour pouvoir réaliser le traitement
        $q.all([ scope.datagrid.$promise, scope.imputationsMap.$promise ]).then(function() {
            var weeks = scope.datagrid.weeks;
            var elemDays = angular.element('<tr class="days"></tr>');
            var activities = angular.element('<th class="header-activities">Activités</th>');
            elemDays.append(activities);
            angular.forEach(weeks, performDatagridHeaderWeek, {
                // context (will be this in the callee)
                scope : scope,
                utils : utils,
                elemDays : elemDays
            });
            element.replaceWith(elemDays);
        });
    }

    /**
     * @private
     */
    function performDatagridHeaderWeek(week) {
        angular.forEach(week.days, performDatagridHeaderDay, this);
    }

    /**
     * @private
     * @this should contains scope, utils and elemDays (parent element)
     */
    function performDatagridHeaderDay(day) {
        var elemDay = angular.element('<th class="day"></th>');
        var elemDayContentWrapper = angular.element('<div></div>');
        var spanCompleteness = angular.element('<span class="completeness glyphicon"></span>');
        var completenessGlyphiconIcon = this.utils.getCompletenessGlyphiconClass(
            this.scope.imputationsMap, day.date);
        spanCompleteness.addClass(completenessGlyphiconIcon);
        var spanDayValue = angular.element('<span class="day-value"></span>');
        spanDayValue.text(this.utils.getDay(day.date));
        elemDay.attr("data-date", day.date);
        elemDayContentWrapper.append(spanDayValue);
        elemDayContentWrapper.append(spanCompleteness);
        elemDay.append(elemDayContentWrapper);
        if (this.utils.areMonthsEqual(this.scope.datagrid.month, day.date)) {
            elemDay.addClass("current-month");
        }
        this.elemDays.append(elemDay);
    }

    module.directive('mandyDatagridBody', [ '$q', '$rootScope', 'Utils',
        function($q, $rootScope, utils) {

            return {
                restrict : 'A',
                scope : false,
                replace : false,
                transclude : false,
                link : function(scope, element) {
                    performDatagridBody($q, $rootScope, scope, utils, element);
                }
            };
        } ]);

    /**
     * @private
     */
    function performDatagridBody($q, $rootScope, scope, utils, element) {
        $q.all(
            [ scope.datagrid.$promise, scope.activities.$promise, scope.imputationsMap.$promise ])
            .then(function() {
                var tbody = angular.element('<tbody></tbody>');
                angular.forEach(scope.activities, performDatagridBodyActivity, {
                    scope : scope,
                    utils : utils,
                    tbody : tbody,
                    preferences : $rootScope.preferences
                });
                element.replaceWith(tbody);
            });
    }

    /**
     * @private
     * @this scope, utils, activity, tbody
     */
    function performDatagridBodyActivity(activity, index) {
        if ($.inArray(activity.id, this.preferences.activitiesFilter) !== -1) {
            return;
        }
        // update activity property on context
        this.activity = activity;
        // processing activity
        var row = angular.element('<tr></tr>');
        row.attr("title", this.activity.longLabel);
        row.attr('data-activity-id', this.activity.id);
        if (index === this.scope.activities.length - 1) {
            row.attr("data-placement", "top");
        } else {
            row.attr("data-placement", "bottom");
        }
        row.tooltip({
            container : 'body'
        });
        this.tbody.append(row);
        var firstCell = angular.element('<td></td>');
        firstCell.addClass('activity');
        firstCell.text(activity.shortLabel);
        row.append(firstCell);
        // attach row to the context
        this.row = row;
        // process all weeks for current activity
        angular.forEach(this.scope.datagrid.weeks, performDatagridBodyWeek, this);
    }

    /**
     * @private
     */
    function performDatagridBodyWeek(week) {
        angular.forEach(week.days, function(day) {
            var cellDay = angular.element('<td class="imputation"></td>');
            var value = angular.element('<span class="value">-</span>');
            var elemDay = angular.element('<div></div>');
            elemDay.append(value);
            cellDay.append(elemDay);
            cellDay.attr('data-activity-id', this.activity.id);
            cellDay.attr('data-date', day.date);
            this.row.append(cellDay);
            var activityImputations = this.scope.imputationsMap[this.activity.id];
            if (activityImputations) {
                var imputation = this.utils.findImputation(activityImputations, day.date);
                if (imputation) {
                    cellDay.attr('data-imputation-id', imputation.imputationId);
                    cellDay.attr('data-quota', imputation.quota);
                    value.text(this.utils.formatQuota(imputation.quota));
                }
            }
        }, this);
    }

    module.directive('ngRightClick', function($parse) {
        return function(scope, element, attrs) {
            var fn = $parse(attrs.ngRightClick);
            element.bind('contextmenu', function(event) {
                scope.$apply(function() {
                    event.preventDefault();
                    fn(scope, {
                        $event : event
                    });
                });
            });
        };
    });

    module.directive('mdDatagridSlider', [
        '$rootScope',
        'Utils',
        function($rootScope, utils) {
            return {
                // refer external template
                templateUrl : 'partials/datagrid-slider.html',
                // declare the directive scope as private
                scope : {
                    quota : '=',
                    item : '=', // required to be pass as callback argument
                    callback : '&onChanged'
                },
                // this function is called on each md-datagrid-slider instance
                // initialisation
                link : function(scope, element, attrs) {

                    /**
                     * TODO: check performance and potentiel memory leaks using watch here
                     * @handler
                     */
                    scope.$watch('quota', function(newValue, oldValue) {
                        updateProgressBarModel(newValue);
                    });

                    /**
                     * Allows to update quota of the imputation 'copy' displayed in modal.
                     * @param minus
                     *          If true, the current value will be reduced, if null or false the
                     *          value will be increased.
                     * @private
                     */
                    function updateCurrentValue(minus) {
                        scope.quota = utils.getNewQuota(scope.quota,
                            $rootScope.preferences.granularity, minus);
                        scope.callback({
                            'item' : scope.item,
                            'quota' : scope.quota
                        });
                    }

                    /**
                     * Prepare PM from given new value.
                     * @private
                     */
                    function updateProgressBarModel(newValue) {
                        var val = newValue || 0;
                        scope.progressValue = val * 100;
                        scope.progressValueStyle = {
                            width : (scope.progressValue + '%')
                        };
                        scope.progressLabel = utils.formatQuota(newValue);
                    }

                    /**
                     * Allows to update quota of the imputation 'copy' displayed in modal.
                     * @public
                     */
                    scope.minusQuota = function() {
                        updateCurrentValue(true);
                    };

                    /**
                     * Allows to update quota of the imputation 'copy' displayed in modal.
                     * @public
                     */
                    scope.plusQuota = function() {
                        updateCurrentValue();
                    };
                }
            };
        } ]);

    return module;
});
