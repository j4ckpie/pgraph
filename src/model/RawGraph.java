package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RawGraph {
    private final int maxColumns;
    private final int[] vertexIndices;
    private final int[] rowStarts;
    private final List<List<Integer>> adj;
    private final int n;
    private final int rows;
    private ArrayList<Node> nodes;

    public int[] getVertexIndices() {
        return vertexIndices;
    }

    public int getMaxColumns() {
        return maxColumns;
    }

    public int[] getRowStarts() {
        return rowStarts;
    }

    public List<List<Integer>> getAdj(){
        return adj;
    }

    public int getN (){
        return n;
    }

    public int getRows(){
        return rows;
    }

    public void setNodes(ArrayList<Node> x){
        nodes = x;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    private RawGraph(int maxColumns, int[] vertexIndices, int[] rowStarts) {
        this.maxColumns    = maxColumns;
        this.vertexIndices = vertexIndices;
        this.rowStarts     = rowStarts;
        this.n             = vertexIndices.length;
        this.rows          = rowStarts.length - 1;
        this.adj            = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        this.nodes = new ArrayList<>(n);
        for(int r=0;r<rows;r++){
            int start = rowStarts[r], end = rowStarts[r+1];
            for(int pos = start; pos < end; pos++){
                nodes.add(new Node(r*rows+vertexIndices[pos]+1,1));
            }
        }
        nodes.sort(Comparator.comparingInt(node->node.getId()));
    }

    public static RawGraph load(File file) throws IOException {
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            // maxColumns
            String l1 = r.readLine();
            if (l1 == null) throw new IOException("Brak linii 1");
            int maxColumns = Integer.parseInt(l1.strip());

            // vertexIndices
            String l2 = r.readLine();
            if (l2 == null) throw new IOException("Brak linii 2");
            String[] t2 = l2.strip().split(";");
            int[] vertexIndices = new int[t2.length];
            for (int i = 0; i < t2.length; i++) {
                vertexIndices[i] = Integer.parseInt(t2[i]);
            }

            // rowStarts
            String l3 = r.readLine();
            if (l3 == null) throw new IOException("Brak linii 3");
            String[] t3 = l3.strip().split(";");
            int[] rowStarts = new int[t3.length];
            for (int i = 0; i < t3.length; i++) {
                rowStarts[i] = Integer.parseInt(t3[i]);
            }

            RawGraph g = new RawGraph(maxColumns, vertexIndices, rowStarts);

            // group
            String l4 = r.readLine();
            if (l4 == null) throw new IOException("Brak linii 4");
            String[] tg = l4.strip().split(";");
            int[] group = new int[tg.length];
            for (int i = 0; i < tg.length; i++) {
                group[i] = Integer.parseInt(tg[i]);
            }

            // gptr
            String l5 = r.readLine();
            if (l5 == null) throw new IOException("Brak linii 5");
            String[] tp = l5.strip().split(";");
            int[] gptr = new int[tp.length];
            for (int i = 0; i < tp.length; i++) {
                gptr[i] = Integer.parseInt(tp[i]);
            }

            for (int h = 0; h + 1 < gptr.length; h++) {
                int st = gptr[h], en = gptr[h + 1];
                for (int i = st; i < en; i++) {
                    for (int j = i + 1; j < en; j++) {
                        int u = group[i], v = group[j];
                        g.adj.get(u).add(v);
                        g.adj.get(v).add(u);
                    }
                }
            }
            return g;
        }
    }
}
