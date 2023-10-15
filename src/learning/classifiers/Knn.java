package learning.classifiers;

import core.Duple;
import learning.core.Classifier;
import learning.core.Histogram;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.ToDoubleBiFunction;

// KnnTest.test() should pass once this is finished.
public class Knn<V, L> implements Classifier<V, L> {
    private ArrayList<Duple<V, L>> data = new ArrayList<>();
    private ToDoubleBiFunction<V, V> distance;
    private int k;

    public Knn(int k, ToDoubleBiFunction<V, V> distance) {
        this.k = k;
        this.distance = distance;
    }

    @Override
    public L classify(V value) {
        // TODO: Find the distance from value to each element of data. Use Histogram.getPluralityWinner()
        //  to find the most popular label.
        PriorityQueue<Duple<V, L>> closestNeighbors = new PriorityQueue<>((d1, d2) ->
                Double.compare(distance.applyAsDouble(value, d2.getFirst()),  distance.applyAsDouble(value, d1.getFirst()))
        );

        for (Duple<V, L> d : data) {
            closestNeighbors.add(d);
            if (distance.applyAsDouble(value, d.getFirst()) <= distance.applyAsDouble(value, closestNeighbors.peek().getFirst())) {
                closestNeighbors.poll();
                closestNeighbors.add(d);
            }
        }

        Histogram<L> histogram = new Histogram<>();
        for (Duple<V, L> d : closestNeighbors)
            histogram.bump(d.getSecond());

        return histogram.getPluralityWinner();
    }

    @Override
    public void train(ArrayList<Duple<V, L>> training) {
        // TODO: Add all elements of training to data.
        data.addAll(training);
    }
}
