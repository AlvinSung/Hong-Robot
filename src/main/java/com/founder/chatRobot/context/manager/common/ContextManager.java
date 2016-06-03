package com.founder.chatRobot.context.manager.common;

import java.util.List;

import com.founder.chatRobot.common.exception.DoubleInsertException;
import com.founder.chatRobot.common.exception.TaskTypeNotFoundException;
import com.founder.chatRobot.common.exception.TypeMissException;
import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.context.common.Context;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.session.common.Session;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;

public abstract class ContextManager {
	protected Context context;
	protected KnowledgeManager knowledgeManager;

	public ContextManager(KnowledgeManager knowledgeManager, Context context) {
		this.context = context;
		this.knowledgeManager = knowledgeManager;
	}

	public abstract List<ResultBean> doTask(ConditionBean condition);

	public abstract List<ResultBean> doLastTask(ConditionBean condition);

	public TaskConception findTaskType(String name) throws TaskTypeNotFoundException {
		return this.context.getTaskType(name);
	}

	@SuppressWarnings("rawtypes")
	public List<Fact> getOverallEntryBySession(String sessionId) {
		Session session = this.context.getSession(sessionId);
		/*
		 * if (session == null) { session = new InternalSession(sessionId);
		 * this.context.addSession(sessionId, session); }
		 */

		return session.getOverallFact();
	}

	public void addFactType(FactType factType)
			throws DoubleInsertException, TypeUnmatchException {
		this.context.addFactType(factType.getTypeName(), factType);
	}

	public void addTaskType(TaskConception taskType) throws DoubleInsertException,
			TypeUnmatchException {
		this.context.addTaskType(taskType.getTypeName(), taskType);
	}

	public FactType getFactType(String typeName) {
		return this.context.getFactType(typeName);
	}

	public void addTypeRelation(String paramterName, String factTypeName,
			String taskTypeName) throws TypeMissException {
		try {
			FactType factType = this.context.getFactType(factTypeName);
			TaskConception taskType;

			taskType = this.context.getTaskType(taskTypeName);

			if (factType == null)
				throw new TypeMissException("fact typ of " + factTypeName
						+ " is not found.\n");
			else if (taskType == null)
				throw new TypeMissException("task typ of " + taskTypeName
						+ " is not found.\n");
			else
				taskType.addFactType(paramterName, factType);
		} catch (TaskTypeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Session getSession(String sessionId) {
		return this.context.getSession(sessionId);
	}

	/*
	 * public void addSession(Session session) {
	 * this.context.addSession(session.getSessionId(), session); }
	 */

}
