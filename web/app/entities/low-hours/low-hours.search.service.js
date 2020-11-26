(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('LowHoursSearch', LowHoursSearch);

    LowHoursSearch.$inject = ['$resource'];

    function LowHoursSearch($resource) {
        var resourceUrl =  'api/_search/low-hours/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
