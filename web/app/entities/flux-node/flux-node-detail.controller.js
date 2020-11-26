(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('FluxNodeDetailController', FluxNodeDetailController);

    FluxNodeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FluxNode', 'MeasurementSource', 'FluxGroup', '$window'];

    function FluxNodeDetailController($scope, $rootScope, $stateParams, previousState, entity, FluxNode, MeasurementSource, FluxGroup, $window) {
        var vm = this;

        vm.fluxNode = entity;
        vm.previousState = previousState.name;
        vm.historyBack = goBack;

        var unsubscribe = $rootScope.$on('synergreenApp:fluxNodeUpdate', function(event, result) {
            vm.fluxNode = result;
        });
        $scope.$on('$destroy', unsubscribe);

      function goBack() {
                $window.history.back();
              }

    }
})();
