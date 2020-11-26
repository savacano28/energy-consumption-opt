(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('MonthsHighSeason', MonthsHighSeason);

    MonthsHighSeason.$inject = ['$resource'];

    function MonthsHighSeason ($resource) {
        var resourceUrl =  'api/months-high-season/:id';

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
