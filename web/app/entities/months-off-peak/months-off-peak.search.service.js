(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('MonthsOffPeakSearch', MonthsOffPeakSearch);

    MonthsOffPeakSearch.$inject = ['$resource'];

    function MonthsOffPeakSearch($resource) {
        var resourceUrl =  'api/_search/months-off-peaks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
