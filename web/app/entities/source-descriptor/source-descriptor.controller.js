(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('SourceDescriptorController', SourceDescriptorController);

    SourceDescriptorController.$inject = ['SourceDescriptor'];

    function SourceDescriptorController(SourceDescriptor) {

        var vm = this;

        vm.sourceDescriptors = [];
        vm.clear = clear;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            SourceDescriptor.query(function(result) {
                vm.sourceDescriptors = result;
                vm.searchQuery = null;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
