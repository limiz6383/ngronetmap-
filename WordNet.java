package ngordnet.main;
import java.util.*;
import edu.princeton.cs.algs4.In;

import static ngordnet.main.DFS.dfs;

public class WordNet {
    private In synsetFile;
    private In hyponymFile;
    private Map<String, Set<Integer>> wordToId;
    private Map<Integer, Set<String>> idToWords;
    private DiGraph hyponymGraph;
    private int size = 0;
    private List<Edge> edges;

    public WordNet(String synsetFilename, String hyopnymFilename) {
        this.synsetFile = new In(synsetFilename);
        this.hyponymFile = new In(hyopnymFilename);
        this.wordToId = new HashMap<>();
        this.idToWords = new HashMap<>();

        int id;
        while(!synsetFile.isEmpty()) {
            String[] line = this.synsetFile.readLine().split(",");
            String[] words = line[1].split(" ");
            id = Integer.parseInt(line[0]);
            TreeSet set = new TreeSet();
            this.size +=1;

            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                set.add(word);
                if (this.wordToId.containsKey(word)) {
                    (this.wordToId.get(word)).add(id);
                } else {
                    TreeSet newSet = new TreeSet();
                    newSet.add(id);
                    this.wordToId.put(word, newSet);
                }
            }

            this.idToWords.put(id, new TreeSet(set));
        }

        edges = new ArrayList<>(size);
        while(!this.hyponymFile.isEmpty()) {
            String[] line = this.hyponymFile.readLine().split(",");
            int src = Integer.parseInt(line[0]);
            List<Integer> dests = new ArrayList<>();
            for (int i = 1; i < line.length; i++) {
                dests.add(Integer.parseInt(line[i]));
            }
            for (int dest : dests) {
                Edge newEdge = new Edge(src, dest);
                edges.add(newEdge);
            }
        }
        this.hyponymGraph = new DiGraph(edges);
        hyponymGraph.addEdge(edges);

    }

    public Set<String> hyponyms(String word) {
        List<Integer> IDs = new ArrayList<>();
        TreeSet result = new TreeSet();

        if (this.wordToId.containsKey(word)) {
            IDs.addAll(this.wordToId.get(word));
            for (int id : IDs) {
                Iterator traverse = dfs(hyponymGraph, id).iterator();
                while (traverse.hasNext()) {
                    int i = (Integer)traverse.next();
                    result.addAll(idToWords.get(i));
                }
            }
        }
        return result;
    }
}
