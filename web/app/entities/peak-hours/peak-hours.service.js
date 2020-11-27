(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('PeakHours', PeakHours);

    PeakHours.$inject = ['$resource'];

    function PeakHours ($resource) {
        var resourceUrl =  'api/peak-hours/:id';

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
