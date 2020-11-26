(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('FluxTopologyDialogController', FluxTopologyDialogController);

    FluxTopologyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Upload', 'FluxTopology'];

    function FluxTopologyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Upload, FluxTopology) {
        var vm = this;

        vm.fluxTopology = entity;
        vm.clear = clear;
        vm.save = save;
        vm.fileCtrl = {};
        vm.file = null;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
         vm.isSaving = true;
         if (vm.fluxTopology.id !== null) {
              vm.fileCtrl.upload = Upload.upload({
                  url: 'api/flux-topology-image',
                  data: {fluxTopology: angular.toJson(vm.fluxTopology),image: vm.file}
              });
              vm.fileCtrl.upload.then(onSaveSuccess, onSaveError, progress);

            }
        }

        function progress(evt) {
            vm.fileCtrl.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
        }

        function onSaveSuccess (result) {
            $scope.$emit('synergreenApp:fluxTopologyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
