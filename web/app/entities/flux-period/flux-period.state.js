(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('flux-period', {
            parent: 'entity',
            url: '/flux-period',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.fluxPeriod.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flux-period/flux-periods.html',
                    controller: 'FluxPeriodController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fluxPeriod');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('flux-period-detail', {
            parent: 'flux-period',
            url: '/flux-period/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.fluxPeriod.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flux-period/flux-period-detail.html',
                    controller: 'FluxPeriodDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fluxPeriod');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FluxPeriod', function($stateParams, FluxPeriod) {
                    return FluxPeriod.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'flux-period',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('flux-period-detail.edit', {
            parent: 'flux-period-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-period/flux-period-dialog.html',
                    controller: 'FluxPeriodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FluxPeriod', function(FluxPeriod) {
                            return FluxPeriod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flux-period.new', {
            parent: 'flux-period',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-period/flux-period-dialog.html',
                    controller: 'FluxPeriodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                start: null,
                                end: null,
                                step: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('flux-period', null, { reload: 'flux-period' });
                }, function() {
                    $state.go('flux-period');
                });
            }]
        })
        .state('flux-period.edit', {
            parent: 'flux-period',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-period/flux-period-dialog.html',
                    controller: 'FluxPeriodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FluxPeriod', function(FluxPeriod) {
                            return FluxPeriod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flux-period', null, { reload: 'flux-period' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flux-period.delete', {
            parent: 'flux-period',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-period/flux-period-delete-dialog.html',
                    controller: 'FluxPeriodDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FluxPeriod', function(FluxPeriod) {
                            return FluxPeriod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flux-period', null, { reload: 'flux-period' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
