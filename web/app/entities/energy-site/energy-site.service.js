(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('EnergySite', EnergySite);

    EnergySite.$inject = ['$resource'];

    function EnergySite ($resource) {
        var resourceUrl =  'api/energy-sites/:id';

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
