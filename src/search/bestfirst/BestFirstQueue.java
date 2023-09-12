package search.bestfirst;

import search.SearchNode;
import search.SearchQueue;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.ToIntFunction;

public class BestFirstQueue<T> implements SearchQueue<T> {
    // TODO: Implement this class
    // HINT: Use java.util.PriorityQueue. It will really help you.

    private final PriorityQueue<SearchNode<T>> queue;
    private final ToIntFunction<T> heuristic;
    private final Map<T, Integer> visited;

    public BestFirstQueue(ToIntFunction<T> heuristic) {
        // TODO: Your code here
        this.heuristic = heuristic;
        this.queue = new PriorityQueue<>(Comparator.comparingInt(this::totalEstimate));
        this.visited = new HashMap<>();
    }

    private int totalEstimate(SearchNode<T> node) {
        // TODO: Your code here (to combine heuristic and depth)
        return this.heuristic.applyAsInt(node.getValue()) + node.getDepth();
    }

    @Override
    public void enqueue(SearchNode<T> node) {
        // TODO: Your code here (to consider visited nodes and their estimates)
        int currentEstimate = totalEstimate(node);

        // If the node is already visited and has a lesser or equal estimate, then skip.
        if (visited.containsKey(node.getValue()) && visited.get(node.getValue()) <= currentEstimate) {
            return;
        }

        queue.offer(node);
        visited.put(node.getValue(), currentEstimate);
    }

    @Override
    public Optional<SearchNode<T>> dequeue() {
        // TODO: Your code here
        return Optional.ofNullable(queue.poll());
    }

}
