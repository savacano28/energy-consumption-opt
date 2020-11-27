(function () {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('OptimizationDetailController', OptimizationDetailController);

    OptimizationDetailController.$inject = ['$scope', '$rootScope', 'FluxTopology', 'FluxGroup', 'entity', 'Optimization', 'Monitoring', '$window', '$timeout'];

    function OptimizationDetailController($scope, $rootScope, FluxTopology, FluxGroup, entity, Optimization, Monitoring, $window, $timeout) {
        var vm = this;

        vm.updateStepSelected = updateStepSelected;
        vm.openStartDate = openStartDate;
        vm.openEndDate = openEndDate;
        vm.setDates = setDates;

         var unsubscribe = $rootScope.$on('synergreenApp:optimization-detail-update', function(event, result) {
                    vm.currentFluxTopology = result;
                });
                $scope.$on('$destroy', unsubscribe);

        /**
         * Liste des steps predefinis
         */
        vm.steps = [
            {model: "1 min", seconds: 60},
            {model: "5 min", seconds: 300},
            {model: "10 min", seconds: 600},
            {model: "15 min", seconds: 900},
            {model: "30 min", seconds: 1800},
            {model: "1h", seconds: 3600},
            {model: "2h", seconds: 7200},
            {model: "6h", seconds: 21600},
            {model: "12h", seconds: 43200},
            {model: "1 day", seconds: 86400},
            {model: "2 days", seconds: 172800},
            {model: "15 days", seconds: 1296000},
            {model: "1 month", seconds: 2592000}
        ];

        /**
         * Liste des selections rapides des dates
         */
        vm.quickranges = [
            {id: 0, model: "Example Range", value: 6, momentUnit: "M"},
            {id: 1, model: "Last Hour", value: 1, momentUnit: "h"},
            {id: 2, model: "Last Two Hours", type: "Hours", value: 2,  momentUnit: "h"},
            {id: 3, model: "Today", value: 1,  momentUnit: "d"},//
            {id: 4, model: "Last day", value: 1,  momentUnit: "d"},
            {id: 5, model: "This week", value: 1,  momentUnit: "w"},//
            {id: 6, model: "Last week", value: 1,  momentUnit: "w"},
            {id: 7, model: "This month", value: 1,  momentUnit: "M"},//
            {id: 8, model: "Last month", value: 1,  momentUnit: "M"},
            {id: 9, model: "Last six months", value: 6,  momentUnit: "M"},
            {id: 10, model: "This year", value: 1,  momentUnit: "y"}, //
            {id: 11, model: "Last year", value: 1,  momentUnit: "y"}
        ];

        /**
         * Configuration pour les datepickers
         */
        vm.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };


        /**
         * Configuration du graphique stacked chart
         */
        vm.optimizationGraphicStacked = {
            chart: {
                type: 'stackedAreaChart',
                height: 450,
                margin: {
                    top: 20,
                    right: 20,
                    bottom: 30,
                    left: 40
                },
                x: function (d) {
                    return d[0];
                },
                y: function (d) {
                    return d[1];
                },
                useVoronoi: true,
                clipEdge: true,
                duration: 500,
                useInteractiveGuideline: true,

                xAxis: {
                    showMaxMin: false,
                    xScale: d3.time.scale(),
                    tickFormat: function (d) {
                        return d3.time.format("%d/%m/%y %H:%M")(new Date(d));
                    }
                },
                yAxis: {
                    tickFormat: function (d) {
                        return d3.format(',.2f')(d);
                    }
                },
                zoom: {
                    enabled: true,
                    scaleExtent: [1, 20],
                    useFixedDomain: false,
                    useNiceScale: true,
                    horizontalOff: true,
                    verticalOff: true,
                    unzoomEventType: 'dblclick.zoom'
                }
            }
        };

        /**
         * Configuration du graphique line chart
         */
        vm.optimizationGraphicLine = {
            chart: {
                type: 'lineChart',
                height: 450,
                margin: {
                    top: 20,
                    right: 20,
                    bottom: 30,
                    left: 40
                },
                x: function (d) {
                    return d[0];
                },
                y: function (d) {
                    return d[1];
                },
                useVoronoi: true,
                clipEdge: true,
                duration: 500,
                useInteractiveGuideline: true,

                xAxis: {
                    showMaxMin: false,
                    xScale: d3.time.scale(),
                    tickFormat: function (d) {
                        return d3.time.format("%d/%m/%y %H:%M")(new Date(d));
                    }
                },
                yAxis: {
                    tickFormat: function (d) {
                        return d3.format(',.2f')(d);
                    }
                },
                zoom: {
                    enabled: true,
                    scaleExtent: [1, 20],
                    useFixedDomain: false,
                    useNiceScale: false,
                    horizontalOff: true,
                    verticalOff: true,
                    unzoomEventType: 'dblclick.zoom'
                }
            }
        };

        /*
        * configuration du donut
        */
         vm.optimizationGraphicPie = {
                    chart: {
                        type: 'pieChart',
                        height: 300,
                        width: 300,
                        donut: true,
                        x: function(d){return d.key;},
                        y: function(d){return d.value;},
                        showLabels: true,
                        showLegend: true,
                        transitionDuration: 500,
                        tooltips: true,
                        labelType: 'percent',
                        labelThreshold: .05,
                        donutRatio: 0.4,
                        color: ['olivedrab', 'crimson', 'teal','salmon']
                    }
                };

        /*
        * configuration du bar chart
        */
         vm.optimizationGraphicBar = {
                  chart: {
                         type: 'discreteBarChart',
                         height: 300,
                         x: function(d){return d.key;},
                         y: function(d){return d.value;},
                         showValues: true,
                         staggerLabels:true ,
                         valueFormat: function(d){
                             return d3.format(',.2f')(d);
                         },
                         transitionDuration: 500,
                         color: ['#5F9EA0', '#FF8C00', '#FF00FF','salmon']
                     }
                };

        vm.checkAll = true;
        vm.currentFluxTopology = entity;
        vm.checkedElement = {};
        vm.msg = entity.name;
        vm.fluxTopologies = [];
        vm.energyElements = [];
        vm.elementsConsumer = [];
        vm.elementsSellProducer = [];
        vm.elementsAuto = [];
        vm.elementsBattery = [];
        vm.listOfBalanceDatas = [];
        vm.checkElementArray = [];

        vm.clear = clear;
        vm.optimization = optimization;
        vm.checkElement = checkElement;
        vm.checkAllElements = checkAllElements;
        vm.clearGraphics = clearGraphics;
        vm.historyBack = goBack;
        vm.startTimeChanged=startTimeChanged;
        vm.endTimeChanged=endTimeChanged;
        vm.eraseElement = eraseElement;
        vm.eraseGroup = eraseGroup;

        vm.popupStart = {
            opened: false
        };

        vm.popupEnd = {
            opened: false
        };

        init();

        function init() {
            vm.stepSelected = vm.steps[12]; //7200 seconds
            vm.qrange = vm.quickranges[0];
            setDates();
            allTopologies();
            loadTopology();
            optimization();
        }


        function updateStepSelected(index) {
            vm.stepSelected = vm.step[index];
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
            vm.elementsAuto = [];
            vm.elementsSellProducer = [];
            var elements = [];
            var eConsumer = 0;
            var eBattery = 0;
            var eSProducer = 0;
            var eAutoCons = 0;

            vm.currentFluxTopology.fluxGroups.forEach(g => elements.push(g.energyElements));
            vm.energyElements = [].concat.apply([], elements);


            vm.energyElements.forEach(e => {
                vm.checkElementArray.push(e.id);
                vm.checkedElement[e.id]=true;

                switch(e.type){
                case("CONSUMER"):
                    vm.elementsConsumer.push(e);
                    eConsumer++;
                    break;
                case("BATTERY"):
                    vm.elementsBattery.push(e);
                    eBattery++;
                    break;
                case("SELL_PRODUCER"):
                    vm.elementsSellProducer.push(e);
                    eSProducer++;
                    break;
                case("AUTOCONSUMPTION_PRODUCER"):
                    vm.elementsAuto.push(e);
                    eAutoCons++;
                    break;
            }
        });

        }

        function optimization(id, dtstart, dtend, step) {
            Optimization.optimization({id: id, start: dtstart, end: dtend, step: step}, function (listOfBalanceDatas) {
                vm.listOfBalanceDatas = listOfBalanceDatas;
            });
        }

        function optimization() {
            // vm.IsVisible =  true;
            var params = {
                id: vm.currentFluxTopology.id,
                start: vm.startDate,
                end: vm.endDate,
                step: vm.stepSelected.seconds
            }

            if(vm.checkAll){
            Optimization.optimization(params, function (listOfBalanceDatas) {
                vm.listOfBalanceDatas = listOfBalanceDatas;
                dataToNVD3(listOfBalanceDatas);

               /* Optimization.price(params, function (priceData) {
                 vm.priceData = priceData;
                 vm.tonvd3Bar = [];
                 vm.tonvd3Bar.push({key :"Prices",
                                    values :
                                    [{key: "Consumption", value: vm.priceData.priceConsumption},
                                    {key: "SellProduction", value: vm.priceData.priceSellProduction},
                                    {key: "AutoConsProduction", value: vm.priceData.priceAutoConsProduction}]}
                                   );
                            });*/
            });
            }else if(vm.checkElementArray.length>0){
                    optimizationTopologyTemporel();
            }
        }

        function checkElement(id, active){
            if (active){
               vm.checkElementArray.push(id);
               vm.checkedElement[id]=true;
              } else{
                   vm.checkElementArray.splice(vm.checkElementArray.indexOf(id), 1);
                   vm.checkedElement[id]=false;
                }

           if(vm.checkElementArray.length == 0){
               vm.checkAll = false;
           }else {
              optimizationTopologyTemporel();
           }
        }

        /*
         call service optimization custom topology
        */
        function optimizationTopologyTemporel(){
         var params = {
            id: vm.currentFluxTopology.id,
            elements: vm.checkElementArray,
            start: vm.startDate,
            end: vm.endDate,
            step: vm.stepSelected.seconds
         }
            Monitoring.monitoringTopologyTemporel(params, function (listOfBalanceDatas) {
                vm.listOfBalanceDatas = listOfBalanceDatas;
                dataToNVD3(listOfBalanceDatas);
            });
        }

        /*
        Clear graphics every click in submit or with a change in elements checked
        */
        function clearGraphics(){
            vm.tonvd3Stacked = [];
        }

        /*
        If all elements are checked call optimization function global
        */
        function checkAllElements(allChecked){
         if (allChecked){
                    vm.checkElementArray = [];
                    vm.energyElements.forEach(e => {
                            vm.checkElementArray.push(e.id);
                            vm.checkedElement[e.id]=true;
                            });
                    optimization();
                   }else {
                        vm.checkElementArray = [];
                        vm.energyElements.forEach(e => vm.checkedElement[e.id] = false );
                        }
                }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            OptimizationSearch.query({query: vm.searchQuery}, function (result) {
                vm.optimization = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }


        /**
         * Construction des objets pour le graphique
         * @param listData
         * @returns {Array}
         */
        function dataToNVD3(listData) {
            vm.cons_Optimization = [];
            vm.sellProd_Optimization = [];
            vm.autoConsProd_Optimization = [];
            vm.strg_Optimization = [];
            vm.tonvd3Stacked = [];
            vm.tonvd3Pie = [];
            vm.optimizationToNVD3 = {};
            var sumC=0, sumP=0, sumAC=0, sumS=0;

            listData.forEach(bd => {
                vm.cons_Optimization.push([new Date(bd.instant), bd.consumption]);
                sumC+=bd.consumption;
                vm.sellProd_Optimization.push([new Date(bd.instant), bd.sellProduction]);
                sumP+=bd.sellProduction;
                vm.autoConsProd_Optimization.push([new Date(bd.instant), bd.autoConsProduction]);
                sumAC+=bd.autoConsProduction;
                vm.strg_Optimization.push([new Date(bd.instant), bd.storage]);
                sumS+=bd.storage;
            });

            vm.optimizationToNVD3["Consumption"] = vm.cons_Optimization;
            vm.optimizationToNVD3["SellProduction"] = vm.sellProd_Optimization;
            vm.optimizationToNVD3["AutoConsProduction"] = vm.autoConsProd_Optimization;
            vm.optimizationToNVD3["Storage"] = vm.strg_Optimization;
            vm.tonvd3Stacked.push({key: "Consumption", values: vm.cons_Optimization},
                {key: "SellProduction", values: vm.sellProd_Optimization},
                {key: "AutoConsProduction", values: vm.autoConsProd_Optimization},
                {key: "Storage", values: vm.strg_Optimization}
            );

            vm.tonvd3Pie.push({key: "Consumption", value: sumC},
                                        {key: "SellProduction", value: sumP},
                                        {key: "AutoConsProduction", value: sumAC},
                                        {key: "Storage", value: sumS}
                                    );

            vm.stackedApi.refresh();

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
            case 3:
                vm.startDate = moment(moment().date(),"DD").toDate();
                vm.startTime = moment(0, "HH");
                break;
            case 5:
                vm.startDate = moment(moment().weeks(),"WW").toDate();
                vm.startTime = moment(0, "HH");
                break;
            case 7:
                vm.startDate = moment(moment().month()+1,"MM").toDate();
                vm.startTime = moment(0, "HH");
                break;
            case 10:
                vm.startDate = moment(moment().year(),"YYYY").toDate();
                vm.startTime = moment(0, "HH");
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
        vm.startDate.setHours(vm.startTime.getHours());
        vm.startDate.setMinutes(vm.startTime.getMinutes());
        }

         /**
         * update time end
         */
        function endTimeChanged(){
        vm.endDate.setHours(vm.endTime.getHours());
        vm.endDate.setMinutes(vm.endTime.getMinutes());
        }

        function eraseGroup(id, e){
         if (e) {
              e.preventDefault();
              e.stopPropagation();
            }
            vm.currentFluxTopology.fluxGroups = vm.currentFluxTopology.fluxGroups.filter(e => e.id != id);
            FluxTopology.update(vm.currentFluxTopology);
            clearGraphics()
            optimizationTopologyTemporel();
        }

        function eraseElement(idG, idE, e){
         if (e) {
              e.preventDefault();
              e.stopPropagation();
            }

            vm.currentFluxTopology.fluxGroups.forEach(e => {
                  if(e.id == idG){
                    e.energyElements = e.energyElements.filter(l=>l.id != idE);
                    }
                 FluxGroup.update(e);
          });
           FluxTopology.update(vm.currentFluxTopology);
           clearGraphics()
           optimizationTopologyTemporel();
        }

        /**
         * Retour en arriere
         */
        function goBack() {
            $window.history.back();
        }
    }
})();


