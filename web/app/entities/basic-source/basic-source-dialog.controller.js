(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('BasicSourceDialogController', BasicSourceDialogController);

    BasicSourceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BasicSource'];

    function BasicSourceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BasicSource) {
        var vm = this;

        vm.basicSource = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.basicSource.id !== null) {
                BasicSource.update(vm.basicSource, onSaveSuccess, onSaveError);
            } else {
                BasicSource.save(vm.basicSource, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:basicSourceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
