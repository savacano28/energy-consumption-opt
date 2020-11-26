(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('ModelValueController', ModelValueController);

    ModelValueController.$inject = ['ModelValue', 'ModelValueSearch'];

    function ModelValueController(ModelValue, ModelValueSearch) {

        var vm = this;

        vm.modelValues = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ModelValue.query(function(result) {
                vm.modelValues = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ModelValueSearch.query({query: vm.searchQuery}, function(result) {
                vm.modelValues = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
