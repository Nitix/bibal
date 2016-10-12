(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('OeuvreDialogController', OeuvreDialogController);

    OeuvreDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Oeuvre', 'Exemplaire', 'Auteur', 'Reservation'];

    function OeuvreDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Oeuvre, Exemplaire, Auteur, Reservation) {
        var vm = this;

        vm.oeuvre = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.exemplaires = Exemplaire.query();
        vm.auteurs = Auteur.query();
        vm.reservations = Reservation.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.oeuvre.id !== null) {
                Oeuvre.update(vm.oeuvre, onSaveSuccess, onSaveError);
            } else {
                Oeuvre.save(vm.oeuvre, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bibalApp:oeuvreUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dataPublication = false;
        vm.datePickerOpenStatus.dateEdition = false;
        vm.datePickerOpenStatus.parution = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
