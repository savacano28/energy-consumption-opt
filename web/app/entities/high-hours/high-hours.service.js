(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('HighHours', HighHours);

    HighHours.$inject = ['$resource'];

    function HighHours ($resource) {
        var resourceUrl =  'api/high-hours/:id';

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
