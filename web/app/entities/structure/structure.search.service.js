(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('StructureSearch', StructureSearch);

    StructureSearch.$inject = ['$resource'];

    function StructureSearch($resource) {
        var resourceUrl =  'api/_search/structure/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
