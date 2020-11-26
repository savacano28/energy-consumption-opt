(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('EnergySiteSearch', EnergySiteSearch);

    EnergySiteSearch.$inject = ['$resource'];

    function EnergySiteSearch($resource) {
        var resourceUrl =  'api/_search/energy-sites/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
