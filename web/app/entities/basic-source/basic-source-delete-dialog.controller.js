(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('BasicSourceDeleteController',BasicSourceDeleteController);

    BasicSourceDeleteController.$inject = ['$uibModalInstance', 'entity', 'BasicSource'];

    function BasicSourceDeleteController($uibModalInstance, entity, BasicSource) {
        var vm = this;

        vm.basicSource = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BasicSource.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
