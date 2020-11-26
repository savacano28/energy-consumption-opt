(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('EnergySiteDetailController', EnergySiteDetailController);

    EnergySiteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EnergySite', 'FluxTopology', 'EnergyManagementSystem', 'Monitoring', '$window'];

    function EnergySiteDetailController($scope, $rootScope, $stateParams, previousState, entity, EnergySite, FluxTopology, EnergyManagementSystem, Monitoring, $window) {

        /* Variables */
        var vm = this;
        vm.energySite = entity;
        vm.previousState = (previousState.name == "monitoring-detail"? "home" :previousState.name) ;
        vm.openGroup = {};
        vm.openTopology = {};
        vm.invoice = {};
        vm.energyElements = {};
        vm.isCollapsedSummary = false;
        vm.isCollapsedConfiguration = false;

       /* Call functions */
        vm.historyBack = goBack;
        vm.changeStateTopology = changeStateTopology;
        vm.changeStateGroup = changeStateGroup;
        vm.getDateFromSeconds = getDateFromSeconds;
        init();

        var unsubscribe = $rootScope.$on('synergreenApp:energySiteUpdate', function(event, result) {
            vm.energySite = result;
        });
        $scope.$on('$destroy', unsubscribe);

        /*Open Topology and groups par default*/
          if(typeof previousState.params.id == "undefined"){
                vm.openTopology[(Object.keys(vm.openTopology)[0])] = true;
            }else{
                vm.openTopology[previousState.params.id] = true;
            }

        /*Functions */
        function init(){
            getAllElements();
        }

        function getAllElements(){
                vm.energySite.fluxTopologies.forEach(t=>{
                     vm.openTopology[t.id] = false;
                     t.invoices.forEach(i=>{
                            if(i.month == moment().format('MMMM').toUpperCase()){
                                vm.invoice[t.id] = i ;
                            }
                     })
                                     });
        }

        function changeStateGroup(id){
            vm.openGroup[id] = !vm.openGroup[id];
        }

        function changeStateTopology(id){
            vm.openTopology[id] = !vm.openTopology[id];
        }

        function getDateFromSeconds(epochSeconds){
            return moment.unix(epochSeconds).format('DD-MM-YYYY HH:mm:ss A');
        }

        function goBack() {
            $window.history.back();
        }
    }
})();
