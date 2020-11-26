(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('HighHoursController', HighHoursController);

    HighHoursController.$inject = ['HighHours', 'HighHoursSearch'];

    function HighHoursController(HighHours, HighHoursSearch) {

        var vm = this;

        vm.highHours = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            HighHours.query(function(result) {
                vm.highHours = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            HighHoursSearch.query({query: vm.searchQuery}, function(result) {
                vm.highHours = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
