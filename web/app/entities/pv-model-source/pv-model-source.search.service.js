(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('PVModelSourceSearch', PVModelSourceSearch);

    PVModelSourceSearch.$inject = ['$resource'];

    function PVModelSourceSearch($resource) {
        var resourceUrl =  'api/_search/pv-model-sources/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
