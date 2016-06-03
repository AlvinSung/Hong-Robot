package com.founder.chatRobot.svm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Service;

import com.founder.chatRobot.common.GeneralApi.TwoValue;
//import com.founder.chatRobot.domain.SISentence;
//import com.founder.chatRobot.entityfind.EntityFind;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;

public class Identify {

	protected Map<Integer, String> classIdNames;
	protected Map<String, Integer> terms;
	protected Model model;

	public static Map<String, Integer> loadTerms(String path)
			throws IOException {
		Map<String, Integer> terms = new HashMap<String, Integer>();

		InputStream is = Thread.currentThread().getClass()
				.getResourceAsStream("/" + path);
		if (is == null) {
			is = Identify.class.getResourceAsStream("/" + path);
		}
		if (is == null) {
			is = new FileInputStream(path);
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"GBK"));

		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] splits = line.split("[\t]");
			terms.put(splits[0], Integer.parseInt(splits[1]));
		}
		reader.close();

		return terms;
	}

	static Map<Integer, String> loadClasses(String path,
			Map<String, Integer> classNameIds) throws IOException {
		Map<Integer, String> classIdNames = new HashMap<Integer, String>();

		InputStream is = Thread.currentThread().getClass()
				.getResourceAsStream("/" + path);
		if (is == null) {
			// System.out.println(Identify.class.getResource(path));
			is = Identify.class.getResourceAsStream("/" + path);
		}

		if (is == null) {
			is = new FileInputStream(path);
		}
		BufferedReader classReader = new BufferedReader(new InputStreamReader(
				is, "GBK"));

		String line = null;
		while ((line = classReader.readLine()) != null) {
			String[] splits = line.split("[\t]");
			classIdNames.put(Integer.parseInt(splits[0]), splits[1]);
			if (null != classNameIds) {
				classNameIds.put(splits[1], Integer.parseInt(splits[0]));
			}
		}
		classReader.close();

		return classIdNames;
	}

	static private Map<Integer, String> loadClasses(String path)
			throws IOException {
		return loadClasses(path, null);
	}

	public boolean loadModel(String classMapFile, String termsFile,
			String modelFile) {
		boolean bRet = false;
		try {
			classIdNames = loadClasses(classMapFile);
			terms = loadTerms(termsFile);

			InputStream is = Thread.currentThread().getClass()
					.getResourceAsStream("/" + modelFile);

			if (is == null) {
				is = Identify.class.getResourceAsStream("/" + modelFile);
			}

			if (is == null) {
				is = new FileInputStream(modelFile);
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "GBK"));

			model = Linear.loadModel(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bRet = true;
		return bRet;
	}

	public static Feature[] getFeatureFromSentence(Map<String, Integer> terms,
			String text) {
		List<Feature> featureList = new ArrayList<Feature>();
		List<String> termList = SVMModelUtil.getSenceTerms(text);
		Set<String> termSet = new HashSet<String>(termList);
		for (String term : termSet) {
			Integer value = terms.get(term);
			if (value == null)
				continue;
			featureList.add(new FeatureNode(value, 1.0));
		}
		return featureList.toArray(new Feature[0]);
	}

	public TwoValue<Double, String> identifySentence(String sentenceString) {
		TwoValue<Double, String> classProbabilityAndName = new TwoValue<Double, String>();
		try {
			Feature[] features = getFeatureFromSentence(terms, sentenceString);
			double[] probabilities = new double[classIdNames.size()];
			double sum = 0, average, variance;
			Integer h = (int) Linear.predictProbability(model, features,
					probabilities);
			for (int i = 0; i < probabilities.length; i++) {
				sum += probabilities[i];
			}
			average = sum / probabilities.length;
			sum = 0;
			for (int i = 0; i < probabilities.length; i++) {
				sum += Math.pow((probabilities[i] - average), 2);
			}
			variance = sum / probabilities.length;

			if (variance > 0.000001) {
				classProbabilityAndName.setA(probabilities[h]);
				classProbabilityAndName.setB(classIdNames.get(h));
				return classProbabilityAndName;
			} else {
				return null;
			}
		} catch (Exception e) {
		}
		return null;
	}
}
