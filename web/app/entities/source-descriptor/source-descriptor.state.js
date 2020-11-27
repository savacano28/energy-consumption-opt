(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('source-descriptor', {
            parent: 'entity',
            url: '/source-descriptor',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.sourceDescriptor.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/source-descriptor/source-descriptors.html',
                    controller: 'SourceDescriptorController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sourceDescriptor');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('source-descriptor-detail', {
            parent: 'source-descriptor',
            url: '/source-descriptor/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'synergreenApp.sourceDescriptor.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/source-descriptor/source-descriptor-detail.html',
                    controller: 'SourceDescriptorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sourceDescriptor');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SourceDescriptor', function($stateParams, SourceDescriptor) {
                    return SourceDescriptor.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'source-descriptor',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('source-descriptor-detail.edit', {
            parent: 'source-descriptor-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/source-descriptor/source-descriptor-dialog.html',
                    controller: 'SourceDescriptorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SourceDescriptor', function(SourceDescriptor) {
                            return SourceDescriptor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('source-descriptor.new', {
            parent: 'source-descriptor',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/source-descriptor/source-descriptor-dialog.html',
                    controller: 'SourceDescriptorDialogController',
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
                    $state.go('source-descriptor', null, { reload: 'source-descriptor' });
                }, function() {
                    $state.go('source-descriptor');
                });
            }]
        })
        .state('source-descriptor.edit', {
            parent: 'source-descriptor',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/source-descriptor/source-descriptor-dialog.html',
                    controller: 'SourceDescriptorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SourceDescriptor', function(SourceDescriptor) {
                            return SourceDescriptor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('source-descriptor', null, { reload: 'source-descriptor' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('source-descriptor.delete', {
            parent: 'source-descriptor',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/source-descriptor/source-descriptor-delete-dialog.html',
                    controller: 'SourceDescriptorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SourceDescriptor', function(SourceDescriptor) {
                            return SourceDescriptor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('source-descriptor', null, { reload: 'source-descriptor' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
