package com.founder.chatRobot.context.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.founder.chatRobot.common.exception.TaskTypeNotFoundException;
import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.context.common.Context;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.session.common.Session;
import com.founder.chatRobot.context.task.InternalTask;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;
import com.founder.chatRobot.recognition.common.RecognitionManager;

public class InternalSession implements Session {
	private Stack<Task> taskStock;
	@SuppressWarnings("rawtypes")
	private Set<Fact> factSet;
	private String sessionId;
	private int factAge;
	private Fact<Entity> position = null;
	private Entity user;
	private Context context;
	// private RecognitionManager recognitionManager;
	@SuppressWarnings("rawtypes")
	private Collection<Fact> removedFactSet;
	private List<Task> removedTasks;
	private Task lastDoTask = null;
	private Task lastFinishedTask = null;
	private Map<String, Object> tmpDate = new HashMap<String, Object>(); 
	
	@SuppressWarnings("rawtypes")
	public InternalSession(String sessionId, Context context) {
		this.taskStock = new Stack<Task>();
		this.factSet = new HashSet<Fact>();
		this.sessionId = sessionId;
		this.factAge = 0;
		this.context = context;
		// this.recognitionManager = new InternalRecognitionManager();
		this.removedFactSet = new HashSet<Fact>();
		this.removedTasks = new LinkedList<Task>();
	}

	public void addFact(@SuppressWarnings("rawtypes") Fact fact) {
		factSet.add(fact);
	}

	public void releaseTask() {
		this.releaseTask(false);
	}

	private boolean releaseTask(boolean needDoSequenceTask) {
		if (this.taskStock.empty())
			return false;

		Task popTask = this.taskStock.pop();

		/*Controler.debugLoger.debug("aa releaseTask : "
				+ popTask.getTaskType().getTypeName() + " : " + popTask + " : "
				+ this.factSet.size() + " : " + popTask.getFactList().size());*/

		/*
		 * for (@SuppressWarnings("rawtypes") Fact fact : popTask.getFactList())
		 * { Controler.debugLoger.debug(fact.getType().getTypeName()); }
		 */

		//Controler.debugLoger.debug("-----------------------------------");

		/*for (@SuppressWarnings("rawtypes")
		Fact fact : this.factSet) {
			Controler.debugLoger.debug(fact.getType().getTypeName());
		}*/

		if (popTask.getAge() != 0) {
			this.lastFinishedTask = popTask;
			this.removedTasks.add(popTask);
			this.factSet.removeAll(popTask.getFactList());
			this.removedFactSet.addAll(popTask.getFactList());
		}

		/*Controler.debugLoger.debug("bb releaseTask" + " : " + this.factSet.size()
				+ " : " + popTask.getFactList().size());

		for (@SuppressWarnings("rawtypes")
		Fact fact : this.factSet) {
			Controler.debugLoger.debug(fact.getType().getTypeName());
		}*/

		if (popTask.getTaskType().getTypeName().equals("ClosingComtask")
				&& this.taskStock.size() > 0) {
			popTask = this.taskStock.pop();

			/*Controler.debugLoger.debug("cc releaseTask : "
					+ popTask.getTaskType().getTypeName() + " : " + popTask
					+ " : " + this.factSet.size() + " : "
					+ popTask.getFactList().size());*/

			/*
			 * for (@SuppressWarnings("rawtypes") Fact fact :
			 * popTask.getFactList()) {
			 * Controler.debugLoger.debug(fact.getType().getTypeName() + " : " + fact);
			 * }
			 * 
			 * System.out .println(
			 * "----------------------------------------------------------------"
			 * );
			 * 
			 * for (@SuppressWarnings("rawtypes") Fact fact : this.factSet) {
			 * Controler.debugLoger.debug(fact.getType().getTypeName() + " : " + fact);
			 * }
			 * 
			 * System.out .println(
			 * "----------------------------------------------------------------"
			 * );
			 */

			if (popTask.getAge() != 0) {
				this.lastFinishedTask = popTask;
				this.removedTasks.add(popTask);
				for (@SuppressWarnings("rawtypes")
				Fact fact : popTask.getFactList()) {
					/*
					 * Controler.debugLoger.debug(fact.getType().getTypeName() + " : " +
					 * fact); Controler.debugLoger.debug(this.factSet.contains(fact));
					 */
					this.factSet.remove(fact);
					/*
					 * Controler.debugLoger.debug(this.factSet.contains(fact) + " : " +
					 * this.factSet.size());
					 */
				}
				// this.factSet.removeAll(popTask.getFactList());
				// this.lastTask = popTask;
				this.removedFactSet.addAll(popTask.getFactList());
			}

			/*
			 * System.out .println(
			 * "----------------------------------------------------------------"
			 * );
			 * 
			 * Controler.debugLoger.debug("dd releaseTask" + " : " + this.factSet.size()
			 * + " : " + popTask.getFactList().size());
			 * 
			 * for (@SuppressWarnings("rawtypes") Fact fact : this.factSet) {
			 * Controler.debugLoger.debug(fact.getType().getTypeName() + " : " + fact);
			 * }
			 */

			/*Controler.debugLoger.debug("dd releaseTask" + " : " + this.factSet.size()
					+ " : " + popTask.getFactList().size());*/
		}
		if (needDoSequenceTask) {
			boolean hasSubSequentTask = false;
			for (TaskConception subSequentTaskType : popTask.getTaskType()
					.getSubsequentTaskType()) {
				Task subSequentTask = null;
				//Controler.debugLoger.debug("check post condition : " + subSequentTaskType.getChinessName());
				//Controler.debugLoger.debug("check post condition : " + subSequentTaskType.checkPosCondition(this));
				if(subSequentTaskType.checkPosCondition(this)){
					hasSubSequentTask = true;
					if (!this.taskStock.isEmpty()) {
						if (this.taskStock.peek().getTaskType() != subSequentTaskType) {
							subSequentTask = new InternalTask(subSequentTaskType,
									this, true);
							this.taskStock.push(subSequentTask);
						}
					} else {
						subSequentTask = new InternalTask(subSequentTaskType, this,
								true);
						this.taskStock.push(subSequentTask);
					}
				}
			}
			return hasSubSequentTask;
		} else {
			return false;
		}

	}

