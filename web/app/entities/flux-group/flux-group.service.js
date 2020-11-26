(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('FluxGroup', FluxGroup);

    FluxGroup.$inject = ['$resource'];

    function FluxGroup ($resource) {
        var resourceUrl =  'api/flux-nodes/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                url: 'api/flux-nodes/flux-groups',
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
