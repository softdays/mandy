/*global define:false */

/**
 * @file Common service
 * @module mandy/common
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define(['angular', 'moment'], function(angular, moment){
    'use strict';

    // configuration de la librairie Moment.js
    moment.locale('fr');

    var module = angular.module('mdCommon', []);

    var UtilsService = function(){
        var DATAGRID_PARAMS = "[year]/YYYY/[month]/MM";
        var DATAGRID_PARAMS_FOR_VIEW_DAY = DATAGRID_PARAMS + "/[day]/DD";

        this.getDate = function(strDate){
            return moment(strDate).toDate();
        };

        this.formatDate = function(strDate){
            return moment(strDate).format("LL");
        };

        this.getDay = function(strDate){
            return moment(strDate).format("DD");
        };

        this.getCurrentDay = function(){
            return moment().format("DD");
        };

        this.areMonthsEqual = function(currentFormattedMonth, date){
            var dateMonth = moment(date).format("MM");
            return currentFormattedMonth === dateMonth;
        };

        this.formatMonthLabel = function(month){
            var nb = (+month - 1);
            return moment().month(nb).format("MMMM");
        };

        this.getYear = function(strDate){
            return new Date(strDate).getFullYear();
        };

        this.buildPathForCurrentMonth = function(){
            return moment().format(DATAGRID_PARAMS);
        };

        this.buildPathForNextMonth = function(year, month){
            return buildPath(year, month);
        };

        this.buildPathForPreviousMonth = function(year, month){
            return buildPath(year, month, true);
        };

        this.buildPathForToday = function(){
            return moment().format(DATAGRID_PARAMS_FOR_VIEW_DAY);
        };

        this.buildPathForNextDay = function(strDate){
            return moment(strDate).add(1, 'days').format(DATAGRID_PARAMS_FOR_VIEW_DAY);
        };

        this.buildPathForPreviousDay = function(strDate){
            return moment(strDate).subtract(1, 'days').format(DATAGRID_PARAMS_FOR_VIEW_DAY);
        };

        /**
         * Retourne vrai si la date dénotée par la chaîne passée en paramètre
         * appartient à la datagrid passée également en paramètre, faux sinon.
         */
        this.isWorkingDay = function(strDate, datagrid){
            // je n'utilise pas un forEach angular car on ne peut pas
            // utiliser break ou return à l'intérieur de la boucle
            // see: https://github.com/angular/angular.js/issues/263
            for (var w = 0; w<datagrid.weeks.length; w++) {
                var days = datagrid.weeks[w].days;
                for (var d = 0; d<days.length; d++) {
                    if (days[d].date === strDate) {
                        return true;
                    }
                }
            }

            return false;
        };

        function buildPath(year, month, previous){
            var strDate = year + "-" + month + "-01";
            var m = moment(strDate);
            if (previous) {
                m.subtract(1, 'M');
            }
            else {
                m.add(1, 'M');
            }

            return m.format(DATAGRID_PARAMS);
        };

        this.findImputation = function(imputations, strDate){
            // je n'utilise pas un forEach angular car on ne peut pas
            // utiliser break ou return à l'intérieur de la boucle
            // see: https://github.com/angular/angular.js/issues/263
            for (var i = 0; i<imputations.length; i++) {
                if (imputations[i].date === strDate) {
                    return imputations[i];
                }
            }

            return undefined;
        };

        this.formatQuota = function(quota){
            var formatted = "Err";
            quota = quota || 0;
            quota = quota === "-" ? 0 : quota;
            switch (+quota) {
                case 0:
                    formatted =  "0.00";
                    break;

                case 0.25:
                    formatted =  "0.25";
                    break;

                case 0.5:
                    formatted =  "0.50";
                    break;

                case 0.75:
                    formatted =  "0.75";
                    break;

                case 1:
                    formatted =  "1.00";
                    break;

                default: break;
            }

            return formatted;
        };

        this.formatQuotaForGrid = function(quota){
            var q = this.formatQuota(quota);
            return q === "0.00" ? "-" : q;
        };

        this.getNewQuota = function(oldQuota, minus){
            var q = (oldQuota && oldQuota !== '-') ? (+oldQuota) : 0;
            if (minus) {
                return q === 0 ? 1 : (q - 0.25);
            }
            else {
                return q === 1 ? 0 : (q + 0.25);
            }
        };

        this.getCompletenessGlyphiconClass = function(imputationsMap, strDate){
            // on additionne l'ensemble des quotas à la date indiquée
            var allQuotasAtDate = 0;
            angular.forEach(imputationsMap, function(imputations){
                // don't use angular.forEach if you want to break
                for (var i = 0; i<imputations.length; i++) {
                    if (imputations[i].date === strDate) {
                        allQuotasAtDate += imputations[i].quota;
                        break;
                    }
                }
            });
            // On détermine l'icône à utiliser
            var glyphicon = 'glyphicon-ok';
            if (allQuotasAtDate<1) {
                glyphicon = 'glyphicon-arrow-down';
            }
            else if (allQuotasAtDate>1) {
                glyphicon = 'glyphicon-arrow-up';
            }

            return glyphicon;
        };

    };

    module.service('Utils', UtilsService);

    var GlobalsService = function(){
        var currentDate = new Date();

        this.getCurrentDate = function(){
            return currentDate;
        };

        this.setCurrentDate = function(date){
            currentDate = date;
        };
    };

    module.service('Globals', GlobalsService);

    var ErrorService = [
        '$rootScope', '$location', '$log', '$timeout', function($rootScope, $location, $log, $timeout){

            this.errorHandler = function(httpResponse, context){
                var status = httpResponse.status ? httpResponse.status : "???";
                var details = httpResponse.statusText ? httpResponse.statusText : "aucune information récupérable";
                var title = "Code HTTP " + status;
                details = "Détails : " + details;
                $log.debug("[" + title + "] " + context + ", " + details);
                $rootScope.alert = {
                    title: title, context: context, details: details
                };
                $rootScope.selectedTab = 0;
                // delay to be sure the modal (if opened) is closed before redirection
                $timeout(function(){
                    $location.path("/error");
                }, 200);
            };

        }
    ];

    module.service('ErrorService', ErrorService);

    return module;
});
