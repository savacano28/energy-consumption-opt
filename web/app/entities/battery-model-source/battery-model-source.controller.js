(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('BatteryModelSourceController', BatteryModelSourceController);

    BatteryModelSourceController.$inject = ['BatteryModelSource', 'BatteryModelSourceSearch'];

    function BatteryModelSourceController(BatteryModelSource, BatteryModelSourceSearch) {

        var vm = this;

        vm.batteryModelSources = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            BatteryModelSource.query(function(result) {
                vm.batteryModelSources = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BatteryModelSourceSearch.query({query: vm.searchQuery}, function(result) {
                vm.batteryModelSources = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
