(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('FluxTopology', FluxTopology);

    FluxTopology.$inject = ['$resource'];

    function FluxTopology ($resource) {
        var resourceUrl =  'api/flux-topologies/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'getAllReal': { url: "api/flux-topologies/real",
                            method: 'GET',
                            isArray: true
            },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'getBasic': {
                url: "api/flux-topologies/basic/:id",
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': {
                method:'PUT'
             },
             'addGroup': {
                url: resourceUrl + "addGroup",
                method: 'PUT'
             }
        });
    }
})();
