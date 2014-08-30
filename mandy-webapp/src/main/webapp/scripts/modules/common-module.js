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
			 
			 this.findImputation = function(imputations, strDate) {
				 // je n'utilise pas un forEach angular car on ne peut pas 
				 // utiliser break ou return à l'intérieur de la boucle
				 // see: https://github.com/angular/angular.js/issues/263
				 for (var i = 0; i < imputations.length; i++) {
					 if (imputations[i].date == strDate) {
						 return imputations[i];
					 }
				 }
				 
				 return undefined;
			 };
			 
			 this.formatQuota = function(quota) {
				 quota = quota || 0;
				 quota = quota == "-" ? 0 : quota;
				 switch(+quota) {				 
				 	case 0:
				    	return "0.00";
				    case 0.25:
				    	return "0.25";
				    	
				    case 0.5:
				    	return "0.50";
				    
				    case 0.75:
				    	return "0.75";
				    	
				    case 1:
				    	return "1.00";
				    	
				    default:
				    	return "Err";
				} 
			 };
			 
			 this.formatQuotaForGrid = function(quota) {
				 var q = this.formatQuota(quota);
				 return q == "0.00" ? "-" : q;
			 };
			 
			 this.getNewQuota = function(oldQuota, minus) {
				 var q = (oldQuota && oldQuota != '-') ? (+oldQuota) : 0;
				 if (minus) {
					 return q == 0 ? 1 : (q-0.25);
				 } else {
					 return q == 1 ? 0 : (q+0.25);
				 }
				 
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
		
		var ErrorService = ['$rootScope', '$location', '$log', '$timeout', function($rootScope, $location, $log, $timeout) {
        	
			this.errorHandler = function(httpResponse, context)  
			{
            	var status = httpResponse.status ? httpResponse.status : "";
            	var details = httpResponse.statusText ? httpResponse.statusText : "aucune information récupérable";
            	var title = "Erreur " + status;
            	var details = "Détails : " + details;
            	$log.debug(title + ", "+ context, +", "+details);
            	$rootScope.alert = { title:title, context:context, details:details };            	
            	$rootScope.selectedTab = 0;
            	// delay to be sure the modal (if opened) is closed before redirection
            	$timeout(function(){
            		$location.path("/error");
            	}, 200);
   		 	};
                
		}];
		
		module.service('ErrorService', ErrorService);

        return module;
    }
);