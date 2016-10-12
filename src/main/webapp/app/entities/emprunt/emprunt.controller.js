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
    }
})();
