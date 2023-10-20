package learning.decisiontree;

import core.Duple;
import learning.core.Histogram;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DTTrainer<V,L, F, FV extends Comparable<FV>> {
	private ArrayList<Duple<V,L>> baseData;
	private boolean restrictFeatures;
	private Function<ArrayList<Duple<V,L>>, ArrayList<Duple<F,FV>>> allFeatures;
	private BiFunction<V,F,FV> getFeatureValue;
	private Function<FV,FV> successor;
	
	public DTTrainer(ArrayList<Duple<V, L>> data, Function<ArrayList<Duple<V, L>>, ArrayList<Duple<F,FV>>> allFeatures,
					 boolean restrictFeatures, BiFunction<V,F,FV> getFeatureValue, Function<FV,FV> successor) {
		baseData = data;
		this.restrictFeatures = restrictFeatures;
		this.allFeatures = allFeatures;
		this.getFeatureValue = getFeatureValue;
		this.successor = successor;
	}
	
	public DTTrainer(ArrayList<Duple<V, L>> data, Function<ArrayList<Duple<V,L>>, ArrayList<Duple<F,FV>>> allFeatures,
					 BiFunction<V,F,FV> getFeatureValue, Function<FV,FV> successor) {
		this(data, allFeatures, false, getFeatureValue, successor);
	}

	// TODO: Call allFeatures.apply() to get the feature list. Then shuffle the list, retaining
	//  only targetNumber features. Should pass DTTest.testReduced().
	public static <V,L, F, FV  extends Comparable<FV>> ArrayList<Duple<F,FV>> reducedFeatures(ArrayList<Duple<V,L>> data, Function<ArrayList<Duple<V, L>>, ArrayList<Duple<F,FV>>> allFeatures, int targetNumber) {
		ArrayList<Duple<F,FV>> features = allFeatures.apply(data);
		Collections.shuffle(features);
		return new ArrayList<>(features.subList(0, Math.min(targetNumber, features.size())));
    }
	
	public DecisionTree<V,L,F,FV> train() {
		return train(baseData);
	}

	public static <V,L> int numLabels(ArrayList<Duple<V,L>> data) {
		return data.stream().map(Duple::getSecond).collect(Collectors.toUnmodifiableSet()).size();
	}
	
	private DecisionTree<V,L,F,FV> train(ArrayList<Duple<V,L>> data) {
		// TODO: Implement the decision tree learning algorithm
		if (numLabels(data) == 1) {
			// TODO: Return a leaf node consisting of the only label in data
			return new DTLeaf<>(data.get(0).getSecond());
		} else {
			// TODO: Return an interior node.
			//  If restrictFeatures is false, call allFeatures.apply() to get a complete list
			//  of features and values, all of which you should cosider when splitting.
			//  If restrictFeatures is true, call reducedFeatures() to get sqrt(# features)
			//  of possible features/values as candidates for the split. In either case,
			//  for each feature/value combination, use the splitOn() function to break the
			//  data into two parts. Then use gain() on each split to figure out which
			//  feature/value combination has the highest gain. Use that combination, as
			//  well as recursively created left and right nodes, to create the new
			//  interior node.
			//  Note: It is possible for the split to fail; that is, you can have a split
			//  in which one branch has zero elements. In this case, return a leaf node
			//  containing the most popular label in the branch that has elements.
			ArrayList<Duple<F,FV>> featureValues;
			Duple<F,FV> bestFeatureValue = null;
			double maxGain = -Double.MAX_VALUE;
			ArrayList<Duple<V,L>> leftSplit = null;
			ArrayList<Duple<V,L>> rightSplit = null;

			if(!restrictFeatures)
				featureValues = allFeatures.apply(data);
			else
				featureValues = reducedFeatures(data, allFeatures, (int) Math.sqrt(allFeatures.apply(data).size()));

			for (Duple<F,FV> featureValue : featureValues) {
				Duple<ArrayList<Duple<V, L>>, ArrayList<Duple<V, L>>> split = splitOn(data, featureValue.getFirst(), featureValue.getSecond(), getFeatureValue);
				double gain = gain(data, split.getFirst(), split.getSecond());

				if (gain > maxGain) {
					maxGain = gain;
					bestFeatureValue = featureValue;
					leftSplit = split.getFirst();
					rightSplit = split.getSecond();
				}
			}
			if (rightSplit.size() == 0 || leftSplit.size() == 0)
				return new DTLeaf<>(mostPopularLabelFrom(leftSplit.size() > 0 ? leftSplit : rightSplit));
			else
				return new DTInterior<>(bestFeatureValue.getFirst(), bestFeatureValue.getSecond(), train(leftSplit), train(rightSplit),  getFeatureValue, successor);
		}
	}

	public static <V,L> L mostPopularLabelFrom(ArrayList<Duple<V, L>> data) {
		Histogram<L> h = new Histogram<>();
		for (Duple<V,L> datum: data) {
			h.bump(datum.getSecond());
		}
		return h.getPluralityWinner();
	}

	// TODO: Generates a new data set by sampling randomly with replacement. It should return
	//    an `ArrayList` that is the same length as `data`, where each element is selected randomly
	//    from `data`. Should pass `DTTest.testResample()`.
	public static <V,L> ArrayList<Duple<V,L>> resample(ArrayList<Duple<V,L>> data) {
		ArrayList<Duple<V,L>> resampledList = new ArrayList<>();
		Random random = new Random();

		for (Duple<V,L> item: data) {
			int randomIndex = random.nextInt(data.size());
			resampledList.add(data.get(randomIndex));
		}

		return resampledList;
	}

	public static <V,L> double getGini(ArrayList<Duple<V,L>> data) {
		// TODO: Calculate the Gini coefficient:
		//  For each label, calculate its portion of the whole (p_i).
		//  Use of a Histogram<L> for this purpose is recommended.
		//  Gini coefficient is 1 - sum(for all labels i, p_i^2)
		//  Should pass DTTest.testGini().
		Map<L, Integer> labels = new HashMap<>();
		double ginicoef = 1.0;
		double totalItems = data.size();

		for (Duple<V,L> item : data) {
			L label = item.getSecond();
			labels.put(label, labels.getOrDefault(label, 0) + 1);
		}

		for (int lablecount : labels.values()) {
			double p = lablecount / totalItems;
			ginicoef -= p * p;
		}

		return ginicoef;
	}

	public static <V,L> double gain(ArrayList<Duple<V,L>> parent, ArrayList<Duple<V,L>> child1,
									ArrayList<Duple<V,L>> child2) {
		// TODO: Calculate the gain of the split. Add the gini values for the children.
		//  Subtract that sum from the gini value for the parent. Should pass DTTest.testGain().
		return getGini(parent) - (getGini(child1) + getGini(child2));
	}

	public static <V,L, F, FV  extends Comparable<FV>> Duple<ArrayList<Duple<V,L>>,ArrayList<Duple<V,L>>> splitOn
			(ArrayList<Duple<V,L>> data, F feature, FV featureValue, BiFunction<V,F,FV> getFeatureValue) {
		// TODO:
		//  Returns a duple of two new lists of training data.
		//  The first returned list should be everything from this set for which
		//  feature has a value less than or equal to featureValue. The second
		//  returned list should be everything else from this list.
		//  Should pass DTTest.testSplit().

		ArrayList<Duple<V,L>> leftSplit = new ArrayList<>();
		ArrayList<Duple<V,L>> rightSplit = new ArrayList<>();

		for(Duple<V, L> item : data){
			if(getFeatureValue.apply(item.getFirst(), feature).compareTo(featureValue) <= 0)
				leftSplit.add(item);
			else
				rightSplit.add(item);
		}

		return new Duple<>(leftSplit, rightSplit);
	}
}
