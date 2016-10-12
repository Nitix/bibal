(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('AuteurDetailController', AuteurDetailController);

    AuteurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Auteur'];

    function AuteurDetailController($scope, $rootScope, $stateParams, previousState, entity, Auteur) {
        var vm = this;

        vm.auteur = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bibalApp:auteurUpdate', function(event, result) {
            vm.auteur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
