package com.founder.chatRobot.domain.struct.svm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ObjectIntegerMap <T>{

	private Map<T, Integer> map;
	private List<T> indexKey;
	private AtomicInteger index = new AtomicInteger(0);

	public Integer queryMapId(T propertyKey) {
		Integer propertyId = getMap().get(propertyKey);
		if (null == propertyId) {
//			synchronized (indexKey) {
				propertyId = index.getAndIncrement();
				getMap().put(propertyKey, propertyId);
				indexKey.add(propertyKey);
//			}
		}
		return propertyId;
	}
	public T queryMapKey(int mapId) {
		return indexKey.get(mapId);
	}
	public void putMapId(T propertyKey) {
		queryMapId(propertyKey);
	}
	public Map<T, Integer> getMap() {
		if (null == map) {
			map = new HashMap<T, Integer>();
			indexKey = new ArrayList<T> ();
		}
		return map;
	}

	public Integer size() {
		return indexKey.size();
	}
}
