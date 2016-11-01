(function() {
    'use strict';
    angular
        .module('bibalApp')
        .factory('Exemplaire', Exemplaire);

    Exemplaire.$inject = ['$resource'];

    function Exemplaire ($resource) {
        var resourceUrl =  'api/exemplaires/:id';
        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'free':  { method: 'GET', isArray: true, params:{free:true}},
        });
    }
})();
