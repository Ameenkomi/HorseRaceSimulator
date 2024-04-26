public class Main {
    public static void main(String[] args) {

        Race race = new Race(100);

        Horse horse1 = new Horse('X', "horse1", 0.2);
        Horse horse2 = new Horse('Y', "horse2", 0.2);
        Horse horse3 = new Horse('Z', "horse3", 0.2);

        race.registerHorse(horse1, 1);
        race.registerHorse(horse2, 2);
        race.registerHorse(horse3, 3);

        race.beginRace();
    }
}
