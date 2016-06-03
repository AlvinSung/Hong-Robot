package com.founder.chatRobot.knowledgeMap.conception.internalImplements;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import com.founder.chatRobot.controler.common.Controler;
import com.founder.chatRobot.knowledgeMap.conception.common.Conception;
import com.founder.chatRobot.knowledgeMap.conception.common.ConceptionType;
import com.founder.chatRobot.knowledgeMap.conception.common.ExpressConception;

public class InternalConception implements Conception {
	private static AtomicLong LARGE_ID = new AtomicLong(0);
	private long ID;
	private String name = "";
	private Conception parent = null;
	private Set<Conception> children;
	// protected KnowledgeManager knowledgeManager;
	private ConceptionType type;
	private Set<Conception> connectedConceptions = new HashSet<Conception>();

	public InternalConception(ConceptionType type) {
		this.ID = InternalConception.LARGE_ID.addAndGet(1);
		// this.knowledgeManager = knowledgeManager;
		this.children = new HashSet<Conception>();
		this.type = type;
	}

	public long getID() {
		return this.ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		// this.addIdentify(name);
	}

	public ConceptionType getType() {
		return this.type;
	}

	public Conception getParent() {
		return parent;
	}

	public void setParent(Conception parent) {
		this.parent = parent;
		parent.addChild(this);
	}

	public Set<Conception> getChildren() {
		return this.children;
	}

	public void addChild(Conception child) {
		this.children.add(child);

	}

	public void addChilds(Collection<Conception> children) {
		this.children.addAll(children);

	}

	public Set<Conception> getAllConnectConception() {
		Set<Conception> result = new HashSet<Conception>(
				this.connectedConceptions);
		if (this.parent != null)
			result.addAll(this.parent.getAllConnectConception());
		return result;
	}

	protected void addConnectedConceptions(Collection<Conception> conceptions) {
		this.connectedConceptions.addAll(conceptions);
	}

	protected void addConnectedConception(Conception conception) {
		if (conception != null)
			this.connectedConceptions.add(conception);
		else
			Controler.debugLoger.debug(this.getName());
	}

	public Set<ExpressConception> getAllConnectExpressConception() {
		Set<ExpressConception> result = new HashSet<ExpressConception>();
		for (Conception conception : this.getAllConnectConception()) {
			if (conception instanceof ExpressConception) {
				result.add((ExpressConception) conception);
			} else {
				result.addAll(conception.getAllConnectExpressConception());
			}
		}

		return result;
	}

	public Set<Conception> getRighteousnessExpressConception() {
		Set<Conception> result = new HashSet<Conception>();
		Conception current = this, parent = this.parent;

		result.add(this);

		while (parent != null && current != parent) {
			result.add(parent);
			current = parent;
		}

		return result;
	}

}
