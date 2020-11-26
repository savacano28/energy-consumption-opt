(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('EnergyElementController', EnergyElementController);

    EnergyElementController.$inject = ['EnergyElement', 'EnergyElementSearch','$window'];

    function EnergyElementController(EnergyElement, EnergyElementSearch, $window) {

        var vm = this;

        vm.energyElements = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;
        vm.historyBack = goBack;

        loadAll();

        function loadAll() {
            EnergyElement.query(function(result) {
                vm.energyElements = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EnergyElementSearch.query({query: vm.searchQuery}, function(result) {
                vm.energyElements = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }


      /**
       * Retour en arriere
       */
       function goBack() {
          $window.history.back();
       }

    }
})();
