package com.founder.chatRobot.knowledgeMap.conception.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.knowledgeMap.conception.common.Conception;
import com.founder.chatRobot.knowledgeMap.conception.common.ConceptionType;
import com.founder.chatRobot.knowledgeMap.conception.common.ExpressConception;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.conception.entity.sortStruct.SortContain;
import com.founder.chatRobot.knowledgeMap.conception.internalImplements.InternalExpressConception;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;

public class InternalEntity extends InternalExpressConception implements Entity {
	private boolean isValueComparable;
	private Set<Entity> attributeToValue;
	private SortContain comparableAttributeToValue;
	private Set<Entity> attributeToProduct;
	private Set<Entity> productToAttribute;
	private Set<Entity> productToValue;
	private Set<Entity> valuesToProducts;
	private Set<Entity> valuesToAttribute;
	private Set<Entity> relationEntity;
	@SuppressWarnings("rawtypes")
	private Class valueType;
	@SuppressWarnings("rawtypes")
	private Comparable value;
	private Map<String, Object> Info;
	private Set<TaskConception> relatedTasks = new HashSet<TaskConception>();
	private FactType factType = null;

	public InternalEntity(boolean isValueComparable) {
		super(ConceptionType.BASE);
		// this.identifies = new LinkedList<String>();
		this.attributeToProduct = new HashSet<Entity>();
		this.isValueComparable = isValueComparable;
		if (isValueComparable) {
			this.comparableAttributeToValue = new SortContain();
			this.attributeToValue = null;
		} else {
			this.attributeToValue = new HashSet<Entity>();
			this.comparableAttributeToValue = null;
		}
		this.productToAttribute = new HashSet<Entity>();
		this.productToValue = new HashSet<Entity>();
		this.valuesToAttribute = new HashSet<Entity>();
		this.valuesToProducts = new HashSet<Entity>();
		this.relationEntity = new HashSet<Entity>();
	}

	public InternalEntity(boolean isValueComparable, ConceptionType type) {
		super(type);
		// this.identifies = new LinkedList<String>();
		this.attributeToProduct = new HashSet<Entity>();
		this.isValueComparable = isValueComparable;
		if (isValueComparable) {
			this.comparableAttributeToValue = new SortContain();
			this.attributeToValue = null;
		} else {
			this.attributeToValue = new HashSet<Entity>();
			this.comparableAttributeToValue = null;
		}
		this.productToAttribute = new HashSet<Entity>();
		this.productToValue = new HashSet<Entity>();
		this.valuesToAttribute = new HashSet<Entity>();
		this.valuesToProducts = new HashSet<Entity>();
		this.relationEntity = new HashSet<Entity>();
	}

	public Set<Entity> getAttributeToProduct() {
		return this.attributeToProduct;
	}

	public void addAttributeToProducts(Collection<Entity> products) {
		this.attributeToProduct.addAll(products);
		this.relationEntity.addAll(products);
		for (Entity product : products)
			this.addConnectedConception(product);
	}

	public void addAttributeToProduct(Entity product) {
		this.attributeToProduct.add(product);
		this.relationEntity.add(product);
		this.addConnectedConception(product);
	}

	public Set<Entity> getAttributeToValue() {
		if (this.attributeToValue != null)
			return this.attributeToValue;
		else {
			return new HashSet<Entity>(
					this.comparableAttributeToValue.getValues());
		}
	}

	public Set<String> getAdverbList() {
		if (this.comparableAttributeToValue != null)
			return this.comparableAttributeToValue.getWordList();
		else
			return null;
	}

	public Collection<Entity> getAttributeToValueByWord(String word) {
		if (this.comparableAttributeToValue != null) {
			Collection<Entity> entityList = this.comparableAttributeToValue
					.findWord(word);
			// System.out.println("gggggg : " + entityList.size());
			return entityList;
		} else
			return null;
	}

	public void addAdverbOfValue(String word, short beginPrecent,
			short endPrecent) {
		if (this.comparableAttributeToValue != null)
			this.comparableAttributeToValue.addWord(word, beginPrecent,
					endPrecent);
	}

	public void addAttributeToValues(Collection<Entity> values) {
		if (this.attributeToValue != null) {
			this.attributeToValue.addAll(values);
			this.relationEntity.addAll(values);
			for (Entity entity : values)
				this.addConnectedConception(entity);
		} else {
			for (Entity entity : values)
				this.comparableAttributeToValue.addEntity(entity);
		}
	}

	public void addAttributeToValue(Entity value) {
		if (this.attributeToValue != null) {
			this.attributeToValue.add(value);
			this.relationEntity.add(value);
			this.addConnectedConception(value);
		} else
			this.comparableAttributeToValue.addEntity(value);
	}

	public Set<Entity> getValueToProduct() {
		return this.valuesToProducts;
	}

