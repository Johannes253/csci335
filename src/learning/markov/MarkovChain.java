package learning.markov;

import learning.core.Histogram;

import java.util.*;

public class MarkovChain<L,S> {
    private LinkedHashMap<L, HashMap<Optional<S>, Histogram<S>>> label2symbol2symbol = new LinkedHashMap<>();

    public Set<L> allLabels() {return label2symbol2symbol.keySet();}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (L language: label2symbol2symbol.keySet()) {
            sb.append(language);
            sb.append('\n');
            for (Map.Entry<Optional<S>, Histogram<S>> entry: label2symbol2symbol.get(language).entrySet()) {
                sb.append("    ");
                sb.append(entry.getKey());
                sb.append(":");
                sb.append(entry.getValue().toString());
                sb.append('\n');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    // Increase the count for the transition from prev to next.
    // Should pass SimpleMarkovTest.testCreateChains().
    public void count(Optional<S> prev, L label, S next) {
        // TODO: YOUR CODE HERE

        if (!label2symbol2symbol.containsKey(label))
                label2symbol2symbol.put(label, new HashMap<>());

        HashMap<Optional<S>, Histogram<S>> innerMap = label2symbol2symbol.get(label);

        if (!innerMap.containsKey(prev))
            innerMap.put(prev, new Histogram<>());

        innerMap.get(prev).bump(next);
    }

    // Returns P(sequence | label)
    // Should pass SimpleMarkovTest.testSourceProbabilities() and MajorMarkovTest.phraseTest()
    //
    // HINT: Be sure to add 1 to both the numerator and denominator when finding the probability of a
    // transition. This helps avoid sending the probability to zero.
    public double probability(ArrayList<S> sequence, L label) {
        // TODO: YOUR CODE HERE
        if (sequence == null || sequence.isEmpty() || !label2symbol2symbol.containsKey(label))
            return 0.0;


        HashMap<Optional<S>, Histogram<S>> innerMap = label2symbol2symbol.get(label);

        double probability = 1.0;
        Optional<S> prev = Optional.empty();

        for (S currentSymbol : sequence) {
            Histogram<S> histogram = innerMap.get(prev);

            int transitionCount = histogram != null ? histogram.getCountFor(currentSymbol) : 0;
            int totalTransitions = histogram != null ? histogram.getTotalCounts() : 0;

            int vocabularySize = innerMap.size();

            double transitionProbability = (double) (transitionCount + 1) / (totalTransitions + vocabularySize + 1);
            probability *= transitionProbability;

            prev = Optional.of(currentSymbol);
        }

        return probability;
    }

    // Return a map from each label to P(label | sequence).
    // Should pass MajorMarkovTest.testSentenceDistributions()
    public LinkedHashMap<L,Double> labelDistribution(ArrayList<S> sequence) {
        // TODO: YOUR CODE HERE
            LinkedHashMap<L, Double> distribution = new LinkedHashMap<>();

            double totalLikelyhood = 0.0;
            for (L label : label2symbol2symbol.keySet()) {
                double likelyhood = probability(sequence, label);
                distribution.put(label, likelyhood);
                totalLikelyhood += likelyhood;
            }

            for (L label : distribution.keySet()) {
                double normalizedProbability = distribution.get(label) / totalLikelyhood;
                distribution.put(label, normalizedProbability);
            }

            return distribution;
    }

    // Calls labelDistribution(). Returns the label with highest probability.
    // Should pass MajorMarkovTest.bestChainTest()
    public L bestMatchingChain(ArrayList<S> sequence) {
        // TODO: YOUR CODE HERE
        return null;
    }
}
