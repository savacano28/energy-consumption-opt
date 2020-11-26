(function () {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('MonitoringDetailController', MonitoringDetailController);

    MonitoringDetailController.$inject = ['FluxTopology', 'entity', 'Monitoring', '$window', '$timeout'];

    function MonitoringDetailController(FluxTopology, entity, Monitoring, $window, $timeout) {
        var vm = this;
        vm.currentFluxTopology = entity;
        /**
         * Liste des steps predefinis
         */
        vm.stepGraphTime = [
            {model: "Hour", format: "%d/%m/%y %H:%M"},
            {model: "Week", format: "%W : %d/%m/%y"},
            {model: "Month", format: "%m/%y"}
        ];

        /**
         * Liste des selections rapides des dates
         */
        vm.quickranges = [
            {id: 0, model: "Custom range", value: 1,  momentUnit: "h"},
            {id: 1, model: "Today", value: 1,  momentUnit: "d"},
            {id: 2, model: "This week", value: 1,  momentUnit: "W"},
            {id: 3, model: "This month", value: 1,  momentUnit: "M"},
            {id: 4, model: "This year", value: 1,  momentUnit: "y"},
            {id: 5, model: "From the Installation", value: 1,  momentUnit: "M"}
        ];

        /**
         * Configuration pour les datepickers
         */
        vm.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };

        vm.checkAll = true;
        vm.allChecked = false; //to use in monitoringTemporel for running dataToNVD3
        vm.consumerGroup = true;
        vm.storageGroup = true;
        vm.sellProducerGroup = true;
        vm.checkedElement = {};
        vm.msg = entity.name;
        vm.fluxTopologies = [];
        vm.energyElements = [];
        vm.elementsConsumer = [];
        vm.elementsProducer = [];
        vm.elementsBattery = [];
        vm.listOfBalanceDatas = [];
        vm.stateTopology = [];
        vm.checkElementArray = [];
        vm.graphTime = {};
        vm.tickValues = [];

        /*Functions */
        init();
        vm.clear = clear;
        vm.openStartDate = openStartDate;
        vm.openEndDate = openEndDate;
        vm.setDates = setDates;
        vm.updateGraphs = updateGraphs;
        vm.optimization = optimization;
        vm.monitoring = monitoring;
        vm.checkElement = checkElement;
        vm.checkAllElements = checkAllElements;
        vm.clearGraphics = clearGraphics;
        vm.historyBack = goBack;
        vm.startTimeChanged = startTimeChanged;
        vm.endTimeChanged = endTimeChanged;
        vm.limitDecimals = limitDecimals;

        vm.popupStart = {
            opened: false
        };

        vm.popupEnd = {
            opened: false
        };


        /*Params pour les graphics*/
        var chartLineEC = {
            chart : {
                type: 'stackedAreaChart',
                height: 450,
                margin: {
                    top: 20,
                    right: 20,
                    bottom: 30,
                    left: 70
                },
                x : function (d) {return new Date(d.instant);},
                y : function (d) {
                    if(d.measure<0.000001){
                        return 0.0;
                        }else{
                        return d.measure;
                        }
                },
                useVoronoi : true,
                clipEdge : true,
                duration : 300,
                showControls: false,
                showLegend: false,
                wrapLabels: true,
                useInteractiveGuideline : true,
                interactiveLayer:{
                      tooltip: {
                      contentGenerator: tooltipCustomContent
                  }},
                xAxis : {
                    axisLabelDistance: -8,
                    showMaxMin: false,
                    xScale: d3.time.scale(),
                    tickFormat: function (d) {
                        return d3.time.format(vm.graphTime.format)(new Date(d));
                    }
                },
                yAxis : {
                    axisLabel: 'Power (kW)',
                    tickFormat: function (d) {
                      return Math.round(Math.abs(d)) ;
                    },
                    tickValues: vm.tickValues
                },
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

        var chartLineEP = {
            chart : {
                type: 'stackedAreaChart',
                height: 450,
                margin: {
                    top: 20,
                    right: 20,
                    bottom: 30,
                    left: 70
                },
                x : function (d) {return new Date(d.instant);},
                y : function (d) {
                    if(d.measure<0.000001){
                        return 0.0;
                        }else{
                        return d.measure;
                        }
                },
                useVoronoi : true,
                clipEdge : true,
                duration : 300,
                showControls: false,
                showLegend: false,
                wrapLabels: true,
                useInteractiveGuideline : true,
                interactiveLayer:{
                      tooltip: {
                      contentGenerator: tooltipCustomContent
                  }},
                xAxis : {
                    axisLabelDistance: -8,
                    showMaxMin: false,
                    xScale: d3.time.scale(),
                    tickFormat: function (d) {
                         return d3.time.format(vm.graphTime.format)(new Date(d));
                    }
                },
                yAxis : {
                    axisLabel: 'Power (kW)',
                    tickFormat: function (d) {
                        return Math.round(Math.abs(d)) ; /*fixme*/
                    },
                     tickValues: vm.tickValues
                },
                zoom :  {
                    enabled: true,
                    scaleExtent: [1, 10],
                    useFixedDomain: false,
                    useNiceScale: false,
                    horizontalOff: false,
                    verticalOff: true,
                    unzoomEventType: 'dblclick.zoom'
                }
                }};

        var chartLineP = {
            chart : {
                type: 'stackedAreaChart',
                height: 450,
                margin: {
                    top: 20,
                    right: 20,
                    bottom: 30,
                    left: 70
                },
                x : function (d) {return new Date(d.instant);},
                y : function (d) {
                    if(d.measure<0.000001){
                        return 0.0;
                        }else{
                        return d.measure;
                        }
                },
                useVoronoi : true,
                clipEdge : true,
                duration : 300,
                showControls: false,
                showLegend: false,
                wrapLabels: true,
                useInteractiveGuideline : true,
                interactiveLayer:{
                      tooltip: {
                      contentGenerator: tooltipCustomContent
                  }},
                xAxis : {
                    axisLabelDistance: -8,
                    showMaxMin: false,
                    xScale: d3.time.scale(),
                    tickFormat: function (d) {
                        return d3.time.format(vm.graphTime.format)(new Date(d));
                    }
                },
                yAxis : {
                    axisLabel: 'Power (kW)',
                    tickFormat: function (d) {
                        return Math.round(Math.abs(d));
                    },
                    tickValues: vm.tickValues
                },
                zoom :  {
                    enabled: true,
                    scaleExtent: [1, 10],
                    useFixedDomain: false,
                    useNiceScale: false,
                    horizontalOff: false,
                    verticalOff: true,
                    unzoomEventType: 'dblclick.zoom'
                }
                }};

        var chartLineC = {
             chart : {
                type: 'stackedAreaChart',
                height: 450,
                margin: {
                    top: 20,
                    right: 20,
                    bottom: 30,
                    left: 70
                },
                x : function (d) {return new Date(d.instant);},
                y : function (d) {
                    if(d.measure<0.000001){
                        return 0.0;
                        }else{
                        return d.measure;
                        }
                },
                useVoronoi : true,
                clipEdge : true,
                duration : 300,
                showControls: false,
                showLegend: false,
                wrapLabels: true,
                useInteractiveGuideline : true,
                interactiveLayer:{
                      tooltip: {
                      contentGenerator: tooltipCustomContent
                  }},
                xAxis : {
                    axisLabelDistance: -8,
                    showMaxMin: false,
                    xScale: d3.time.scale(),
                    tickFormat: function (d) {
                         return d3.time.format(vm.graphTime.format)(new Date(d));
                    }
                },
                yAxis : {
                    axisLabel: 'Power (kW)',
                    tickValues: vm.tickValues,
                    tickFormat: function (d) {
                        return Math.round(Math.abs(d)) ;
                    }
                },
                zoom :  {
                    enabled: true,
                    scaleExtent: [1, 1],
                    useFixedDomain: false,
                    useNiceScale: false,
                    horizontalOff: false,
                    verticalOff: true,
                    unzoomEventType: 'dblclick.zoom'
                }
                }};

        var chartPie = {
            chart: {
                type: 'pieChart',
                height: 330,
                width: 330,
                donut: true,
                x: function(d){return d.key;},
                y: function(d){return d.value;},
                valueFormat: function(d) {
                   return Math.floor(Math.abs(d)) + ' €';
                   },
                showLabels: true,
                showLegend: true,
                transitionDuration: 300,
                tooltips: true,
                tooltip: {
                    valueFormatter: function(d) {
                 return Math.floor(Math.abs(d))  + ' €'; }
                },
                labelType: 'value',
                labelThreshold: .05,
                donutRatio: 0.4,
                color: ['olivedrab', 'crimson', 'teal','salmon', 'aqua'],
                wrapLabels: true
            }
        };

        var chartBar = {
          chart: {
               type: 'multiBarChart',
               height: 450,
               margin: {
                   top: 20,
                   right: 20,
                   bottom: 30,
                   left: 40
               },
               x: function(d){return new Date(d.instant);},
               y: function(d){return d.measure;},
               showValues: true,
               showLegend: false,
               clipEdge: true,
               stacked: true,
               showControls: false,
               xAxis : {
                   showMaxMin: false,
                   xScale: d3.time.scale(),
                   tickFormat: function (d) {
                       return d3.time.format("%d/%m/%y %H:%M")(new Date(d));
                   }
               },
               yAxis : {
                   tickFormat: function (d) {
                       return Math.round(Math.abs(d)) ;
                   }
               },
               useInteractiveGuideline : true,
               zoom :  {
                 enabled: true,
                 scaleExtent: [1, 20],
                 useFixedDomain: false,
                 useNiceScale: false,
                 horizontalOff: true,
                 verticalOff: true,
                 unzoomEventType: 'dblclick.zoom'
              },
              noData: "Loading data ..."
             }
        };

        function init() {
            vm.qrange = vm.quickranges[3];
            vm.graphTime = vm.stepGraphTime[0];
            setDates();
            allTopologies();
            loadTopology();
            monitoring();
            makeTicks();
        }

        function makeTicks(){
           var i;
           for (i = 0; i < 1000; i++) {
            vm.tickValues.push(20*i);
           }

        }

        function allTopologies() {
            FluxTopology.query(function (result) {
                vm.fluxTopologies = result;
            });
        }

        function loadTopology() {
            vm.IsVisible = false;
            vm.elementsConsumer = [];
            vm.elementsBattery = [];
            vm.elementsProducer = [];
            var elements = [];
            var eConsumer = 0;
            var eBattery = 0;
            var eProducer = 0;

            vm.energyElements = vm.currentFluxTopology.allElementsChildren;
            vm.energyElements.forEach(e => {
                vm.checkElementArray.push(e.id);
                vm.checkedElement[e.id] = true;
                switch(e.type){
                case("CONSUMER"):
                    vm.elementsConsumer.push(e);
                    eConsumer++;
                    break;
                case("BATTERY"):
                    vm.elementsBattery.push(e);
                    eBattery++;
                    break;
                case("PRODUCER"):
                    vm.elementsProducer.push(e);
                    eProducer++;
                    break;
            }
        });
        }

        function optimization(id, dtstart, dtend, step) {
            Monitoring.optimization({id: id, start: dtstart, end: dtend, step: step}, function (listOfBalanceDatas) {
                vm.listOfBalanceDatas = listOfBalanceDatas;
            });
        }

        function monitoring() {
            var params = {
                id: vm.currentFluxTopology.id,
                start: vm.startDate,
                end: vm.endDate,
                step: 600,
            }
            Monitoring.monitoring(params, function (listOfBalanceDatas) {
                vm.listOfBalanceDatas = listOfBalanceDatas; //stateNodes
                vm.auxListBalanceData = listOfBalanceDatas.slice(); //copy
                vm.stateTopology = vm.auxListBalanceData[0];
                d3.selectAll('.nvtooltip').remove();
                dataToNVD3();  //Data for graphics
                params = {
                    id: vm.currentFluxTopology.id
                }
            });
        }

        function updateGraphs(){
          var diffD = moment(vm.endDate).diff(vm.startDate,'days');
          var diffM = moment(vm.endDate).diff(vm.startDate,'month');
          vm.graphicPie = chartPie;

           if(diffM>0 && diffM<13){
                vm.graphTime = vm.stepGraphTime[1];
           }else if(diffM<1){
                vm.graphTime = vm.stepGraphTime[0];
            }else{
                vm.graphTime = vm.stepGraphTime[2];
                }

          if(diffM>12){
             makeAChartBar();
                }else{
                    makeAChartLine()
                }
         }

        function makeAChartBar(){
            vm.graphicConsBySource = chartBar;
            vm.graphicConsByElement = chartBar;
            vm.graphicProdBySource = chartBar;
            vm.graphicProdByElement = chartBar;
        }

        function makeAChartLine(){
            vm.graphicConsBySource = chartLineC;
            vm.graphicConsBySource.chart.xAxis.axisLabel = vm.graphTime.model;

            vm.graphicConsByElement = chartLineEC;
            vm.graphicConsByElement.chart.xAxis.axisLabel = vm.graphTime.model;

            if (vm.tonvd3ConsommationByElement.length == 0){
                  vm.graphicConsBySource.chart.noData = "No Element Selected";
                  vm.graphicPie.chart.noData = "No Element Selected";
                  vm.graphicConsByElement.chart.noData = "No Element Selected";
              }else{
                  vm.graphicPie.chart.noData = "Loading data";
                  vm.graphicConsByElement.chart.noData = "Loading data ...";
                  vm.graphicConsBySource.chart.noData = "Loading data ...";
              }

            vm.graphicProdBySource = chartLineP;
            vm.graphicProdBySource.chart.xAxis.axisLabel = vm.graphTime.model;
            vm.graphicProdByElement = chartLineEP;
            vm.graphicProdByElement.chart.xAxis.axisLabel = vm.graphTime.model;

            if (vm.tonvd3ProductionByElement.length == 0){
                 vm.graphicProdBySource.chart.noData = "No Element Selected"
                 vm.graphicProdByElement.chart.noData = "No Element Selected"
            }else{
                 vm.graphicProdByElement.chart.noData = "Loading data ...";
                 vm.graphicProdBySource.chart.noData = "Loading data ...";
            }
      }

        function tooltipCustomContent(d){
        var html = "<table><thead><tr><th colspan='2'><b>" + d3.time.format(vm.graphTime.format)(new Date(d.value)) + "</b></th></tr></thead><tbody>";
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

        function limitDecimals(value){
        if(typeof (value) != "undefined"){
            if (value>=0 && value <10){
                    return value.toFixed(1);
                }else{
                    return value.toFixed(0);
                }
             }
             return 0;
        }
        /**
         * Construction des objets pour le graphique
         * @param listData
         * @returns {Array}
         */
        function dataToNVD3(){
            vm.tonvd3ConsommationBySource = [];
            vm.tonvd3ConsommationByElement = [];
            vm.tonvd3ProductionBySource = [];
            vm.tonvd3ProductionByElement = [];
            vm.tonvd3Pie = [];
            vm.stateTopology.priceConsTotal = 0;
//cons
            vm.auxListBalanceData.forEach(r => {
                   if(r.type == "CONSUMER"){
                       vm.tonvd3ConsommationByElement.push(
                       {key: r.name, values: r.pConsoGlobal, color: setColor("CONSUMER"), area:true});
                    }
           });

           if(vm.tonvd3ConsommationByElement.length>0){
                 if(vm.stateTopology.pConsoFromProd.length>0){
                    vm.tonvd3ConsommationBySource.push(
                        {key: "Local Production", values: vm.stateTopology.pConsoFromProd, color: '#3CE550', area: true}
                    );
                 }

                 if(vm.stateTopology.pConsoFromBat.length>0){
                     vm.tonvd3ConsommationBySource.push(
                         {key: "Local Storage", values: vm.stateTopology.pConsoFromBat, color: '#AA86E3', area: true}
                     );
                  }

                if(vm.stateTopology.pConsoFromGrid.length>0){
                    vm.tonvd3ConsommationBySource.push(
                        {key: "Grid", values: vm.stateTopology.pConsoFromGrid, color: '#FAF476', area: true}
                    );
                 }

                vm.tonvd3Pie.push({key: "Off-Peak", value: vm.stateTopology.priceCons.HC},
                     {key: "Peak", value: vm.stateTopology.priceCons.HP}
                 );

                 vm.stateTopology.priceConsTotal = vm.stateTopology.priceCons.HC + vm.stateTopology.priceCons.HP;

            }

    //prod
            vm.auxListBalanceData.forEach(r => {
                        if(r.type == "PRODUCER"){
                          vm.tonvd3ProductionByElement.push(
                          {key: r.name, values: r.pProdGlobal, color: setColor("PRODUCER"), area:true});
                       }
            });

            if( vm.tonvd3ProductionByElement.length>0){
                if(vm.stateTopology.pProdConsByConsumers.length>0){
                    vm.tonvd3ProductionBySource.push(
                        {key: "Consumed", values: vm.stateTopology.pProdConsByConsumers, color: '#3CE550', area: true}
                    );
                }
                 if(vm.stateTopology.pProdConsByBat.length>0 ){
                    vm.tonvd3ProductionBySource.push(
                        {key: "Stored", values: vm.stateTopology.pProdConsByBat, color: '#C2A0F6', area: true}
                    );
                 }
                if(vm.stateTopology.pProdSentToGrid.length>0){
                    vm.tonvd3ProductionBySource.push(
                        {key: "Sent to Grid", values: vm.stateTopology.pProdSentToGrid, color: '#396EF3', area: true}
                     );
                }
            }

            vm.apiProdBySource.refresh();
            vm.apiProdByElement.refresh();
            vm.apiConsBySource.refresh();
            vm.apiConsByElement.refresh();
            updateGraphs();
        }

        function setColor(color){
            if(color=="PRODUCER"){
                return 'rgb(98,'+ Math.floor(Math.random() * 255)+',243)';
            }else{
                return 'rgb(243,'+ Math.floor(Math.random() * 255)+',153)';
            }
        }

        function checkElement(id, active){
            if (active){
                vm.checkElementArray.push(id);
                vm.checkedElement[id]=true;
                monitoringTopologyTemporel(id, 2); //add
               } else{
                    vm.checkElementArray.splice(vm.checkElementArray.indexOf(id), 1);
                    vm.checkedElement[id]=false;
                    vm.allChecked = false;
                    monitoringTopologyTemporel(id, 1); //sub
                 }

            if(vm.checkElementArray.length == 0){
                vm.checkAll = false;
                vm.auxListBalanceData = [];
            }
        }

        /*
         call service monitoring custom topology
        */
        function monitoringTopologyTemporel(id, op){
            /* Take from data of Topology the data of element*/
            vm.listOfBalanceDatas.forEach(r => {
                  if(r.id == id){
                    var i ;
                    for(i = 0; i< vm.stateTopology.pConsoFromProd.length;i++){
                        vm.stateTopology.pConsoFromProd[i].measure =
                        (vm.allChecked? 0. : vm.stateTopology.pConsoFromProd[i].measure) +
                         (Math.pow(-1, op) * r.pConsoFromProd[i].measure) ;

                         vm.stateTopology.pConsoFromBat[i].measure =
                         (vm.allChecked? 0. : vm.stateTopology.pConsoFromBat[i].measure) +
                         (Math.pow(-1, op) * r.pConsoFromBat[i].measure) ;

                         vm.stateTopology.pConsoFromGrid[i].measure =
                         (vm.allChecked? 0. :vm.stateTopology.pConsoFromGrid[i].measure) +
                        (Math.pow(-1, op) * r.pConsoFromGrid[i].measure) ;

                         vm.stateTopology.pProdConsByConsumers[i].measure =
                         (vm.allChecked? 0. :vm.stateTopology.pProdConsByConsumers[i].measure) +
                         (Math.pow(-1, op) * r.pProdConsByConsumers[i].measure) ;

                         vm.stateTopology.pProdConsByBat[i].measure =
                         (vm.allChecked? 0. :vm.stateTopology.pProdConsByBat[i].measure) +
                         (Math.pow(-1, op) * r.pProdConsByBat[i].measure) ;

                         vm.stateTopology.pProdSentToGrid[i].measure =
                         (vm.allChecked? 0. :vm.stateTopology.pProdSentToGrid[i].measure) +
                        (Math.pow(-1, op) * r.pProdSentToGrid[i].measure);
                    }

                   if(r.type == "CONSUMER"){
                        vm.stateTopology.priceCons.HC =
                        (vm.allChecked? 0. :vm.stateTopology.priceCons.HC) +
                        (Math.pow(-1, op) * r.priceCons.HC);

                        vm.stateTopology.priceCons.HP =
                        (vm.allChecked? 0. :vm.stateTopology.priceCons.HP) +
                        (Math.pow(-1, op) * r.priceCons.HP);

                        vm.stateTopology.priceCons.EnergyTotal =
                         (vm.allChecked? 0. :vm.stateTopology.priceCons.EnergyTotal) +
                        (Math.pow(-1, op) * r.priceCons.EnergyTotal);

                        vm.stateTopology.priceCons.EnergyGrid =
                         (vm.allChecked? 0. :vm.stateTopology.priceCons.EnergyGrid)  +
                        (Math.pow(-1, op) * r.priceCons.EnergyGrid );

                          vm.stateTopology.priceCons.SelfCons=
                        (vm.allChecked? 0. :vm.stateTopology.priceCons.SelfCons) +
                        (Math.pow(-1, op) * r.priceCons.SelfCons );

                    }else if(r.type == "PRODUCER"){
                        vm.stateTopology.priceProdSell.EnergyTotal =
                        (vm.allChecked? 0. :vm.stateTopology.priceProdSell.EnergyTotal) +
                        (Math.pow(-1, op) * r.priceProdSell.EnergyTotal );

                        vm.stateTopology.priceProdSell.Cost =
                        (vm.allChecked? 0. :vm.stateTopology.priceProdSell.Cost) +
                        (Math.pow(-1, op) * r.priceProdSell.Cost );

                        vm.stateTopology.priceProdSell.EnergySell =
                        (vm.allChecked? 0. :vm.stateTopology.priceProdSell.EnergySell) +
                        (Math.pow(-1, op) * r.priceProdSell.EnergySell );

                         vm.stateTopology.priceProdSell.SelfProd =
                        (vm.allChecked? 0. :vm.stateTopology.priceProdSell.SelfProd) +
                        (Math.pow(-1, op) * r.priceProdSell.SelfProd );
                    }

                   if(op==2){ //add
                     vm.auxListBalanceData.push(r);
                    }else{
                    vm.auxListBalanceData.splice(vm.auxListBalanceData.indexOf(r), 1);
                    }
                 }
            });

            if(!vm.allChecked){
            dataToNVD3();}
        }

        /*
        Clear graphics every click in submit or with a change in elements checked
        */
        function clearGraphics(){
            vm.tonvd3ConsommationByElement = [];
            vm.tonvd3ConsommationBySource = [];
            vm.tonvd3ProductionBySource = [];
            vm.tonvd3ProductionByElement = [];
        }

        /*
        If all elements are checked by cell checkAll call monitoring function global
        */
        function checkAllElements(allChecked){
        if (allChecked){
            vm.checkElementArray = [];
            vm.auxListBalanceData = [];
            vm.allChecked = true;
            vm.energyElements.forEach(e => {
                    vm.checkElementArray.push(e.id);
                    vm.checkedElement[e.id] = true;
                    monitoringTopologyTemporel(e.id, 2);
                    vm.allChecked = false;
                    });
            dataToNVD3();
           }else {
                vm.checkAll = false;
                vm.checkElementArray = [];
                vm.energyElements.forEach(e => {
                    vm.checkedElement[e.id] = false ;
                    });
                vm.tonvd3ConsommationBySource = [];
                vm.tonvd3ConsommationByElement = [];
                vm.tonvd3ProductionBySource = [];
                vm.tonvd3ProductionByElement = [];
                vm.tonvd3Pie = [];
                vm.stateTopology.priceConsTotal = 0;
                vm.stateTopology.priceCons.HC = 0;
                vm.stateTopology.priceCons.HP = 0;
                vm.stateTopology.priceCons.EnergyGrid = 0;
                vm.stateTopology.priceCons.EnergyTotal = 0;
                vm.stateTopology.priceCons.SelfCons = 0;
                vm.stateTopology.priceProdSell.EnergySell = 0;
                vm.stateTopology.priceProdSell.Cost = 0;
                vm.stateTopology.priceProdSell.EnergyTotal = 0;
                vm.stateTopology.priceProdSell.SelfProd = 0;
                vm.apiProdBySource.refresh();
                vm.apiProdByElement.refresh();
                vm.apiConsBySource.refresh();
                vm.apiConsByElement.refresh();
                updateGraphs();
                }
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MonitoringSearch.query({query: vm.searchQuery}, function (result) {
                vm.monitoring = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }

        /**
         * Ouverture du datepicker date de debut
         */
        function openStartDate() {
            vm.popupStart.opened = true;
        }

        /**
         * Ouverture du datepicker date de fin
         */
        function openEndDate() {
            vm.popupEnd.opened = true;
        }

        /**
         * Intialise les dates a partir du modele de date selectionne
         */
        function setDates() {
           switch(vm.qrange.id){
            case 1:
                vm.startDate = moment(moment().date(),"DD").toDate();
                vm.startTime = moment(0, "HH").toDate();
                break;
            case 2:
                vm.startDate = moment(moment().weeks(),"WW").toDate();
                vm.startTime = moment(0, "HH").toDate();
                break;
            case 3:
                vm.startDate = moment(moment().month()+1,"MM").toDate();
                vm.startTime = moment(0, "HH").toDate();
                break;
            case 4:
                vm.startDate = moment(moment().year(),"YYYY").toDate();
                vm.startTime = moment(0, "HH").toDate();
                break;
             default:
                 vm.startDate = moment().subtract(vm.qrange.value, vm.qrange.momentUnit).toDate();
                 vm.startTime = moment().subtract(vm.qrange.value, vm.qrange.momentUnit).toDate();
                 break;
            }
            vm.endDate = new Date();
            vm.endTime = new Date();
        }
                /**
         * update time start
         */
        function startTimeChanged(){
        vm.qrange = vm.quickranges[0]
        if(vm.startDate && vm.startTime){
            vm.startDate.setHours(vm.startTime.getHours());
            vm.startDate.setMinutes(vm.startTime.getMinutes());
            }
        }

         /**
         * update time end
         */
        function endTimeChanged(){
        vm.qrange = vm.quickranges[0]
        if(vm.endDate && vm.endTime){
            vm.endDate.setHours(vm.endTime.getHours());
            vm.endDate.setMinutes(vm.endTime.getMinutes());
            }
        }

        /**
         * Retour en arriere
         */
        function goBack() {
            $window.history.back();
        }
    }
})();
