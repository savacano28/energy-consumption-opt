(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('energy-provider', {
            parent: 'entity',
            url: '/energy-provider',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.energyProvider.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/energy-provider/energy-providers.html',
                    controller: 'EnergyProviderController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('energyProvider');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('energy-provider-detail', {
            parent: 'energy-provider',
            url: '/energy-provider/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.energyProvider.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/energy-provider/energy-provider-detail.html',
                    controller: 'EnergyProviderDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('energyProvider');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EnergyProvider', function($stateParams, EnergyProvider) {
                    return EnergyProvider.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'energy-provider',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('energy-provider-detail.edit', {
            parent: 'energy-provider-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/energy-provider/energy-provider-dialog.html',
                    controller: 'EnergyProviderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EnergyProvider', function(EnergyProvider) {
                            return EnergyProvider.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('energy-provider.new', {
            parent: 'energy-provider',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/energy-provider/energy-provider-dialog.html',
                    controller: 'EnergyProviderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                purchaseRate: null,
                                purchaseAutoConsoRate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('energy-provider', null, { reload: 'energy-provider' });
                }, function() {
                    $state.go('energy-provider');
                });
            }]
        })
        .state('energy-provider.edit', {
            parent: 'energy-provider',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/energy-provider/energy-provider-dialog.html',
                    controller: 'EnergyProviderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EnergyProvider', function(EnergyProvider) {
                            return EnergyProvider.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('energy-provider', null, { reload: 'energy-provider' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('energy-provider.delete', {
            parent: 'energy-provider',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/energy-provider/energy-provider-delete-dialog.html',
                    controller: 'EnergyProviderDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EnergyProvider', function(EnergyProvider) {
                            return EnergyProvider.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('energy-provider', null, { reload: 'energy-provider' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
