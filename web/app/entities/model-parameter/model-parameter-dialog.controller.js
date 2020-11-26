(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('ModelParameterDialogController', ModelParameterDialogController);

    ModelParameterDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ModelParameter', 'AbstractSource'];

    function ModelParameterDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ModelParameter, AbstractSource) {
        var vm = this;

        vm.modelParameter = entity;
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
            if (vm.modelParameter.id !== null) {
                ModelParameter.update(vm.modelParameter, onSaveSuccess, onSaveError);
            } else {
                ModelParameter.save(vm.modelParameter, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:modelParameterUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
