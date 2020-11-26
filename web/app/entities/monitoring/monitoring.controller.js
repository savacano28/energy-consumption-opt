(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('MonitoringController', MonitoringController);

    MonitoringController.$inject = ['$scope','FluxTopology','EnergySite', '$window','$anchorScroll'];

    function MonitoringController($scope, FluxTopology, EnergySite, $window,$anchorScroll) {
        var vm = this;
        vm.showme = false;
        vm.msg = "Type a fluxTopology";
        vm.isCollapsed = false;
        vm.isCollapsedSummary = false;
        vm.IsVisible = true;
        vm.siteSelected;
        vm.fluxTopologySelected;
        vm.gotoMonitoring = false;
        vm.fluxTopologiesFromSite = {};
        vm.showConfigurationForm = false;

        /*Methods*/
        vm.historyBack = goBack;
        vm.getFluxTopologiesFromSite = getFluxTopologiesFromSite;
        vm.enableMonitoring = enableMonitoring;

        init();

        $anchorScroll();

        function init(){
            allSites();
        }

        function allSites(){
            EnergySite.query(function(result){
                vm.energySites = result;
            })
        }

        function getFluxTopologiesFromSite(id){
            vm.showConfigurationForm = true;
            vm.energySites.forEach(f=>{
                if(f.id == id){
                    vm.fluxTopologiesFromSite = f.fluxTopologies;
                }
             })
         }

        function enableMonitoring(){
            if(typeof (vm.siteSelected) !== "undefined" && (vm.siteSelected) !== null
                && typeof (vm.fluxTopologySelected) !== "undefined" && (vm.fluxTopologySelected) !== null){
                vm.gotoMonitoring= true;
                }else{
                 vm.gotoMonitoring= false;
                }

            if(typeof (vm.siteSelected) !== "undefined" && (vm.siteSelected) !== null  ){
            vm.showConfigurationForm = true;
            }else{
            vm.showConfigurationForm = false;
            }

        }

        /*back button*/
        function goBack() {
          $window.history.back();
        }
        }

})();
