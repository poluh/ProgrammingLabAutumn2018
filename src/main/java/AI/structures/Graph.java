package AI.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Graph<T> {

    private Map<Node<T>, List<Node<T>>> nodes;

    public Graph<T> addNode(Node<T> node) {
        if (nodes.containsKey(node)) return this;
        nodes.put(node, new ArrayList<>());
        return this;
    }

    public Graph<T> connectNodes(Node<T> from, Node<T> to) {
        if (!nodes.containsKey(from)) return this;
        nodes.get(from).add(to);
        return this;
    }

    public void clear() {
        nodes.clear();
    }

    public Stream<Node<T>> getNodes() {
        return nodes.keySet().stream();
    }

}
