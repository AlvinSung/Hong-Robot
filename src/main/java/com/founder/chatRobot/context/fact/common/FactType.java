package com.founder.chatRobot.context.fact.common;

import java.util.Collection;
import java.util.Set;

import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;

public interface FactType {
	public String getMessage();

	public String getTypeName();

	public Entity getAttribute();

	public boolean isOverallFact();

	public Set<TaskConception> getUsingTaskType();

	public void addUsingTaskType(TaskConception taskType);

	public void addUsingTaskTypes(Collection<TaskConception> taskTypes);
}
