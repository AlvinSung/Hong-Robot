package com.founder.chatRobot.knowledgeMap.conception.common;

import java.util.Collection;
import java.util.Set;

public interface Conception {
	public long getID();

	public String getName();

	public void setName(String name);

	public Conception getParent();

	public void setParent(Conception parent);

	public Set<Conception> getChildren();

	public void addChild(Conception child);

	public void addChilds(Collection<Conception> children);

	public ConceptionType getType();

	public Set<Conception> getAllConnectConception();

	public Set<ExpressConception> getAllConnectExpressConception();

	public Set<Conception> getRighteousnessExpressConception();

}
