(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('BatteryModelSourceDialogController', BatteryModelSourceDialogController);

    BatteryModelSourceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BatteryModelSource'];

    function BatteryModelSourceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BatteryModelSource) {
        var vm = this;

        vm.batteryModelSource = entity;
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
            if (vm.batteryModelSource.id !== null) {
                BatteryModelSource.update(vm.batteryModelSource, onSaveSuccess, onSaveError);
            } else {
                BatteryModelSource.save(vm.batteryModelSource, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:batteryModelSourceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
