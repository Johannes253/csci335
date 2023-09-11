package search.bestfirst;

import search.SearchNode;
import search.SearchQueue;

import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.ToIntFunction;

public class BestFirstQueue<T> implements SearchQueue<T> {
    // TODO: Implement this class
    // HINT: Use java.util.PriorityQueue. It will really help you.

    private final PriorityQueue<SearchNode<T>> queue;
    private final ToIntFunction<T> heuristic;

    public BestFirstQueue(ToIntFunction<T> heuristic) {
        // TODO: Your code here
        this.heuristic = heuristic;
        this.queue = new PriorityQueue<>(Comparator.comparingInt(node -> this.heuristic.applyAsInt(node.getValue())));
    }

    @Override
    public void enqueue(SearchNode<T> node) {
        // TODO: Your code here
        queue.offer(node);
    }

    @Override
    public Optional<SearchNode<T>> dequeue() {
        // TODO: Your code here
        return Optional.ofNullable(queue.poll());
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}