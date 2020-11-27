(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('PeakHoursController', PeakHoursController);

    PeakHoursController.$inject = ['PeakHours', 'PeakHoursSearch'];

    function PeakHoursController(PeakHours, PeakHoursSearch) {

        var vm = this;

        vm.peakHours = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            PeakHours.query(function(result) {
                vm.peakHours = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PeakHoursSearch.query({query: vm.searchQuery}, function(result) {
                vm.peakHours = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
