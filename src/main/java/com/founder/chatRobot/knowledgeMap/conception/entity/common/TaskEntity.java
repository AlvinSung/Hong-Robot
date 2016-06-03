package com.founder.chatRobot.knowledgeMap.conception.entity.common;

import java.util.Collection;

import com.founder.chatRobot.knowledgeMap.conception.common.Conception;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;

public interface TaskEntity extends Entity {
	
	public abstract TaskConception getTaskType();
	
	public abstract TaskConception checkTaskConception(Collection<Conception> conceptionCollection);
	
}
