(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('MonthsHighSeasonSearch', MonthsHighSeasonSearch);

    MonthsHighSeasonSearch.$inject = ['$resource'];

    function MonthsHighSeasonSearch($resource) {
        var resourceUrl =  'api/_search/months-high-season/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
