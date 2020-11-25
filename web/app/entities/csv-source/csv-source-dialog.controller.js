(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('CSVSourceDialogController', CSVSourceDialogController);

    CSVSourceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CSVSource'];

    function CSVSourceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CSVSource) {
        var vm = this;

        vm.cSVSource = entity;
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
            if (vm.cSVSource.id !== null && vm.cSVSource.file !== null) {
                CSVSource.update(vm.cSVSource, onSaveSuccess, onSaveError);
            } else {
                CSVSource.save(vm.cSVSource, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:cSVSourceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
