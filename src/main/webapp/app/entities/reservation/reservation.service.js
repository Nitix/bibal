(function() {
    'use strict';
    angular
        .module('bibalApp')
        .factory('Reservation', Reservation);

    Reservation.$inject = ['$resource', 'DateUtils'];

    function Reservation ($resource, DateUtils) {
        var resourceUrl =  'api/reservations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateReservation = DateUtils.convertDateTimeFromServer(data.dateReservation);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
