(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('HistorianDeleteController',HistorianDeleteController);

    HistorianDeleteController.$inject = ['$uibModalInstance', 'entity', 'Historian'];

    function HistorianDeleteController($uibModalInstance, entity, Historian) {
        var vm = this;

        vm.historian = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Historian.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
