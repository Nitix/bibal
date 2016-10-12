(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('UsagerController', UsagerController);

    UsagerController.$inject = ['$scope', '$state', 'Usager'];

    function UsagerController ($scope, $state, Usager) {
        var vm = this;
        
        vm.usagers = [];

        loadAll();

        function loadAll() {
            Usager.query(function(result) {
                vm.usagers = result;
            });
        }
    }
})();
