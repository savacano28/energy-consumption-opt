(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('FluxNodeDialogController', FluxNodeDialogController);

    FluxNodeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FluxNode', 'MeasurementSource', 'FluxGroup'];

    function FluxNodeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FluxNode, MeasurementSource, FluxGroup) {
        var vm = this;

        vm.fluxNode = entity;
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
            if (vm.fluxNode.id !== null) {
                FluxNode.update(vm.fluxNode, onSaveSuccess, onSaveError);
            } else {
                FluxNode.save(vm.fluxNode, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:fluxNodeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
