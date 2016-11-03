(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('EmpruntController', EmpruntController);

    EmpruntController.$inject = ['$scope', '$state', 'Emprunt'];

    function EmpruntController ($scope, $state, Emprunt) {
        var vm = this;

        vm.emprunts = [];
        loadAll();

        function loadAll() {
            Emprunt.query(function(result) {
                vm.emprunts = result;
            });
        }

        $scope.quickReturn = function quickReturn(id) {
            console.log(id);
            console.log(vm.emprunts["0"].dateEmprunt);
            var emprunt = vm.emprunts[id];
            emprunt.dateRetour = new Date();
            Emprunt.update(emprunt);
        }

    }
})();
