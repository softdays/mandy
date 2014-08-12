/*global define:false */

/**
 * @file Common service
 * @module mandy/common
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define(['angular', 'moment'], 
    function (angular, moment) 
    {
		'use strict';
		
		// configuration de la librairie Moment.js
		moment.locale('fr');

		var module = angular.module('CommonModule', []);
		
		var UtilsService = function()
		{
			var DATAGRID_PARAMS = "[year]/YYYY/[month]/MM";
			
			this.formatDate = function(date) {
				 var fDate = date.toJSON();
             	 return fDate.substring(0, fDate.indexOf('T'));
			 };
			 
			 this.getDay = function(strDate) {
				 return moment(strDate).format("DD");
			 };
			 
			 this.areMonthsEqual = function(currentFormattedMonth, date) {
				 var dateMonth = moment(date).format("MM");
				 return currentFormattedMonth == dateMonth;
			 };
			 
			 this.formatMonthLabel = function(month) {
				 var nb = (+month-1);
				 return moment().month(nb).format("MMMM");
			 };
			 
			 this.formatShortMonthLabel = function(month, year) {
				 var monthNb = (+month-1);
				 var yearNb = (+year);
				 return moment().year(yearNb).month(monthNb).format("MM/YYYY");
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
			 
			 function buildPath(year, month, previous) {
				 var strDate = year+"-"+month+"-01";
				 var m = moment(strDate);
				 if (previous) {
					m.subtract(1,'M'); 
				 } else {
					 m.add(1,'M');
				 }
				 
				 return m.format(DATAGRID_PARAMS);
			 };

		};
		
		module.service('Utils', UtilsService);
		
		var GlobalsService = function() 
		{
			var currentDate = new Date();
        	
			this.getCurrentDate= function() 
			{
				return currentDate;
   		 	};
           
            this.setCurrentDate= function (date) 
            {
            	currentDate = date;
            };                  
		};
		
		module.service('Globals', GlobalsService);

        return module;
    }
);