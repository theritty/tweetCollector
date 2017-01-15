package weka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class BaseNaiveBayesClassifier {
	private File resourcesDirectory = new File("src/main/resources");
	private File classifierFile;
	
	public BaseNaiveBayesClassifier(){
		
	}
	
	public BufferedReader readDataFile(String filename) {
		File dataFile = new File(resourcesDirectory.getAbsolutePath() + File.separator + filename);
		BufferedReader inputReader = null;
 
		try {
			inputReader = new BufferedReader(new FileReader(dataFile.getAbsolutePath()));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
 
		return inputReader;
	}
	
	public void createClassifier(String trainingFileName, String attributeName1, 
			String attributeName2, String filterPrefix) throws Exception {
		// Read train data
		BufferedReader trainingFile = readDataFile(trainingFileName);
		Instances trainData = new Instances(trainingFile);
		trainData.setClassIndex(trainData.numAttributes() - 1);
	
		// To handle string attributes
		StringToWordVector filter = new StringToWordVector();
		filter.setAttributeNamePrefix(filterPrefix);
		filter.setAttributeIndices("first-last");
		
		// Prepare classifier
		FilteredClassifier classifier = new FilteredClassifier(); 
		classifier.setFilter(filter); 
		classifier.setClassifier(new NaiveBayes());
		classifier.buildClassifier(trainData);
		
		// Define attributes
		ArrayList<Attribute> classifierAttributes = new ArrayList<Attribute>(2);
		ArrayList<String> classValues = new ArrayList<String>(2);
		
		classifierAttributes.add(new Attribute(attributeName1, (ArrayList<String>)null));
		
		classValues.add("yes");
		classValues.add("no");
		classifierAttributes.add(new Attribute(attributeName2, classValues));
		
		// Save the built model
		classifierFile = new File(resourcesDirectory.getAbsolutePath() + File.separator + attributeName2 + ".classifer");
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(classifierFile));
		oos.writeObject(classifier);
		oos.flush();
		oos.close();
	}
	
	public String classify(String tweet, String attributeName1, String attributeName2) throws Exception {
		// Read classifier model
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(classifierFile));
		FilteredClassifier classifier = (FilteredClassifier) ois.readObject();
		ois.close();
		
		// Define attributes
		ArrayList<Attribute> classifierAttributes = new ArrayList<Attribute>(2);
		ArrayList<String> classValues = new ArrayList<String>(2);
		
		classifierAttributes.add(new Attribute(attributeName1, (ArrayList<String>)null));
		
		classValues.add("yes");
		classValues.add("no");
		classifierAttributes.add(new Attribute(attributeName2, classValues));
				 
		// Create a dataset for each classifer, using the attributes
		Instances data = new Instances("category", classifierAttributes, 0);
		data.setClassIndex(data.numAttributes() - 1);
		
		// Create an instance for each classifier, using the given arguments
		double[] testInstance = new double[data.numAttributes()];
		testInstance[0] = data.attribute(0).addStringValue(tweet);
		testInstance[1] = Utils.missingValue();

		// Add instance to dataset
		data.add(new DenseInstance(1.0, testInstance));

		// Classify the instance
		double predictionValue = classifier.classifyInstance(data.instance(0));
		String prediction = data.classAttribute().value((int) predictionValue);
		return prediction;
	}
	
	public static void main(String[] args) throws Exception {
		String politicsTrainingFileName = "politicsTweets.txt";
		
		BaseNaiveBayesClassifier bnbc = new BaseNaiveBayesClassifier();
		bnbc.createClassifier(politicsTrainingFileName, "tweet", "politics", "politics");
		String result = bnbc.classify("breaking find trump reach number delegate need clinch republican president","tweet", "politics");
		System.out.println(result);
	}

}
