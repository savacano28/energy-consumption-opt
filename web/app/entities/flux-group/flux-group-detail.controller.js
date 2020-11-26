(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('FluxGroupDetailController', FluxGroupDetailController);

    FluxGroupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FluxGroup', 'EnergyElement', 'FluxTopology', '$window'];

    function FluxGroupDetailController($scope, $rootScope, $stateParams, previousState, entity, FluxGroup, EnergyElement, FluxTopology, $window) {
        var vm = this;

        vm.fluxGroup = entity;
        vm.previousState = previousState.name;
        vm.historyBack = goBack;

        var unsubscribe = $rootScope.$on('synergreenApp:fluxGroupUpdate', function(event, result) {
            vm.fluxGroup = result;
        });
        $scope.$on('$destroy', unsubscribe);

        function goBack() {
                  $window.history.back();
                }
    }
})();
