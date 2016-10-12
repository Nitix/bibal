(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('OeuvreController', OeuvreController);

    OeuvreController.$inject = ['$scope', '$state', 'Oeuvre'];

    function OeuvreController ($scope, $state, Oeuvre) {
        var vm = this;
        
        vm.oeuvres = [];

        loadAll();

        function loadAll() {
            Oeuvre.query(function(result) {
                vm.oeuvres = result;
            });
        }
    }
})();
