(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('BasicSourceController', BasicSourceController);

    BasicSourceController.$inject = ['BasicSource', 'BasicSourceSearch'];

    function BasicSourceController(BasicSource, BasicSourceSearch) {

        var vm = this;

        vm.basicSources = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            BasicSource.query(function(result) {
                vm.basicSources = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BasicSourceSearch.query({query: vm.searchQuery}, function(result) {
                vm.basicSources = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
