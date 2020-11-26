(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('LowHours', LowHours);

    LowHours.$inject = ['$resource'];

    function LowHours ($resource) {
        var resourceUrl =  'api/low-hours/:id';

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
