(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('MonthsOffPeakDialogController', MonthsOffPeakDialogController);

    MonthsOffPeakDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MonthsOffPeak', 'EnergyProvider'];

    function MonthsOffPeakDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MonthsOffPeak, EnergyProvider) {
        var vm = this;

        vm.monthsOffPeak = entity;
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
            if (vm.monthsOffPeak.id !== null) {
                MonthsOffPeak.update(vm.monthsOffPeak, onSaveSuccess, onSaveError);
            } else {
                MonthsOffPeak.save(vm.monthsOffPeak, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:monthsOffPeakUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
