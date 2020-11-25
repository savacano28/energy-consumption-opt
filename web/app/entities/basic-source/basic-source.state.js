(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('basic-source', {
            parent: 'entity',
            url: '/basic-source',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.basicSource.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/basic-source/basic-sources.html',
                    controller: 'BasicSourceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('basicSource');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('basic-source-detail', {
            parent: 'basic-source',
            url: '/basic-source/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.basicSource.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/basic-source/basic-source-detail.html',
                    controller: 'BasicSourceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('basicSource');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BasicSource', function($stateParams, BasicSource) {
                    return BasicSource.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'basic-source',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('basic-source-detail.edit', {
            parent: 'basic-source-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/basic-source/basic-source-dialog.html',
                    controller: 'BasicSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BasicSource', function(BasicSource) {
                            return BasicSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('basic-source.new', {
            parent: 'basic-source',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/basic-source/basic-source-dialog.html',
                    controller: 'BasicSourceDialogController',
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
                    $state.go('basic-source', null, { reload: 'basic-source' });
                }, function() {
                    $state.go('basic-source');
                });
            }]
        })
        .state('basic-source.edit', {
            parent: 'basic-source',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/basic-source/basic-source-dialog.html',
                    controller: 'BasicSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BasicSource', function(BasicSource) {
                            return BasicSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('basic-source', null, { reload: 'basic-source' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('basic-source.delete', {
            parent: 'basic-source',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/basic-source/basic-source-delete-dialog.html',
                    controller: 'BasicSourceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BasicSource', function(BasicSource) {
                            return BasicSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('basic-source', null, { reload: 'basic-source' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
