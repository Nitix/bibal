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
                        data.dataPublication = DateUtils.convertLocalDateFromServer(data.dataPublication);
                        data.dateEdition = DateUtils.convertLocalDateFromServer(data.dateEdition);
                        data.parution = DateUtils.convertLocalDateFromServer(data.parution);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dataPublication = DateUtils.convertLocalDateToServer(copy.dataPublication);
                    copy.dateEdition = DateUtils.convertLocalDateToServer(copy.dateEdition);
                    copy.parution = DateUtils.convertLocalDateToServer(copy.parution);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dataPublication = DateUtils.convertLocalDateToServer(copy.dataPublication);
                    copy.dateEdition = DateUtils.convertLocalDateToServer(copy.dateEdition);
                    copy.parution = DateUtils.convertLocalDateToServer(copy.parution);
                    return angular.toJson(copy);
                }
            },
            'withExemplaire' : { method: 'GET', isArray: true, params:{withExemplaire:true}}

    });
    }
})();
