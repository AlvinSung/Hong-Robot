package com.founder.chatRobot.context.context;

import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.founder.chatRobot.common.exception.DoubleInsertException;
import com.founder.chatRobot.common.exception.TaskTypeNotFoundException;
import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.context.context.common.Context;
import com.founder.chatRobot.context.fact.InternalFactType;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.session.InternalSession;
import com.founder.chatRobot.context.session.common.Session;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;

public class InternalContext implements Context {
	private Map<String, Session> contextMap;
	private Map<String, TaskConception> taskTypeMap;
	private Map<String, InternalFactType> factTypeMap;
	private KnowledgeManager knowledgeManager;
	private Map<String, Integer> sessionTimeUp;
	private Timer timer;

	private class SessionClear extends TimerTask {
		private InternalContext context;

		public SessionClear(InternalContext context) {
			this.context = context;
		}

		public void run() {
			context.clearSession();
		}
	}

	public InternalContext(KnowledgeManager knowledgeManager) {
		this.contextMap = new Hashtable<String, Session>();
		this.taskTypeMap = new Hashtable<String, TaskConception>();
		this.factTypeMap = new Hashtable<String, InternalFactType>();
		this.sessionTimeUp = new Hashtable<String, Integer>();
		this.knowledgeManager = knowledgeManager;

		timer = new Timer(true);
		timer.schedule(new SessionClear(this), 0, 6 * 1000);
	}

	private void clearSession() {
		for (String sessionName : this.sessionTimeUp.keySet()) {
			int timeUp = this.sessionTimeUp.get(sessionName);
			if (timeUp < 10) {
				this.sessionTimeUp.put(sessionName, timeUp + 1);
			} else {
				this.sessionTimeUp.remove(sessionName);
				this.contextMap.remove(sessionName);
			}
		}
	}

	public Session getSession(String sessionId) {
		Session result = this.contextMap.get(sessionId);
		if (result == null) {
			result = new InternalSession(sessionId, this);
			this.addSession(sessionId, result);
		} else {
			this.sessionTimeUp.put(sessionId, 0);
		}
		return result;
	}

	private void addSession(String sessionId, Session session) {
		this.contextMap.put(sessionId, session);
		this.sessionTimeUp.put(sessionId, 0);
	}

	public TaskConception getTaskType(String typeName)
			throws TaskTypeNotFoundException {
		TaskConception taskType = this.taskTypeMap.get(typeName);

		if (taskType != null)
			return this.taskTypeMap.get(typeName);
		else
			throw new TaskTypeNotFoundException(
					"System can't find task's type of " + typeName + "\n");
	}

	public void addTaskType(String typeName, TaskConception type)
			throws DoubleInsertException, TypeUnmatchException {
		this.taskTypeMap.put(typeName, type);
	}

	public FactType getFactType(String typeName) {
		return this.factTypeMap.get(typeName);
	}

	public void addFactType(String typeName, FactType type)
			throws DoubleInsertException, TypeUnmatchException {
		if (this.factTypeMap.containsKey(typeName))
			throw new DoubleInsertException(
					"The fact type has been in this context. \n");
		else if (type instanceof InternalFactType)
			this.factTypeMap.put(typeName, (InternalFactType) type);
		else
			throw new TypeUnmatchException(
					"The FactType is not InternalFactType. It's "
							+ type.getClass() + " .\n");
	}

	public KnowledgeManager getKnowledgeManager() {
		return knowledgeManager;
	}
}
