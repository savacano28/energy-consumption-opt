(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('MeasurementSourceSearch', MeasurementSourceSearch);

    MeasurementSourceSearch.$inject = ['$resource'];

    function MeasurementSourceSearch($resource) {
        var resourceUrl =  'api/_search/measurement-sources/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
