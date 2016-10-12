'use strict';

describe('Controller Tests', function() {

    describe('Exemplaire Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockExemplaire, MockOeuvre;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockExemplaire = jasmine.createSpy('MockExemplaire');
            MockOeuvre = jasmine.createSpy('MockOeuvre');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Exemplaire': MockExemplaire,
                'Oeuvre': MockOeuvre
            };
            createController = function() {
                $injector.get('$controller')("ExemplaireDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'bibalApp:exemplaireUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
