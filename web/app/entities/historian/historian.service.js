(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('Historian', Historian);

    Historian.$inject = ['$resource'];

    function Historian ($resource) {
        var resourceUrl =  'api/historians/:id';

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
