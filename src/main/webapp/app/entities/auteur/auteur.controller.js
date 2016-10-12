(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('AuteurController', AuteurController);

    AuteurController.$inject = ['$scope', '$state', 'Auteur'];

    function AuteurController ($scope, $state, Auteur) {
        var vm = this;
        
        vm.auteurs = [];

        loadAll();

        function loadAll() {
            Auteur.query(function(result) {
                vm.auteurs = result;
            });
        }
    }
})();
