'use strict';

describe('Controller Tests', function() {

    describe('Oeuvre Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOeuvre, MockExemplaire, MockAuteur, MockReservation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOeuvre = jasmine.createSpy('MockOeuvre');
            MockExemplaire = jasmine.createSpy('MockExemplaire');
            MockAuteur = jasmine.createSpy('MockAuteur');
            MockReservation = jasmine.createSpy('MockReservation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Oeuvre': MockOeuvre,
                'Exemplaire': MockExemplaire,
                'Auteur': MockAuteur,
                'Reservation': MockReservation
            };
            createController = function() {
                $injector.get('$controller')("OeuvreDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bibalApp:oeuvreUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
