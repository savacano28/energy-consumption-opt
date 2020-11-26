(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('MonthsOffPeakDetailController', MonthsOffPeakDetailController);

    MonthsOffPeakDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MonthsOffPeak', 'EnergyProvider'];

    function MonthsOffPeakDetailController($scope, $rootScope, $stateParams, previousState, entity, MonthsOffPeak, EnergyProvider) {
        var vm = this;

        vm.monthsOffPeak = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('synergreenApp:monthsOffPeakUpdate', function(event, result) {
            vm.monthsOffPeak = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
