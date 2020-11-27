(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pv-model-source', {
            parent: 'entity',
            url: '/pv-model-source',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.pVModelSource.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pv-model-source/pv-model-sources.html',
                    controller: 'PVModelSourceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pVModelSource');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('pv-model-source-detail', {
            parent: 'pv-model-source',
            url: '/pv-model-source/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.pVModelSource.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pv-model-source/pv-model-source-detail.html',
                    controller: 'PVModelSourceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pVModelSource');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PVModelSource', function($stateParams, PVModelSource) {
                    return PVModelSource.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'pv-model-source',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('pv-model-source-detail.edit', {
            parent: 'pv-model-source-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pv-model-source/pv-model-source-dialog.html',
                    controller: 'PVModelSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PVModelSource', function(PVModelSource) {
                            return PVModelSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pv-model-source.new', {
            parent: 'pv-model-source',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pv-model-source/pv-model-source-dialog.html',
                    controller: 'PVModelSourceDialogController',
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
                    $state.go('pv-model-source', null, { reload: 'pv-model-source' });
                }, function() {
                    $state.go('pv-model-source');
                });
            }]
        })
        .state('pv-model-source.edit', {
            parent: 'pv-model-source',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pv-model-source/pv-model-source-dialog.html',
                    controller: 'PVModelSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PVModelSource', function(PVModelSource) {
                            return PVModelSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pv-model-source', null, { reload: 'pv-model-source' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pv-model-source.delete', {
            parent: 'pv-model-source',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pv-model-source/pv-model-source-delete-dialog.html',
                    controller: 'PVModelSourceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PVModelSource', function(PVModelSource) {
                            return PVModelSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pv-model-source', null, { reload: 'pv-model-source' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
