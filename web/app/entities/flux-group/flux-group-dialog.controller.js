(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('FluxGroupDialogController', FluxGroupDialogController);

    FluxGroupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FluxGroup', 'EnergyElement', 'FluxTopology'];

    function FluxGroupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FluxGroup, EnergyElement, FluxTopology) {
        var vm = this;

        vm.fluxGroup = entity;
        vm.clear = clear;
        vm.save = save;
        vm.energyelements = EnergyElement.query();
        vm.fluxtopologies = FluxTopology.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.fluxGroup.id !== null) {
                FluxGroup.update(vm.fluxGroup, onSaveSuccess, onSaveError);
            } else {
                FluxGroup.save(vm.fluxGroup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:fluxGroupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
