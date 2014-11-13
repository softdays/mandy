/*global define:false */

/**
 * @file Datagrid directives
 * @module mandy/datagrid/directive
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define(['angular', 'mandy-common'],
    function(angular){
        'use strict';

        var module = angular.module('mdDatagridDirective', ['mdCommon']);

        module.directive('mandyDatagridHeader', ["$q", "Utils", function($q, utils){
            return {
                restrict: 'A',
                scope: false,
                replace: false,
                transclude: false,
                link: function(scope, element){
                    // on doit disposer de la datagrid et des imputations
                    // pour pouvoir réaliser le traitement
                    $q.all([scope.datagrid.$promise,
                        scope.imputationsMap.$promise]).then(function(){
                        var weeks = scope.datagrid.weeks;
                        var elemDays = angular.element('<tr class="days"></tr>');
                        var activities = angular.element('<th class="header-activities">Activités</th>');
                        elemDays.append(activities);
                        angular.forEach(weeks, function(week, index){
                            angular.forEach(week.days, function(day, dIndex){
                                var elemDay = angular.element('<th class="day"></th>');
                                var elemDayContentWrapper = angular.element('<div></div>');
                                var spanCompleteness = angular.element('<span class="completeness glyphicon"></span>');
                                var completenessGlyphiconIcon = utils.getCompletenessGlyphiconClass(scope.imputationsMap, day.date);
                                spanCompleteness.addClass(completenessGlyphiconIcon);
                                var spanDayValue = angular.element('<span class="day-value"></span>');
                                spanDayValue.text(utils.getDay(day.date));
                                elemDay.attr("data-date", day.date);
                                elemDayContentWrapper.append(spanDayValue);
                                elemDayContentWrapper.append(spanCompleteness);
                                elemDay.append(elemDayContentWrapper);
                                if (utils.areMonthsEqual(scope.datagrid.month, day.date)) {
                                    elemDay.addClass("current-month");
                                }
                                elemDays.append(elemDay);
                            });
                        });
                        element.replaceWith(elemDays);
                    });
                }
            }
        }]);

        module.directive('mandyDatagridBody', ["$q", "Utils", function($q, utils){

            return {
                restrict: 'A',
                scope: false,
                replace: false,
                transclude: false,
                link: function(scope, element){
                    $q.all([scope.datagrid.$promise,
                        scope.activities.$promise,
                        scope.imputationsMap.$promise])
                        .then(function(){
                            var tbody = angular.element('<tbody></tbody>');
                            angular.forEach(scope.activities, function(activity, index){
                                var row = angular.element('<tr></tr>');
                                row.attr("title", activity.longLabel);
                                row.attr('data-activity-id', activity.id);
                                if (index == scope.activities.length - 1) {
                                    row.attr("data-placement", "top");
                                } else {
                                    row.attr("data-placement", "bottom");
                                }
                                row.tooltip({container: 'body'});
                                tbody.append(row);
                                var firstCell = angular.element('<td></td>');
                                firstCell.addClass('activity');
                                firstCell.text(activity.shortLabel);
                                row.append(firstCell);
                                angular.forEach(scope.datagrid.weeks, function(week, index){
                                    angular.forEach(week.days, function(day, dIndex){
                                        var cellDay = angular.element('<td class="imputation"></td>');
                                        //var marker = angular.element('<span class="marker invisible" />');
                                        var value = angular.element('<span class="value">-</span>');
                                        var elemDay = angular.element('<div></div>');
                                        //elemDay.append(marker);
                                        elemDay.append(value);
                                        cellDay.append(elemDay);
                                        cellDay.attr('data-activity-id', activity.id);
                                        cellDay.attr('data-date', day.date);
                                        row.append(cellDay);
                                        var activityImputations = scope.imputationsMap[activity.id];
                                        if (activityImputations) {
                                            var imputation = utils.findImputation(activityImputations, day.date);
                                            if (imputation) {
                                                cellDay.attr('data-imputation-id', imputation.imputationId);
                                                cellDay.attr('data-quota', imputation.quota);
                                                value.text(utils.formatQuota(imputation.quota));
                                            }
                                            ;
                                        }
                                        ;
                                    });
                                });
                            });
                            element.replaceWith(tbody);
                        });
                }
            }
        }]);

        module.directive('mandyDatagridBodyViewDay', ["$q", "Utils", function($q, utils){

            return {
                restrict: 'A',
                scope: false,
                replace: false,
                transclude: false,
                link: function(scope, element){
                    $q.all([scope.datagrid.$promise,
                        scope.activities.$promise,
                        scope.imputationsMap.$promise])
                        .then(function(){
                            var tbody = angular.element('<tbody></tbody>');
                            angular.forEach(scope.activities, function(activity, index){
                                var row = angular.element('<tr></tr>');
                                row.attr("title", activity.longLabel);
                                row.attr('data-activity-id', activity.id);
                                tbody.append(row);
                                var cellActivity = angular.element('<td></td>');
                                cellActivity.addClass('activity');
                                cellActivity.text(activity.shortLabel)
                                row.append(cellActivity);

                                /*
                                 <div class="btn-group">
                                 <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                 Action
                                 <span class="caret"></span>
                                 </button>
                                 <ul class="dropdown-menu" role="menu">
                                 <li><a href="#">Action</a></li>
                                 <li><a href="#">Another action</a></li>
                                 <li><a href="#">Something else here</a></li>
                                 <li class="divider"></li>
                                 <li><a href="#">Separated link</a></li>
                                 </ul>
                                 </div>
                                 */

                                // imputation value
                                var imputationValue = '0.00';
                                var dropdownCaret = angular.element('<span class="caret"></span>');
                                var dropdownBtnValue = angular.element('<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" />');
                                dropdownBtnValue.append(imputationValue);
                                dropdownBtnValue.append('&nbsp;');
                                dropdownBtnValue.append(dropdownCaret);
                                // dropdown sub items
                                var dropdownUlLiNone = angular.element('<li role="presentation"><a role="menuitem" tabindex="-1" ng-click="">0.00</a></li>');
                                var dropdownUlLiQuarter = angular.element('<li role="presentation"><a role="menuitem" tabindex="-1" ng-click="">0.25</a></li>');
                                var dropdownUlLiHalf = angular.element('<li role="presentation"><a role="menuitem" tabindex="-1" ng-click="">0.50</a></li>');
                                var dropdownUlLiThreeQuarters = angular.element('<li role="presentation"><a role="menuitem" tabindex="-1" ng-click="">0.75</a></li>');
                                var dropdownUlLiWhole = angular.element('<li role="presentation"><a role="menuitem" tabindex="-1" ng-click="">1.00</a></li>');
                                // dropdown element
                                var dropdownUl = angular.element('<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenuImputation" />');
                                dropdownUl.append(dropdownUlLiNone);
                                dropdownUl.append(dropdownUlLiQuarter);
                                dropdownUl.append(dropdownUlLiHalf);
                                dropdownUl.append(dropdownUlLiThreeQuarters);
                                dropdownUl.append(dropdownUlLiWhole);
                                // imputation value button group
                                var btnGrImputationValue = angular.element('<div class="btn-group"></div>');
                                btnGrImputationValue.append(dropdownBtnValue);
                                btnGrImputationValue.append(dropdownUl);
                                // comment element
                                var btnComment = angular.element('<button type="button" class="btn btn-default btn-comment"><span class="glyphicon glyphicon-comment"></span></button>');
                                var btnGrpComment = angular.element('<div class="btn-group" />');
                                btnGrpComment.append(btnComment);

                                // imputation btn group
                                var btnGrpImputationXxs = angular.element('<div class="btn-group btn-group-justified visible-xxs-table" />');
                                btnGrpImputationXxs.append(btnGrImputationValue);
                                btnGrpImputationXxs.append(btnGrpComment);

                                // imputation table cell
                                var cellImputation = angular.element('<td class="imputation"></td>');
                                cellImputation.attr('data-activity-id', activity.id);
                                cellImputation.attr('data-date', scope.shortCurrentDayIndicator);
                                cellImputation.append(btnGrpImputationXxs);

                                row.append(cellImputation);

//                        	var activityImputations = scope.imputationsMap[activity.id];
//                        	if (activityImputations) {
//                        		var imputation = utils.findImputation(activityImputations, day.date);
//	                        	if (imputation) {
//	                        		cellDay.attr('data-imputation-id', imputation.imputationId);
//		                        	cellDay.attr('data-quota', imputation.quota);
//		                        	value.text(utils.formatQuota(imputation.quota));
//	                        	};
//                        	};
                            });
                            element.replaceWith(tbody);
                        });
                }
            }
        }]);

        module.directive('ngRightClick', function($parse){
            return function(scope, element, attrs){
                var fn = $parse(attrs.ngRightClick);
                element.bind('contextmenu', function(event){
                    scope.$apply(function(){
                        event.preventDefault();
                        fn(scope, {$event: event});
                    });
                });
            };
        });

        return module;
    }
);