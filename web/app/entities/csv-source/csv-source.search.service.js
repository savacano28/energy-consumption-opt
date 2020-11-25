(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('CSVSourceSearch', CSVSourceSearch);

    CSVSourceSearch.$inject = ['$resource'];

    function CSVSourceSearch($resource) {
        var resourceUrl =  'api/_search/csv-sources/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
