package com.founder.chatRobot.knowledgeMap.conception.entity.common;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.knowledgeMap.conception.common.ExpressConception;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;

public interface Entity extends ExpressConception {

	public Set<Entity> getAttributeToProduct();

	public void addAttributeToProducts(Collection<Entity> products);

	public void addAttributeToProduct(Entity product);

	public Set<Entity> getAttributeToValue();

	public Set<String> getAdverbList();

	public Collection<Entity> getAttributeToValueByWord(String word);

	public void addAdverbOfValue(String word, short beginPrecent,
			short endPrecent);

	public void addAttributeToValues(Collection<Entity> values);

	public void addAttributeToValue(Entity value);

	public Set<Entity> getValueToProduct();

	public void addValueToProduts(Collection<Entity> products);

	public void addValueToProduct(Entity product);

	public Set<Entity> getValueToAttribute();

	public void addValueToAttributes(Collection<Entity> attributes);

	public void addValueToAttribute(Entity attribute);

	public Map<String, Object> getInfo();

	public void addInfos(Map<String, Object> infos);

	public void addOther(String key, Object info);

	public Set<Entity> getProductToAttribute();

	public void addProductToAttributes(Collection<Entity> attributes);

	public void addProductToAttribute(Entity attribute);

	public Set<Entity> getProductToValue();

	public void addProductToValues(Collection<Entity> values);

	public void addProductToValue(Entity value);

	@SuppressWarnings("rawtypes")
	public Class getValueType();

	@SuppressWarnings("rawtypes")
	public Comparable getValue();

	public void setValue(@SuppressWarnings("rawtypes") Comparable value);

	public boolean isValueComparable();

	public Set<Entity> getRelationEntity();

	public void addRelatedTask(TaskConception task);

	public void addRelatedTasks(Collection<TaskConception> tasks);

	public Set<TaskConception> getRelatedTasks();

	public FactType getFactType();

	public void setFactType(FactType factType);

	public void createAVP(Entity product, Entity attribute, Entity value);

}
