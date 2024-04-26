import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class AdvancedRaceGUI {
    private JFrame frame;
    private Map<String, AdvancedHorse> horses;
    private JPanel raceTrackPanel;
    private int raceTrackLength;
    private Thread raceSimulationThread;
    private JPanel controlPanel;
    private int numberOfHorses=0;
    private Map<Integer, AdvancedPlayer> players;
    private JPanel playerStatsPanel;
    private boolean isConfigured = false;
    public AdvancedRaceGUI() throws IOException {
        horses = new HashMap<>();
        players = new HashMap<>();
        initializeUI();
    }

    private void initializeUI() throws IOException {
        frame = new JFrame("Advanced Horse Racing Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);

        Image backgroundImg = ImageIO.read(getClass().getResource("/background.jpeg"));
        Image scaledImg = backgroundImg.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_SMOOTH);
        JLabel backgroundLabel = new JLabel(new ImageIcon(scaledImg));
        backgroundLabel.setLayout(new BorderLayout());
        frame.setContentPane(backgroundLabel);
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(Box.createRigidArea(new Dimension(0, 80))); // Space between buttons
        inputPanel.add(createButton("Set Track Length", this::setTrackLength));
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        inputPanel.add(createButton("Set Number of Horses", this::getNumberOfHorses));
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        inputPanel.add(createButton("Set Number of Players", this::getNumberOfPlayers));
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        inputPanel.add(createButton("Configure Horses", this::configureHorses));
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        inputPanel.add(createButton("Start Race", this::startRace));
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        inputPanel.setOpaque(false);
        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
     
    
        controlPanel.add(Box.createRigidArea(new Dimension(0, 150))); // Space between buttons
        controlPanel.add(createButton("Show Statistics", this::showStatistics));
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        controlPanel.add(createButton("Place Wagers", this::placeWagers));
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        controlPanel.add(createButton("View Wager History", this::viewWagerHistory));
        controlPanel.setOpaque(false);
        playerStatsPanel = createPlayerStatsPanel();
        playerStatsPanel.setOpaque(false);
        mainPanel.add(inputPanel, BorderLayout.WEST);
        mainPanel.add(controlPanel, BorderLayout.EAST);
        mainPanel.add(playerStatsPanel, BorderLayout.SOUTH);
        mainPanel.setOpaque(false);
        raceTrackPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(isConfigured){
                    drawRaceTrack(g);
                }
            }
        };
        raceTrackPanel.setOpaque(false);
        mainPanel.add(raceTrackPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
  
    }

    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setForeground(Color.white);
        button.setBackground(Color.blue);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setOpaque(true);
        button.setMargin(new Insets(10, 20, 10, 20));
        button.setPreferredSize(new Dimension(200, 70));
        button.setMaximumSize(new Dimension(200, 70));
        button.addActionListener(e -> action.run());
        return button;
    }

    private void setTrackLength() {
        String input = JOptionPane.showInputDialog(frame, "Enter the length of the race track:");
        if (input != null && !input.isEmpty()) {
            try {
                raceTrackLength = Integer.parseInt(input);
                if(raceTrackLength>500 || raceTrackLength<300){
                    JOptionPane.showMessageDialog(frame, "Please enter number betwee 300 and 500");
                    setTrackLength();
                }
                }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number.");
                setTrackLength();
            }
        }
    }

    private JPanel createPlayerStatsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        updatePlayerStatsDisplay();
        return panel;
    }

    private void updatePlayerStatsDisplay() {
        if(playerStatsPanel!=null)
        {
            playerStatsPanel.removeAll();
        }

        for (Map.Entry<Integer, AdvancedPlayer> entry : players.entrySet()) {
            int playerId = entry.getKey();
            AdvancedPlayer player = entry.getValue();

            JPanel playerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            playerPanel.add(new JLabel("Player " + playerId + ":"));
            playerPanel.add(new JLabel("Balance: $" + player.getBalance()));
            playerPanel.add(new JLabel("Total Victories: " + player.getTotalVictories()));
            playerPanel.add(new JLabel("Win Percentage: " + String.format("%.2f", player.getWinPercentage()) + "%"));

            playerStatsPanel.add(playerPanel);
        }

        if(playerStatsPanel!=null)
        {
            playerStatsPanel.revalidate();
            playerStatsPanel.repaint();
        }
        
    }

    private void getNumberOfHorses() {
        String input = JOptionPane.showInputDialog(frame, "Enter the number of horses:");
        if (input != null && !input.isEmpty()) {
            try {
                numberOfHorses = Integer.parseInt(input);
                if(numberOfHorses <2 || numberOfHorses>5){
                    JOptionPane.showMessageDialog(frame, "Pleas enter number between 2 and 5");
                    getNumberOfHorses();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number.");
                getNumberOfHorses();
            }
        }
    }

    private void getNumberOfPlayers() {
        String input = JOptionPane.showInputDialog(frame, "Enter the number of players:");
        if (input != null && !input.isEmpty()) {
            try {
                int numPlayers = Integer.parseInt(input);
                if(numPlayers<2 || numPlayers>4){
                    JOptionPane.showMessageDialog(frame, "Please input a number between 2 and 4");
                    getNumberOfPlayers();
                }
                else{
                    for (int i = 1; i <= numPlayers; i++) {
                        players.put(i, new AdvancedPlayer(1000.0));
                    }
            }
                updatePlayerStatsDisplay();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number.");
                getNumberOfPlayers();
            }
        }
    }

    private void configureHorses() {
        horses.clear();
        for (int i = 1; i <= numberOfHorses; i++) {
            AdvancedHorse horse = getHorseConfiguration(i);
            while (horse==null){
                horse = getHorseConfiguration(i);
            }
            if (horse != null) {
                horses.put(horse.getName(), horse);
            }
        }
        isConfigured = true;
        raceTrackPanel.repaint();
    }

    private void showStatistics() {
        JDialog statisticsDialog = new JDialog(frame, "Race Statistics", true);
        statisticsDialog.setSize(800, 600);
        statisticsDialog.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Victories Chart
        AdvancedBarChart victoriesChart = createVictoriesChart();
        tabbedPane.addTab("Victories", victoriesChart);

        // Best Time Chart
        AdvancedBarChart bestTimeChart = createBestTimeChart();
        tabbedPane.addTab("Best Time", bestTimeChart);

        // Fitness Level Chart
        AdvancedBarChart fitnessLevelChart = createFitnessLevelChart();
        tabbedPane.addTab("Fitness Level", fitnessLevelChart);

        // Odds to Win Chart
        AdvancedBarChart oddsToWinChart = createOddsToWinChart();
        tabbedPane.addTab("Odds to Win", oddsToWinChart);

        statisticsDialog.add(tabbedPane, BorderLayout.CENTER);
        statisticsDialog.setLocationRelativeTo(frame);
        statisticsDialog.setVisible(true);
    }

    private AdvancedBarChart createVictoriesChart() {
        AdvancedBarChart chart = new AdvancedBarChart();
        chart.setBarThickness(50);

        for (AdvancedHorse horse : horses.values()) {
            chart.addData(horse.getColor(), horse.getVictoriesCount(), horse.getName());
        }

        return chart;
    }

    private AdvancedBarChart createBestTimeChart() {
        AdvancedBarChart chart = new AdvancedBarChart();
        chart.setBarThickness(50);

        for (AdvancedHorse horse : horses.values()) {
            chart.addData(horse.getColor(), horse.getBestTime(), horse.getName());
        }

        return chart;
    }

    private AdvancedBarChart createFitnessLevelChart() {
        AdvancedBarChart chart = new AdvancedBarChart();
        chart.setBarThickness(50);

        for (AdvancedHorse horse : horses.values()) {
            chart.addData(horse.getColor(), horse.getFitnessLevel(), horse.getName());
        }

        return chart;
    }

    private AdvancedBarChart createOddsToWinChart() {
        AdvancedBarChart chart = new AdvancedBarChart();
        chart.setBarThickness(50);

        for (AdvancedHorse horse : horses.values()) {
            chart.addData(horse.getColor(), horse.getOddsToWin(), horse.getName());
        }

        return chart;
    }
    private void viewWagerHistory() {
        StringBuilder historyBuilder = new StringBuilder();
        historyBuilder.append("Wager History:\n\n");

        for (Map.Entry<Integer, AdvancedPlayer> entry : players.entrySet()) {
            int playerId = entry.getKey();
            AdvancedPlayer player = entry.getValue();

            historyBuilder.append("Player ").append(playerId).append(":\n");

            for (Map.Entry<AdvancedHorse, ArrayList<AdvancedPlayer.Wager>> wagerEntry : player.getBetHistory().entrySet()) {
                AdvancedHorse horse = wagerEntry.getKey();
                ArrayList<AdvancedPlayer.Wager> wagers = wagerEntry.getValue();

                for (AdvancedPlayer.Wager wager : wagers) {
                    historyBuilder.append("- Horse: ").append(horse.getName());
                    historyBuilder.append(", Amount: $").append(wager.getAmount());
                    historyBuilder.append(", Result: ").append(wager.isWinning() ? "Win" : "Loss");
                    historyBuilder.append("\n");
                }
            }

            historyBuilder.append("\n");
        }

        JOptionPane.showMessageDialog(frame, historyBuilder.toString(), "Wager History", JOptionPane.INFORMATION_MESSAGE);
    }

    private AdvancedHorse getHorseConfiguration(int horseNumber) {
        JTextField nameField = new JTextField();
        JTextField fitnessField = new JTextField();
        JComboBox<String> colorComboBox = new JComboBox<>(new String[]{"Red", "Blue", "Green", "Yellow"});
        JComboBox<String> iconComboBox = new JComboBox<>(new String[]{"🐎", "🦄", "🐴", "🏇"});

        JPanel configPanel = new JPanel(new GridLayout(0, 2));
        configPanel.add(new JLabel("Name:"));
        configPanel.add(nameField);
        configPanel.add(new JLabel("Fitness Level (0-100):"));
        configPanel.add(fitnessField);
        configPanel.add(colorComboBox);
        configPanel.add(iconComboBox);

    JCheckBox saddleCheckBox = new JCheckBox("Saddle");
    JCheckBox blinkersCheckBox = new JCheckBox("Blinkers");
    JCheckBox horseshoesCheckBox = new JCheckBox("Horseshoes");

    configPanel.add(new JLabel("Equipment:"));
    configPanel.add(saddleCheckBox);
    configPanel.add(new JLabel());
    configPanel.add(blinkersCheckBox);
    configPanel.add(new JLabel());
    configPanel.add(horseshoesCheckBox);

    int option = JOptionPane.showConfirmDialog(frame, configPanel, "Configure Horse " + horseNumber,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (option == JOptionPane.OK_OPTION) {
        String name = nameField.getText();
        if(name==""){
            JOptionPane.showMessageDialog(frame, "name should not be left empty");
            return null;
        }

        double fitnessLevel;
        try{
            fitnessLevel = Double.parseDouble(fitnessField.getText());
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(frame, "must input fitness level");
            return null;
        } 
        if(fitnessLevel>100 || fitnessLevel<0){
            JOptionPane.showMessageDialog(frame, "fitness level should be from 0 and 1");
            return null;
        }
        Color color = getColorFromName((String) colorComboBox.getSelectedItem());
        String icon = (String) iconComboBox.getSelectedItem();

        Set<String> equipment = new HashSet<>();
        if (saddleCheckBox.isSelected()) {
            equipment.add("Saddle");
        }
        if (blinkersCheckBox.isSelected()) {
            equipment.add("Blinkers");
        }
        if (horseshoesCheckBox.isSelected()) {
            equipment.add("Horseshoes");
        }

        return new AdvancedHorse(name, icon, fitnessLevel, color, equipment);
    }

    return null;
}

private Color getColorFromName(String colorName) {
    return switch (colorName) {
        case "Red" -> Color.RED;
        case "Blue" -> Color.BLUE;
        case "Green" -> Color.GREEN;
        case "Yellow" -> Color.YELLOW;
        default -> Color.BLACK;
    };
}

private void startRace() {

    if(numberOfHorses == 0 || players==null){
        return;
    }
    if (raceSimulationThread != null && raceSimulationThread.isAlive()) {
        raceSimulationThread.interrupt();
    }

    for (AdvancedHorse horse : horses.values()) {
        horse.resetForNewRace();
        horse.updateOddsToWin();
    }

    raceSimulationThread = new Thread(() -> {
        boolean raceFinished = false;
        while (!raceFinished) {
            boolean allHorsesFallen = true;
            boolean hasWinner = false;

            for (AdvancedHorse horse : horses.values()) {
                if (!horse.hasFallen() && !horse.isWinner()) {
                    allHorsesFallen = false;
                    double randomValue = Math.random();
                    if (randomValue < 0.03) {
                        horse.fall();
                    } else {
                        horse.moveForward();
                    }

                    if (horse.getDistanceCovered() >= raceTrackLength) {
                        horse.setWinner(true);
                        hasWinner = true;
                    }
                }
            }

            raceTrackPanel.repaint();

            if (allHorsesFallen) {
                JOptionPane.showMessageDialog(frame, "No winner. All horses have fallen.");
                raceFinished = true;
            } else if (hasWinner) {
                raceFinished = true;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
        AdvancedHorse winnerHorse = null;
        for (AdvancedHorse horse : horses.values()) {
            if (horse.isWinner()) {
                winnerHorse = horse;
                break;
            }
        }

        if (winnerHorse != null) {
            JOptionPane.showMessageDialog(frame, "The winner is: " + winnerHorse.getName());
            winnerHorse.incrementVictories();
            winnerHorse.updateFitnessLevel(10);
            for (AdvancedHorse horse : horses.values()) {
                horse.resetForNewRace();
                horse.updateOddsToWin();
            }
            raceTrackPanel.repaint();
        } 

        for (AdvancedPlayer player : players.values()) {
            player.settleWagers(winnerHorse);
        }

        updateRaceStatistics();
        updatePlayerStatsDisplay();
    });

    raceSimulationThread.start();
}

private void placeWagerForPlayer(AdvancedPlayer player, int playerIndex) {
    JComboBox<String> horseComboBox = new JComboBox<>(horses.keySet().toArray(new String[0]));
    JTextField wagerField = new JTextField();

    JPanel wagerPanel = new JPanel(new GridLayout(0, 2));
    wagerPanel.add(new JLabel("Select Horse:"));
    wagerPanel.add(horseComboBox);
    wagerPanel.add(new JLabel("Wager Amount:"));
    wagerPanel.add(wagerField);

    int option = JOptionPane.showConfirmDialog(frame, wagerPanel, "Place Wager - Player " + playerIndex,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (option == JOptionPane.OK_OPTION) {
        String horseName = (String) horseComboBox.getSelectedItem();
        double wagerAmount = Double.parseDouble(wagerField.getText());

        AdvancedHorse selectedHorse = horses.get(horseName);
        if (selectedHorse != null) {
            boolean wagerPlaced = player.placeWager(selectedHorse, wagerAmount);
            if (!wagerPlaced) {
                JOptionPane.showMessageDialog(frame, "Insufficient balance to place the wager.");
            }
        }
    }
}

private void placeWagers() {
    for (Map.Entry<Integer, AdvancedPlayer> entry : players.entrySet()) {
        int playerIndex = entry.getKey();
        AdvancedPlayer player = entry.getValue();
        placeWagerForPlayer(player, playerIndex);
    }
}

private void updateRaceStatistics() {
    for (AdvancedHorse horse : horses.values()) {
        horse.incrementTotalRaces();
        horse.updateBestTime(horse.getDistanceCovered() / 100.0);
        horse.updateTotalDistance(raceTrackLength);
    }
}


private void drawRaceTrack(Graphics g) {
    Graphics2D g2d = (Graphics2D) g.create();
    
 
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    int panelWidth = raceTrackPanel.getWidth();
    int trackHeight = 50; 
    int startY = (raceTrackPanel.getHeight() - (trackHeight * horses.size())) / 2; 
    int spaceBetweenTracks = 10; 
    
   
    Stroke dotted = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{9}, 0);
    g2d.setStroke(dotted);

    int i=0;
    for (AdvancedHorse steed : horses.values()) {
        int laneY = startY + i * (trackHeight + spaceBetweenTracks);
       
        g2d.setColor(new Color(0, 0, 0, 0)); 
        g2d.fillRect(50, laneY, panelWidth - 100, trackHeight);
        

        g2d.setColor(Color.BLACK);
        g2d.drawRect(50, laneY, panelWidth - 100, trackHeight);
        
      
        int horseX = (int) ((panelWidth - 100) * (steed.getDistanceCovered() / (double) raceTrackLength)) + 50;
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        String horseText = steed.getIcon() + " - " + steed.getName();
        if (steed.hasFallen()) {
            horseText = "❌";
        }
        g2d.drawString(horseText, horseX, laneY + trackHeight / 2);

        i++;
    }

  
    if (horses.size() > 0) {
        g2d.drawRect(50, startY, panelWidth - 100, horses.size() * (trackHeight + spaceBetweenTracks) - spaceBetweenTracks);
    }
    int preferredHeight = calculatePanelHeight();
    Dimension newSize = new Dimension(raceTrackPanel.getWidth(), preferredHeight);
    raceTrackPanel.setPreferredSize(newSize); 
    raceTrackPanel.revalidate(); 
    g2d.dispose();
    g2d.dispose();
}

private int calculatePanelHeight() {
    int trackHeight = 50; // Height of each lane
    int spaceBetweenTracks = 10; // Space between tracks
    int padding = 20; // Additional padding at top and bottom
    int totalHeight = (trackHeight + spaceBetweenTracks) * horses.size() + padding;
    return totalHeight;
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        try {
            new AdvancedRaceGUI();
        } catch (IOException e) {
        
            e.printStackTrace();
        }
    });
}
}