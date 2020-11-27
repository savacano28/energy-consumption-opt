(function () {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('StructureDetailController', StructureDetailController);

    StructureDetailController.$inject = ['EnergyElement', 'entity', 'Structure', '$window', '$timeout'];

    function StructureDetailController(EnergyElement, entity, Structure, $window, $timeout) {
        var vm = this;
        vm.historyBack = goBack;


         function goBack() {
                  $window.history.back();
                }


    }
})();


