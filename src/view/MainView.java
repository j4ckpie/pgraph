package view;

import model.ImportType;
import model.Node;
import model.Status;
import util.FileReader;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MainView extends JFrame {
    private List<Node> nodes;
    private int maxNode;
    private MainScrollPane scrollPane;
    private ArgsButton generateButton;
    private ArgsLabel status;
    private ArgsTextField k;
    private ArgsTextField x;
    private ImportType importType;

    public MainView() {
        createMainViewWindow();
    }

    private void createMainViewWindow() {
        // Basic config
        setTitle("pgraph");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.DARK_GRAY);
        setLocationRelativeTo(null);

        // Create components
        MainMenuBar menuBar = new MainMenuBar();
        MainMenu menu = new MainMenu("pgraph");
        MainMenu settings = new MainMenu("settings");
        MainMenu help = new MainMenu("help");
        MainMenuItem newItem = new MainMenuItem("new");
        MainMenuItem importDividedItem = new MainMenuItem("import divided");
        MainMenuItem importRawItem = new MainMenuItem("import raw");
        MainMenuItem exitItem = new MainMenuItem("exit");
        MainMenuItem fontSmallItem = new MainMenuItem("font 16pt");
        MainMenuItem fontMediumItem = new MainMenuItem("font 20pt");
        MainMenuItem fontLargeItem = new MainMenuItem("font 24pt");
        MainMenuItem aboutItem = new MainMenuItem("about");
        MainMenuItem creditsItem = new MainMenuItem("credits");
        ArgsPanel argsPanel = new ArgsPanel();
        generateButton = new ArgsButton("Generate");
        k = new ArgsTextField();
        x = new ArgsTextField();
        ArgsLabel kLabel = new ArgsLabel("Parts [k]: ");
        ArgsLabel xLabel = new ArgsLabel("Margin percentage [x]: ");
        status = new ArgsLabel(Status.GRAPH_NOT_LODAED.toString());

        // Add components
        menu.add(newItem);
        menu.add(importDividedItem);
        menu.add(importRawItem);
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
        argsPanel.add(kLabel);
        argsPanel.add(k);
        argsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        argsPanel.add(xLabel);
        argsPanel.add(x);
        argsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        generateButton.setEnabled(false);
        argsPanel.add(generateButton);
        argsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        argsPanel.add(status);
        add(argsPanel, BorderLayout.SOUTH);

        // Actions
        newItem.addActionListener(e -> newWindow());
        importDividedItem.addActionListener(e -> importDividedGraph());
        importRawItem.addActionListener(e -> importRawGraph());
        exitItem.addActionListener(e -> exitWindow());
        fontSmallItem.addActionListener(e -> setFontSmall());
        fontMediumItem.addActionListener(e -> setFontMedium());
        fontLargeItem.addActionListener(e -> setFontLarge());
        aboutItem.addActionListener(e -> aboutWindow());
        creditsItem.addActionListener(e -> creditsWindow());
        generateButton.addActionListener(e -> drawGraph());

        // Render window
        setVisible(true);
    }

    // Draw graph on screen
    private void drawGraph() {
        switch(importType) {
            case DIVIDED -> createImportGraphView((int)Math.ceil(Math.sqrt(maxNode)));
            case RAW -> throw new UnsupportedOperationException("Not supported yet!");
        }
    }

    // Create view for imported view
    private void createImportGraphView(int size) {
        GraphPanel graphPanel = new GraphPanel(size, size, nodes);
        scrollPane = new MainScrollPane(graphPanel);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    // Create new app window
    private void newWindow() {
        dispose();
        getContentPane().removeAll();
        createMainViewWindow();
    }

    // Import graph from file
    private void importDividedGraph() {
        MainFileChooser chooser = new MainFileChooser();
        chooser.setCurrentDirectory(new File("../pgraph/"));
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            nodes = FileReader.readFile(file);
            maxNode = 0;
            for(Node node : nodes) {
                if(node.getId() > maxNode) {
                    maxNode = node.getId();
                }
            }

            System.out.println(maxNode);

            // Clear panel before drawing new
            if(scrollPane != null) {
                getContentPane().remove(scrollPane);
            }

            importType = ImportType.DIVIDED;
            generateButton.setEnabled(true);
            status.setText(Status.DIVIDED_GRAPH_LOADED.toString());
            k.setText("");
            k.setEnabled(false);
            x.setText("");
            x.setEnabled(false);
        }
    }

    // Import raw graph to file
    private void importRawGraph() {
        MainFileChooser chooser = new MainFileChooser();
        chooser.setCurrentDirectory(new File("../pgraph/"));
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
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
