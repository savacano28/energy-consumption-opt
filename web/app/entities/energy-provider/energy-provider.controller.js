(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('EnergyProviderController', EnergyProviderController);

    EnergyProviderController.$inject = ['EnergyProvider', 'EnergyProviderSearch'];

    function EnergyProviderController(EnergyProvider, EnergyProviderSearch) {

        var vm = this;

        vm.energyProviders = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            EnergyProvider.query(function(result) {
                vm.energyProviders = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EnergyProviderSearch.query({query: vm.searchQuery}, function(result) {
                vm.energyProviders = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
