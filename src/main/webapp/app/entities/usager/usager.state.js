(function() {
    'use strict';

    angular
        .module('bibalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('usager', {
            parent: 'entity',
            url: '/usager',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bibalApp.usager.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/usager/usagers.html',
                    controller: 'UsagerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('usager');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('usager-detail', {
            parent: 'entity',
            url: '/usager/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bibalApp.usager.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/usager/usager-detail.html',
                    controller: 'UsagerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('usager');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Usager', function($stateParams, Usager) {
                    return Usager.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'usager',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('usager-detail.edit', {
            parent: 'usager-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/usager/usager-dialog.html',
                    controller: 'UsagerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Usager', function(Usager) {
                            return Usager.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('usager.new', {
            parent: 'usager',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/usager/usager-dialog.html',
                    controller: 'UsagerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                prenom: null,
                                dateNaissance: null,
                                adresse: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('usager', null, { reload: 'usager' });
                }, function() {
                    $state.go('usager');
                });
            }]
        })
        .state('usager.edit', {
            parent: 'usager',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/usager/usager-dialog.html',
                    controller: 'UsagerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Usager', function(Usager) {
                            return Usager.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('usager', null, { reload: 'usager' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('usager.delete', {
            parent: 'usager',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/usager/usager-delete-dialog.html',
                    controller: 'UsagerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Usager', function(Usager) {
                            return Usager.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('usager', null, { reload: 'usager' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
