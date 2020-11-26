(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('HistorianController', HistorianController);

    HistorianController.$inject = ['Historian', 'HistorianSearch'];

    function HistorianController(Historian, HistorianSearch) {

        var vm = this;

        vm.historians = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Historian.query(function(result) {
                vm.historians = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            HistorianSearch.query({query: vm.searchQuery}, function(result) {
                vm.historians = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
