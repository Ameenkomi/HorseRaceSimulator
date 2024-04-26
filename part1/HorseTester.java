public class HorseTester {
    public static void main(String[] args) {
        HorseTester tester = new HorseTester();
        tester.testHorse();
    }

    public void testHorse() {
        Horse horse = new Horse('B', "Bucephalus", 0.9);
        assert "Bucephalus".equals(horse.getName()) : "Name does not match";
        assert horse.getSymbol() == 'B' : "Symbol does not match";
        assert horse.getConfidence() == 0.9 : "Initial confidence does not match";
        assert !horse.hasFallen() : "Horse should not have fallen yet";
        assert horse.getDistanceTravelled() == 0 : "Initial distance travelled should be 0";
        horse.moveForward();
        horse.moveForward();
        horse.moveForward();
        assert horse.getDistanceTravelled() == 3 : "Distance travelled after moving forward should be 3";
        horse.fall();
        assert horse.hasFallen() : "Horse should have fallen";
        assert horse.getConfidence() == 0.8 : "Confidence after falling should decrease by 0.1";
        assert horse.getConfidence() >= 0 : "Confidence should not go below 0";
        horse.setConfidence(0.6);
        assert horse.getConfidence() == 0.6 : "Confidence should be set to 0.6";
        horse.setConfidence(0.0);
        assert horse.getConfidence() == 0.0 : "Confidence should be set to 0.0";
        horse.setConfidence(1.2);
        assert horse.getConfidence() == 1.0 : "Confidence should cap at 1.0 when setting to 1.2";
        horse.goBackToStart();
        assert horse.getDistanceTravelled() == 0 : "Distance travelled should reset to 0";
        horse.setSymbol('X');
        assert horse.getSymbol() == 'X' : "Symbol should update to 'X'";
        System.out.println("All tests passed.");
    }
}