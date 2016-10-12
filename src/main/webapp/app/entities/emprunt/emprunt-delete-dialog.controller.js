(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('EmpruntDeleteController',EmpruntDeleteController);

    EmpruntDeleteController.$inject = ['$uibModalInstance', 'entity', 'Emprunt'];

    function EmpruntDeleteController($uibModalInstance, entity, Emprunt) {
        var vm = this;

        vm.emprunt = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Emprunt.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
