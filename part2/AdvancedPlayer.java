import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class AdvancedPlayer {
    private double balance;
    private int totalVictories;
    private double totalEarnings;
    private Map<AdvancedHorse, ArrayList<Wager>> betHistory;
    private Wager currentWager;

    public AdvancedPlayer(double initialBalance) {
        this.balance = initialBalance;
        this.totalVictories = 0;
        this.totalEarnings = 0;
        this.betHistory = new HashMap<>();
    }

    public boolean placeWager(AdvancedHorse horse, double wagerAmount) {
        if (balance >= wagerAmount) {
            balance -= wagerAmount;
            currentWager = new Wager(horse, wagerAmount);
            betHistory.computeIfAbsent(horse, h -> new ArrayList<>()).add(currentWager);
            return true;
        }
        return false;
    }

    public void settleWagers(AdvancedHorse winnerHorse) {
        if (currentWager != null && winnerHorse!=null && winnerHorse.equals(currentWager.getHorse())) {
            double winnings = currentWager.getAmount() * winnerHorse.getOddsToWin();
            balance += winnings;
            totalVictories++;
            totalEarnings += winnings;
            currentWager.setWinning(true);
        }
        currentWager = null;
    }

    public double getBalance() {
        return balance;
    }

    public int getTotalVictories() {
        return totalVictories;
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }

    public Map<AdvancedHorse, ArrayList<Wager>> getBetHistory() {
        return betHistory;
    }

    public double getWinPercentage() {
        int totalBets = betHistory.values().stream().mapToInt(List::size).sum();
        return totalBets > 0 ? (double) totalVictories / totalBets * 100 : 0;
    }

    static class Wager {
        private final AdvancedHorse horse;
        private final double amount;
        private boolean isWinning;

        public Wager(AdvancedHorse horse, double amount) {
            this.horse = horse;
            this.amount = amount;
            this.isWinning = false;
        }

        public AdvancedHorse getHorse() {
            return horse;
        }

        public double getAmount() {
            return amount;
        }

        public boolean isWinning() {
            return isWinning;
        }

        public void setWinning(boolean winning) {
            isWinning = winning;
        }
    }
}