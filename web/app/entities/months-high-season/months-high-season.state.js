(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('months-high-season', {
            parent: 'entity',
            url: '/months-high-season',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.monthsHighSeason.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/months-high-season/months-high-season.html',
                    controller: 'MonthsHighSeasonController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('monthsHighSeason');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('months-high-season-detail', {
            parent: 'months-high-season',
            url: '/months-high-season/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.monthsHighSeason.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/months-high-season/months-high-season-detail.html',
                    controller: 'MonthsHighSeasonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('monthsHighSeason');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MonthsHighSeason', function($stateParams, MonthsHighSeason) {
                    return MonthsHighSeason.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'months-high-season',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('months-high-season-detail.edit', {
            parent: 'months-high-season-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/months-high-season/months-high-season-dialog.html',
                    controller: 'MonthsHighSeasonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MonthsHighSeason', function(MonthsHighSeason) {
                            return MonthsHighSeason.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('months-high-season.new', {
            parent: 'months-high-season',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/months-high-season/months-high-season-dialog.html',
                    controller: 'MonthsHighSeasonDialogController',
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
                    $state.go('months-high-season', null, { reload: 'months-high-season' });
                }, function() {
                    $state.go('months-high-season');
                });
            }]
        })
        .state('months-high-season.edit', {
            parent: 'months-high-season',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/months-high-season/months-high-season-dialog.html',
                    controller: 'MonthsHighSeasonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MonthsHighSeason', function(MonthsHighSeason) {
                            return MonthsHighSeason.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('months-high-season', null, { reload: 'months-high-season' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('months-high-season.delete', {
            parent: 'months-high-season',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/months-high-season/months-high-season-delete-dialog.html',
                    controller: 'MonthsHighSeasonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MonthsHighSeason', function(MonthsHighSeason) {
                            return MonthsHighSeason.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('months-high-season', null, { reload: 'months-high-season' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
