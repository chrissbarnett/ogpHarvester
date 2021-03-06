/**
 *
 */
(function() {
	'use strict';

	angular.module(
		'ogpHarvester', ['ngRoute', 'pascalprecht.translate', 'ngResource', 'rcForm',
			'ui.bootstrap',
			'ogpHarvester.filters', 'ogpHarvester.services',
			'ogpHarvester.services', 'ogpHarvester.directives',
			'ogpHarvester.controllers', 'ogpHavester.controllers.adminCtrl',
			'ogpHavester.controllers.editIngestCtrl', 'ui.select2'
		]).config(
		['$routeProvider', '$locationProvider',
			function($routeProvider, $locationProvider) {
				//$locationProvider.html5Mode(true);
				$routeProvider.when('/manageIngests', {
					templateUrl: 'resources/ingestsList.html',
					controller: 'ManageIngestsCtrl'
				});
				$routeProvider.when('/manageIngests/view/:id', {
					templateUrl: 'resources/ingestDetails.html',
					controller: 'IngestDetailsCtrl'

				});
				$routeProvider.when('/newIngest', {
					templateUrl: 'resources/newIngestForm.html',
					controller: 'NewIngestCtrl'
				});
				$routeProvider.when('/newIngest/step2', {
					templateUrl: 'resources/newIngestFormStep2.html',
					controller: 'NewIngestCtrl'
				});
				$routeProvider.when('/newIngest/:back', {
					templateUrl: 'resources/newIngestForm.html',
					controller: 'NewIngestCtrl'
				});
				$routeProvider.when('/editIngest/:id', {
					templateUrl: 'resources/newIngestForm.html',
					controller: 'NewIngestCtrl'
				});
				$routeProvider.when('/editIngest/:id/step2', {
					templateUrl: 'resources/newIngestFormStep2.html',
					controller: 'NewIngestCtrl'
				});
				$routeProvider.when('/editIngest/:id/:back', {
					templateUrl: 'resources/newIngestForm.html',
					controller: 'NewIngestCtrl'
				});

				$routeProvider.when('/admin', {
					templateUrl: 'resources/admin.html',
					controller: 'AdminCtrl'
				});

				$routeProvider.otherwise({
					redirectTo: '/manageIngests'
				});
			}
		]).config(['$translateProvider',
		function($translateProvider) {
			$translateProvider.useStaticFilesLoader({
				prefix: 'resources/locales/locale-',
				suffix: '.json'
			});
			$translateProvider.preferredLanguage('en');
		}
	]);
	angular.module('ogpHarvester').value('uiJqConfig', {
		datetimepicker: {
			pickTime: false
		}
	});
})();