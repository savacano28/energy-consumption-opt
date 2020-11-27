(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('PeakHoursDetailController', PeakHoursDetailController);

    PeakHoursDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PeakHours', 'EnergyProvider'];

    function PeakHoursDetailController($scope, $rootScope, $stateParams, previousState, entity, PeakHours, EnergyProvider) {
        var vm = this;

        vm.peakHours = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('synergreenApp:peakHoursUpdate', function(event, result) {
            vm.peakHours = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
