(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('CSVSourceController', CSVSourceController);

    CSVSourceController.$inject = ['CSVSource', 'CSVSourceSearch'];

    function CSVSourceController(CSVSource, CSVSourceSearch) {

        var vm = this;

        vm.cSVSources = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            CSVSource.query(function(result) {
                vm.cSVSources = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CSVSourceSearch.query({query: vm.searchQuery}, function(result) {
                vm.cSVSources = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
