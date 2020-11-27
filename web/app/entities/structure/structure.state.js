(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('structure', {
            parent: 'entity',
            url: '/structure',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.structure.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/structure/structure.html',
                    controller: 'StructureController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('structure');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('structure-detail', {
            parent: 'structure',
            url: '/structure-detail/structure/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.structure.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/structure/structure-detail.html',
                    controller: 'StructureDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('structure');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Structure', function($stateParams, Structure) {
                    return Structure.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'structure',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('structure.new', {
            parent: 'structure',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/structure/structure-dialog.html',
                    controller: 'StructureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                priceConsumption: null,
                                priceSellProduction: null,
                                priceAutoConsProduction: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('structure', null, { reload: 'structure' });
                }, function() {
                    $state.go('structure');
                });
            }]
        })
    }

})();
