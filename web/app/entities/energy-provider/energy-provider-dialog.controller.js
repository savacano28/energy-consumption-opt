(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('EnergyProviderDialogController', EnergyProviderDialogController);

    EnergyProviderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EnergyProvider', 'FluxTopology'];

    function EnergyProviderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EnergyProvider, FluxTopology) {
        var vm = this;

        vm.energyProvider = entity;
        vm.clear = clear;
        vm.save = save;
        vm.fluxtopologies = FluxTopology.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.energyProvider.id !== null) {
                EnergyProvider.update(vm.energyProvider, onSaveSuccess, onSaveError);
            } else {
                EnergyProvider.save(vm.energyProvider, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:energyProviderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
