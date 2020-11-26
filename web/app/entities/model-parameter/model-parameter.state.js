(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('model-parameter', {
            parent: 'entity',
            url: '/model-parameter',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.modelParameter.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/model-parameter/model-parameters.html',
                    controller: 'ModelParameterController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('modelParameter');
                    $translatePartialLoader.addPart('modelParameterType');
                    $translatePartialLoader.addPart('visibilityType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('model-parameter-detail', {
            parent: 'model-parameter',
            url: '/model-parameter/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.modelParameter.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/model-parameter/model-parameter-detail.html',
                    controller: 'ModelParameterDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('modelParameter');
                    $translatePartialLoader.addPart('modelParameterType');
                    $translatePartialLoader.addPart('visibilityType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ModelParameter', function($stateParams, ModelParameter) {
                    return ModelParameter.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'model-parameter',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('model-parameter-detail.edit', {
            parent: 'model-parameter-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-parameter/model-parameter-dialog.html',
                    controller: 'ModelParameterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ModelParameter', function(ModelParameter) {
                            return ModelParameter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model-parameter.new', {
            parent: 'model-parameter',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-parameter/model-parameter-dialog.html',
                    controller: 'ModelParameterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                label: null,
                                type: null,
                                internal: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('model-parameter', null, { reload: 'model-parameter' });
                }, function() {
                    $state.go('model-parameter');
                });
            }]
        })
        .state('model-parameter.edit', {
            parent: 'model-parameter',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-parameter/model-parameter-dialog.html',
                    controller: 'ModelParameterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ModelParameter', function(ModelParameter) {
                            return ModelParameter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model-parameter', null, { reload: 'model-parameter' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model-parameter.delete', {
            parent: 'model-parameter',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-parameter/model-parameter-delete-dialog.html',
                    controller: 'ModelParameterDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ModelParameter', function(ModelParameter) {
                            return ModelParameter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model-parameter', null, { reload: 'model-parameter' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
