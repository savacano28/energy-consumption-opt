(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('CarteTopologyController', CarteTopologyController);

    CarteTopologyController.$inject = ['$timeout', '$scope', '$stateParams', '$localStorage', '$uibModalInstance', 'entity', 'Upload', 'FluxTopology'];

    function CarteTopologyController ($timeout, $scope, $stateParams, $localStorage, $uibModalInstance, entity, Upload, FluxTopology) {
        var vm = this;
        vm.currentFluxTopology = entity;
        vm.ok= ok;
        vm.bindToLocalStorage = bindToLocalStorage;

        function bindToLocalStorage() {
            // bind to local storage
            vm.$storage = $localStorage;
            // on initialise pour la première fois
            // filtre par défaut
            if (!vm.$storage.typeTechnologieEdit) {
                vm.$storage.typeTechnologieEdit = {
                    filter: ""
                };
            }
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function ok() {
            $uibModalInstance.close(vm);
          };
    }
})();
