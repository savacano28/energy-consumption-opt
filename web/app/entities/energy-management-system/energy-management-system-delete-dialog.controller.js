(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('EnergyManagementSystemDeleteController',EnergyManagementSystemDeleteController);

    EnergyManagementSystemDeleteController.$inject = ['$uibModalInstance', 'entity', 'EnergyManagementSystem'];

    function EnergyManagementSystemDeleteController($uibModalInstance, entity, EnergyManagementSystem) {
        var vm = this;

        vm.energyManagementSystem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EnergyManagementSystem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
