(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('peak-hours', {
            parent: 'entity',
            url: '/peak-hours',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.peakHours.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/peak-hours/peak-hours.html',
                    controller: 'PeakHoursController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('peakHours');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('peak-hours-detail', {
            parent: 'peak-hours',
            url: '/peak-hours/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.peakHours.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/peak-hours/peak-hours-detail.html',
                    controller: 'PeakHoursDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('peakHours');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PeakHours', function($stateParams, PeakHours) {
                    return PeakHours.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'peak-hours',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('peak-hours-detail.edit', {
            parent: 'peak-hours-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/peak-hours/peak-hours-dialog.html',
                    controller: 'PeakHoursDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PeakHours', function(PeakHours) {
                            return PeakHours.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('peak-hours.new', {
            parent: 'peak-hours',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/peak-hours/peak-hours-dialog.html',
                    controller: 'PeakHoursDialogController',
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
                    $state.go('peak-hours', null, { reload: 'peak-hours' });
                }, function() {
                    $state.go('peak-hours');
                });
            }]
        })
        .state('peak-hours.edit', {
            parent: 'peak-hours',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/peak-hours/peak-hours-dialog.html',
                    controller: 'PeakHoursDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PeakHours', function(PeakHours) {
                            return PeakHours.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('peak-hours', null, { reload: 'peak-hours' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('peak-hours.delete', {
            parent: 'peak-hours',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/peak-hours/peak-hours-delete-dialog.html',
                    controller: 'PeakHoursDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PeakHours', function(PeakHours) {
                            return PeakHours.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('peak-hours', null, { reload: 'peak-hours' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
