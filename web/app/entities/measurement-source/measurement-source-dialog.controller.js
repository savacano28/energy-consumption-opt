(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('MeasurementSourceDialogController', MeasurementSourceDialogController);

    MeasurementSourceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MeasurementSource', 'EnergyElement'];

    function MeasurementSourceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MeasurementSource, EnergyElement) {
        var vm = this;

        vm.measurementSource = entity;
        vm.clear = clear;
        vm.save = save;
        vm.energyelements = EnergyElement.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.measurementSource.id !== null) {
                MeasurementSource.update(vm.measurementSource, onSaveSuccess, onSaveError);
            } else {
                MeasurementSource.save(vm.measurementSource, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:measurementSourceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
