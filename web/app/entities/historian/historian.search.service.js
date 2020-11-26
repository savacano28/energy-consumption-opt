(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('HistorianSearch', HistorianSearch);

    HistorianSearch.$inject = ['$resource'];

    function HistorianSearch($resource) {
        var resourceUrl =  'api/_search/historians/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
