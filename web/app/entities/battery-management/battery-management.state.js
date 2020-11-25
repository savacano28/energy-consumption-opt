(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('battery-management', {
            parent: 'entity',
            url: '/battery-management',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.battery-management.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/battery-management/battery-management.html',
                    controller: 'BatteryManagementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('battery-management');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('battery-management-detail', {
            parent: 'battery-management',
            url: '/battery-management-detail/flux-topology/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.battery-management.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/battery-management/battery-management-detail.html',
                    controller: 'BatteryManagementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('battery-management');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BatteryManagement', function($stateParams, BatteryManagement) {
                    return BatteryManagement.pilotable({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'battery-management',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
    }

})();
