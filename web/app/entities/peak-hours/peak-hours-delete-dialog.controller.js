(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('PeakHoursDeleteController',PeakHoursDeleteController);

    PeakHoursDeleteController.$inject = ['$uibModalInstance', 'entity', 'PeakHours'];

    function PeakHoursDeleteController($uibModalInstance, entity, PeakHours) {
        var vm = this;

        vm.peakHours = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PeakHours.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
