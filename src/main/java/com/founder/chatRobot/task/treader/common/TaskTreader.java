package com.founder.chatRobot.task.treader.common;

import java.util.ArrayList;
import java.util.List;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.recognition.common.interfaces.old.Recognition;

public abstract class TaskTreader {
	public static final String DOTASK = "doNextTask";
	public static final String BACKMESSAGE = "BackMessage";
	// private Set<String> addedRecognitions = new HashSet<String>();
	private String reloadInfo, loadInfo;

	public TaskTreader(String reloadInfo, String loadInfo) {
		this.reloadInfo = reloadInfo;
		this.loadInfo = loadInfo;
	}

	public String getRelaodInfo() {
		return this.reloadInfo;
	}
	
	public String getLoadInfo(){
		return this.loadInfo;
	}

	public abstract ResultBean tread(
			@SuppressWarnings("rawtypes") List<Fact> factList, Task task);

	// must return a List. The first elements is DOTASK or BACKMESSAGE;
	// this implements is for taskTreaders which need not check facts.
	public List<String> checkFact(Task task) {
		List<String> result = new ArrayList<String>(1);
		task.checkFacts(false);
		result.add(TaskTreader.DOTASK);
		return result;
	}

	protected void cleanRecognition(Task task) {
		task.cleanRecognition();
	}

	protected void addRecognition(Task task, String name,
			Recognition recognition) {
		task.addRecognition(name, recognition);
		// this.addedRecognitions.add(task.getTaskType().getTypeName() + name);
	}

	protected void removeRecognition(Task task, String name) {
		task.removeRecognition(name);
	}

	public abstract ResultBean reload(
			@SuppressWarnings("rawtypes") List<Fact> factList, Task task);

}
