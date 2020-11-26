(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('high-hours', {
            parent: 'entity',
            url: '/high-hours',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.highHours.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/high-hours/high-hours.html',
                    controller: 'HighHoursController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('highHours');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('high-hours-detail', {
            parent: 'high-hours',
            url: '/high-hours/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.highHours.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/high-hours/high-hours-detail.html',
                    controller: 'HighHoursDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('highHours');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'HighHours', function($stateParams, HighHours) {
                    return HighHours.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'high-hours',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('high-hours-detail.edit', {
            parent: 'high-hours-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/high-hours/high-hours-dialog.html',
                    controller: 'HighHoursDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HighHours', function(HighHours) {
                            return HighHours.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('high-hours.new', {
            parent: 'high-hours',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/high-hours/high-hours-dialog.html',
                    controller: 'HighHoursDialogController',
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
                    $state.go('high-hours', null, { reload: 'high-hours' });
                }, function() {
                    $state.go('high-hours');
                });
            }]
        })
        .state('high-hours.edit', {
            parent: 'high-hours',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/high-hours/high-hours-dialog.html',
                    controller: 'HighHoursDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HighHours', function(HighHours) {
                            return HighHours.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('high-hours', null, { reload: 'high-hours' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('high-hours.delete', {
            parent: 'high-hours',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/high-hours/high-hours-delete-dialog.html',
                    controller: 'HighHoursDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HighHours', function(HighHours) {
                            return HighHours.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('high-hours', null, { reload: 'high-hours' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
