(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('flux-topology', {
            parent: 'entity',
            url: '/flux-topology',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.fluxTopology.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flux-topology/flux-topologies.html',
                    controller: 'FluxTopologyController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fluxTopology');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('flux-topology-detail', {
            parent: 'flux-topology',
            url: '/flux-topology/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.fluxTopology.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flux-topology/flux-topology-detail.html',
                    controller: 'FluxTopologyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fluxTopology');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FluxTopology', function($stateParams, FluxTopology) {
                    return FluxTopology.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'flux-topology',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('flux-topology-detail.edit', {
            parent: 'flux-topology-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-topology/flux-topology-dialog.html',
                    controller: 'FluxTopologyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FluxTopology', function(FluxTopology) {
                            return FluxTopology.getBasic({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flux-topology.new', {
            parent: 'flux-topology',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-topology/flux-topology-dialog.html',
                    controller: 'FluxTopologyDialogController',
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
                    $state.go('flux-topology', null, { reload: 'flux-topology' });
                }, function() {
                    $state.go('flux-topology');
                });
            }]
        })
        .state('flux-topology.edit', {
            parent: 'flux-topology',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-topology/flux-topology-dialog.html',
                    controller: 'FluxTopologyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FluxTopology', function(FluxTopology) {
                            return FluxTopology.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flux-topology', null, { reload: 'flux-topology' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flux-topology.delete', {
            parent: 'flux-topology',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-topology/flux-topology-delete-dialog.html',
                    controller: 'FluxTopologyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FluxTopology', function(FluxTopology) {
                            return FluxTopology.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flux-topology', null, { reload: 'flux-topology' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
