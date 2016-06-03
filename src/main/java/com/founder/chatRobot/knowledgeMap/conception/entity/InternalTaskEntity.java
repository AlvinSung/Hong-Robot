package com.founder.chatRobot.knowledgeMap.conception.entity;

import java.util.Collection;

import com.founder.chatRobot.knowledgeMap.conception.common.Conception;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.TaskEntity;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;

public class InternalTaskEntity extends InternalEntity implements TaskEntity {
	private TaskConception taskConception;

	public InternalTaskEntity(TaskConception taskConception) {
		super(false);
		this.taskConception = taskConception;
	}

	@Override
	public TaskConception getTaskType() {
		return this.taskConception;
	}

	@Override
	public TaskConception checkTaskConception(
			Collection<Conception> conceptionCollection) {
		if(conceptionCollection.containsAll(this.taskConception.getAllConnectConception())){
			return this.taskConception;
		}else{
			return null;
		}
	}

}
