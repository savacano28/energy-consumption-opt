(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('ModelValueDialogController', ModelValueDialogController);

    ModelValueDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ModelValue', 'AbstractSource'];

    function ModelValueDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ModelValue, AbstractSource) {
        var vm = this;

        vm.modelValue = entity;
        vm.clear = clear;
        vm.save = save;
        vm.abstractsources = AbstractSource.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.modelValue.id !== null) {
                ModelValue.update(vm.modelValue, onSaveSuccess, onSaveError);
            } else {
                ModelValue.save(vm.modelValue, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:modelValueUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
