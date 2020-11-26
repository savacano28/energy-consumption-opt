(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('EnergyProviderDetailController', EnergyProviderDetailController);

    EnergyProviderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EnergyProvider', 'MonthsHighSeason', 'MonthsOffPeak', 'HighHours', 'LowHours', 'PeakHours', 'FluxTopology'];

    function EnergyProviderDetailController($scope, $rootScope, $stateParams, previousState, entity, EnergyProvider, MonthsHighSeason, MonthsOffPeak, HighHours, LowHours, PeakHours, FluxTopology) {
        var vm = this;

        vm.energyProvider = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('synergreenApp:energyProviderUpdate', function(event, result) {
            vm.energyProvider = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
