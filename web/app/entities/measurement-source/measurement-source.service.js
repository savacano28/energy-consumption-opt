(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('MeasurementSource', MeasurementSource);

    MeasurementSource.$inject = ['$resource'];

    function MeasurementSource ($resource) {
        var resourceUrl =  'api/measurement-source/:id';

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
