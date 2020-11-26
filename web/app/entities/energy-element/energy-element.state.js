(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('energy-element', {
            parent: 'entity',
            url: '/energy-element',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.energyElement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/energy-element/energy-elements.html',
                    controller: 'EnergyElementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('energyElement');
                    $translatePartialLoader.addPart('elementType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('energy-element-detail', {
            parent: 'energy-element',
            url: '/energy-element/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.energyElement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/energy-element/energy-element-detail.html',
                    controller: 'EnergyElementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('energyElement');
                    $translatePartialLoader.addPart('elementType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EnergyElement', function($stateParams, EnergyElement) {
                    return EnergyElement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'energy-element',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('energy-element-detail.edit', {
            parent: 'energy-element-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/energy-element/energy-element-dialog.html',
                    controller: 'EnergyElementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EnergyElement', function(EnergyElement) {
                            return EnergyElement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('energy-element.new', {
            parent: 'energy-element',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/energy-element/energy-element-dialog.html',
                    controller: 'EnergyElementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                elementName: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('energy-element', null, { reload: 'energy-element' });
                }, function() {
                    $state.go('energy-element');
                });
            }]
        })
        .state('energy-element.edit', {
            parent: 'energy-element',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/energy-element/energy-element-dialog.html',
                    controller: 'EnergyElementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EnergyElement', function(EnergyElement) {
                            return EnergyElement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('energy-element', null, { reload: 'energy-element' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('energy-element.delete', {
            parent: 'energy-element',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/energy-element/energy-element-delete-dialog.html',
                    controller: 'EnergyElementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EnergyElement', function(EnergyElement) {
                            return EnergyElement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('energy-element', null, { reload: 'energy-element' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
