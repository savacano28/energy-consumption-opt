(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('BatteryManagementController', BatteryManagementController);

    BatteryManagementController.$inject = ['$scope','EnergySite', 'BatteryManagement', '$window','$anchorScroll'];

    function BatteryManagementController($scope, EnergySite, BatteryManagement, $window,$anchorScroll) {
        var vm = this;
        vm.siteSelected;
        vm.fluxTopologySelected;
        vm.pilotingUnitSelected;
        vm.gotoBatteryManagement = false;
        vm.fluxTopologiesFromSite = [];
        vm.pilotingUnitsFromTopology = [];
        vm.showConfigurationForm = false;
        vm.showBatteryManagementForm = false;

        /*Methods*/
        vm.historyBack = goBack;
        vm.getFluxTopologiesFromSite = getFluxTopologiesFromSite;
        vm.getPilotingUnitsFromTopology = getPilotingUnitsFromTopology;
        vm.enableBatteryManagement = enableBatteryManagement;

        init();

        $anchorScroll();

        function init(){
            allPilotablesSites();
        }

         function allPilotablesSites(){
            BatteryManagement.pilotables(function(result){
                vm.pilotablesSites = result;
                if(vm.pilotablesSites.length==1){
                    getFluxTopologiesFromSite(vm.pilotablesSites[0].id);
                }
            })
         }

        function getFluxTopologiesFromSite(id){
            vm.showConfigurationForm = true;
            vm.fluxTopologiesFromSite = [];
            vm.pilotablesSites.forEach(s=>{
                if(s.id == id){
                    s.fluxTopologies.forEach(t => {
                        t.allElementsChildren.forEach(n => {
                            if(n.type == "BATTERY"){
                                vm.fluxTopologiesFromSite.push(t);
                                }
                            });
                        });
                   }
                });

            if(vm.fluxTopologiesFromSite.length==1){
                vm.fluxTopologySelected = vm.fluxTopologiesFromSite[0];
                getPilotingUnitsFromTopology(vm.fluxTopologiesFromSite[0].id)
            }
         }

         function getPilotingUnitsFromTopology(id){
            vm.showBatteryManagementForm = true;
            vm.pilotingUnitsFromTopology = [];
            vm.fluxTopologiesFromSite.forEach(f=>{
                if(f.id == id){
                    f.allElementsChildren.forEach(e=>{
                        if(e.type == "BATTERY"){
                            vm.pilotingUnitsFromTopology.push(e);
                        }
                     })
                }
             })

             if(vm.pilotingUnitsFromTopology.length==1){
                 vm.gotoBatteryManagement = true;
             }
         }

        function enableBatteryManagement(){
            if(typeof (vm.siteSelected) !== "undefined" && (vm.siteSelected) !== null
                && typeof (vm.fluxTopologySelected) !== "undefined" && (vm.fluxTopologySelected) !== null
                && typeof (vm.pilotingUnitSelected) !== "undefined" && (vm.pilotingUnitSelected) !== null
                ||
                typeof (vm.siteSelected) !== "undefined" && (vm.siteSelected) !== null
                && typeof (vm.fluxTopologySelected) !== "undefined" && (vm.fluxTopologySelected) !== null
                && vm.pilotingUnitsFromTopology.length==1
                ){
                    vm.gotoBatteryManagement= true;
                }else{
                    vm.gotoBatteryManagement= false;
                }

             if(typeof (vm.siteSelected) !== "undefined" && (vm.siteSelected) !== null  ){
                    vm.showConfigurationForm = true;
                }else{
                    vm.showConfigurationForm = false;
                }

             if(typeof (vm.fluxTopologySelected) !== "undefined" && (vm.fluxTopologySelected) !== null  ){
                 vm.showBatteryManagementForm = true;
             }else{
                 vm.showBatteryManagementForm = false;
             }
        }

        /*back button*/
        function goBack() {
          $window.history.back();
        }

        }

})();
