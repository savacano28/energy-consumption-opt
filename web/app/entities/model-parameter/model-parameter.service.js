(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('ModelParameter', ModelParameter);

    ModelParameter.$inject = ['$resource'];

    function ModelParameter ($resource) {
        var resourceUrl =  'api/model-parameters/:id';

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
