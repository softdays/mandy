/*global define:false */

/**
 * @file Datagrid module
 * @module mandy/datagrid
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define(['angular', 
        'mandy-datagrid-directive',        
        'mandy-datagrid-controller',
        'mandy-datagrid-route'], 
        function (angular) 
        {
    		'use strict';

    		return angular.module('mdDatagrid', 
    				['mdDatagridDirective', 
    				 'mdDatagridController', 
    				 'mdDatagridRoute']);

        }
);