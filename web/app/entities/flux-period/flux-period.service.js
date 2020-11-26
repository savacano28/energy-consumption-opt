(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('FluxPeriod', FluxPeriod);

    FluxPeriod.$inject = ['$resource', 'DateUtils'];

    function FluxPeriod ($resource, DateUtils) {
        var resourceUrl =  'api/flux-periods/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.start = DateUtils.convertDateTimeFromServer(data.start);
                        data.end = DateUtils.convertDateTimeFromServer(data.end);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
