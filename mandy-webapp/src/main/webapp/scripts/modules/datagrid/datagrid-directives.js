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
    function (angular) {
		'use strict';
		
		var module = angular.module('mdDatagridDirective', ['mdCommon']);
		
        module.directive('mandyDatagridHeader', ["$q", "Utils", function($q, utils) {
            return {
                restrict: 'A',
                scope:false,
                replace: false,
                transclude:false,
                link: function (scope, element) {
                	// on doit disposer de la datagrid et des imputations
                	// pour pouvoir réaliser le traitement
                	$q.all([scope.datagrid.$promise, 
                	        scope.imputationsMap.$promise]).then(function(){
                    	var weeks = scope.datagrid.weeks;
                    	var elemDays = angular.element('<tr class="days"></tr>');
                    	var activities = angular.element('<th class="header-activities">Activités</th>');
                    	elemDays.append(activities);	
	                    angular.forEach(weeks, function(week, index) {
	                        angular.forEach(week.days, function(day, dIndex) {
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
        
        module.directive('mandyDatagridBody', ["$q", "Utils", function($q, utils) {
        	
            return {
                restrict: 'A',
                scope:false,
                replace: false,
                transclude:false,
                link: function (scope, element) {	
                	$q.all([scope.datagrid.$promise, 
                	        scope.activities.$promise, 
                	        scope.imputationsMap.$promise])
                	.then(function() {
                		var tbody = angular.element('<tbody></tbody>');
	                    angular.forEach(scope.activities, function(activity, index) {
	                    	var row = angular.element('<tr></tr>');
	                    	row.attr("title", activity.longLabel);
	                    	row.attr('data-activity-id', activity.id);
	                    	if (index == scope.activities.length-1) {
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
		                    angular.forEach(scope.datagrid.weeks, function(week, index) {
		                        angular.forEach(week.days, function(day, dIndex) {
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
			                        	};
		                        	};
		                        });
		                    });
	                    });
	                    element.replaceWith(tbody);
                	});
                }
            }
        }]);
        
        module.directive('ngRightClick', function($parse) {
            return function(scope, element, attrs) {
                var fn = $parse(attrs.ngRightClick);
                element.bind('contextmenu', function(event) {
                    scope.$apply(function() {
                        event.preventDefault();
                        fn(scope, {$event:event});
                    });
                });
            };
        });
        
        return module;
    }
);