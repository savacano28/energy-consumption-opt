(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('OptimizationController', OptimizationController);

    OptimizationController.$inject = ['$scope','FluxTopology','EnergySite', '$window','$anchorScroll'];

    function OptimizationController($scope, FluxTopology, EnergySite, $window,$anchorScroll) {
        var vm = this;
        vm.showme = false;
        vm.isCollapsed = false;
        vm.isCollapsedSummary = false;
        vm.IsVisible = true;
        vm.siteSelected;
        vm.fluxTopologySelected;
        vm.gotoOptimization = false;
        vm.fluxTopologiesFromSite = [];
        vm.showConfigurationForm = false;

        /*Methods*/
        vm.historyBack = goBack;
        vm.getFluxTopologiesFromSite = getFluxTopologiesFromSite;
        vm.enableOptimization = enableOptimization;

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
                    f.fluxTopologies.forEach(f=>{
                        if(f.simulation){
                            vm.fluxTopologiesFromSite.push(f);
                        }
                   });
                }
             })
         }

        function enableOptimization(){
            if(typeof (vm.siteSelected) !== "undefined" && (vm.siteSelected) !== null
                && typeof (vm.fluxTopologySelected) !== "undefined" && (vm.fluxTopologySelected) !== null){
                vm.gotoOptimization= true;
                }else{
                 vm.gotoOptimization= false;
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
