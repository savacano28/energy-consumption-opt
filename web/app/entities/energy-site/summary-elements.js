(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('SummaryElementsController', SummaryElementsController);

    SummaryElementsController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FluxTopology'];

    function SummaryElementsController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FluxTopology) {
        var vm = this;

        vm.currentFluxTopology = entity;
        vm.ok= ok;

        init();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });


        function init(){
            var elements = [];
            vm.currentFluxTopology.fluxGroups.forEach(g => elements.push(g.energyElements));
            vm.energyElements = [].concat.apply([], elements);

        }

        function ok() {
            $uibModalInstance.close(vm);
          };
    }
})();
