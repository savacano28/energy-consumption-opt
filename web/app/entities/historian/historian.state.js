(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('historian', {
            parent: 'entity',
            url: '/historian',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.historian.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/historian/historians.html',
                    controller: 'HistorianController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('historian');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('historian-detail', {
            parent: 'historian',
            url: '/historian/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.historian.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/historian/historian-detail.html',
                    controller: 'HistorianDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('historian');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Historian', function($stateParams, Historian) {
                    return Historian.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'historian',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('historian-detail.edit', {
            parent: 'historian-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historian/historian-dialog.html',
                    controller: 'HistorianDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Historian', function(Historian) {
                            return Historian.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('historian.new', {
            parent: 'historian',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historian/historian-dialog.html',
                    controller: 'HistorianDialogController',
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
                    $state.go('historian', null, { reload: 'historian' });
                }, function() {
                    $state.go('historian');
                });
            }]
        })
        .state('historian.edit', {
            parent: 'historian',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historian/historian-dialog.html',
                    controller: 'HistorianDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Historian', function(Historian) {
                            return Historian.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('historian', null, { reload: 'historian' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('historian.delete', {
            parent: 'historian',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historian/historian-delete-dialog.html',
                    controller: 'HistorianDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Historian', function(Historian) {
                            return Historian.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('historian', null, { reload: 'historian' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
