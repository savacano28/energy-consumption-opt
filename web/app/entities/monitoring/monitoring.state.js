(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('monitoring', {
            parent: 'entity',
            url: '/monitoring',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.monitoring.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/monitoring/monitoring.html',
                    controller: 'MonitoringController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('monitoring');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('monitoring-detail', {
            parent: 'monitoring',
            params: {
                id: null
            },
            url: '/monitoring-detail/fluxTopology/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.monitoring.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/monitoring/monitoring-detail.html',
                    controller: 'MonitoringDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('monitoring');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FluxTopology', function($stateParams, FluxTopology) {
                    return FluxTopology.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'monitoring',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
    }

})();
