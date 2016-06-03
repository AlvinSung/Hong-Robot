package com.founder.chatRobot.knowledgeMap.conception.taskType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.session.common.Session;
import com.founder.chatRobot.knowledgeMap.conception.common.Conception;
import com.founder.chatRobot.knowledgeMap.conception.entity.InternalEntity;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.conception.status.common.Status;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public class InternalTaskConception extends InternalEntity implements
		TaskConception {
	private Map<String, FactType> neededFacts;
	private TaskTreader treader;
	private Set<Status> precondition;
	private Status poscondition = null;
	private List<TaskConception> compseTaskTypes = new ArrayList<TaskConception>();
	//private Set<PrimitiveConception> actionPrimitives = new HashSet<PrimitiveConception>();
	private Entity subject, object;
	private Set<Conception> defineCollection = new HashSet<Conception>();
	private Set<TaskConception> subsequentTaskConception = new HashSet<TaskConception>();
	private String chineseName;

	public InternalTaskConception(String taskType, TaskTreader treader, String chineseName) {
		super(false);
		this.setName(taskType);
		this.neededFacts = new Hashtable<String, FactType>();
		this.treader = treader;
		this.precondition = new HashSet<Status>();
		this.chineseName = chineseName;
	}

	/*public InternalTaskConception(String taskType, TaskTreader treader,String chineseName,
			Entity subject, Entity object, PrimitiveConception actionPrimitive,
			PrimitiveConception... actionPrimitives) {
		super(false);
		this.setName(taskType);
		this.chineseName = chineseName;
		this.neededFacts = new Hashtable<String, FactType>();
		this.treader = treader;
		this.precondition = new HashSet<Status>();
		this.actionPrimitives.add(actionPrimitive);
		if (actionPrimitive != null) {
			actionPrimitive.addRelatedTask(this);
			this.addConnectedConception(actionPrimitive);
		}
		if (actionPrimitives.length > 0) {
			for (PrimitiveConception action : actionPrimitives) {
				this.actionPrimitives.add(action);
				action.addRelatedTask(this);
				this.addConnectedConception(action);
			}
		}
		this.subject = subject;
		if (this.subject != null) {
			this.subject.addRelatedTask(this);
			this.addConnectedConception(subject);
		}
		this.object = object;
		if (this.object != null) {
			this.object.addRelatedTask(this);
			this.addConnectedConception(object);
		}
	}*/

	public Map<String, FactType> getNeededFacts() {
		return this.neededFacts;
	}

	public void addFactType(String paramterName, FactType fact) {
		this.neededFacts.put(paramterName, fact);
		fact.addUsingTaskType(this);
	}

	public TaskTreader getTreader() {
		return this.treader;
	}

	public void addPrecondition(Status precondition) {
		this.precondition.add(precondition);
	}

	public void addPreconditions(Collection<Status> preconditions) {
		this.precondition.addAll(preconditions);
	}

	public Status checkPreCondition(Session session) {
		for (Status status : this.precondition) {
			if (!status.check(session).isMeet()) {
				return status;
			}
		}
		return null;
	}

	public void setPoscondition(Status poscondition) {
		this.poscondition = poscondition;
		// this.addConnectedConception(poscondition);
	}

	public boolean checkPosCondition(Session session) {
		if (this.poscondition != null) {
			return this.poscondition.check(session).isNeedDo();
		} else
			return true;
	}

	public String getTypeName() {
		return this.getName();
	}

	public List<TaskConception> getComposeTaskTypes() {
		return this.compseTaskTypes;
	}

	public void addComposeTaskType(TaskConception compseType, int pos) {
		this.compseTaskTypes.add(pos, compseType);

	}

	public void addComposeTaskType(TaskConception compseType) {
		this.compseTaskTypes.add(compseType);
	}

	public Collection<Conception> getDefines() {
		return defineCollection;
	}

	public void addDefine(Conception define) {
		this.defineCollection.add(define);
		this.addConnectedConception(define);
	}

	public void addDefines(Collection<Conception> defines) {
		this.defineCollection.addAll(defines);
		for (Conception define : defines)
			this.addConnectedConception(define);
	}

	/*public Set<PrimitiveConception> getActionPrimitiveSet() {
		return actionPrimitives;
	}*/

	public Entity getSubject() {
		return subject;
	}

	public Entity getObject() {
		return object;
	}

	public Set<TaskConception> getSubsequentTaskType() {
		return this.subsequentTaskConception;
	}

	public void addSubSequentTaskTypes(Set<TaskConception> subSequents) {
		this.subsequentTaskConception.addAll(subSequents);

	}

	public void addSubSequentTaskType(TaskConception subSequent) {
		this.subsequentTaskConception.add(subSequent);
	}

	public String getChinessName() {
		return this.chineseName;
	}

}
