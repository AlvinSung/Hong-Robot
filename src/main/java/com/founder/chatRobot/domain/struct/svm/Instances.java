package com.founder.chatRobot.domain.struct.svm;

import java.util.ArrayList;
import java.util.List;

import com.founder.chatRobot.common.GeneralApi.TwoValue;

public class Instances {
	private int numOfProperties;
	private int numOfClasses;
	private int numOfInstances;
	
	public Instances(FeatureDatas featureDatas) {
		resetInstances(featureDatas);
	}
	
	public void resetInstances(FeatureDatas featureDatas) {
		numOfClasses = featureDatas.queryNumOfClasses();
		numOfProperties = featureDatas.queryNumOfFeatures();
		numOfInstances = featureDatas.queryNumOfInstances();
		instances = new ArrayList<TwoValue<Integer, Double[]>>(numOfInstances);
		
		for (TwoValue<String, List<String>> featureData : featureDatas.getInstances()) {
			String classType = featureData.getA();
			Integer classTypeId = featureDatas.queryClassMapId(classType);
			List<String> features = featureData.getB();
			Double[] properties = new Double[numOfProperties];
			for (String feature : features) {
				properties[featureDatas.queryFeatureMapId(feature)] = 1.0;
			}
			instances.add(new TwoValue<Integer, Double[]> (classTypeId, properties));
		}
	}
	
	//TwoValue 决策（分类），所有属性（特征值）
	private List<TwoValue<Integer, Double[]>> instances;
	public boolean addInstance(Integer decision, Double[] properties) {
		return getInstantces().add(new TwoValue<Integer, Double[]> (decision, properties));
	}
	private List<TwoValue<Integer, Double[]>> getInstantces() {
		if (null == instances) {
			instances = new ArrayList<TwoValue<Integer, Double[]>>();
		}
		return instances;
	}
	public Integer queryClass(int instenceId) {
		return getInstantces().get(instenceId).getA();
	}
	public void modifyClass(int instenceId, int valueId) {
		getInstantces().get(instenceId).setA(valueId);
	}
	public Double queryProperty(int instenceId, int propertyId) {
		if (null == getInstantces().get(instenceId).getB()
				|| getInstantces().get(instenceId).getB().length <= propertyId) {
			//LogHelper.PRESALES_LOG.info("查询产品属性越界instenceId:" + instenceId + ", propertyId" + propertyId);
			return -1.0;
		}
		Double propertyValue = getInstantces().get(instenceId).getB()[propertyId];
		propertyValue = null == propertyValue ? 0 : propertyValue;
		return propertyValue;
	}
	public void modifyProperty(int instenceId, int propertyId, Double value) {
		getInstantces().get(instenceId).getB()[propertyId] = value;
	}

	public int getNumOfProperties() {
		return numOfProperties;
	}

	public int getNumOfClasses() {
		return numOfClasses;
	}

	public int getNumOfInstances() {
		return numOfInstances;
	}
}
