(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('BasicSourceDetailController', BasicSourceDetailController);

    BasicSourceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BasicSource'];

    function BasicSourceDetailController($scope, $rootScope, $stateParams, previousState, entity, BasicSource) {
        var vm = this;

        vm.basicSource = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('synergreenApp:basicSourceUpdate', function(event, result) {
            vm.basicSource = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
