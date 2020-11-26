(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('ModelParameterController', ModelParameterController);

    ModelParameterController.$inject = ['ModelParameter', 'ModelParameterSearch'];

    function ModelParameterController(ModelParameter, ModelParameterSearch) {

        var vm = this;

        vm.modelParameters = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            ModelParameter.query(function(result) {
                vm.modelParameters = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ModelParameterSearch.query({query: vm.searchQuery}, function(result) {
                vm.modelParameters = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
