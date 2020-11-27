(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('PVModelSource', PVModelSource);

    PVModelSource.$inject = ['$resource'];

    function PVModelSource ($resource) {
        var resourceUrl =  'api/pv-model-sources/:id';

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
