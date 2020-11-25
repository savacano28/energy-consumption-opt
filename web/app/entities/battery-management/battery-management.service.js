(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('BatteryManagement', BatteryManagement);

    BatteryManagement.$inject = ['$resource'];

    function BatteryManagement ($resource) {
        var resourceUrl =  'api/energy-management-systems/';

         return $resource(resourceUrl, {}, {
            'pilotable' : {
                url: "api/flux-topologies/pilotable",
                method: 'GET'
            },
            'pilotables' : {
                url: "api/energy-sites/pilotables",
                method: 'GET',
                isArray: true
            },
            'statusBattery' : {
                url: "api/energy-management-systems/battery-management/getInstructions",
                method: 'GET',
                isArray: false
            },
             'batteryOnDemand' : {
                url: "api/energy-management-systems/battery-management/computeNewInstructions",
                method: 'GET',
                isArray: false
            }
        });
    }
})();
