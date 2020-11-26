(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('FluxTopologyDetailController', FluxTopologyDetailController);

    FluxTopologyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FluxTopology', 'EnergyProvider', 'FluxGroup', 'EnergySite', '$window'];

    function FluxTopologyDetailController($scope, $rootScope, $stateParams, previousState, entity, FluxTopology, EnergyProvider, FluxGroup, EnergySite, $window) {
        var vm = this;

        vm.fluxTopology = entity;
        vm.previousState = previousState.name;
        vm.historyBack = goBack;

        var unsubscribe = $rootScope.$on('synergreenApp:fluxTopologyUpdate', function(event, result) {
            vm.fluxTopology = result;
        });
        $scope.$on('$destroy', unsubscribe);

        function goBack() {
          $window.history.back();
        }

    }
})();
