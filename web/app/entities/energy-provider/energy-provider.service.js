(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('EnergyProvider', EnergyProvider);

    EnergyProvider.$inject = ['$resource'];

    function EnergyProvider ($resource) {
        var resourceUrl =  'api/energy-providers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
