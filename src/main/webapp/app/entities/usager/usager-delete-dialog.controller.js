(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('UsagerDeleteController',UsagerDeleteController);

    UsagerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Usager'];

    function UsagerDeleteController($uibModalInstance, entity, Usager) {
        var vm = this;

        vm.usager = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Usager.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
