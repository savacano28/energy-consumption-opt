(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('HighHoursDeleteController',HighHoursDeleteController);

    HighHoursDeleteController.$inject = ['$uibModalInstance', 'entity', 'HighHours'];

    function HighHoursDeleteController($uibModalInstance, entity, HighHours) {
        var vm = this;

        vm.highHours = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HighHours.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
