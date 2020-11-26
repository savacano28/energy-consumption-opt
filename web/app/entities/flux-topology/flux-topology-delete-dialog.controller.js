(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('FluxTopologyDeleteController',FluxTopologyDeleteController);

    FluxTopologyDeleteController.$inject = ['$uibModalInstance', 'entity', 'FluxTopology'];

    function FluxTopologyDeleteController($uibModalInstance, entity, FluxTopology) {
        var vm = this;

        vm.fluxTopology = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FluxTopology.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
