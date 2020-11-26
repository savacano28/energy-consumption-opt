(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('FluxGroupController', FluxGroupController);

    FluxGroupController.$inject = ['FluxGroup', 'FluxGroupSearch','$window'];

    function FluxGroupController(FluxGroup, FluxGroupSearch, $window) {

        var vm = this;

        vm.fluxGroups = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;
        vm.historyBack = goBack;

        loadAll();

        function loadAll() {
            FluxGroup.query(function(result) {
                vm.fluxGroups = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FluxGroupSearch.query({query: vm.searchQuery}, function(result) {
                vm.fluxGroups = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }

      /**
       * Retour en arriere
       */
       function goBack() {
          $window.history.back();
       }
           }
})();
