package util;

import model.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {
    // Read file and return nodes
    public static List<Node> readFile(File file) {
        List<Node> nodes = new ArrayList<>();
        try {
            // Read file
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine().replace(",", "");
                String[] tokens = line.trim().split("\\s+");

                // Add new nodes
                int nodeGroup = Integer.parseInt(tokens[1]);
                for(int i = 5; i < tokens.length; i++) {
                    nodes.add(new Node(Integer.parseInt(tokens[i]), nodeGroup));
                }

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return nodes;
    }
}
