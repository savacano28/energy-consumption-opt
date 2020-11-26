(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('ModelValueDetailController', ModelValueDetailController);

    ModelValueDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ModelValue', 'AbstractSource'];

    function ModelValueDetailController($scope, $rootScope, $stateParams, previousState, entity, ModelValue, AbstractSource) {
        var vm = this;

        vm.modelValue = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('synergreenApp:modelValueUpdate', function(event, result) {
            vm.modelValue = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
