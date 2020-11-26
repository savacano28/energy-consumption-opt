(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('EnergyProviderSearch', EnergyProviderSearch);

    EnergyProviderSearch.$inject = ['$resource'];

    function EnergyProviderSearch($resource) {
        var resourceUrl =  'api/_search/energy-providers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
