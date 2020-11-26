(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('FluxPeriodDeleteController',FluxPeriodDeleteController);

    FluxPeriodDeleteController.$inject = ['$uibModalInstance', 'entity', 'FluxPeriod'];

    function FluxPeriodDeleteController($uibModalInstance, entity, FluxPeriod) {
        var vm = this;

        vm.fluxPeriod = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FluxPeriod.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
