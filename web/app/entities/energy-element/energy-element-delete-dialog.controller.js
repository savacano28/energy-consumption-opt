(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('EnergyElementDeleteController',EnergyElementDeleteController);

    EnergyElementDeleteController.$inject = ['$uibModalInstance', 'entity', 'EnergyElement'];

    function EnergyElementDeleteController($uibModalInstance, entity, EnergyElement) {
        var vm = this;

        vm.energyElement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EnergyElement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