	public void addValueToProduts(Collection<Entity> products) {
		this.valuesToProducts.addAll(products);
		this.relationEntity.addAll(products);
		for (Entity entity : products)
			this.addConnectedConception(entity);
	}

	public void addValueToProduct(Entity product) {
		this.valuesToProducts.add(product);
		this.relationEntity.add(product);
		this.addConnectedConception(product);
	}

	public Set<Entity> getValueToAttribute() {
		return this.valuesToAttribute;
	}

	public void addValueToAttributes(Collection<Entity> attributes) {
		this.valuesToAttribute.addAll(attributes);
		this.relationEntity.addAll(attributes);
		for (Entity entity : attributes)
			this.addConnectedConception(entity);
	}

	public void addValueToAttribute(Entity attribute) {
		this.valuesToAttribute.add(attribute);
		this.relationEntity.add(attribute);
		this.addConnectedConception(attribute);
	}

	public Map<String, Object> getInfo() {
		return Info;
	}

	public void addInfos(Map<String, Object> infos) {
		this.Info.putAll(infos);
	}

	public void addOther(String key, Object info) {
		this.Info.put(key, info);
	}

	public Set<Entity> getProductToAttribute() {
		return this.productToAttribute;
	}

	public void addProductToAttributes(Collection<Entity> attributes) {
		this.productToAttribute.addAll(attributes);
		this.relationEntity.addAll(attributes);
		for (Entity entity : attributes)
			this.addConnectedConception(entity);
	}

	public void addProductToAttribute(Entity attribute) {
		this.productToAttribute.add(attribute);
		this.relationEntity.add(attribute);
		this.addConnectedConception(attribute);
	}

	public Set<Entity> getProductToValue() {
		return this.productToValue;
	}

	public void addProductToValues(Collection<Entity> values) {
		this.productToValue.addAll(values);
		this.relationEntity.addAll(values);
		for (Entity value : values)
			this.addConnectedConception(value);
	}

	public void addProductToValue(Entity value) {
		this.productToValue.add(value);
		this.relationEntity.add(value);
		this.addConnectedConception(value);
	}

	@SuppressWarnings("rawtypes")
	public Class getValueType() {
		return valueType;
	}

	private void setValueType(@SuppressWarnings("rawtypes") Class valueType) {
		this.valueType = valueType;
	}

	@SuppressWarnings("rawtypes")
	public Comparable getValue() {
		return value;
	}

	public void setValue(@SuppressWarnings("rawtypes") Comparable value) {
		this.value = value;
		this.setValueType(this.value.getClass());
	}

	public boolean isValueComparable() {
		return isValueComparable;
	}

	public Set<Entity> getRelationEntity() {
		return this.relationEntity;
	}

	public int compareTo(ExpressConception o) {
		if (o instanceof Entity) {
			return (int) (o.getID() - this.getID());
		} else
			return -1;
	}

	public void addRelatedTask(TaskConception task) {
		this.relatedTasks.add(task);
		this.addConnectedConception(task);
	}

	public void addRelatedTasks(Collection<TaskConception> tasks) {
		this.relatedTasks.addAll(tasks);
		for (TaskConception task : tasks)
			this.addConnectedConception(task);

	}

	public Set<TaskConception> getRelatedTasks() {
		return this.relatedTasks;
	}

	public Set<Conception> getRighteousnessExpressConception() {
		Set<Conception> result = new HashSet<Conception>();
		Conception current = this, parent = this.getParent();

		result.add(this);

		while (parent != null && current != parent) {
			result.add(parent);
			current = parent;
		}

		for (Conception conception : this.valuesToAttribute) {
			result.add(conception);
		}

		return result;
	}

	public Set<ExpressConception> getAllConnectExpressConception() {
		Set<ExpressConception> result = new HashSet<ExpressConception>();

		Set<Conception> connectConceptions = this.getAllConnectConception();
		// connectConceptions.remove(this);
		for (Conception conception : connectConceptions) {
			if (conception instanceof ExpressConception) {
				result.add((ExpressConception) conception);
			} else {
				result.addAll(conception.getAllConnectExpressConception());
			}
		}

		Set<Conception> parents = this.getRighteousnessExpressConception();
		parents.remove(this);
		for (Conception conception : parents) {
			result.addAll(conception.getAllConnectExpressConception());
		}
		return result;
	}

	public FactType getFactType() {
		return this.factType;
	}

	public void setFactType(FactType factType) {
		this.factType = factType;
	}

	@Override
	public void createAVP(Entity product, Entity attribute, Entity value) {
		product.addProductToAttribute(attribute);
		product.addProductToValue(value);
		attribute.addAttributeToProduct(product);
		attribute.addAttributeToValue(value);
		value.addValueToAttribute(attribute);
		value.addValueToProduct(product);
	}

}
