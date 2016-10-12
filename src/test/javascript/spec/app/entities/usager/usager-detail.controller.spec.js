'use strict';

describe('Controller Tests', function() {

    describe('Usager Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUsager, MockEmprunt, MockReservation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUsager = jasmine.createSpy('MockUsager');
            MockEmprunt = jasmine.createSpy('MockEmprunt');
            MockReservation = jasmine.createSpy('MockReservation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Usager': MockUsager,
                'Emprunt': MockEmprunt,
                'Reservation': MockReservation
            };
            createController = function() {
                $injector.get('$controller')("UsagerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bibalApp:usagerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
