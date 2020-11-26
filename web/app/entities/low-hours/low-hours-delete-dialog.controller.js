(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('LowHoursDeleteController',LowHoursDeleteController);

    LowHoursDeleteController.$inject = ['$uibModalInstance', 'entity', 'LowHours'];

    function LowHoursDeleteController($uibModalInstance, entity, LowHours) {
        var vm = this;

        vm.lowHours = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LowHours.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
