(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('SourceDescriptor', SourceDescriptor);

    SourceDescriptor.$inject = ['$resource'];

    function SourceDescriptor ($resource) {
        var resourceUrl =  'api/source-descriptor/:id';

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
