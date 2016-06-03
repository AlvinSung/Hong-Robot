package com.founder.chatRobot.knowledgeMap.conception.entity.sortStruct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;

public class SortContain {
	private class Pair {
		private short begin, end;

		public Pair(short begin, short end) {
			this.begin = begin;
			this.end = end;
		}

		protected short getBegin() {
			return this.begin;
		}

		protected short getEnd() {
			return this.end;
		}
	}

	private class MyCompare implements Comparator<Entity> {

		@SuppressWarnings("unchecked")
		public int compare(Entity entity1, Entity entity2) {
			/*System.out.println(entity1.getValue());
			System.out.println(entity2.getValue());*/
			if (entity1.getValueType().equals(entity2.getValueType()))
				return (entity1.getValue().compareTo(entity2.getValue()));
			else
				return -1;
		}

	}

	@SuppressWarnings({})
	private List<Entity> contain;
	private Map<String, Pair> wordMap;
	private int entitySize = 0;
	private boolean isNeedSort;

	public SortContain() {
		this.wordMap = new HashMap<String, Pair>();
		contain = new LinkedList<Entity>();
		this.isNeedSort = false;
	}

	public void addEntity(Entity entity) {
		this.contain.add(entity);
		this.entitySize++;
		this.isNeedSort = true;
	}

	public void addWord(String word, short beginPrecent, short endPrecent) {
		Pair pair = new Pair(beginPrecent, endPrecent);
		this.wordMap.put(word, pair);
	}

	public Collection<Entity> findWord(String word) {
		Pair pair = this.wordMap.get(word);
		int beginPosition, endPosition;
		if (pair != null) {
			beginPosition = pair.getBegin() * entitySize / 100;
			endPosition = pair.getEnd() * entitySize / 100;
			if (isNeedSort) {
				Collections.sort(this.contain, new MyCompare());
				this.isNeedSort = false;
			}
			return (new ArrayList<Entity>(this.contain.subList(beginPosition,
					endPosition)));
		} else
			return null;
	}

	public List<Entity> getValues() {
		return this.contain;
	}

	public Set<String> getWordList() {
		return this.wordMap.keySet();
	}
}
