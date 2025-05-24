package util;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GraphPartitioner {
    private final RawGraph g;
    private final int k;
    private final int minSize;
    private final int maxSize;

    public GraphPartitioner(RawGraph g, int k, double x) {
        this.g = g;
        this.k = k;
        double avg = (double) g.getN() / k;
        this.minSize = (int) Math.floor(avg * (1 - x/100.0));
        this.maxSize = (int) Math.ceil (avg * (1 + x/100.0));
    }

    public int[] partition(int numStarts, double initialTemp, double coolingRate) {
        int n = g.getN();
        int bestCut = Integer.MAX_VALUE;
        int[] bestPart = new int[n];

        for (int run = 0; run < numStarts; run++) {
            int[] part     = new int[n];
            int[] partSize = new int[k];
            initialPartition(part, partSize);
            simulatedAnnealing(part, partSize, initialTemp, coolingRate);
            localRefinement(part, partSize);
            int cut = computeCutEdges(part);
            if (cut < bestCut) {
                bestCut = cut;
                System.arraycopy(part, 0, bestPart, 0, n);
            }
        }
        System.out.println("Best cut = " + bestCut);
        return bestPart;
    }

    private void initialPartition(int[] part, int[] partSize) {
        for (int p = 0; p < k; p++) partSize[p] = 0;
        for (int v = 0; v < g.getN(); v++) {
            int p = v % k;
            part[v] = p;
            partSize[p]++;
        }
    }

    private int computeCutEdges(int[] part) {
        int cut = 0;
        List<List<Integer>> adj = g.getAdj();
        for (int u = 0; u < g.getN(); u++) {
            for (int v : adj.get(u)) {
                if (u < v && part[u] != part[v]) cut++;
            }
        }
        return cut;
    }

    private boolean validBalance(int[] partSize, int p, int q) {
        int newP = partSize[p] - 1;
        int newQ = partSize[q] + 1;
        return newP >= minSize && newQ <= maxSize;
    }

    private int deltaCut(int v, int p, int q, int[] part) {
        int delta = 0;
        for (int u : g.getAdj().get(v)) {
            if (part[u] == p) delta++;
            if (part[u] == q) delta--;
        }
        return delta;
    }

    private void simulatedAnnealing(int[] part, int[] partSize, double T, double coolingRate) {
        Random rnd = ThreadLocalRandom.current();
        int n = g.getN();
        while (T > 1e-4) {
            int v = rnd.nextInt(n);
            int p = part[v];
            int q = rnd.nextInt(k);
            if (q == p) { T *= coolingRate; continue; }
            if (!validBalance(partSize, p, q)) { T *= coolingRate; continue; }
            int d = deltaCut(v, p, q, part);
            if (d < 0 || Math.exp(-d/T) > rnd.nextDouble()) {
                part[v] = q;
                partSize[p]--;
                partSize[q]++;
            }
            T *= coolingRate;
        }
    }

    private void localRefinement(int[] part, int[] partSize) {
        boolean improved = true;
        while (improved) {
            improved = false;
            for (int v = 0; v < g.getN(); v++) {
                int p = part[v];
                for (int q = 0; q < k; q++) {
                    if (q == p) continue;
                    if (!validBalance(partSize, p, q)) continue;
                    int d = deltaCut(v, p, q, part);
                    if (d < 0) {
                        part[v] = q;
                        partSize[p]--;
                        partSize[q]++;
                        improved = true;
                        break;
                    }
                }
            }
        }
    }
}
