(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('SourceDescriptorDialogController', SourceDescriptorDialogController);

    SourceDescriptorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SourceDescriptor', 'EnergyElement'];

    function SourceDescriptorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SourceDescriptor, EnergyElement) {
        var vm = this;

        vm.sourceDescriptor = entity;
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
            if (vm.sourceDescriptor.id !== null) {
                SourceDescriptor.update(vm.sourceDescriptor, onSaveSuccess, onSaveError);
            } else {
                SourceDescriptor.save(vm.sourceDescriptor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:sourceDescriptorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
