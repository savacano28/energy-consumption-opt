(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('flux-node', {
            parent: 'entity',
            url: '/flux-node',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.fluxNode.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flux-node/flux-nodes.html',
                    controller: 'FluxNodeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fluxNode');
                    $translatePartialLoader.addPart('elementType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('flux-node-detail', {
            parent: 'flux-node',
            url: '/flux-node/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.fluxNode.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flux-node/flux-node-detail.html',
                    controller: 'FluxNodeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fluxNode');
                    $translatePartialLoader.addPart('elementType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FluxNode', function($stateParams, FluxNode) {
                    return FluxNode.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'flux-node',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('flux-node-detail.edit', {
            parent: 'flux-node-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-node/flux-node-dialog.html',
                    controller: 'FluxNodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FluxNode', function(FluxNode) {
                            return FluxNode.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flux-node.new', {
            parent: 'flux-node',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-node/flux-node-dialog.html',
                    controller: 'FluxNodeDialogController',
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
                    $state.go('flux-node', null, { reload: 'flux-node' });
                }, function() {
                    $state.go('flux-node');
                });
            }]
        })
        .state('flux-node.edit', {
            parent: 'flux-node',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-node/flux-node-dialog.html',
                    controller: 'FluxNodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FluxNode', function(FluxNode) {
                            return FluxNode.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flux-node', null, { reload: 'flux-node' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flux-node.delete', {
            parent: 'flux-node',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-node/flux-node-delete-dialog.html',
                    controller: 'FluxNodeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FluxNode', function(FluxNode) {
                            return FluxNode.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flux-node', null, { reload: 'flux-node' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
