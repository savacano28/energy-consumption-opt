(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('FluxTopologyController', FluxTopologyController);

    FluxTopologyController.$inject = ['FluxTopology', 'FluxTopologySearch'];

    function FluxTopologyController(FluxTopology, FluxTopologySearch) {

        var vm = this;

        vm.fluxTopologies = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            FluxTopology.query(function(result) {
                vm.fluxTopologies = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FluxTopologySearch.query({query: vm.searchQuery}, function(result) {
                vm.fluxTopologies = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
