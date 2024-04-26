import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Race{
    private int trackLength;
    private List<Horse> competitors;

    public Race(int length) {
        trackLength = length;
        competitors = new ArrayList<>();
    }

    public void registerHorse(Horse horse, int lane) {
        if (lane >= 1 && lane <= competitors.size() + 1) {
            competitors.add(lane - 1, horse);
        } else {
            System.out.println("Invalid lane number. Unable to add the horse to the race.");
        }
    }

    public void beginRace() {
        boolean raceFinished = false;
    
        for (Horse horse : competitors) {
            horse.goBackToStart();
        }
    
        while (!raceFinished) {
            for (Horse horse : competitors) {
                moveHorseForward(horse);
            }
    
            showRaceProgress();
    
            if (allHorsesDown()) {
                System.out.println("All horses have fallen. The race concludes.");
                raceFinished = true;
            } else if (anyHorseWins()) {
                for (Horse horse : competitors) {
                    if (horseWins(horse)) {
                        System.out.println("The winning horse is " + horse.getName());
                    }
                }
                raceFinished = true;
            }
    
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (Exception e) {
                // Handle the exception
            }
        }
    }

    private boolean allHorsesDown() {
        for (Horse horse : competitors) {
            if (!horse.hasFallen()) {
                return false;
            }
        }
        return true;
    }

    private boolean anyHorseWins() {
        for (Horse horse : competitors) {
            if (horseWins(horse)) {
                return true;
            }
        }
        return false;
    }

    private void moveHorseForward(Horse horse) {
        if (!horse.hasFallen()) {
            if (Math.random() < horse.getConfidence()) {
                horse.moveForward();
                adjustConfidence(horse, false);
            }

            if (Math.random() < (0.5 * horse.getConfidence() * horse.getConfidence())) {
                horse.fall();
            }
        }
    }

    private boolean horseWins(Horse horse) {
        return horse.getDistanceTravelled() == trackLength;
    }

    private void showRaceProgress() {
        StringBuilder raceDisplay = new StringBuilder();

        raceDisplay.append("\033[2J");

        raceDisplay.append("\033[H");
    
        raceDisplay.append(String.format("%0" + (trackLength + 3) + "d", 0).replace("0", "="));
        raceDisplay.append(System.lineSeparator());

        for (Horse horse : competitors) {
            raceDisplay.append("|");
            int spacesBefore = horse.getDistanceTravelled();
            int spacesAfter = trackLength - horse.getDistanceTravelled();
    
            for (int i = 0; i < spacesBefore; i++) {
                raceDisplay.append(" ");
            }
    
            if (horse.hasFallen()) {
                raceDisplay.append("❌");
            } else {
                raceDisplay.append(horse.getSymbol());
            }
    
            for (int i = 0; i < spacesAfter; i++) {
                raceDisplay.append(" ");
            }
    
            raceDisplay.append("| ");
    
            if (horse.hasFallen()) {
                raceDisplay.append(horse.getName()).append(" (Horse has fallen)");
            } else {
                raceDisplay.append(horse.getName()).append(" (Confidence Level: ")
                        .append(String.format("%.2f", horse.getConfidence())).append(")");
            }
    
            raceDisplay.append(System.lineSeparator());
        }
    
        raceDisplay.append(String.format("%0" + (trackLength + 3) + "d", 0).replace("0", "="));
        raceDisplay.append(System.lineSeparator());

        System.out.print(raceDisplay.toString());
    }

    private void adjustConfidence(Horse horse, boolean wins) {
        double currentConfidence = horse.getConfidence();

        if (wins) {
            horse.setConfidence(Math.min(currentConfidence + 0.1, 1.0));
        } else {
            horse.setConfidence(Math.max(currentConfidence - 0.01, 0.0));
        }
    }
}
