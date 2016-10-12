(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('ExemplaireDeleteController',ExemplaireDeleteController);

    ExemplaireDeleteController.$inject = ['$uibModalInstance', 'entity', 'Exemplaire'];

    function ExemplaireDeleteController($uibModalInstance, entity, Exemplaire) {
        var vm = this;

        vm.exemplaire = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Exemplaire.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
