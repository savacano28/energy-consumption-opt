(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('BasicSource', BasicSource);

    BasicSource.$inject = ['$resource'];

    function BasicSource ($resource) {
        var resourceUrl =  'api/basic-sources/:id';

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
