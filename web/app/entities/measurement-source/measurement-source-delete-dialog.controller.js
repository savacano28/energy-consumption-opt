(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('MeasurementSourceDeleteController',MeasurementSourceDeleteController);

    MeasurementSourceDeleteController.$inject = ['$uibModalInstance', 'entity', 'MeasurementSource'];

    function MeasurementSourceDeleteController($uibModalInstance, entity, MeasurementSource) {
        var vm = this;

        vm.measurementSource = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MeasurementSource.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
