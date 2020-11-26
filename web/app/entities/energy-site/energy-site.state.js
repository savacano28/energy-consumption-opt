(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('energy-site', {
            parent: 'entity',
            url: '/energy-site',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.energySite.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/energy-site/energy-sites.html',
                    controller: 'EnergySiteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('energySite');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('energy-site-detail', {
            parent: 'energy-site',
            url: '/energy-site/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.energySite.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/energy-site/energy-site-detail.html',
                    controller: 'EnergySiteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('energySite');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EnergySite', function($stateParams, EnergySite) {
                    return EnergySite.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", "$window", function ($state, $window) {
                    var currentStateData = {
                        name: $state.current.name || 'energy-site',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('energy-site-detail.edit', {
            parent: 'energy-site-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/energy-site/energy-site-dialog.html',
                    controller: 'EnergySiteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EnergySite', function(EnergySite) {
                            return EnergySite.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('carte-topology', {
            parent: 'energy-site-detail',
            params: {
            idTopology: null
            },
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal' ,function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/energy-site/carte-topology.html',
                    controller: 'CarteTopologyController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size:'lg',
                    resolve: {
                        entity: ['FluxTopology', function(FluxTopology) {
                            return FluxTopology.get({id : $stateParams.idTopology}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', null, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('summary-elements-topology', {
            parent: 'energy-site-detail',
            params: {
            idTopology: null
            },
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal' ,function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-group/summary-elements.html',
                    controller: 'SummaryElementsController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size:'md',
                    resolve: {
                        entity: ['FluxTopology', function(FluxTopology) {
                            return FluxTopology.get({id : $stateParams.idTopology}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', null, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('energy-site.new', {
            parent: 'energy-site',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/energy-site/energy-site-dialog.html',
                    controller: 'EnergySiteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('energy-site', null, { reload: 'energy-site' });
                }, function() {
                    $state.go('energy-site');
                });
            }]
        })
        .state('energy-site.edit', {
            parent: 'energy-site',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/energy-site/energy-site-dialog.html',
                    controller: 'EnergySiteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EnergySite', function(EnergySite) {
                            return EnergySite.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('energy-site', null, { reload: 'energy-site' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('energy-site.delete', {
            parent: 'energy-site',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/energy-site/energy-site-delete-dialog.html',
                    controller: 'EnergySiteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EnergySite', function(EnergySite) {
                            return EnergySite.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('energy-site', null, { reload: 'energy-site' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();


/*
             bill: ['$stateParams', 'Monitoring', function($stateParams, Monitoring) {
                var params = {
                    id: $stateParams.id,
                    start: moment().subtract(1,"M").toDate(),
                    end: moment(),
                    step: 600
                 }
                return Monitoring.monitoring(params).$promise;
            }],
            .state('energy-site-map', {
            parent: 'energy-site-detail',
            params: {
            idTopology: null
            },
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal' ,function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flux-group/summary-elements.html',
                    controller: 'SummaryElementsController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size:'md',
                    resolve: {
                        entity: ['FluxTopology', function(FluxTopology) {
                            return FluxTopology.get({id : $stateParams.idTopology}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', null, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })*/
