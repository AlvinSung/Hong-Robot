package com.founder.chatRobot.knowledgeMap.conception.status.common;

import java.util.Collection;
import java.util.Set;

import com.founder.chatRobot.context.session.common.Session;
import com.founder.chatRobot.knowledgeMap.conception.common.Conception;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;

public interface Status extends Conception {
	public Set<TaskConception> getRestrictTaskTypes();

	public void addRestrictTaskType(TaskConception type);

	public TaskConception getTreaderTask();

	public void addRestrictTaskTypes(Collection<TaskConception> types);

	public String getMeetedInfo();

	public String getUnmeetedInfo();

	public StatusInfo check(Session session);

	public Conception getObject();
}
