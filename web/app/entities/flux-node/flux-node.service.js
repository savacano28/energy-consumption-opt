(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('FluxNode', FluxNode);

    FluxNode.$inject = ['$resource'];

    function FluxNode ($resource) {
        var resourceUrl =  'api/flux-nodes/:id';

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
