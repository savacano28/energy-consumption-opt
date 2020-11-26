(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('FluxPeriodDetailController', FluxPeriodDetailController);

    FluxPeriodDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FluxPeriod'];

    function FluxPeriodDetailController($scope, $rootScope, $stateParams, previousState, entity, FluxPeriod) {
        var vm = this;

        vm.fluxPeriod = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('synergreenApp:fluxPeriodUpdate', function(event, result) {
            vm.fluxPeriod = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
