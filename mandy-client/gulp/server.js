'use strict';

var path = require('path');
var gulp = require('gulp');
var conf = require('./conf');

var browserSync = require('browser-sync');
var browserSyncSpa = require('browser-sync-spa');

var util = require('util');

var proxyMiddleware = require('http-proxy-middleware');

function browserSyncInit(baseDir, browser) {
  browser = browser === undefined ? 'default' : browser;

  var routes = null;
  if (baseDir === conf.paths.src || (util.isArray(baseDir) && baseDir.indexOf(conf.paths.src) !== -1)) {
    routes = {
      '/bower_components': 'bower_components'
    };
  }

  var server = {
    baseDir: baseDir,
    routes: routes
  };

  /*
   * You can add a proxy to your backend by uncommenting the line below.
   * You just have to configure a context which will we redirected and the target url.
   * Example: $http.get('/users') requests will be automatically proxified.
   *
   * For more details and option, https://github.com/chimurai/http-proxy-middleware/blob/v0.9.0/README.md
   */
  // server.middleware = proxyMiddleware('/users', {target: 'http://jsonplaceholder.typicode.com', changeOrigin: true});
  // Modification pour gérer la redirection '/' vers '/mandy'
  function replaceHeader(headers, headerName, replaceFunction) {
    var headerValues = headers[headerName];

    if (headerValues === undefined) {
      return;
    }

    if (Array.isArray(headerValues)) {
      var newHeaderValues = [];
      headerValues.forEach(function(headerValue) {
        newHeaderValues.push(replaceFunction(headerValue));
      });
      headers[headerName] = newHeaderValues;
    } else {
      headers[headerName] = replaceFunction(headerValues)
    }
  }

  var proxy = proxyMiddleware(['/api', '/login', '/mandy/login'], {
    target: 'http://localhost:8080/mandy',
    pathRewrite: {'/mandy/login' : '/login'},
    onProxyRes: function(proxyRes, req, res) {
      replaceHeader(proxyRes.headers, 'location', function(value) {
        value = value.replace('/mandy/', '/');
        value = value.replace('/mandy?', '/?');
        value = value.replace(/\/mandy$/, '/');
        return value;
      })
      replaceHeader(proxyRes.headers, 'set-cookie', function(value) {
        return value.replace('/mandy/', '/');
      })
    }
  });

  server.middleware = [proxy];

  browserSync.instance = browserSync.init({
    notify: false, // La notification peut poser problème lors des tests IHM.
    server: server,
    browser: browser,
    ghostMode: false, // Désactive la synchronisation entre l'ensemble des navigateurs connectés.
    startPath: ''
  });
}

browserSync.use(browserSyncSpa({
  selector: '[ng-app]'// Only needed for angular apps
}));

gulp.task('serve', ['watch'], function() {
  browserSyncInit([path.join(conf.paths.tmp, '/serve'), conf.paths.src]);
});

gulp.task('serve:dist', ['build'], function() {
  browserSyncInit(conf.paths.dist);
});

gulp.task('serve:e2e', ['inject'], function() {
  browserSyncInit([conf.paths.tmp + '/serve', conf.paths.src], []);
});

gulp.task('serve:e2e-dist', ['build'], function() {
  browserSyncInit(conf.paths.dist, []);
});
