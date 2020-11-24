(function() {
    'use strict';

    angular
        .module('synergreenApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'EnergySite', '$timeout'];

    function HomeController ($scope, Principal, LoginService, $state, EnergySite, $timeout) {
        var vm = this;
        vm.energySites = [];
        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.groupsSite = {};
        vm.optimizationsSite = {};
        vm.topologyRealBySite = {};

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        init();


        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function init(){
            getAccount();
            loadAllSites();
         }

        function loadAllSites() {
            EnergySite.query(function(result) {
                vm.energySites = result;
                vm.searchQuery = null;
                loadAllGroupsBySite();
            });
        }

        function loadAllGroupsBySite() {
            var groups = [];
            var optimizations = [];
            vm.energySites.forEach(
                    s => {
                        groups = [];
                        optimizations = [];
                        s.fluxTopologies.forEach(t=>{
                                                   groups.push(t.fluxGroups);
                                                    optimizations.push(t.optimizations);
                                                    if(!t.simulation){
                                                            vm.topologyRealBySite[s.id] = t.id ;
                                                                }
                                                    });
                        vm.groups = [].concat.apply([], groups);
                        vm.optimizations = [].concat.apply([], optimizations);
                        vm.groupsSite[s.id] = vm.groups;                //toutes les groups liees a ce site
                        vm.optimizationsSite[s.id] = vm.optimizations;  //Les optimizations liees a ce topology
                    }
            );
        }

        function register () {
            $state.go('register');
        }

        $timeout(function() {
                 vm.loginAlertMessage = true;
              }, 500);
    }
})();
