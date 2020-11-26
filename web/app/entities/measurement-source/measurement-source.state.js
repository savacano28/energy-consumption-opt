(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('measurement-source', {
            parent: 'entity',
            url: '/measurement-source',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.measurementSource.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/measurement-source/measurement-sources.html',
                    controller: 'MeasurementSourceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('measurementSource');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('measurement-source-detail', {
            parent: 'measurement-source',
            url: '/measurement-source/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.measurementSource.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/measurement-source/measurement-source-detail.html',
                    controller: 'MeasurementSourceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('measurementSource');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MeasurementSource', function($stateParams, MeasurementSource) {
                    return MeasurementSource.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'measurement-source',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('measurement-source-detail.edit', {
            parent: 'measurement-source-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/measurement-source/measurement-source-dialog.html',
                    controller: 'MeasurementSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MeasurementSource', function(MeasurementSource) {
                            return MeasurementSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('measurement-source.new', {
            parent: 'measurement-source',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/measurement-source/measurement-source-dialog.html',
                    controller: 'MeasurementSourceDialogController',
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
                    $state.go('measurement-source', null, { reload: 'measurement-source' });
                }, function() {
                    $state.go('measurement-source');
                });
            }]
        })
        .state('measurement-source.edit', {
            parent: 'measurement-source',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/measurement-source/measurement-source-dialog.html',
                    controller: 'MeasurementSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MeasurementSource', function(MeasurementSource) {
                            return MeasurementSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('measurement-source', null, { reload: 'measurement-source' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('measurement-source.delete', {
            parent: 'measurement-source',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/measurement-source/measurement-source-delete-dialog.html',
                    controller: 'MeasurementSourceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MeasurementSource', function(MeasurementSource) {
                            return MeasurementSource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('measurement-source', null, { reload: 'measurement-source' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