	public void pushTask(Task task) {
		this.taskStock.push(task);
	}

	public Task getPopTask() {
		try {
			return this.taskStock.peek();
		} catch (EmptyStackException e) {
			return null;
		}
	}

	public Task getLastFinishedTask() {
		/*
		 * if(this.lastfinishedTask!=null)
		 * Controler.debugLoger.debug(this.lastfinishedTask
		 * .getTaskType().getTypeName());
		 */
		return this.lastFinishedTask;
	}

	public Task getLastDoTask() {
		/*
		 * if(this.lastfinishedTask!=null)
		 * Controler.debugLoger.debug(this.lastfinishedTask
		 * .getTaskType().getTypeName());
		 */
		return this.lastDoTask;
	}

	@SuppressWarnings("rawtypes")
	public Collection<Fact> getFacts() {
		return this.getAllFacts();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection<Fact> getAllFacts(boolean needOldFact) {
		List<Fact> result = new ArrayList<Fact>(this.factSet);
		
		/*for(Fact fact : result){
			Controler.debugLoger.debug(fact);
		}
		
		Controler.debugLoger.debug("-----------------------------------------------------------");*/
		
		if (this.lastFinishedTask != null && needOldFact) {
			//Controler.debugLoger.debug(this.lastDoTask.getFactList().size());
			result.addAll(this.lastDoTask.getFactList());
		}
		
		/*for(Fact fact : result){
			Controler.debugLoger.debug(fact);
		}*/

		Collections.sort(result);
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection<Fact> getAllFacts() {
		List<Fact> result = new ArrayList<Fact>(this.factSet);
		
		Collections.sort(result);
		return result;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	@SuppressWarnings({ "rawtypes" })
	public List<Fact> getOverallFact() {
		Map<FactType, Fact> result = new Hashtable<FactType, Fact>();

		for (Fact fact : this.factSet) {
			if (fact.getType().isOverallFact()) {
				if (!result.keySet().contains(fact.getType())
						|| fact.getFactAge() > result.get(fact.getType())
								.getFactAge()) {
					result.put(fact.getType(), fact);
				}
			}
		}

		return new LinkedList<Fact>(result.values());

	}

	public int getCurrentFactAge(FactType type) {
		/*
		 * List<Fact> factList = new LinkedList<Fact>();
		 * 
		 * for (Fact fact : this.factSet) { if (fact.getType().equals(type))
		 * factList.add(fact); }
		 * 
		 * return factList.size();
		 */
		this.factAge++;
		return this.factAge;
	}

	public Task addNewTask(Task task) {
		if (this.taskStock.size() <= 0) {
			this.taskStock.push(task);
			return task;
		} else {
			Task popTask = this.taskStock.peek();
			if (!popTask.getTaskType().equals(task.getTaskType())) {
				this.taskStock.clear();
				// Controler.debugLoger.debug("oooooooooooooooo");
				this.taskStock.push(popTask);
				this.taskStock.push(task);
				popTask.setNeededConfirm();
				return task;
			} else {
				// popTask.cleanFactList();
				return popTask;
			}
		}
	}

	public List<ResultBean> doTask() {
		List<ResultBean> result = new ArrayList<ResultBean>();
		ResultBean just;
		Task popTask;

		if (this.taskStock.size() > 0) {
			do {
				popTask = this.taskStock.peek();
				just = popTask.doTask();
				result.add(just);
				this.lastDoTask = popTask;
			} while (just.isNewTask());

			if (just.isFinished()) {
				this.releaseTask(just.isStartSubsequentTask());
				if (this.taskStock.size() > 0) {
					popTask = this.taskStock.peek();
					/*if (popTask.getReloadInfo() != null) {
						just = new ResultBean();
						just.setAnswer(popTask.getReloadInfo());
						Controler.debugLoger.debug("adding confirmInfo : ");
						popTask.cleanRecognition();
						Recognition recognition = new ConfirmRecognition();
						popTask.addRecognition("ConfirmInfo", recognition);
					} else */
					just = popTask.reload(this);
					if (just != null)
						result.add(just);
				}
			}

		}

		return result;
	}

	public List<ResultBean> doLastTask() {
		List<ResultBean> result = new ArrayList<ResultBean>();
		ResultBean just;
		Task lastTask;

		//Controler.debugLoger.debug("do last task");
		/*for (@SuppressWarnings("rawtypes")
		Fact fact : this.factSet) {
			//Controler.debugLoger.debug(fact.getType().getTypeName());
			if (fact.getInfo().getInfo().get(0) instanceof Entity)
				Controler.debugLoger.debug(((Entity) fact.getInfo().getInfo().get(0))
						.getName());
		}*/

		if (this.lastFinishedTask != null) {
			lastTask = this.lastFinishedTask;
			// lastTask.setReload();
			lastTask.cleanFactList();
			just = lastTask.doTask();
			result.add(just);

			//Controler.debugLoger.debug("kkk : " + lastTask.getFactList().size());

			if (!just.isFinished()) {
				this.taskStock.push(lastTask);
			} else {
				//Controler.debugLoger.debug(this.factSet.size());
				this.factSet.removeAll(lastTask.getFactList());
				this.removedFactSet.addAll(lastTask.getFactList());
				//Controler.debugLoger.debug(this.factSet.size());
			}
		}

		/*
		 * Controler.debugLoger.debug("ddd : " + this.taskStock.size()); for (Task task
		 * : this.taskStock) { Controler.debugLoger.debug("aaa : " +
		 * task.getTaskType().getTypeName()); Controler.debugLoger.debug("aaa : " +
		 * task); }
		 */
		return result;
	}

	public Fact<Entity> getPosition() {
		return this.position;
	}

	public void setPosition(Fact<Entity> position) {
		this.position = position;
	}

	public void setUser(Entity user) {
		this.user = user;
	}

	public Entity getUser() {
		return this.user;
	}

	public Context getContext() {
		return context;
	}

	public RecognitionManager getRecognitionManager() {
		if (this.taskStock.empty())
			return null;
		return this.taskStock.peek().getRecognitionManager();
	}

	public boolean isConfirm() {
		for (@SuppressWarnings("rawtypes")
		Fact fact : this.factSet) {
			if (fact.getType().getTypeName().equals("ConfirmInfo")) {
				this.factSet.remove(fact);
				if (!(Boolean) fact.getInfo().getInfo().get(0)) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	public void removeFact(@SuppressWarnings("rawtypes") Fact fact) {
		this.factSet.remove(fact);
	}

	public void addSubsequentTask(String taskTypeName) {
		try {
			TaskConception taskType = this.context.getTaskType(taskTypeName);

			Task task = new InternalTask(taskType, this, true);
			Task popTask = this.taskStock.pop();
			this.taskStock.push(task);
			this.taskStock.push(popTask);
		} catch (TaskTypeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void addTempDate(String key, Object date) {
		this.tmpDate.put(key, date);
		
	}

	@Override
	public Object getTempDate(String key) {
		return this.tmpDate.get(key);
	}

}
