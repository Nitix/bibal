(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('ExemplaireDialogController', ExemplaireDialogController);

    ExemplaireDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Exemplaire', 'Oeuvre'];

    function ExemplaireDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Exemplaire, Oeuvre) {
        var vm = this;

        vm.exemplaire = entity;
        vm.clear = clear;
        vm.save = save;
        vm.oeuvres = Oeuvre.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.exemplaire.id !== null) {
                Exemplaire.update(vm.exemplaire, onSaveSuccess, onSaveError);
            } else {
                Exemplaire.save(vm.exemplaire, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bibalApp:exemplaireUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
