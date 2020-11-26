(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('MeasurementSourceDetailController', MeasurementSourceDetailController);

    MeasurementSourceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MeasurementSource', 'EnergyElement'];

    function MeasurementSourceDetailController($scope, $rootScope, $stateParams, previousState, entity, MeasurementSource, EnergyElement) {
        var vm = this;

        vm.measurementSource = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('synergreenApp:measurementSourceUpdate', function(event, result) {
            vm.measurementSource = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
