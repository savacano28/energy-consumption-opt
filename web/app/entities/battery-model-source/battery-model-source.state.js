(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('battery-model-source', {
            parent: 'entity',
            url: '/battery-model-source',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.batteryModelSource.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/battery-model-source/battery-model-sources.html',
                    controller: 'BatteryModelSourceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('batteryModelSource');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('battery-model-source-detail', {
            parent: 'battery-model-source',
            url: '/battery-model-source/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.batteryModelSource.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/battery-model-source/battery-model-source-detail.html',
                    controller: 'BatteryModelSourceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('batteryModelSource');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BatteryModelSource', function($stateParams, BatteryModelSource) {
                    return BatteryModelSource.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'battery-model-source',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('battery-model-source-detail.edit', {
            parent: 'battery-model-source-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/battery-model-source/battery-model-source-dialog.html',
                    controller: 'BatteryModelSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BatteryModelSource', function(BatteryModelSource) {
                            return BatteryModelSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('battery-model-source.new', {
            parent: 'battery-model-source',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/battery-model-source/battery-model-source-dialog.html',
                    controller: 'BatteryModelSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('battery-model-source', null, { reload: 'battery-model-source' });
                }, function() {
                    $state.go('battery-model-source');
                });
            }]
        })
        .state('battery-model-source.edit', {
            parent: 'battery-model-source',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/battery-model-source/battery-model-source-dialog.html',
                    controller: 'BatteryModelSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BatteryModelSource', function(BatteryModelSource) {
                            return BatteryModelSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('battery-model-source', null, { reload: 'battery-model-source' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('battery-model-source.delete', {
            parent: 'battery-model-source',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/battery-model-source/battery-model-source-delete-dialog.html',
                    controller: 'BatteryModelSourceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BatteryModelSource', function(BatteryModelSource) {
                            return BatteryModelSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('battery-model-source', null, { reload: 'battery-model-source' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
