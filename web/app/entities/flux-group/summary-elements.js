(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('FluxGroupSummaryElementsController', FluxGroupSummaryElementsController);

    FluxGroupSummaryElementsController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity'];

    function FluxGroupSummaryElementsController ($timeout, $scope, $stateParams, $uibModalInstance, entity) {
        var vm = this;

        vm.currentFluxGroup = entity;
        vm.energyElements =  entity.energyElements
        vm.ok= ok;

        init();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });


        function init(){
        }

        function ok() {
            $uibModalInstance.close(vm);
          };
    }
})();
