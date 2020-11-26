(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('HighHoursDialogController', HighHoursDialogController);

    HighHoursDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'HighHours', 'EnergyProvider'];

    function HighHoursDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, HighHours, EnergyProvider) {
        var vm = this;

        vm.highHours = entity;
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
            if (vm.highHours.id !== null) {
                HighHours.update(vm.highHours, onSaveSuccess, onSaveError);
            } else {
                HighHours.save(vm.highHours, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:highHoursUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
