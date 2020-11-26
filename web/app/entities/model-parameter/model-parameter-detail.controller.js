(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('ModelParameterDetailController', ModelParameterDetailController);

    ModelParameterDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ModelParameter', 'AbstractSource'];

    function ModelParameterDetailController($scope, $rootScope, $stateParams, previousState, entity, ModelParameter, AbstractSource) {
        var vm = this;

        vm.modelParameter = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('synergreenApp:modelParameterUpdate', function(event, result) {
            vm.modelParameter = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
