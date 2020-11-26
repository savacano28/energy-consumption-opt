(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('EnergyElementDialogController', EnergyElementDialogController);

    EnergyElementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EnergyElement', 'MeasurementSource', 'FluxGroup'];

    function EnergyElementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EnergyElement, MeasurementSource, FluxGroup) {
        var vm = this;

        vm.energyElement = entity;
        vm.clear = clear;
        vm.save = save;
        vm.measurementsources = MeasurementSource.query();
        vm.fluxgroups = FluxGroup.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.energyElement.id !== null) {
                EnergyElement.update(vm.energyElement, onSaveSuccess, onSaveError);
            } else {
                EnergyElement.save(vm.energyElement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:energyElementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
