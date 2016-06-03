package com.founder.chatRobot.context.task;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.session.common.Session;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;

public class InternalTask extends Task {
	@SuppressWarnings("rawtypes")
	private Set<Fact> factList;
	private TaskConception type;

	@SuppressWarnings("rawtypes")
	public InternalTask(TaskConception type, Session session, boolean neededConfirm) {
		super(type, session, neededConfirm);
		this.factList = new HashSet<Fact>();
		this.type = type;
	}

	public TaskConception getTaskType() {
		return this.type;
	}

	public void addFact(@SuppressWarnings("rawtypes") Fact fact) {
		if(fact!=null)
			factList.add(fact);
	}

	public void addFacts(@SuppressWarnings("rawtypes") List<Fact> facts) {
		factList.addAll(facts);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Fact> getFactList() {
		return new LinkedList<Fact>(this.factList);
	}

	@Override
	public void cleanFactList() {
		this.factList.clear();

	}

	@Override
	public void removeFact(@SuppressWarnings("rawtypes") Fact fact) {
		this.factList.remove(fact);
		this.getSession().removeFact(fact);
	}

}
