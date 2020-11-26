(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('FluxPeriodSearch', FluxPeriodSearch);

    FluxPeriodSearch.$inject = ['$resource'];

    function FluxPeriodSearch($resource) {
        var resourceUrl =  'api/_search/flux-periods/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
