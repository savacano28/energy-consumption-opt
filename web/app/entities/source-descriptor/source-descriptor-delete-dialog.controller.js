(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('SourceDescriptorDeleteController',SourceDescriptorDeleteController);

    SourceDescriptorDeleteController.$inject = ['$uibModalInstance', 'entity', 'SourceDescriptor'];

    function SourceDescriptorDeleteController($uibModalInstance, entity, SourceDescriptor) {
        var vm = this;

        vm.sourceDescriptor = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SourceDescriptor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
