(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('EnergySiteDialogController', EnergySiteDialogController);

    EnergySiteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EnergySite', 'FluxTopology', 'EnergyManagementSystem'];

    function EnergySiteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EnergySite, FluxTopology, EnergyManagementSystem) {
        var vm = this;

        vm.energySite = entity;
        vm.clear = clear;
        vm.save = save;
        vm.fluxTopologies = FluxTopology.query();
        vm.energymanagementsystems = EnergyManagementSystem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.energySite.id !== null) {
                EnergySite.update(vm.energySite, onSaveSuccess, onSaveError);
            } else {
                EnergySite.save(vm.energySite, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            vm.energySite = result;
            $scope.$emit('synergreenApp:energySiteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
