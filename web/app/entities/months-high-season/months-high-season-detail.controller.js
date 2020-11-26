(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('MonthsHighSeasonDetailController', MonthsHighSeasonDetailController);

    MonthsHighSeasonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MonthsHighSeason', 'EnergyProvider'];

    function MonthsHighSeasonDetailController($scope, $rootScope, $stateParams, previousState, entity, MonthsHighSeason, EnergyProvider) {
        var vm = this;

        vm.monthsHighSeason = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('synergreenApp:monthsHighSeasonUpdate', function(event, result) {
            vm.monthsHighSeason = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
