(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('EnergyProviderDeleteController',EnergyProviderDeleteController);

    EnergyProviderDeleteController.$inject = ['$uibModalInstance', 'entity', 'EnergyProvider'];

    function EnergyProviderDeleteController($uibModalInstance, entity, EnergyProvider) {
        var vm = this;

        vm.energyProvider = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EnergyProvider.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
