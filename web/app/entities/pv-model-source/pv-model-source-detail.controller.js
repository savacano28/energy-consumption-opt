(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('PVModelSourceDetailController', PVModelSourceDetailController);

    PVModelSourceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PVModelSource'];

    function PVModelSourceDetailController($scope, $rootScope, $stateParams, previousState, entity, PVModelSource) {
        var vm = this;

        vm.pVModelSource = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('synergreenApp:pVModelSourceUpdate', function(event, result) {
            vm.pVModelSource = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
