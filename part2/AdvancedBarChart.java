
import javax.swing.*;
import java.awt.*;

public class AdvancedBarChart extends JPanel {
    private java.util.List<Double> data;
    private java.util.List<Color> barColors;
    private java.util.List<String> barLabels;
    private int barThickness;

    public AdvancedBarChart() {
        data = new java.util.ArrayList<>();
        barColors = new java.util.ArrayList<>();
        barLabels = new java.util.ArrayList<>();
        barThickness = 50;
    }

    public void addData(Color color, double value, String label) {
        data.add(value);
        barColors.add(color);
        barLabels.add(label);
    }

    public void setBarThickness(int thickness) {
        barThickness = thickness;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        int barHeight = height - 100;
        int barSpacing = 10;
        int chartWidth = width - 100;
        int chartHeight = barHeight - 50;
        int x = 50;
        int y = 50;

        double maxValue = data.stream().mapToDouble(Double::doubleValue).max().orElse(0);

        g.setColor(Color.BLACK);
        g.drawRect(x, y, chartWidth, chartHeight);

        int barCount = data.size();
        int totalBarWidth = barCount * barThickness + (barCount - 1) * barSpacing;
        int startX = x + (chartWidth - totalBarWidth) / 2;

        for (int i = 0; i < barCount; i++) {
            double value = data.get(i);
            barHeight = (int) (chartHeight * (value / maxValue));
            int barY = y + chartHeight - barHeight;

            g.setColor(barColors.get(i));
            g.fillRect(startX, barY, barThickness, barHeight);

            g.setColor(Color.BLACK);
            g.drawString(barLabels.get(i), startX, barY - 10);

            startX += barThickness + barSpacing;
        }

        g.setColor(Color.BLACK);
        g.drawLine(x, y + chartHeight, x + chartWidth, y + chartHeight);
        g.drawLine(x, y, x, y + chartHeight);

        int numTicks = 5;
        for (int i = 0; i <= numTicks; i++) {
            double value = maxValue * i / numTicks;
            int tickY = y + chartHeight - (int) (chartHeight * i / numTicks);
            g.drawLine(x - 5, tickY, x, tickY);
            g.drawString(String.format("%.1f", value), x - 40, tickY);
        }
    }
}