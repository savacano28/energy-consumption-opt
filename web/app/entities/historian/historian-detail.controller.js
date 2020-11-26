(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('HistorianDetailController', HistorianDetailController);

    HistorianDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Historian'];

    function HistorianDetailController($scope, $rootScope, $stateParams, previousState, entity, Historian) {
        var vm = this;

        vm.historian = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('synergreenApp:historianUpdate', function(event, result) {
            vm.historian = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
