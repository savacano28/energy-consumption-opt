(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('ModelParameterDeleteController',ModelParameterDeleteController);

    ModelParameterDeleteController.$inject = ['$uibModalInstance', 'entity', 'ModelParameter'];

    function ModelParameterDeleteController($uibModalInstance, entity, ModelParameter) {
        var vm = this;

        vm.modelParameter = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ModelParameter.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
