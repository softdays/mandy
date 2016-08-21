/**
 *  This file contains the variables used in other gulp files
 *  which defines tasks
 *  By design, we only put there very generic config values
 *  which are used in several places to keep good readability
 *  of the tasks
 */

var gutil = require('gulp-util');

var argv = require('yargs')
    .default('dist', 'dist')
    .default('tmp', '.tmp')
    .default('disableMinifyIndex', false)
    // La minification des templates et des CSS poser problème sur certains écrans.
    .default('disableMinifyTemplates', true)
    .default('disableMinifyCss', true)
    .default('disableMinifyJs', false)
    .default('disableMinify', false)
    .argv;

/**
 *  The main paths of your project handle these with care
 */
exports.paths = {
  src: 'src/main/webapp',
  dist: argv.dist,
  tmp: argv.tmp,
  e2e: 'e2e'
};

exports.disableMinifyIndex = argv.disableMinifyIndex || argv.disableMinify;
exports.disableMinifyTemplates = argv.disableMinifyTemplates || argv.disableMinify;
exports.disableMinifyCss = argv.disableMinifyCss || argv.disableMinify;
exports.disableMinifyJs = argv.disableMinifyJs || argv.disableMinify;

/**
 *  Wiredep is the lib which inject bower dependencies in your project
 *  Mainly used to inject script tags in the index.html but also used
 *  to inject css preprocessor deps and js files in karma
 */
exports.wiredep = {
  exclude: [/\/bootstrap\.css/],
  directory: 'bower_components'
};

/**
 *  Common implementation for an error handler of a Gulp plugin
 */
exports.errorHandler = function(title) {
  'use strict';

  return function(err) {
    gutil.log(gutil.colors.red('[' + title + ']'), err.toString());
    this.emit('end');
  };
};
