package com.founder.chatRobot.context.fact.common;

import com.founder.chatRobot.context.session.common.Session;

public interface Fact<T> extends Comparable<Fact<T>> {
	public Session getContext();

	public void setContext(Session context);

	public FactInfo<T> getInfo();

	public void setInfo(FactInfo<T> info);

	public FactType getType();
	
	public int getFactAge();
	
	public boolean isName();
	
	public boolean isUsed();

}
