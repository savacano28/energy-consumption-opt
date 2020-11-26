(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('FluxNodeDeleteController',FluxNodeDeleteController);

    FluxNodeDeleteController.$inject = ['$uibModalInstance', 'entity', 'FluxNode'];

    function FluxNodeDeleteController($uibModalInstance, entity, FluxNode) {
        var vm = this;

        vm.fluxNode = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FluxNode.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
