(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('model-value', {
            parent: 'entity',
            url: '/model-value',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.modelValue.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/model-value/model-values.html',
                    controller: 'ModelValueController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('modelValue');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('model-value-detail', {
            parent: 'model-value',
            url: '/model-value/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.modelValue.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/model-value/model-value-detail.html',
                    controller: 'ModelValueDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('modelValue');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ModelValue', function($stateParams, ModelValue) {
                    return ModelValue.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'model-value',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('model-value-detail.edit', {
            parent: 'model-value-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-value/model-value-dialog.html',
                    controller: 'ModelValueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ModelValue', function(ModelValue) {
                            return ModelValue.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model-value.new', {
            parent: 'model-value',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-value/model-value-dialog.html',
                    controller: 'ModelValueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                stringValue: null,
                                doubleValue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('model-value', null, { reload: 'model-value' });
                }, function() {
                    $state.go('model-value');
                });
            }]
        })
        .state('model-value.edit', {
            parent: 'model-value',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-value/model-value-dialog.html',
                    controller: 'ModelValueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ModelValue', function(ModelValue) {
                            return ModelValue.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model-value', null, { reload: 'model-value' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model-value.delete', {
            parent: 'model-value',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-value/model-value-delete-dialog.html',
                    controller: 'ModelValueDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ModelValue', function(ModelValue) {
                            return ModelValue.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model-value', null, { reload: 'model-value' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
