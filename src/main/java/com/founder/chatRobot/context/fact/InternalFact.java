package com.founder.chatRobot.context.fact;

import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.session.common.Session;

public class InternalFact<T> implements Fact<T> {
	private FactType type;
	private FactInfo<T> info;
	private Session session;
	private int age;
	private boolean isName;
	private boolean isUsed;

	public InternalFact(FactType type, FactInfo<T> info, Session session,
			boolean isName) {
		this.type = type;
		this.info = info;
		this.session = session;
		//this.session.addFact(this);
		this.age = session.getCurrentFactAge(type);
		this.isName = isName;
		this.isUsed = false;
	}

	public InternalFact(FactType type, FactInfo<T> info) {
		this.type = type;
		this.info = info;
	}

	public Session getContext() {
		return session;
	}

	public void setContext(Session context) {
		this.session = context;
		this.session.addFact(this);
	}

	public FactInfo<T> getInfo() {
		return this.info;
	}

	public void setInfo(FactInfo<T> info) {
		this.info = info;
	}

	public FactType getType() {
		return this.type;
	}

	public int getFactAge() {
		return this.age;
	}

	public boolean isName() {
		return isName;
	}

	public boolean isUsed() {
		return this.isUsed;
	}

	public int compareTo(Fact<T> fact) {
		return fact.getFactAge() - this.age;
	}

}
