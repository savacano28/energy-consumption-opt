(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('FluxGroupSearch', FluxGroupSearch);

    FluxGroupSearch.$inject = ['$resource'];

    function FluxGroupSearch($resource) {
        var resourceUrl =  'api/_search/flux-groups/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
