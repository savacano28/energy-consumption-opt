(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('SourceDescriptorDetailController', SourceDescriptorDetailController);

    SourceDescriptorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SourceDescriptor', 'EnergyElement'];

    function SourceDescriptorDetailController($scope, $rootScope, $stateParams, previousState, entity, SourceDescriptor, EnergyElement) {
        var vm = this;

        vm.sourceDescriptor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('synergreenApp:sourceDescriptorUpdate', function(event, result) {
            vm.sourceDescriptor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
