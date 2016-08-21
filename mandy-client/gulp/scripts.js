'use strict';

var path = require('path');
var gulp = require('gulp');
var conf = require('./conf');

var browserSync = require('browser-sync');
var mergeStream = require('merge-stream');

var $ = require('gulp-load-plugins')();


gulp.task('scripts-reload', function() {
  return mergeStream(
    buildScripts('/app').pipe(browserSync.stream()),
    buildScripts('/starter').pipe(browserSync.stream())
  );
});

gulp.task('scripts', function() {
  return mergeStream(buildScripts('/app'), buildScripts('/starter'));
});

function buildScripts(directory) {
  return gulp.src(path.join(conf.paths.src, directory, '/**/*.js'))
    //.pipe($.eslint())
    //.pipe($.eslint.format())
    .pipe($.size())
};
