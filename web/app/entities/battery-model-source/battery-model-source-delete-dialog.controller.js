(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('BatteryModelSourceDeleteController',BatteryModelSourceDeleteController);

    BatteryModelSourceDeleteController.$inject = ['$uibModalInstance', 'entity', 'BatteryModelSource'];

    function BatteryModelSourceDeleteController($uibModalInstance, entity, BatteryModelSource) {
        var vm = this;

        vm.batteryModelSource = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BatteryModelSource.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
