(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('optimization', {
            parent: 'entity',
            url: '/optimization',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.optimization.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/optimization/optimization.html',
                    controller: 'OptimizationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('optimization');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('optimization-detail', {
            parent: 'optimization',
            url: '/optimization-detail/fluxTopology/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.optimization.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/optimization/optimization-detail.html',
                    controller: 'OptimizationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('optimization');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FluxTopology', function($stateParams, FluxTopology) {
                    return FluxTopology.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'optimization',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('summary-groups-dialog', {
            parent: 'optimization-detail',
            data: {
                authorities: ['ROLE_USER']
            },
           resolve: {
                 translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                         $translatePartialLoader.addPart('fluxGroup');
                         return $translate.refresh();
                 }]
            },
            onEnter: ['$stateParams', '$state', '$uibModal' ,function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/optimization/summary-groups-dialog.html',
                    controller: 'SummaryGroupsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    animation: true,
                    size:'lg',
                    resolve : {
                         entity: ['FluxTopology', function(FluxTopology) {
                             return FluxTopology.get({id : $stateParams.id}).$promise;
                         }]
                     }
                }).result.then(function() {
                    $state.go('^',{id: $stateParams.id}, { reload: true});
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('summary-elements-dialog', {
            parent: 'optimization-detail',
            params: {
            idGroup: null
            },
            data: {
                authorities: ['ROLE_USER']
            },
           resolve: {
                 translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                         $translatePartialLoader.addPart('energyElement');
                         return $translate.refresh();
                 }]
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/optimization/summary-elements-dialog.html',
                    controller: 'SummaryElementsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size:'lg',
                    resolve: {
                        entity: ['FluxGroup', function(FluxGroup) {
                                return FluxGroup.get({id : $stateParams.idGroup}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {id: $stateParams.id}, { reload: true});
                }, function() {
                    $state.go('^');
                });
            }]
        })
    }

})();
