(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('PVModelSourceDialogController', PVModelSourceDialogController);

    PVModelSourceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PVModelSource'];

    function PVModelSourceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PVModelSource) {
        var vm = this;

        vm.pVModelSource = entity;
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
            if (vm.pVModelSource.id !== null) {
                PVModelSource.update(vm.pVModelSource, onSaveSuccess, onSaveError);
            } else {
                PVModelSource.save(vm.pVModelSource, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:pVModelSourceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
