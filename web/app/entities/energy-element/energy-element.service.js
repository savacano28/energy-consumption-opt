(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('EnergyElement', EnergyElement);

    EnergyElement.$inject = ['$resource'];

    function EnergyElement ($resource) {
        var resourceUrl =  'api/flux-nodes/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                    url: 'api/flux-nodes/energy-elements',
                    method: 'GET', isArray: true},
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
