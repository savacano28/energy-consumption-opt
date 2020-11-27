(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('StructureController', StructureController);

    StructureController.$inject = ['Structure'];

    function StructureController(Structure) {

        var vm = this;

        vm.structure = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Structure.query(function(result) {
                vm.structure = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
