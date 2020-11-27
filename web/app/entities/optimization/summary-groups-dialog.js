(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('SummaryGroupsDialogController', SummaryGroupsDialogController);

    SummaryGroupsDialogController.$inject = ['$state','$scope','$timeout', 'entity', 'FluxTopology','FluxGroup', 'FluxGroupSearch','$window', '$uibModalInstance'];

    function SummaryGroupsDialogController( $state, $scope, $timeout, entity, FluxTopology, FluxGroup, FluxGroupSearch, $window, $uibModalInstance) {

        var vm = this;
        vm.fluxTopology = entity;
        vm.fluxGroups = [];
        vm.search = search;
        vm.clear = clear;
        vm.loadAll = loadAll;
        vm.addGroup = addGroup;
        vm.deleteGroup = deleteGroup;
        vm.inTopology = inTopology;
        vm.cloneGroup = cloneGroup;
        vm.save = save;

        loadAll();

        $timeout(function (){
                   angular.element('.form-group:eq(1)>input').focus();
               });

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

        function addGroup(id){
                  FluxGroup.get({id : id}, function (result) {
                        vm.fluxTopology.fluxGroups.push(result);
                  });
                }

        function deleteGroup(id){
                  vm.fluxTopology.fluxGroups =
                    vm.fluxTopology.fluxGroups.filter(e => e.id != id);
                }

        function inTopology(id){
                var groupIn = false ;
                vm.fluxTopology.fluxGroups.forEach(g => {
                                                            if (g.id == id){
                                                                groupIn = true;
                                                            }
                                                         }
                                                   );

                  return groupIn;
                }

        function cloneGroup(id){
            FluxGroup.get({id : id}, function (result){
                      FluxGroup.clone(result, function (){
                        loadAll();
                      });
             });
        }


       function clear () {
          $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.fluxTopology.id !== null) {
                FluxTopology.update(vm.fluxTopology, onSaveSuccess, onSaveError);
            } else {
                FluxTopology.save(vm.fluxTopology, onSaveSuccess, onSaveError);
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
