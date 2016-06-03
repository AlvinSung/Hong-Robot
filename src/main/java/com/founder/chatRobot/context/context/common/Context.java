package com.founder.chatRobot.context.context.common;

import com.founder.chatRobot.common.exception.DoubleInsertException;
import com.founder.chatRobot.common.exception.TaskTypeNotFoundException;
import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.session.common.Session;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;

public interface Context {

	public Session getSession(String sessionId);

	// public void addSession(String sessionId, Session session);

	public TaskConception getTaskType(String typeName)
			throws TaskTypeNotFoundException;

	public void addTaskType(String typeName, TaskConception type)
			throws DoubleInsertException, TypeUnmatchException;

	public FactType getFactType(String typeName);

	public void addFactType(String typeName, FactType type)
			throws DoubleInsertException, TypeUnmatchException;

	public KnowledgeManager getKnowledgeManager();
}
