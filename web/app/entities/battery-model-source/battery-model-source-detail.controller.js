(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('BatteryModelSourceDetailController', BatteryModelSourceDetailController);

    BatteryModelSourceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BatteryModelSource'];

    function BatteryModelSourceDetailController($scope, $rootScope, $stateParams, previousState, entity, BatteryModelSource) {
        var vm = this;

        vm.batteryModelSource = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('synergreenApp:batteryModelSourceUpdate', function(event, result) {
            vm.batteryModelSource = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
