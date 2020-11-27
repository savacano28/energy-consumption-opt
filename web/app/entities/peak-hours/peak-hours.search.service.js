(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('PeakHoursSearch', PeakHoursSearch);

    PeakHoursSearch.$inject = ['$resource'];

    function PeakHoursSearch($resource) {
        var resourceUrl =  'api/_search/peak-hours/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
