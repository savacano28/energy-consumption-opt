(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('LowHoursDetailController', LowHoursDetailController);

    LowHoursDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LowHours', 'EnergyProvider'];

    function LowHoursDetailController($scope, $rootScope, $stateParams, previousState, entity, LowHours, EnergyProvider) {
        var vm = this;

        vm.lowHours = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('synergreenApp:lowHoursUpdate', function(event, result) {
            vm.lowHours = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
