package com.founder.chatRobot.common.task;

import java.util.Hashtable;
import java.util.Map;

public class ResultBean {
	private Map<String, Object> reslutMap;
	private String type;
	private String sessionId;
	private String answer;
	private boolean isFinished = false;
	private boolean isNewTask = false;
	private boolean isStartSubsequentTask = true;

	public ResultBean() {
		this.reslutMap = new Hashtable<String, Object>();
		this.answer = "";
	}

	public Map<String, Object> getReslutMap() {
		return reslutMap;
	}

	public void addResult(String key, Object value) {
		this.reslutMap.put(key, value);
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished() {
		this.isFinished = true;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isNewTask() {
		return isNewTask;
	}

	public void setNewTask(boolean isNewTask) {
		this.isNewTask = isNewTask;
	}

	public boolean isStartSubsequentTask() {
		return this.isStartSubsequentTask;
	}

	public void setNotStartSubsequentTask() {
		this.isStartSubsequentTask = false;
	}
}
