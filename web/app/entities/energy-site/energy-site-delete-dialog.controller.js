(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('EnergySiteDeleteController',EnergySiteDeleteController);

    EnergySiteDeleteController.$inject = ['$uibModalInstance', 'entity', 'EnergySite'];

    function EnergySiteDeleteController($uibModalInstance, entity, EnergySite) {
        var vm = this;

        vm.energySite = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EnergySite.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
