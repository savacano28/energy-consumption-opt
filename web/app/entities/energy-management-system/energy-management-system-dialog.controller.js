(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('EnergyManagementSystemDialogController', EnergyManagementSystemDialogController);

    EnergyManagementSystemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EnergyManagementSystem', 'EnergySite'];

    function EnergyManagementSystemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EnergyManagementSystem, EnergySite) {
        var vm = this;

        vm.energyManagementSystem = entity;
        vm.clear = clear;
        vm.save = save;
        vm.energysites = EnergySite.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.energyManagementSystem.id !== null) {
                EnergyManagementSystem.update(vm.energyManagementSystem, onSaveSuccess, onSaveError);
            } else {
                EnergyManagementSystem.save(vm.energyManagementSystem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:energyManagementSystemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
