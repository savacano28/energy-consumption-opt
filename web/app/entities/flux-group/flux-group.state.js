(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('flux-group', {
            parent: 'entity',
            url: '/flux-group',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.fluxGroup.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flux-group/flux-groups.html',
                    controller: 'FluxGroupController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fluxGroup');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('flux-group-detail', {
            parent: 'flux-group',
            url: '/flux-group/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.fluxGroup.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flux-group/flux-group-detail.html',
                    controller: 'FluxGroupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fluxGroup');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FluxGroup', function($stateParams, FluxGroup) {
                    return FluxGroup.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'flux-group',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('summary-elements-group', {
            parent: 'summary-groups-dialog',
            params: {
            idGroup: null
            },
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal' ,function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-group/summary-elements.html',
                    controller: 'FluxGroupSummaryElementsController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size:'md',
                    resolve: {
                        entity: ['FluxGroup', function(FluxGroup) {
                            return FluxGroup.get({id : $stateParams.idGroup}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flux-group-detail.edit', {
            parent: 'flux-group-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-group/flux-group-dialog.html',
                    controller: 'FluxGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FluxGroup', function(FluxGroup) {
                            return FluxGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flux-group.new', {
            parent: 'flux-group',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-group/flux-group-dialog.html',
                    controller: 'FluxGroupDialogController',
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
                    $state.go('flux-group', null, { reload: 'flux-group' });
                }, function() {
                    $state.go('flux-group');
                });
            }]
        })
        .state('flux-group.edit', {
            parent: 'flux-group',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-group/flux-group-dialog.html',
                    controller: 'FluxGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FluxGroup', function(FluxGroup) {
                            return FluxGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flux-group', null, { reload: 'flux-group' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flux-group.delete', {
            parent: 'flux-group',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-group/flux-group-delete-dialog.html',
                    controller: 'FluxGroupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FluxGroup', function(FluxGroup) {
                            return FluxGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flux-group', null, { reload: 'flux-group' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
