(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('PVModelSourceController', PVModelSourceController);

    PVModelSourceController.$inject = ['PVModelSource', 'PVModelSourceSearch'];

    function PVModelSourceController(PVModelSource, PVModelSourceSearch) {

        var vm = this;

        vm.pVModelSources = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            PVModelSource.query(function(result) {
                vm.pVModelSources = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PVModelSourceSearch.query({query: vm.searchQuery}, function(result) {
                vm.pVModelSources = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
