(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('MeasurementSourceController', MeasurementSourceController);

    MeasurementSourceController.$inject = ['MeasurementSource', 'MeasurementSourceSearch'];

    function MeasurementSourceController(MeasurementSource, MeasurementSourceSearch) {

        var vm = this;

        vm.measurementSources = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            MeasurementSource.query(function(result) {
                vm.measurementSources = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MeasurementSourceSearch.query({query: vm.searchQuery}, function(result) {
                vm.measurementSources = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
