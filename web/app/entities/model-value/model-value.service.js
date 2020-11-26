(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('ModelValue', ModelValue);

    ModelValue.$inject = ['$resource'];

    function ModelValue ($resource) {
        var resourceUrl =  'api/model-values/:id';

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
