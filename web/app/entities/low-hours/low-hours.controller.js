(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('LowHoursController', LowHoursController);

    LowHoursController.$inject = ['LowHours', 'LowHoursSearch'];

    function LowHoursController(LowHours, LowHoursSearch) {

        var vm = this;

        vm.lowHours = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            LowHours.query(function(result) {
                vm.lowHours = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            LowHoursSearch.query({query: vm.searchQuery}, function(result) {
                vm.lowHours = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
