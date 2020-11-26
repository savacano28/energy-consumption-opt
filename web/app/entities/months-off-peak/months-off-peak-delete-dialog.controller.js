(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('MonthsOffPeakDeleteController',MonthsOffPeakDeleteController);

    MonthsOffPeakDeleteController.$inject = ['$uibModalInstance', 'entity', 'MonthsOffPeak'];

    function MonthsOffPeakDeleteController($uibModalInstance, entity, MonthsOffPeak) {
        var vm = this;

        vm.monthsOffPeak = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MonthsOffPeak.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
