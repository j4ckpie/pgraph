package view;

import model.Node;
import util.FileReader;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainView extends JFrame {
    private List<Node> nodes;

    public MainView() {
        createMainViewWindow();
    }

    private void createMainViewWindow() {
        // Basic config
        setTitle("pgraph");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        MainMenuBar menuBar = new MainMenuBar();
        MainMenu menu = new MainMenu("pgraph");
        MainMenu settings = new MainMenu("settings");
        MainMenu help = new MainMenu("help");
        MainMenuItem newItem = new MainMenuItem("new");
        MainMenuItem importItem = new MainMenuItem("import");
        MainMenuItem exportItem = new MainMenuItem("export");
        MainMenuItem exitItem = new MainMenuItem("exit");
        MainMenuItem fontSmallItem = new MainMenuItem("font 16pt");
        MainMenuItem fontMediumItem = new MainMenuItem("font 20pt");
        MainMenuItem fontLargeItem = new MainMenuItem("font 24pt");
        MainMenuItem aboutItem = new MainMenuItem("about");
        MainMenuItem creditsItem = new MainMenuItem("credits");

        // Add components
        menu.add(newItem);
        menu.add(importItem);
        menu.add(exportItem);
        menu.add(exitItem);
        settings.add(fontSmallItem);
        settings.add(fontMediumItem);
        settings.add(fontLargeItem);
        help.add(aboutItem);
        help.add(creditsItem);
        menuBar.add(menu);
        menuBar.add(settings);
        menuBar.add(help);
        setJMenuBar(menuBar);

        // Actions
        newItem.addActionListener(e -> newWindow());
        importItem.addActionListener(e -> importGraph());
        exportItem.addActionListener(e -> exportGraph());
        exitItem.addActionListener(e -> exitWindow());
        fontSmallItem.addActionListener(e -> setFontSmall());
        fontMediumItem.addActionListener(e -> setFontMedium());
        fontLargeItem.addActionListener(e -> setFontLarge());
        aboutItem.addActionListener(e -> aboutWindow());
        creditsItem.addActionListener(e -> creditsWindow());

        // Render window
        setVisible(true);
    }

    // Create view for imported view
    private void createImportGraphView(int size) {
        GraphPanel graphPanel = new GraphPanel(size, size, nodes);
        JScrollPane scrollPane = new JScrollPane(graphPanel);
        add(scrollPane);
        setVisible(true);
    }

    // Create new app window
    private void newWindow() {
        dispose();
        createMainViewWindow();
    }

    // Import graph from file
    private void importGraph() {
        MainFileChooser chooser = new MainFileChooser();
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            nodes = FileReader.readFile(file);
            int maxNode = 0;
            for(Node node : nodes) {
                if(node.getId() > maxNode) {
                    maxNode = node.getId();
                }
            }
            createImportGraphView((int)Math.ceil(Math.sqrt(maxNode)));
        }
    }

    // Export graph to file
    private void exportGraph(/* data */) {
        MainFileChooser chooser = new MainFileChooser();
        if(chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            // Write data to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("" /* data */);
                JOptionPane.showMessageDialog(null, "Saved to file\n" + file.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Cannot save to file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Exit the app
    private void exitWindow() {
        System.exit(0);
    }

    // Set font size accordingly
    private void setFontSmall() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    private void setFontMedium() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    private void setFontLarge() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    // Show about section
    private void aboutWindow() {
        JOptionPane.showMessageDialog(this,
                """
                        pgraph partitions any graph into k parts (not necessarily connected), ensuring:
                        • balanced vertex distribution (max x% size difference),
                        • minimal edge cuts between parts.
                        pgraph can also import already divided graph provided by another team.
                        """, "about", JOptionPane.INFORMATION_MESSAGE);
    }

    // Show credits section
    private void creditsWindow() {
        JOptionPane.showMessageDialog(this,
                """
                        Made by Jakub Pietrala & Bartosz Starzyński.
                        JIMP2, PW 2025
                        """, "credits", JOptionPane.INFORMATION_MESSAGE);
    }
}
