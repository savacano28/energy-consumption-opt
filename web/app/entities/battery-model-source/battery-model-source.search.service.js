(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .factory('BatteryModelSourceSearch', BatteryModelSourceSearch);

    BatteryModelSourceSearch.$inject = ['$resource'];

    function BatteryModelSourceSearch($resource) {
        var resourceUrl =  'api/_search/battery-model-sources/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
