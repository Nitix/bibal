(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('OeuvreDeleteController',OeuvreDeleteController);

    OeuvreDeleteController.$inject = ['$uibModalInstance', 'entity', 'Oeuvre'];

    function OeuvreDeleteController($uibModalInstance, entity, Oeuvre) {
        var vm = this;

        vm.oeuvre = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Oeuvre.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
