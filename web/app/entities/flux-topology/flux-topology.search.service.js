(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('FluxTopologySearch', FluxTopologySearch);

    FluxTopologySearch.$inject = ['$resource'];

    function FluxTopologySearch($resource) {
        var resourceUrl =  'api/_search/flux-topologies/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
