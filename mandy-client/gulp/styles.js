'use strict';

var path = require('path');
var gulp = require('gulp');
var conf = require('./conf');

var browserSync = require('browser-sync');
var mergeStream = require('merge-stream');

var $ = require('gulp-load-plugins')();

var wiredep = require('wiredep').stream;
var _ = require('lodash');

gulp.task('styles-reload', ['styles'], function() {
  return buildStyles()
    .pipe(browserSync.stream());
});

gulp.task('styles', function() {
  mergeStream(buildStyles('/app'), buildStyles('/style'));
});

var buildStyles = function(directory) {
  var lessOptions = {
    paths : [
      'bower_components',
      path.join(conf.paths.src, directory),
    ],
    relativeUrls : true
  };

  var injectFiles = gulp.src([
    path.join(conf.paths.src, directory, '/**/*.less'),
    path.join('!' + conf.paths.src, directory, '/index.less'),
    path.join('!' + conf.paths.src, directory, '/rwd/**')
  ], { read : false });

  var injectOptions = {
    transform : function(filePath) {
      filePath = filePath.replace(conf.paths.src + directory + '/', '');
      return '@import "' + filePath + '";';
    },
    starttag : '// injector',
    endtag : '// endinjector',
    addRootSlash : false
  };

  return gulp.src([
      path.join(conf.paths.src, directory, '/index.less')
    ])
    .pipe($.inject(injectFiles, injectOptions))    
    .pipe(wiredep(_.extend({}, conf.wiredep)))
    .pipe($.sourcemaps.init())
    .pipe($.less(lessOptions)).on('error', conf.errorHandler('Less'))
    .pipe($.autoprefixer()).on('error', conf.errorHandler('Autoprefixer'))
    .pipe($.sourcemaps.write())
    .pipe(gulp.dest(path.join(conf.paths.tmp, '/serve/', directory)));
};
