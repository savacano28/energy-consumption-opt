(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('low-hours', {
            parent: 'entity',
            url: '/low-hours',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.lowHours.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/low-hours/low-hours.html',
                    controller: 'LowHoursController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lowHours');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('low-hours-detail', {
            parent: 'low-hours',
            url: '/low-hours/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.lowHours.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/low-hours/low-hours-detail.html',
                    controller: 'LowHoursDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lowHours');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LowHours', function($stateParams, LowHours) {
                    return LowHours.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'low-hours',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('low-hours-detail.edit', {
            parent: 'low-hours-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/low-hours/low-hours-dialog.html',
                    controller: 'LowHoursDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LowHours', function(LowHours) {
                            return LowHours.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('low-hours.new', {
            parent: 'low-hours',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/low-hours/low-hours-dialog.html',
                    controller: 'LowHoursDialogController',
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
                    $state.go('low-hours', null, { reload: 'low-hours' });
                }, function() {
                    $state.go('low-hours');
                });
            }]
        })
        .state('low-hours.edit', {
            parent: 'low-hours',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/low-hours/low-hours-dialog.html',
                    controller: 'LowHoursDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LowHours', function(LowHours) {
                            return LowHours.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('low-hours', null, { reload: 'low-hours' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('low-hours.delete', {
            parent: 'low-hours',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/low-hours/low-hours-delete-dialog.html',
                    controller: 'LowHoursDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LowHours', function(LowHours) {
                            return LowHours.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('low-hours', null, { reload: 'low-hours' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
