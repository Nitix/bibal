(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('AuteurDialogController', AuteurDialogController);

    AuteurDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Auteur', 'Oeuvre'];

    function AuteurDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Auteur, Oeuvre) {
        var vm = this;

        vm.auteur = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.auteur.id !== null) {
                Auteur.update(vm.auteur, onSaveSuccess, onSaveError);
            } else {
                Auteur.save(vm.auteur, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bibalApp:auteurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateNaissance = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
