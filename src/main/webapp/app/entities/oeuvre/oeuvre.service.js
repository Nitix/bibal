(function() {
    'use strict';
    angular
        .module('bibalApp')
        .factory('Oeuvre', Oeuvre);

    Oeuvre.$inject = ['$resource', 'DateUtils'];

    function Oeuvre ($resource, DateUtils) {
        var resourceUrl =  'api/oeuvres/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dataPublication = DateUtils.convertDateTimeFromServer(data.dataPublication);
                        data.dateEdition = DateUtils.convertDateTimeFromServer(data.dateEdition);
                        data.parution = DateUtils.convertDateTimeFromServer(data.parution);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
