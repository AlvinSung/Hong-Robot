package com.founder.chatRobot.common.semanticRecognition;

import com.founder.chatRobot.context.fact.common.FactInfo;

public class EntryInfo<T> {
	private String typeName;
	private FactInfo<T> info;
	private int startPos, length;

	public EntryInfo(String typeName, FactInfo<T> info, int startPos, int length) {
		this.typeName = typeName;
		this.info = info;
		this.startPos = startPos;
		this.length = length;
	}

	public EntryInfo(String typeName, FactInfo<T> info) {
		this.typeName = typeName;
		this.info = info;
		this.startPos = -1;
		this.length = -1;
	}

	public String getTypeName() {
		return typeName;
	}

	public FactInfo<T> getInfo() {
		return info;
	}

	public int startPos() {
		return startPos;
	}

	public int length() {
		return length;
	}

	public int hashCode() {
		return (this.typeName.hashCode() + this.info.getInfo().hashCode()) / 2;
	}

}
