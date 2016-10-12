(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('ExemplaireDetailController', ExemplaireDetailController);

    ExemplaireDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Exemplaire', 'Oeuvre'];

    function ExemplaireDetailController($scope, $rootScope, $stateParams, previousState, entity, Exemplaire, Oeuvre) {
        var vm = this;

        vm.exemplaire = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bibalApp:exemplaireUpdate', function(event, result) {
            vm.exemplaire = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
