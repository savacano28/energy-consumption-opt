(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('MonthsOffPeak', MonthsOffPeak);

    MonthsOffPeak.$inject = ['$resource'];

    function MonthsOffPeak ($resource) {
        var resourceUrl =  'api/months-off-peaks/:id';

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
