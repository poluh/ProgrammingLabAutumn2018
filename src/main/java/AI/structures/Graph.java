package AI.structures;

import java.util.*;
import java.util.stream.Stream;

public class Graph<V> {

    private Map<Node<V>, Set<Node<V>>> nodes;
    private Map<V, Node<V>> values;

    public Graph() {
        nodes = new HashMap<>();
        values = new HashMap<>();
    }

    public Graph<V> addNode(Node<V> node) {
        if (nodes.containsKey(node)) return this;
        nodes.put(node, new HashSet<>());
        values.put(node.getValue(), node);
        return this;
    }

    public Graph<V> connectNodes(Node<V> from, Node<V> to) {
        if (!nodes.containsKey(from)) addNode(from);
        nodes.get(from).add(to);
        return this;
    }

    public void clear() {
        nodes.clear();
    }

    public Set<Node<V>> getNeighbors(V root) {
        if (!values.containsKey(root)) return Collections.emptySet();
        return nodes.get(values.get(root));
    }

    public Stream<Node<V>> getNodes() {
        return nodes.keySet().stream();
    }

}
