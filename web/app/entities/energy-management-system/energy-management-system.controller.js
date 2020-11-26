(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('EnergyManagementSystemController', EnergyManagementSystemController);

    EnergyManagementSystemController.$inject = ['EnergyManagementSystem', 'EnergyManagementSystemSearch'];

    function EnergyManagementSystemController(EnergyManagementSystem, EnergyManagementSystemSearch) {

        var vm = this;

        vm.energyManagementSystems = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            EnergyManagementSystem.query(function(result) {
                vm.energyManagementSystems = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EnergyManagementSystemSearch.query({query: vm.searchQuery}, function(result) {
                vm.energyManagementSystems = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
