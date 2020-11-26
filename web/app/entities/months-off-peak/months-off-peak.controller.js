(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('MonthsOffPeakController', MonthsOffPeakController);

    MonthsOffPeakController.$inject = ['MonthsOffPeak', 'MonthsOffPeakSearch'];

    function MonthsOffPeakController(MonthsOffPeak, MonthsOffPeakSearch) {

        var vm = this;

        vm.monthsOffPeaks = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            MonthsOffPeak.query(function(result) {
                vm.monthsOffPeaks = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MonthsOffPeakSearch.query({query: vm.searchQuery}, function(result) {
                vm.monthsOffPeaks = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
