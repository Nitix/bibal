(function() {
    'use strict';
    angular
        .module('bibalApp')
        .factory('Usager', Usager);

    Usager.$inject = ['$resource', 'DateUtils'];

    function Usager ($resource, DateUtils) {
        var resourceUrl =  'api/usagers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateNaissance = DateUtils.convertLocalDateFromServer(data.dateNaissance);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateNaissance = DateUtils.convertLocalDateToServer(copy.dateNaissance);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateNaissance = DateUtils.convertLocalDateToServer(copy.dateNaissance);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
