(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('UsagerDetailController', UsagerDetailController);

    UsagerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Usager'];

    function UsagerDetailController($scope, $rootScope, $stateParams, previousState, entity, Usager) {
        var vm = this;

        vm.usager = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bibalApp:usagerUpdate', function(event, result) {
            vm.usager = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
