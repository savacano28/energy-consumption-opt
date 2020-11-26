(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('ModelValueSearch', ModelValueSearch);

    ModelValueSearch.$inject = ['$resource'];

    function ModelValueSearch($resource) {
        var resourceUrl =  'api/_search/model-values/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
