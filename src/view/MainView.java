package view;

import model.ImportType;
import model.Node;
import model.Status;
import util.FileReader;
import util.GraphPartitioner;
import model.RawGraph;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
    private RawGraph currentRaw;
    private ArrayList<Node> colored;
    private MainMenuItem exportRawItem;


    public MainView() {createMainViewWindow();}

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
        MainMenu help = new MainMenu("help");
        MainMenuItem newItem = new MainMenuItem("new");
        MainMenuItem importDividedItem = new MainMenuItem("import divided");
        MainMenuItem importRawItem = new MainMenuItem("import raw");
        exportRawItem = new MainMenuItem("export raw");
        MainMenuItem exitItem = new MainMenuItem("exit");
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
        menu.add(exportRawItem);
        exportRawItem.setEnabled(false);
        menu.add(exitItem);
        help.add(aboutItem);
        help.add(creditsItem);
        menuBar.add(menu);
        menuBar.add(help);
        setJMenuBar(menuBar);
        argsPanel.add(kLabel);
        argsPanel.add(k);
        argsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        argsPanel.add(xLabel);
        argsPanel.add(x);
        argsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        generateButton.setEnabled(false);
        k.setEnabled(false);
        x.setEnabled(false);
        argsPanel.add(generateButton);
        argsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        argsPanel.add(status);
        add(argsPanel, BorderLayout.SOUTH);

        // Actions
        newItem.addActionListener(e -> newWindow());
        importDividedItem.addActionListener(e -> importDividedGraph());
        importRawItem.addActionListener(e -> importRawGraph());
        exportRawItem.addActionListener(e -> export(colored));
        exitItem.addActionListener(e -> exitWindow());
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
            case RAW -> createImportRawGraphView();
        }
    }
    // Create view for imported raw view
    private void createImportRawGraphView() {
        int kparam = Integer.parseInt(k.getText());
        double xparam = Double.parseDouble(x.getText());
        GraphPartitioner p = new GraphPartitioner(currentRaw, kparam, xparam);
        int[] part = p.partition(5, 10000.0, 0.999);
        colored = new ArrayList<>(currentRaw.getVertexIndices().length);
        int[] ind = currentRaw.getVertexIndices();
        for (int r = 0; r < currentRaw.getRows(); r++) {
            int start = currentRaw.getRowStarts()[r];
            int end   = currentRaw.getRowStarts()[r+1];
            for (int pos = start; pos < end; pos++) {
                int v = currentRaw.getVertexIndices()[pos];
                int grp = part[v];
                colored.add(new Node(r*currentRaw.getRows()+ind[pos]+1, grp));
            }
        }
        currentRaw.setNodes(colored);
        if(scrollPane != null) {
            getContentPane().remove(scrollPane);
        }

        exportRawItem.setEnabled(true);
        GraphPanel graphPanel = new GraphPanel(currentRaw.getMaxColumns(), currentRaw.getRows(), colored, currentRaw);
        scrollPane = new MainScrollPane(graphPanel);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    // Create view for imported view
    private void createImportGraphView(int size) {
        GraphPanel graphPanel = new GraphPanel(size, size, nodes, null);
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
            exportRawItem.setEnabled(false);
        }
    }

    // Import raw graph to file
    private void importRawGraph() {
        MainFileChooser chooser = new MainFileChooser();
        chooser.setCurrentDirectory(new File("../pgraph/"));
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                currentRaw = RawGraph.load(file);
                status.setText(Status.RAW_GRAPH_LOADED.toString());
                importType = ImportType.RAW;
                generateButton.setEnabled(true);
                k.setText("");
                k.setEnabled(true);
                x.setText("");
                x.setEnabled(true);
            } catch (IOException ex){
                JOptionPane.showMessageDialog(this,
                        "Błąd wczytywania:\n" + ex.getMessage(),
                        "Błąd", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    // Export raw graph divided into parts
    private void export(List<Node> nodes) {
        MainFileChooser chooser = new MainFileChooser();
        chooser.setCurrentDirectory(new File("../pgraph/"));
        if(chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                FileWriter writer = new FileWriter(file, true);
                for(Node node : nodes) {
                    writer.write(node.getGroup() + " ");
                }
            } catch (IOException ex){
                JOptionPane.showMessageDialog(this,
                        "Błąd wczytywania:\n" + ex.getMessage(),
                        "Błąd", JOptionPane.ERROR_MESSAGE);
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
