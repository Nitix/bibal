(function() {
    'use strict';
    angular
        .module('bibalApp')
        .factory('Emprunt', Emprunt);

    Emprunt.$inject = ['$resource', 'DateUtils'];

    function Emprunt ($resource, DateUtils) {
        var resourceUrl =  'api/emprunts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateEmprunt = DateUtils.convertDateTimeFromServer(data.dateEmprunt);
                        data.dateRetour = DateUtils.convertDateTimeFromServer(data.dateRetour);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
