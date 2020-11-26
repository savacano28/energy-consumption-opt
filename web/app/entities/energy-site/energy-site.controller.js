(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('EnergySiteController', EnergySiteController);

    EnergySiteController.$inject = ['EnergySite', 'EnergySiteSearch'];

    function EnergySiteController(EnergySite, EnergySiteSearch) {

        var vm = this;

        vm.energySites = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            EnergySite.query(function(result) {
                vm.energySites = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EnergySiteSearch.query({query: vm.searchQuery}, function(result) {
                vm.energySites = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
