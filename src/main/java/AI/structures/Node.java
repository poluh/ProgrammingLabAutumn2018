package AI.structures;

import java.util.Objects;

public class Node<V> {

    private V value;
    private double weight;

    public Node(V value, double weight) {
        this.value = value;
        this.weight = weight;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node<?> node = (Node<?>) o;
        return Double.compare(node.getWeight(), getWeight()) == 0 &&
                Objects.equals(getValue(), node.getValue());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getValue(), getWeight());
    }
}
