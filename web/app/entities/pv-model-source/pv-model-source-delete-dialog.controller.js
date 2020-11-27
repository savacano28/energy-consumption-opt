(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('PVModelSourceDeleteController',PVModelSourceDeleteController);

    PVModelSourceDeleteController.$inject = ['$uibModalInstance', 'entity', 'PVModelSource'];

    function PVModelSourceDeleteController($uibModalInstance, entity, PVModelSource) {
        var vm = this;

        vm.pVModelSource = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PVModelSource.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
