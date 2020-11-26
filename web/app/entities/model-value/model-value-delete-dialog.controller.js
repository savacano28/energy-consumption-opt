(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('ModelValueDeleteController',ModelValueDeleteController);

    ModelValueDeleteController.$inject = ['$uibModalInstance', 'entity', 'ModelValue'];

    function ModelValueDeleteController($uibModalInstance, entity, ModelValue) {
        var vm = this;

        vm.modelValue = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ModelValue.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
