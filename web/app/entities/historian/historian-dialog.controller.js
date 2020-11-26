(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('HistorianDialogController', HistorianDialogController);

    HistorianDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Historian'];

    function HistorianDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Historian) {
        var vm = this;

        vm.historian = entity;
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
            if (vm.historian.id !== null) {
                Historian.update(vm.historian, onSaveSuccess, onSaveError);
            } else {
                Historian.save(vm.historian, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:historianUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
