package com.founder.chatRobot.domain.struct.svm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.founder.chatRobot.common.GeneralApi.TwoValue;

public class FeatureDatas {
	private ObjectIntegerMap<String> classMap;
	private ObjectIntegerMap<String> featureMap;
	private List<TwoValue<String, List<String>>> instances;
	
	public ObjectIntegerMap<String> getFeatureMap() {
		if (null == featureMap) {
			featureMap = new ObjectIntegerMap<String> ();
		}
		return featureMap;
	}
	private ObjectIntegerMap<String> getClassMap() {
		if (null == classMap) {
			classMap = new ObjectIntegerMap<String> ();
		}
		return classMap;
	}
	public Integer queryClassMapId(String classKey) {
		return getClassMap().queryMapId(classKey);
	}
	public String queryClassMapKey(int classId) {
		return getClassMap().queryMapKey(classId);
	}
	public void putClassMap(String featureKey) {
		queryFeatureMapId(featureKey);
	}
	public Integer queryFeatureMapId(String featureKey) {
		return getFeatureMap().queryMapId(featureKey);
	}
	public String queryFeatureMapKey(int classId) {
		return getFeatureMap().queryMapKey(classId);
	}
	public void putFeatureMap(String propertyKey) {
		queryFeatureMapId(propertyKey);
	}
	
	public List<TwoValue<String, List<String>>> getInstances() {
		if (null == instances) {
			instances = new ArrayList<TwoValue<String, List<String>>> ();
		}
		return instances;
	}
	public boolean addInstance(String classType, List<String> featureValues) {
		getClassMap().putMapId(classType);
		for (String featureValue : featureValues) {
			getFeatureMap().putMapId(featureValue);
		}
		return getInstances().add(new TwoValue<String, List<String>> (classType, featureValues));
	}
	public Integer queryNumOfInstances() {
		return instances.size();
	}
	public Integer queryNumOfClasses() {
		return classMap.size();
	}
	public Integer queryNumOfFeatures() {
		return featureMap.size();
	}
	public int queryClass(int instanceIndex) {
		return classMap.queryMapId(instances.get(instanceIndex).getA());
	}
	public Set<Integer> queryFeatureIndexes(int instanceIndex) {
		List<String> features = instances.get(instanceIndex).getB();
		Set<Integer> featureIndexes = new HashSet<Integer> (features.size());
		for (String feature : instances.get(instanceIndex).getB()) {
			featureIndexes.add(featureMap.queryMapId(feature));
		}
		return featureIndexes;
	}
	public double queryProperty(int instanceIndex, int featureIndex) {
		List<String> features = instances.get(instanceIndex).getB();
		String feature = featureMap.queryMapKey(featureIndex);
		return features.contains(feature) ? 1 : 0;
	}
}
