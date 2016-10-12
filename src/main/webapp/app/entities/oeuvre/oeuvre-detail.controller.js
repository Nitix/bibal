(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('OeuvreDetailController', OeuvreDetailController);

    OeuvreDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Oeuvre', 'Exemplaire', 'Auteur', 'Reservation'];

    function OeuvreDetailController($scope, $rootScope, $stateParams, previousState, entity, Oeuvre, Exemplaire, Auteur, Reservation) {
        var vm = this;

        vm.oeuvre = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bibalApp:oeuvreUpdate', function(event, result) {
            vm.oeuvre = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
