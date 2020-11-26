(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('MonthsHighSeasonDeleteController',MonthsHighSeasonDeleteController);

    MonthsHighSeasonDeleteController.$inject = ['$uibModalInstance', 'entity', 'MonthsHighSeason'];

    function MonthsHighSeasonDeleteController($uibModalInstance, entity, MonthsHighSeason) {
        var vm = this;

        vm.monthsHighSeason = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MonthsHighSeason.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
