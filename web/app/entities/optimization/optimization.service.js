(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('Optimization', Optimization);

    Optimization.$inject = ['$resource'];

    function Optimization ($resource) {
        var resourceUrl =  'api/energy-management-systems/';

        return $resource(resourceUrl, {}, {
            'optimization' : {
                url: resourceUrl + "optimization",
                method: 'GET',
                isArray: true
            },
            'optimizationTopologyTemporel': {
                url: resourceUrl + "optimizationTopologyTemporel",
                method: 'GET',
                isArray :true
            },
            'price': {
                url: resourceUrl + "pricing",
                method: 'GET',
                isArray :false
            }

        });
    }
})();
