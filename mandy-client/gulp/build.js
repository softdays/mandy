'use strict';

var path = require('path');
var gulp = require('gulp');
var gutil = require('gulp-util');
var conf = require('./conf');

var $ = require('gulp-load-plugins')({
  pattern : ['gulp-*', 'main-bower-files', 'uglify-save-license', 'del']
});

gulp.task('partials', function() {
  var ret = gulp.src([
      path.join(conf.paths.src, '/app/**/*.html'),
      path.join(conf.paths.tmp, '/serve/app/**/*.html')
    ]);

    if (!conf.disableMinifyTemplates) {
      //La minification des templates HTML provoque quelques problèmes (mantis #155530)
      ret = ret.pipe($.minifyHtml({
        empty : true,
        spare : true,
        quotes : true,
        loose:  true
      }))
    } else {
      gutil.log("Templates minification is disabled.");
    }

    ret = ret.pipe($.angularTemplatecache('templateCacheHtml.js', {
      module : 'mandyApp',
      root : 'app'
    }))
    .pipe(gulp.dest(conf.paths.tmp + '/partials/'));

    return ret;
});

gulp.task('html', ['inject', 'partials'], function() {
  var partialsInjectFile = gulp.src(path.join(conf.paths.tmp, '/partials/templateCacheHtml.js'), { read : false });
  var partialsInjectOptions = {
    starttag : '<!-- inject:partials -->',
    ignorePath : path.join(conf.paths.tmp, '/partials'),
    addRootSlash : false
  };

  var htmlFilter = $.filter('*.html', { restore : true });
  var jsFilter = $.filter('**/*.js', { restore : true });
  var cssFilter = $.filter('**/*.css', { restore : true });
  var assets;

  var ret = gulp.src(path.join(conf.paths.tmp, '/serve/*.html'))
    .pipe($.inject(partialsInjectFile, partialsInjectOptions))
    .pipe(assets = $.useref.assets())
    .pipe($.rev());

    if (!conf.disableMinifyJs) {
      ret = ret.pipe(jsFilter)
        .pipe($.sourcemaps.init())
        .pipe($.ngAnnotate())
        .pipe($.uglify({ preserveComments : $.uglifySaveLicense })).on('error', conf.errorHandler('Uglify'))
        .pipe($.sourcemaps.write('maps'))
        .pipe(jsFilter.restore);
    } else {
      gutil.log("JS minification is disabled.");
    }

  if (!conf.disableMinifyCss) {
    ret = ret.pipe(cssFilter)
      .pipe($.sourcemaps.init())
      .pipe($.minifyCss({ processImport : false }))
      .pipe($.sourcemaps.write('maps'))
      .pipe($.replace(/(?:\.\.\/)*bower_components\/.*?\/([^\/]*?\.(?:eot|svg|ttf|woff2|woff))/g, '../fonts/$1'))
      .pipe(cssFilter.restore);
  } else {
    ret = ret.pipe($.replace(/(?:\.\.\/)*bower_components\/.*?\/([^\/]*?\.(?:eot|svg|ttf|woff2|woff))/g, '../fonts/$1'))
    gutil.log("CSS minification is disabled.");
  }

  ret = ret.pipe(assets.restore())
    .pipe($.useref())
    .pipe($.revReplace());

  if (!conf.disableMinifyIndex) {
    ret = ret.pipe(htmlFilter)
      .pipe($.minifyHtml({
        empty : true,
        spare : true,
        quotes : true,
        conditionals : true,
        loose:  true
      }))
      .pipe(htmlFilter.restore)
  } else {
    gutil.log("Index minification is disabled.");
  }

  ret = ret.pipe(gulp.dest(path.join(conf.paths.dist, '/')))
    .pipe($.size({ title : path.join(conf.paths.dist, '/'), showFiles : true }));

    return ret;
});

// Only applies for fonts from bower dependencies
// Custom fonts are handled by the "other" task
gulp.task('fonts', function() {
  return gulp.src($.mainBowerFiles())
    .pipe($.filter('**/*.{eot,svg,ttf,woff,woff2}'))
    .pipe($.flatten())
    .pipe(gulp.dest(path.join(conf.paths.dist, '/fonts/')));
});

gulp.task('other', function() {
  var fileFilter = $.filter(function(file) {
    return file.stat.isFile();
  });

  return gulp.src([
      path.join(conf.paths.src, '/**/*'),
      path.join('!' + conf.paths.src, '/**/*.{html,css,js,less}')
    ])
    .pipe(fileFilter)
    .pipe(gulp.dest(path.join(conf.paths.dist, '/')));
});

gulp.task('clean', function() {
  return $.del([path.join(conf.paths.dist, '/'), path.join(conf.paths.tmp, '/')]);
});

gulp.task('build', ['html', 'fonts', 'other']);
