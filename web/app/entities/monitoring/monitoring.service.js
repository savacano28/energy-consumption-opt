(function() {
    'use strict';
    angular
        .module('synergreenApp')
        .factory('Monitoring', Monitoring);

    Monitoring.$inject = ['$resource'];

    function Monitoring ($resource) {
        var resourceUrl =  'api/energy-management-systems/';

        return $resource(resourceUrl, {}, {
            'monitoring' : {
                url: resourceUrl + "monitoring",
                method: 'GET',
                isArray: true
            }
        });
    }
})();
