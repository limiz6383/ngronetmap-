package ngordnet.main;
import java.util.*;

public class DFS {

    private TreeSet result;

    public DFS() {
    }

    public static Set<Integer> dfs (DiGraph graph, int start) {
        Set<Integer> marked = new TreeSet<>();
        TreeSet result = new TreeSet<>();
        Stack<Integer> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            int curr = stack.pop();
            if (!stack.contains(curr)) {
                result.add(curr);
                marked.add(curr);
            }

            for (int v : graph.adjacent(curr)) {
                if (!marked.contains(v)) {
                    stack.push(v);
                }
            }
        }
        return result;
    }
//        result.add(start);
//        marked.add(start);
//        if (graph.adjacent(start) == null) {
//            return result.iterator();
//        } else {
//            for (int v : graph.adjacent(start)) {
//                if (!marked.contains(v)) {
//                    dfs(graph, v, marked);
//                }

}
