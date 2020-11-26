(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('MonthsHighSeasonDialogController', MonthsHighSeasonDialogController);

    MonthsHighSeasonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MonthsHighSeason', 'EnergyProvider'];

    function MonthsHighSeasonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MonthsHighSeason, EnergyProvider) {
        var vm = this;

        vm.monthsHighSeason = entity;
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
            if (vm.monthsHighSeason.id !== null) {
                MonthsHighSeason.update(vm.monthsHighSeason, onSaveSuccess, onSaveError);
            } else {
                MonthsHighSeason.save(vm.monthsHighSeason, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:monthsHighSeasonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
