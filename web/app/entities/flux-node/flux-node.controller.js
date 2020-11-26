(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('FluxNodeController', FluxNodeController);

    FluxNodeController.$inject = ['FluxNode', '$window'];

    function FluxNodeController(FluxNode,  $window) {

        var vm = this;

        vm.fluxNodes = [];
        vm.clear = clear;
        vm.loadAll = loadAll;
        vm.historyBack = goBack;

        loadAll();

        function loadAll() {
            FluxNode.query(function(result) {
                vm.fluxNodes = result;
            });
        }


        function clear() {
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
