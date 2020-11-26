(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('ModelParameterSearch', ModelParameterSearch);

    ModelParameterSearch.$inject = ['$resource'];

    function ModelParameterSearch($resource) {
        var resourceUrl =  'api/_search/model-parameters/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
