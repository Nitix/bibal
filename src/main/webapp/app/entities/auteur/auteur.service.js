(function() {
    'use strict';
    angular
        .module('bibalApp')
        .factory('Auteur', Auteur);

    Auteur.$inject = ['$resource', 'DateUtils'];

    function Auteur ($resource, DateUtils) {
        var resourceUrl =  'api/auteurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateNaissance = DateUtils.convertDateTimeFromServer(data.dateNaissance);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
