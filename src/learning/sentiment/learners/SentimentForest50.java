package learning.sentiment.learners;

import learning.core.Histogram;
import learning.decisiontree.RandomForest;
import learning.sentiment.core.SentimentAnalyzer;

public class SentimentForest50 extends RandomForest<Histogram<String>, String, String, Integer> {
    public SentimentForest50() {
        super(50, SentimentAnalyzer::allFeatures, Histogram::getCountFor, i -> i + 1);
    }
}
