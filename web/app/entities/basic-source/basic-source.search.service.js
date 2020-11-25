(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('BasicSourceSearch', BasicSourceSearch);

    BasicSourceSearch.$inject = ['$resource'];

    function BasicSourceSearch($resource) {
        var resourceUrl =  'api/_search/basic-sources/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
