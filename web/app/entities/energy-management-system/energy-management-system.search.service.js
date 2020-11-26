(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('EnergyManagementSystemSearch', EnergyManagementSystemSearch);

    EnergyManagementSystemSearch.$inject = ['$resource'];

    function EnergyManagementSystemSearch($resource) {
        var resourceUrl =  'api/_search/energy-management-systems/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
