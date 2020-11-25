(function () {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('BatteryManagementDetailController', BatteryManagementDetailController);

    BatteryManagementDetailController.$inject = ['EnergyElement', 'entity', 'BatteryManagement', '$window', '$timeout'];

    function BatteryManagementDetailController(EnergyElement, entity, BatteryManagement, $window, $timeout) {
        var vm = this;
        vm.historyBack = goBack;
        vm.currentFluxTopology = entity;
        vm.currentBatteryChecked = 18;
        vm.runId ;
        vm.batteryManagementResult = {};

        vm.fluxTopologiesFromSite = [];
        vm.pilotingUnitsFromTopology = [];
        vm.siteSelected;
        vm.fluxTopologySelected;
        vm.pilotingUnitSelected;
        vm.gotoBatteryManagement = false;
        vm.showConfigurationForm = false;
        vm.showBatteryManagementForm = false;

        /*methods*/
        init();
        vm.batteryManagementOnDemand = batteryManagementOnDemand;

        /*graphs*/
        vm.graphicPOWER = {
            chart : {
                type: 'lineChart',
                height: 450,
                margin: {
                    top: 20,
                    right: 20,
                    bottom: 30,
                    left: 70
                },
                x : function (d) {return new Date(d.key);},
                y : function (d) {
                    if(d.measure<0.000001){
                        return 0.0;
                        }else{
                        return d.measure;
                        }
                },
                wrapLabels: true,
                useInteractiveGuideline : true,
                interactiveLayer:{
                  tooltip: {
                  contentGenerator: tooltipCustomContent
                }},
                xAxis : {
                    axisLabelDistance: -8,
                    tickFormat: function (d) {
                        return d3.time.format("%d/%m/%y %H:%M")(new Date(d));
                    }
                },
                yAxis : {
                    showMaxMin: false,
                    axisLabel: 'Power (kW)'
                },
                forceY: [0,5] ,
                zoom :  {
                   enabled: true,
                   scaleExtent: [1, 10],
                   useFixedDomain: false,
                   useNiceScale: false,
                   horizontalOff: false,
                   verticalOff: true,
                   unzoomEventType: "dblclick.zoom"
                }
                }};

        vm.graphicSOC = {
            chart : {
                type: 'lineChart',
                height: 450,
                margin: {
                    top: 20,
                    right: 20,
                    bottom: 30,
                    left: 70
                },
                x : function (d) {return new Date(d.key);},
                y : function (d) {
                        return 100 * d.measure;
                },
                wrapLabels: true,
                useInteractiveGuideline : true,
                interactiveLayer:{
                  tooltip: {
                  contentGenerator: tooltipCustomContent
                }},
                xAxis : {
                    axisLabelDistance: -8,
                    tickFormat: function (d) {
                        return d3.time.format("%d/%m/%y %H:%M")(new Date(d));
                    }
                },
                yAxis : {
                    showMaxMin: false,
                    axisLabel: 'State of Charge %'
                },
                forceY: [0,100] ,
                zoom :  {
                   enabled: true,
                   scaleExtent: [1, 10],
                   useFixedDomain: false,
                   useNiceScale: false,
                   horizontalOff: false,
                   verticalOff: true,
                   unzoomEventType: "dblclick.zoom"
                }
                }};

        /*functions*/
        function init(){
            allPilotablesSites();
            batteryManagement();
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

        function batteryManagement(){
            var params = {
                clientId: "3",
                fluxTopologyId: vm.currentFluxTopology.id,
                batteryId: vm.currentBatteryChecked,
                runId: vm.runId,
            }
            BatteryManagement.statusBattery(params, function (results) {
                 vm.batteryManagementResult = results;
                 d3.selectAll('.nvtooltip').remove();
                 dataToNVD3();
                 });
        }

        /*Genere a idRun, à qui appel? check if a run before*/
        function batteryManagementOnDemand(){
            var params = {
                clientId: "3", //getIdClientFrom user
                fluxTopologyId: vm.currentFluxTopology.id,
                batteryId: vm.currentBatteryChecked,
                runId: vm.runId,
                SoC0: 0.5 //value par default, bien soit mettre dans @requestParams ou le donne à partir d'ici?
            }
            BatteryManagement.batteryOnDemand(params, function (results) {
                 vm.batteryManagementResult = results;
                 d3.selectAll('.nvtooltip').remove();
                 dataToNVD3();
                 });
        }

        function dataToNVD3(){
            vm.tonvd3Summary = [];
            vm.tonvd3Battery = [];
            var prod  = [];
            var cons  = [];
            var bat  = [];
            var soc = [];

            vm.batteryManagementResult.result.forEach(r => {
                  prod.push({key: r.date, measure: r.pProdGlobal});
                  cons.push({key: r.date, measure: r.pConsoGlobal});
                  bat.push({key: r.date,  measure: r.p_bat});
                  soc.push({key: r.date,  measure: r.soc});
           });

           vm.tonvd3Summary.push(
           {key: "Production", values: prod, color: '#66d18d', area: true},
           {key: "Consommation", values: cons, color: '#ff82b6', area: true},
           {key: "Storage", values: bat, color: '#3fcef7', area: true}
           );

           vm.tonvd3Battery.push(
           {key: "Soc", values: soc, color: '#3fcef7', area: true}
           )

//           vm.graphic.chart.forceX = [0,new Date(vm.tonvd3Battery[0].values[vm.tonvd3Battery[0].values.length-1].key)];
//
         }

        function tooltipCustomContent(d){
                var html = "<table><thead><tr><th colspan='2'><b>" + d3.time.format("%d/%m/%y %H:%M")(new Date(d.value)) + "</b></th></tr></thead><tbody>";
                        d.series.forEach(function(elem){
                                        var totalRound = 0;
                                         html += "<tr>" +
                                                 "<td class='legend-color-guide'><div style='background-color: " + elem.color + ";'></div></td>" +
                                                 "<td class='key'><strong>" + elem.key + "</strong></td>";

                                        if (elem.key == "TOTAL"){
                                              d.series.forEach(function(m){
                                                if (m.key !== "TOTAL"){
                                                   totalRound += Math.round(m.value);
                                                 }
                                              });
                                            html += "<td class='x-value'>" + Math.round(totalRound) + "</td></tr>" ;
                                       }else{
                                            html += "<td class='x-value'>" + Math.round(elem.value) + "</td></tr>" ;
                                       }
                                     });
                            html += "</tbody></table>";
                        return html;
                }

        function goBack() {
          $window.history.back();
        }
    }
})();


