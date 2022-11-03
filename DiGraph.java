package ngordnet.main;
import java.util.*;

public class DiGraph {
    private List<List<Integer>> adj = new ArrayList<>();

    public DiGraph(List<Edge> edges) {
        int n = 0;
        for (Edge e: edges) {
            n = Integer.max(n, Integer.max(e.src, e.dest));
        }
        for (int i = 0; i <= n; i++) {
            adj.add(i, new ArrayList<>());
        }
    }

    public void addEdge(List<Edge> edges) {
        for (Edge curr: edges) {
            adj.get(curr.src).add(curr.dest);
        }
    }
    Iterable<Integer> adjacent(int src) {
        return this.adj.get(src);
    }
}
