package com.founder.chatRobot.knowledgeMap.conception.status;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.founder.chatRobot.knowledgeMap.conception.common.Conception;
import com.founder.chatRobot.knowledgeMap.conception.common.ConceptionType;
import com.founder.chatRobot.knowledgeMap.conception.internalImplements.InternalConception;
import com.founder.chatRobot.knowledgeMap.conception.status.common.Status;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;

public abstract class InternalStatus extends InternalConception implements
		Status {
	private Set<TaskConception> restrictTaskTypes;
	private TaskConception treaderTask;
	private String meetedInfo;
	private String unmeetedInfo;
	private Conception object;

	public InternalStatus(TaskConception treaderTask, String meetedInfo,
			String unmeetedInfo) {
		super(ConceptionType.STATUS);
		this.restrictTaskTypes = new HashSet<TaskConception>();
		this.treaderTask = treaderTask;
		this.treaderTask.setPoscondition(this);
		this.meetedInfo = meetedInfo;
		this.unmeetedInfo = unmeetedInfo;
	}

	public Set<TaskConception> getRestrictTaskTypes() {
		return this.restrictTaskTypes;
	}

	public void addRestrictTaskType(TaskConception type) {
		this.restrictTaskTypes.add(type);
	}

	public TaskConception getTreaderTask() {
		return treaderTask;
	}

	public void addRestrictTaskTypes(Collection<TaskConception> types) {
		this.restrictTaskTypes.addAll(types);
	}

	public String getMeetedInfo() {
		return meetedInfo;
	}

	public String getUnmeetedInfo() {
		return unmeetedInfo;
	}

	public Conception getObject() {
		return this.object;
	}

}
