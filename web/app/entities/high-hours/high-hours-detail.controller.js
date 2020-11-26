(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('HighHoursDetailController', HighHoursDetailController);

    HighHoursDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HighHours', 'EnergyProvider'];

    function HighHoursDetailController($scope, $rootScope, $stateParams, previousState, entity, HighHours, EnergyProvider) {
        var vm = this;

        vm.highHours = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('synergreenApp:highHoursUpdate', function(event, result) {
            vm.highHours = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
