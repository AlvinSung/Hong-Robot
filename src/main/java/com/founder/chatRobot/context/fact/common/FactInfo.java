package com.founder.chatRobot.context.fact.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FactInfo<T> {
	List<T> infoList;

	@SafeVarargs
	public FactInfo(T info, T... otherInfo) {
		this.infoList = new ArrayList<T>(otherInfo.length + 1);
		this.infoList.add(info);
		for (T t : otherInfo)
			this.infoList.add(t);
	}

	public FactInfo(Collection<T> infoList) {
		this.infoList = new ArrayList<T>(infoList);
	}

	@SuppressWarnings("unchecked")
	public boolean equals(@SuppressWarnings("rawtypes") FactInfo other) {
		boolean result = true;
		for (int i = 0; i < this.infoList.size(); i++) {
			if (!(this.infoList.get(i).getClass().equals(other.getInfo().get(i)
					.getClass()))
					|| !this.infoList.get(i).equals((T) other.getInfo().get(i))) {
				result = false;
				break;
			}
		}
		return result;
	}

	/*
	 * public void setInfo(T info) { this.info = info; }
	 */

	public List<T> getInfo() {
		return this.infoList;
	}
}
