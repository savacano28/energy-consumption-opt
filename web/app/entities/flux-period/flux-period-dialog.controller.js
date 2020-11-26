(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('FluxPeriodDialogController', FluxPeriodDialogController);

    FluxPeriodDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FluxPeriod'];

    function FluxPeriodDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FluxPeriod) {
        var vm = this;

        vm.fluxPeriod = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.fluxPeriod.id !== null) {
                FluxPeriod.update(vm.fluxPeriod, onSaveSuccess, onSaveError);
            } else {
                FluxPeriod.save(vm.fluxPeriod, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:fluxPeriodUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.start = false;
        vm.datePickerOpenStatus.end = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
