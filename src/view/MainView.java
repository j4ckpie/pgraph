package view;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    public MainView() {
        createMainViewWindow();
    }

    private void createMainViewWindow() {
        // Basic config
        setTitle("pgraph");
        setSize(800, 600);
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
        MainMenuItem fontSmallItem = new MainMenuItem("font 16px");
        MainMenuItem fontMediumItem = new MainMenuItem("font 20px");
        MainMenuItem fontLargeItem = new MainMenuItem("font 24px");
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

    // Create new app window
    private void newWindow() {
        dispose();
        createMainViewWindow();
    }

    // Import graph from file
    private void importGraph() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    // Export graph to file
    private void exportGraph() {
        throw new UnsupportedOperationException("Not supported yet");
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
