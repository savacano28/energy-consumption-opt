(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('SummaryElementsDialogController', SummaryElementsDialogController);

    SummaryElementsDialogController.$inject = ['$state','$scope','$timeout', 'entity', 'FluxGroup', 'EnergyElement', 'FluxGroupSearch','$window', '$uibModalInstance'];

    function SummaryElementsDialogController( $state, $scope, $timeout, entity, FluxGroup, EnergyElement, EnergyElementSearch, $window, $uibModalInstance) {

        var vm = this;
        vm.fluxGroup = entity;
        vm.clear = clear;
        vm.energyElements = [];
        vm.search = search;
        vm.loadAll = loadAll;
        vm.addElement = addElement;
        vm.deleteElement = deleteElement;
        vm.inGroup = inGroup ;
        vm.save = save;

        loadAll();

        $timeout(function (){
                   angular.element('.form-group:eq(1)>input').focus();
               });

        function loadAll() {
            EnergyElement.query(function(result) {
                vm.energyElements = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EnergyElementSearch.query({query: vm.searchQuery}, function(result) {
                vm.energyElements = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function addElement(id){
          EnergyElement.get({id : id}, function (result) {
                vm.fluxGroup.energyElements.push(result);
          });
        }

         function deleteElement(id){
                          vm.fluxGroup.energyElements =
                          vm.fluxGroup.energyElements.filter(e => e.id != id);
                        }

        function inGroup(id){
                var elementIn = false ;
                vm.fluxGroup.energyElements.forEach(e => {
                                                            if (e.id == id){
                                                                elementIn = true;
                                                            }
                                                         }
                                                   );

                  return elementIn;
                }

       function clear () {
                $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.fluxGroup.id !== null) {
                FluxGroup.update(vm.fluxGroup, onSaveSuccess, onSaveError);
            } else {
                FluxGroup.save(vm.fluxGroup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
              $scope.$emit('synergreenApp:optimization-detail-update', result);
              $uibModalInstance.close(result);
              vm.isSaving = false;
           }

        function onSaveError () {
            vm.isSaving = false;
        }

     }
})();
