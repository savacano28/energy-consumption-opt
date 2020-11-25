(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('CSVSource', CSVSource);

    CSVSource.$inject = ['$resource'];

    function CSVSource ($resource) {
        var resourceUrl =  'api/csv-sources/:id';

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
