(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('FluxGroupDeleteController',FluxGroupDeleteController);

    FluxGroupDeleteController.$inject = ['$uibModalInstance', 'entity', 'FluxGroup'];

    function FluxGroupDeleteController($uibModalInstance, entity, FluxGroup) {
        var vm = this;

        vm.fluxGroup = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FluxGroup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
