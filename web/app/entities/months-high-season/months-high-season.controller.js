(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('MonthsHighSeasonController', MonthsHighSeasonController);

    MonthsHighSeasonController.$inject = ['MonthsHighSeason', 'MonthsHighSeasonSearch'];

    function MonthsHighSeasonController(MonthsHighSeason, MonthsHighSeasonSearch) {

        var vm = this;

        vm.monthsHighSeasons = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            MonthsHighSeason.query(function(result) {
                vm.monthsHighSeasons = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MonthsHighSeasonSearch.query({query: vm.searchQuery}, function(result) {
                vm.monthsHighSeasons = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
