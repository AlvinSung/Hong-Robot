package com.founder.chatRobot.context.fact;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;

public class InternalFactType implements FactType {
	private String name;
	private String message;
	private boolean isOverall;
	// private String infoTypeName;
	private Entity attribute;
	private Set<TaskConception> usingTaskType = new HashSet<TaskConception>();

	public InternalFactType(String name, String message, boolean isOverall,
			Entity attribute) {
		this.name = name;
		this.message = message;
		this.isOverall = isOverall;
		// this.infoTypeName = infoTypeName;
		this.attribute = attribute;
		this.attribute.setFactType(this);
	}

	/*
	 * public InternalFactType(String factType) { this.name = factType; }
	 */

	public void setOverall(boolean isOverall) {
		this.isOverall = isOverall;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTypeName() {
		return name;
	}

	public boolean isOverallFact() {
		return this.isOverall;
	}

	public Entity getAttribute() {
		return attribute;
	}

	public Set<TaskConception> getUsingTaskType() {
		return this.usingTaskType;
	}

	public void addUsingTaskType(TaskConception taskType) {
		this.usingTaskType.add(taskType);
	}

	public void addUsingTaskTypes(Collection<TaskConception> taskTypes) {
		this.usingTaskType.addAll(taskTypes);
	}

}
