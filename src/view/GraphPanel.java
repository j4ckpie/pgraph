package view;

import model.Node;
import util.ValueHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GraphPanel extends JPanel {
    private final int COLOR_AMOUNT = 8;
    private int rows;
    private int cols;
    private int cellSize = 15;
    private List<Node> nodes;
    private List<Color> palette;

    public GraphPanel(int cols, int rows, List<Node> nodes) {
        this.rows = rows;
        this.cols = cols;
        this.nodes = nodes;

        // Set cell size
        cellSize = (int)ValueHelper.clamp((double)Toolkit.getDefaultToolkit().getScreenSize().width / cols, 15.0, 50.0);

        // Basic Config
        setBackground(Color.DARK_GRAY);
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        generateColorPalette(COLOR_AMOUNT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        // Draw grid lines
        for (int row = 0; row <= rows; row++) {
            g2d.drawLine(0, row * cellSize, cols * cellSize, row * cellSize);
        }
        for (int col = 0; col <= cols; col++) {
            g2d.drawLine(col * cellSize, 0, col * cellSize, rows * cellSize);
        }

        // Draw nodes
        for(Node node : nodes) {
            int nodeSize = (int)(cellSize * 0.75);
            int nodeId = node.getId();
            int nodeGroup = node.getGroup();
            int x = (nodeId - 1) / cols;
            int y = (nodeId - 1) % cols;

            int drawX = y * cellSize + (cellSize - nodeSize) / 2;
            int drawY = x * cellSize + (cellSize - nodeSize) / 2;

            g2d.setColor(palette.get(nodeGroup % 32));
            g2d.fillOval(drawX, drawY, nodeSize, nodeSize);
        }
    }

    // Generate colors
    public void generateColorPalette(int size) {
        palette = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            // float hue = ThreadLocalRandom.current().nextFloat(0, 255);
            float hue = (float)i / size;
            float saturation = 1.0f;
            float brightness = 1.0f;
            palette.add(Color.getHSBColor(hue, saturation, brightness));
        }
    }
}
