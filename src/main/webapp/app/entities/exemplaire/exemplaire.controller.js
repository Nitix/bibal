(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('ExemplaireController', ExemplaireController);

    ExemplaireController.$inject = ['$scope', '$state', 'Exemplaire'];

    function ExemplaireController ($scope, $state, Exemplaire) {
        var vm = this;
        
        vm.exemplaires = [];

        loadAll();

        function loadAll() {
            Exemplaire.query(function(result) {
                vm.exemplaires = result;
            });
        }
    }
})();
