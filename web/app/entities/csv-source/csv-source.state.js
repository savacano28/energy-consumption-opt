(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('csv-source', {
            parent: 'entity',
            url: '/csv-source',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.cSVSource.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/csv-source/csv-sources.html',
                    controller: 'CSVSourceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cSVSource');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('csv-source-detail', {
            parent: 'csv-source',
            url: '/csv-source/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.cSVSource.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/csv-source/csv-source-detail.html',
                    controller: 'CSVSourceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cSVSource');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CSVSource', function($stateParams, CSVSource) {
                    return CSVSource.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'csv-source',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('csv-source-detail.edit', {
            parent: 'csv-source-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/csv-source/csv-source-dialog.html',
                    controller: 'CSVSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CSVSource', function(CSVSource) {
                            return CSVSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('csv-source.new', {
            parent: 'csv-source',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/csv-source/csv-source-dialog.html',
                    controller: 'CSVSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null,
                                file: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('csv-source', null, { reload: 'csv-source' });
                }, function() {
                    $state.go('csv-source');
                });
            }]
        })
        .state('csv-source.edit', {
            parent: 'csv-source',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/csv-source/csv-source-dialog.html',
                    controller: 'CSVSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CSVSource', function(CSVSource) {
                            return CSVSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('csv-source', null, { reload: 'csv-source' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('csv-source.delete', {
            parent: 'csv-source',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/csv-source/csv-source-delete-dialog.html',
                    controller: 'CSVSourceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CSVSource', function(CSVSource) {
                            return CSVSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('csv-source', null, { reload: 'csv-source' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
