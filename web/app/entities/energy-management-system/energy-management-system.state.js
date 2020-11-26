(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('energy-management-system', {
            parent: 'entity',
            url: '/energy-management-system',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.energyManagementSystem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/energy-management-system/energy-management-systems.html',
                    controller: 'EnergyManagementSystemController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('energyManagementSystem');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('energy-management-system-detail', {
            parent: 'energy-management-system',
            url: '/energy-management-system/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.energyManagementSystem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/energy-management-system/energy-management-system-detail.html',
                    controller: 'EnergyManagementSystemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('energyManagementSystem');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EnergyManagementSystem', function($stateParams, EnergyManagementSystem) {
                    return EnergyManagementSystem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'energy-management-system',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('energy-management-system-detail.edit', {
            parent: 'energy-management-system-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/energy-management-system/energy-management-system-dialog.html',
                    controller: 'EnergyManagementSystemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EnergyManagementSystem', function(EnergyManagementSystem) {
                            return EnergyManagementSystem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('energy-management-system.new', {
            parent: 'energy-management-system',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/energy-management-system/energy-management-system-dialog.html',
                    controller: 'EnergyManagementSystemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('energy-management-system', null, { reload: 'energy-management-system' });
                }, function() {
                    $state.go('energy-management-system');
                });
            }]
        })
        .state('energy-management-system.edit', {
            parent: 'energy-management-system',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/energy-management-system/energy-management-system-dialog.html',
                    controller: 'EnergyManagementSystemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EnergyManagementSystem', function(EnergyManagementSystem) {
                            return EnergyManagementSystem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('energy-management-system', null, { reload: 'energy-management-system' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('energy-management-system.delete', {
            parent: 'energy-management-system',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/energy-management-system/energy-management-system-delete-dialog.html',
                    controller: 'EnergyManagementSystemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EnergyManagementSystem', function(EnergyManagementSystem) {
                            return EnergyManagementSystem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('energy-management-system', null, { reload: 'energy-management-system' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
