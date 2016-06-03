package com.founder.chatRobot.knowledgeMap.manager;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

import com.founder.chatRobot.knowledgeMap.conception.common.Conception;

public class KnowledgeManager {
	Map<String, LinkedList<Conception>> index;
	Map<String, Conception> uniqueIndex;

	public KnowledgeManager() {
		this.index = new Hashtable<String, LinkedList<Conception>>();
		this.uniqueIndex = new Hashtable<String, Conception>();
	}

	public void addIndex(String index, Conception entity) {
		LinkedList<Conception> entities = this.index.get(index);
		if (entities == null) {
			entities = new LinkedList<Conception>();
			this.index.put(index, entities);
		}
		entities.add(entity);

		this.addUniqueIndex(index, entity);
	}

	public void addIndexs(Map<String, Conception> entities) {
		LinkedList<Conception> newEntities;

		for (String index : entities.keySet()) {
			newEntities = this.index.get(index);
			if (newEntities == null) {
				newEntities = new LinkedList<Conception>();
				this.index.put(index, newEntities);
			}
			newEntities.add(entities.get(index));
		}

		this.addUniqueIndexs(entities);
	}

	public LinkedList<Conception> index(String index) {
		return this.index.get(index);
	}

	public Conception uniqueIndex(String index) {
		return uniqueIndex.get(index);
	}

	public void addUniqueIndex(String uniqueIndex, Conception entity) {
		this.uniqueIndex.put(uniqueIndex, entity);
	}

	public void addUniqueIndexs(Map<String, Conception> uniqueIndexs) {
		this.uniqueIndex.putAll(uniqueIndexs);
	}

}
