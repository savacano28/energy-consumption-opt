(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('LowHoursDialogController', LowHoursDialogController);

    LowHoursDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LowHours', 'EnergyProvider'];

    function LowHoursDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LowHours, EnergyProvider) {
        var vm = this;

        vm.lowHours = entity;
        vm.clear = clear;
        vm.save = save;
        vm.energyproviders = EnergyProvider.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.lowHours.id !== null) {
                LowHours.update(vm.lowHours, onSaveSuccess, onSaveError);
            } else {
                LowHours.save(vm.lowHours, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:lowHoursUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
