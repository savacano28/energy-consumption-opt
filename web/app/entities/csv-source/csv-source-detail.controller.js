(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('CSVSourceDetailController', CSVSourceDetailController);

    CSVSourceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CSVSource'];

    function CSVSourceDetailController($scope, $rootScope, $stateParams, previousState, entity, CSVSource) {
        var vm = this;

        vm.cSVSource = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('synergreenApp:cSVSourceUpdate', function(event, result) {
            vm.cSVSource = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
