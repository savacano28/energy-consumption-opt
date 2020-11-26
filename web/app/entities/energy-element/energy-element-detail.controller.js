(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('EnergyElementDetailController', EnergyElementDetailController);

    EnergyElementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EnergyElement', 'MeasurementSource', 'FluxGroup', '$window'];

    function EnergyElementDetailController($scope, $rootScope, $stateParams, previousState, entity, EnergyElement, MeasurementSource, FluxGroup, $window) {
        var vm = this;

        vm.energyElement = entity;
        vm.previousState = previousState.name;
        vm.historyBack = goBack;

        var unsubscribe = $rootScope.$on('synergreenApp:energyElementUpdate', function(event, result) {
            vm.energyElement = result;
        });
        $scope.$on('$destroy', unsubscribe);

      function goBack() {
                $window.history.back();
              }

    }
})();
