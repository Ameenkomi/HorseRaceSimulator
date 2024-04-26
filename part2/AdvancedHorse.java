import java.awt.Color;
import java.util.Map;
import java.util.Set;

public class AdvancedHorse {
    private String name;
    private String icon;
    private int distanceCovered;
    private boolean hasFallen;
    private double fitnessLevel;
    private boolean isWinner;
    private Color color;
    private Set<String> equipment;
    private int victoriesCount;
    private double totalFitnessLevel;
    private int totalRaces;
    private double bestTime;
    private double totalDistance;
    private double oddsToWin;

    private static final Map<String, Double> EQUIPMENT_BONUSES = Map.of(
        "Saddle", 1.1,
        "Blinkers", 1.05,
        "Horseshoes", 1.03
    );

    public AdvancedHorse(String name, String icon, double fitnessLevel, Color color, Set<String> equipment) {
        this.name = name;
        this.icon = icon;
        this.fitnessLevel = fitnessLevel;
        this.color = color;
        this.equipment = equipment;
        this.distanceCovered = 0;
        this.hasFallen = false;
        this.isWinner = false;
        this.victoriesCount = 0;
        this.totalFitnessLevel = 0;
        this.totalRaces = 0;
        this.bestTime = Double.MAX_VALUE;
        this.totalDistance = 0;
        this.oddsToWin = 1.0;
    }

    public void updateOddsToWin() {
      
        oddsToWin = 1.0 + (victoriesCount * 0.1) + (fitnessLevel * 0.05);
    }

    public void incrementVictories() {
        victoriesCount++;
    }

    public void updateFitnessLevel(double newFitnessLevel) {
        totalFitnessLevel += newFitnessLevel;
        fitnessLevel = totalFitnessLevel / totalRaces;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public int getDistanceCovered() {
        return distanceCovered;
    }

    public boolean hasFallen() {
        return hasFallen;
    }

    public double getFitnessLevel() {
        return fitnessLevel;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public Color getColor() {
        return color;
    }

    public Set<String> getEquipment() {
        return equipment;
    }

    public int getVictoriesCount() {
        return victoriesCount;
    }

    public double getBestTime() {
        return bestTime;
    }

    public double getOddsToWin() {
        return oddsToWin;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public void fall() {
        hasFallen = true;
    }

    public void moveForward() {
        if (!hasFallen && !isWinner) {
            double equipmentBonus = calculateEquipmentBonus();
            distanceCovered += (1 + (equipmentBonus * 2)) * fitnessLevel / 100;
        }
    }

    private double calculateEquipmentBonus() {
        double bonus = 0;
        for (String item : equipment) {
            bonus += EQUIPMENT_BONUSES.getOrDefault(item, 0.0);
        }
        return bonus;
    }

    public void resetForNewRace() {
        distanceCovered = 0;
        hasFallen = false;
        isWinner = false;
    }

    public void incrementTotalRaces() {
        totalRaces++;
    }

    public void updateBestTime(double time) {
        if (time < bestTime) {
            bestTime = time;
        }
    }

    public void updateTotalDistance(int distance) {
        totalDistance += distance;
    }
}