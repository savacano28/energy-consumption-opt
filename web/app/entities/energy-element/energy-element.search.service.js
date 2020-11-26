(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('EnergyElementSearch', EnergyElementSearch);

    EnergyElementSearch.$inject = ['$resource'];

    function EnergyElementSearch($resource) {
        var resourceUrl =  'api/_search/energy-elements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
