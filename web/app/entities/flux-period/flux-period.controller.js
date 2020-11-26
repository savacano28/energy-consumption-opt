(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('FluxPeriodController', FluxPeriodController);

    FluxPeriodController.$inject = ['FluxPeriod', 'FluxPeriodSearch'];

    function FluxPeriodController(FluxPeriod, FluxPeriodSearch) {

        var vm = this;

        vm.fluxPeriods = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            FluxPeriod.query(function(result) {
                vm.fluxPeriods = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FluxPeriodSearch.query({query: vm.searchQuery}, function(result) {
                vm.fluxPeriods = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
