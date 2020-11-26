(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('EnergyManagementSystemDetailController', EnergyManagementSystemDetailController);

    EnergyManagementSystemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EnergyManagementSystem', 'EnergySite', '$window'];

    function EnergyManagementSystemDetailController($scope, $rootScope, $stateParams, previousState, entity, EnergyManagementSystem, EnergySite, $window) {
        var vm = this;

        vm.energyManagementSystem = entity;
        vm.previousState = previousState.name;
        vm.historyBack = goBack;

        var unsubscribe = $rootScope.$on('synergreenApp:energyManagementSystemUpdate', function(event, result) {
            vm.energyManagementSystem = result;
        });
        $scope.$on('$destroy', unsubscribe);

      function goBack() {
           $window.history.back();
         }

    }
})();
