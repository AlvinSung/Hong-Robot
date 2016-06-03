package com.founder.chatRobot.context.session.common;

import java.util.Collection;
import java.util.List;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.context.common.Context;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.recognition.common.RecognitionManager;

public interface Session {

	public Task getPopTask();

	public Task getLastFinishedTask();

	public void addFact(@SuppressWarnings("rawtypes") Fact fact);

	public void releaseTask();

	public void pushTask(Task task);

	@SuppressWarnings("rawtypes")
	public Collection<Fact> getAllFacts();
	
	@SuppressWarnings("rawtypes")
	public Collection<Fact> getAllFacts(boolean needOldFact);

	public String getSessionId();

	@SuppressWarnings("rawtypes")
	public List<Fact> getOverallFact();
	
	@SuppressWarnings("rawtypes")
	public Collection<Fact> getFacts();

	public int getCurrentFactAge(FactType type);

	// return pop task
	public Task addNewTask(Task task);

	public List<ResultBean> doTask();
	
	public Task getLastDoTask();

	public List<ResultBean> doLastTask();

	public Fact<Entity> getPosition();

	public void setPosition(Fact<Entity> position);

	public void setUser(Entity user);

	public Entity getUser();

	public Context getContext();

	public RecognitionManager getRecognitionManager();

	public boolean isConfirm();

	public void removeFact(@SuppressWarnings("rawtypes") Fact fact);

	public void addSubsequentTask(String taskTypeName);
	
	public void addTempDate(String key, Object date);
	
	public Object getTempDate(String key);

}
