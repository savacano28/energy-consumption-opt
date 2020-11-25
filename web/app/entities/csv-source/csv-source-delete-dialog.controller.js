(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('CSVSourceDeleteController',CSVSourceDeleteController);

    CSVSourceDeleteController.$inject = ['$uibModalInstance', 'entity', 'CSVSource'];

    function CSVSourceDeleteController($uibModalInstance, entity, CSVSource) {
        var vm = this;

        vm.cSVSource = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CSVSource.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
