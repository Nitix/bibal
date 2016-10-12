(function() {
    'use strict';

    angular
        .module('bibalApp')
        .controller('ReservationController', ReservationController);

    ReservationController.$inject = ['$scope', '$state', 'Reservation'];

    function ReservationController ($scope, $state, Reservation) {
        var vm = this;
        
        vm.reservations = [];

        loadAll();

        function loadAll() {
            Reservation.query(function(result) {
                vm.reservations = result;
            });
        }
    }
})();
