(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('BatteryModelSource', BatteryModelSource);

    BatteryModelSource.$inject = ['$resource'];

    function BatteryModelSource ($resource) {
        var resourceUrl =  'api/battery-model-sources/:id';

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
