(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('HighHoursSearch', HighHoursSearch);

    HighHoursSearch.$inject = ['$resource'];

    function HighHoursSearch($resource) {
        var resourceUrl =  'api/_search/high-hours/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
