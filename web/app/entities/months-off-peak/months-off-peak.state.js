(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('months-off-peak', {
            parent: 'entity',
            url: '/months-off-peak',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.monthsOffPeak.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/months-off-peak/months-off-peaks.html',
                    controller: 'MonthsOffPeakController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('monthsOffPeak');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('months-off-peak-detail', {
            parent: 'months-off-peak',
            url: '/months-off-peak/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.monthsOffPeak.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/months-off-peak/months-off-peak-detail.html',
                    controller: 'MonthsOffPeakDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('monthsOffPeak');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MonthsOffPeak', function($stateParams, MonthsOffPeak) {
                    return MonthsOffPeak.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'months-off-peak',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('months-off-peak-detail.edit', {
            parent: 'months-off-peak-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/months-off-peak/months-off-peak-dialog.html',
                    controller: 'MonthsOffPeakDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MonthsOffPeak', function(MonthsOffPeak) {
                            return MonthsOffPeak.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('months-off-peak.new', {
            parent: 'months-off-peak',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/months-off-peak/months-off-peak-dialog.html',
                    controller: 'MonthsOffPeakDialogController',
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
                    $state.go('months-off-peak', null, { reload: 'months-off-peak' });
                }, function() {
                    $state.go('months-off-peak');
                });
            }]
        })
        .state('months-off-peak.edit', {
            parent: 'months-off-peak',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/months-off-peak/months-off-peak-dialog.html',
                    controller: 'MonthsOffPeakDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MonthsOffPeak', function(MonthsOffPeak) {
                            return MonthsOffPeak.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('months-off-peak', null, { reload: 'months-off-peak' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('months-off-peak.delete', {
            parent: 'months-off-peak',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/months-off-peak/months-off-peak-delete-dialog.html',
                    controller: 'MonthsOffPeakDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MonthsOffPeak', function(MonthsOffPeak) {
                            return MonthsOffPeak.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('months-off-peak', null, { reload: 'months-off-peak' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
