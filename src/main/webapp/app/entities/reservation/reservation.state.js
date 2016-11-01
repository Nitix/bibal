(function() {
    'use strict';

    angular
        .module('bibalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('reservation', {
            parent: 'entity',
            url: '/reservation',
            data: {
                authorities: [],
                pageTitle: 'bibalApp.reservation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reservation/reservations.html',
                    controller: 'ReservationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reservation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('reservation-detail', {
            parent: 'entity',
            url: '/reservation/{id}',
            data: {
                authorities: [],
                pageTitle: 'bibalApp.reservation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reservation/reservation-detail.html',
                    controller: 'ReservationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reservation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Reservation', function($stateParams, Reservation) {
                    return Reservation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'reservation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('reservation-detail.edit', {
            parent: 'reservation-detail',
            url: '/detail/edit',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reservation/reservation-dialog.html',
                    controller: 'ReservationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Reservation', function(Reservation) {
                            return Reservation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reservation.new', {
            parent: 'reservation',
            url: '/new',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reservation/reservation-dialog.html',
                    controller: 'ReservationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateReservation: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('reservation', null, { reload: 'reservation' });
                }, function() {
                    $state.go('reservation');
                });
            }]
        })
        .state('reservation.edit', {
            parent: 'reservation',
            url: '/{id}/edit',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reservation/reservation-dialog.html',
                    controller: 'ReservationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Reservation', function(Reservation) {
                            return Reservation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reservation', null, { reload: 'reservation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reservation.delete', {
            parent: 'reservation',
            url: '/{id}/delete',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reservation/reservation-delete-dialog.html',
                    controller: 'ReservationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Reservation', function(Reservation) {
                            return Reservation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reservation', null, { reload: 'reservation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
