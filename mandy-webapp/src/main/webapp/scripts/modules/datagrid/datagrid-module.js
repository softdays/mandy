/*global define:false */

/**
 * @file Datagrid module
 * @module mandy/datagrid
 * @exports Module
 * @version 1.0
 * @since 1.0
 * @author rpatriarche
 */
define([ 'angular', 'mandy-datagrid-directive', 'mandy-datagrid-controller',
    'mandy-datagrid-controller-view-month',
    'mandy-datagrid-controller-view-day', 'mandy-datagrid-controller-modal',
    'mandy-datagrid-route' ], function(angular) {
  'use strict';

  return angular.module('mdDatagrid', [ 'mdDatagridDirective',
      'mdDatagridController', 'mdDatagridControllerViewMonth',
      'mdDatagridControllerViewDay', 'mdDatagridControllerModal',
      'mdDatagridRoute' ]);

});
