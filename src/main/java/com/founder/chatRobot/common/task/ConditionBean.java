package com.founder.chatRobot.common.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.knowledgeMap.conception.status.common.Status;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;

public class ConditionBean {
	@SuppressWarnings("rawtypes")
	private Set<EntryInfo> facts;
	private String sessionId;
	private String userId;
	private String position;
	private String sentence;
	private List<TaskConception> tasks;
	private List<Status> Status;

	@SuppressWarnings("rawtypes")
	public ConditionBean(String sentence) {
		this.facts = new HashSet<EntryInfo>();
		this.sentence = sentence;
		this.tasks = new ArrayList<TaskConception>(5);
		this.Status = new ArrayList<Status>(5);
	}

	public String getSentence() {
		return this.sentence;
	}

	/*
	 * public void setSentence(String changedSentence) { this.sentence =
	 * changedSentence; }
	 */

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@SuppressWarnings("rawtypes")
	public Set<EntryInfo> getFactSet() {
		// System.out.println("dddddddddd : " + this.conditions.size());
		return facts;
	}

	@SuppressWarnings("rawtypes")
	public void addFact(EntryInfo info) {
		this.facts.add(info);
	}

	@SuppressWarnings("rawtypes")
	public void addFacts(Collection<EntryInfo> conditions) {
		// System.out.println("bbbbbb : " + this.conditions.size());
		this.facts.addAll(conditions);
		// System.out.println("cccccccccc : " + this.conditions.size());
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void addInfos(ConditionBean other) {
		this.facts.addAll(other.getFactSet());
	}

	public List<TaskConception> getTasks() {
		return tasks;
	}

	public void addTasks(Collection<TaskConception> tasks) {
		this.tasks.addAll(tasks);
	}

	public void addTask(TaskConception task) {
		this.tasks.add(task);
	}

	public List<Status> getStatus() {
		return Status;
	}

	public void addStatus(Collection<Status> status) {
		this.Status.addAll(status);
	}

	public void addStatu(Status statu) {
		this.Status.add(statu);
	}

}
