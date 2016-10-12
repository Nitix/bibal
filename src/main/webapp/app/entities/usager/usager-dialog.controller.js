(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('UsagerDialogController', UsagerDialogController);

    UsagerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Usager'];

    function UsagerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Usager) {
        var vm = this;

        vm.usager = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.usager.id !== null) {
                Usager.update(vm.usager, onSaveSuccess, onSaveError);
            } else {
                Usager.save(vm.usager, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bibalApp:usagerUpdate', result);
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
