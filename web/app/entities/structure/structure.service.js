(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('Structure', Structure);

    Structure.$inject = ['$resource'];

    function Structure ($resource) {
        var resourceUrl =  'api/structure/';

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
            'update': { method:'PUT' },
            'parametersStructure' : {
                url: resourceUrl + "parameters",
                method: 'GET',
                isArray: true
            }
        });
    }
})();
