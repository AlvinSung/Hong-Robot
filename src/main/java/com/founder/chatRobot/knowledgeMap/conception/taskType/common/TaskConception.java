package com.founder.chatRobot.knowledgeMap.conception.taskType.common;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.session.common.Session;
import com.founder.chatRobot.knowledgeMap.conception.common.Conception;
import com.founder.chatRobot.knowledgeMap.conception.status.common.Status;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public interface TaskConception extends Conception {
	public Map<String, FactType> getNeededFacts();

	public void addFactType(String paramterName, FactType fact);

	public String getTypeName();
	
	public String getChinessName();

	public TaskTreader getTreader();

	public void addPrecondition(Status precondition);

	public void addPreconditions(Collection<Status> preconditions);

	// 如果所有前置条件都满足，返回null。否则返回第一个不满足的状态
	public Status checkPreCondition(Session session);

	public void setPoscondition(Status poscondition);

	public boolean checkPosCondition(Session session);

	public List<TaskConception> getComposeTaskTypes();

	public void addComposeTaskType(TaskConception compseType, int pos);

	public void addComposeTaskType(TaskConception compseType);

	public Set<TaskConception> getSubsequentTaskType();

	public void addSubSequentTaskTypes(Set<TaskConception> subSequents);

	public void addSubSequentTaskType(TaskConception subSequent);
}
