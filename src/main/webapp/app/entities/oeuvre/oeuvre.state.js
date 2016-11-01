(function() {
    'use strict';

    angular
        .module('bibalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('oeuvre', {
            parent: 'entity',
            url: '/oeuvre',
            data: {
                authorities: [],
                pageTitle: 'bibalApp.oeuvre.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/oeuvre/oeuvres.html',
                    controller: 'OeuvreController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('oeuvre');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('oeuvre-detail', {
            parent: 'entity',
            url: '/oeuvre/{id}',
            data: {
                authorities: [],
                pageTitle: 'bibalApp.oeuvre.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/oeuvre/oeuvre-detail.html',
                    controller: 'OeuvreDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('oeuvre');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Oeuvre', function($stateParams, Oeuvre) {
                    return Oeuvre.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'oeuvre',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('oeuvre-detail.edit', {
            parent: 'oeuvre-detail',
            url: '/detail/edit',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/oeuvre/oeuvre-dialog.html',
                    controller: 'OeuvreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Oeuvre', function(Oeuvre) {
                            return Oeuvre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('oeuvre.new', {
            parent: 'oeuvre',
            url: '/new',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/oeuvre/oeuvre-dialog.html',
                    controller: 'OeuvreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                titre: null,
                                editeur: null,
                                description: null,
                                dataPublication: null,
                                dateEdition: null,
                                numero: null,
                                parution: null,
                                periodicite: null,
                                estLivre: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('oeuvre', null, { reload: 'oeuvre' });
                }, function() {
                    $state.go('oeuvre');
                });
            }]
        })
        .state('oeuvre.edit', {
            parent: 'oeuvre',
            url: '/{id}/edit',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/oeuvre/oeuvre-dialog.html',
                    controller: 'OeuvreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Oeuvre', function(Oeuvre) {
                            return Oeuvre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('oeuvre', null, { reload: 'oeuvre' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('oeuvre.delete', {
            parent: 'oeuvre',
            url: '/{id}/delete',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/oeuvre/oeuvre-delete-dialog.html',
                    controller: 'OeuvreDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Oeuvre', function(Oeuvre) {
                            return Oeuvre.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('oeuvre', null, { reload: 'oeuvre' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
