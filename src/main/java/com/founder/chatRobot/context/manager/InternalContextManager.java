package com.founder.chatRobot.context.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.context.InternalContext;
import com.founder.chatRobot.context.fact.InternalFact;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.manager.common.ContextManager;
import com.founder.chatRobot.context.session.common.Session;
import com.founder.chatRobot.context.task.InternalTask;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.controler.common.Controler;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;

public class InternalContextManager extends ContextManager {

	public InternalContextManager(KnowledgeManager knowledgeManager) {
		super(knowledgeManager, new InternalContext(knowledgeManager));
	}

	@Override
	public List<ResultBean> doTask(ConditionBean condition) {
		Session session = this.context.getSession(condition.getSessionId());
		Task task;
		List<ResultBean> result = new LinkedList<ResultBean>();

		for (TaskConception taskType : condition.getTasks()) {
			task = new InternalTask(taskType, session, false);
			task = session.addNewTask(task);
		}

		result.addAll(session.doTask());

		Controler.debugLoger.debug("releaseTask" + " : " + session.getFacts().size()
				+ " : ");

		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ResultBean> doLastTask2(ConditionBean condition) {
		Session session = this.context.getSession(condition.getSessionId());
		Task task = null, lastTask = null;
		Fact fact = null;
		boolean inScene = false, isLast = false, isConfirmed = false, notInScene = false;
		// boolean hasResult = false;
		FactType factType;

		if (session.getPosition() == null) {
			factType = this.context.getFactType("Division");
			Entity positionEntity = (Entity) this.knowledgeManager
					.uniqueIndex(condition.getPosition());
			FactInfo<Entity> factInfo = new FactInfo<Entity>(positionEntity);
			Fact position = new InternalFact(factType, factInfo);
			session.setPosition(position);
		}

		try {
			task = session.getPopTask();
		} catch (java.util.EmptyStackException e) {
			// return null;
		}

		lastTask = session.getLastFinishedTask();

		// Controler.debugLoger.debug("fffffff : ");
		for (EntryInfo info : condition.getFactSet()) {
			// Controler.debugLoger.debug("hhhh : " + info.getTypeName());
			if (info.getTypeName().startsWith("__ATTRIBUTE__")) {
				factType = this.context.getFactType(info.getTypeName()
						.replaceFirst("__ATTRIBUTE__", ""));
				if (factType != null) {
					fact = new InternalFact(factType, info.getInfo(), session,
							true);
					notInScene = true;
				}
			} else {
				factType = this.context.getFactType(info.getTypeName());
				if (factType != null)
					fact = new InternalFact(factType, info.getInfo(), session,
							false);
			}
			if (task != null && task.isNeededConfirm() && fact != null
					&& fact.getType().getTypeName().equals("Confirm")) {
				// Controler.debugLoger.debug("confirm task");
				isConfirmed = true;
				if (((Entity) fact.getInfo().getInfo().get(0)).getName()
						.equals("Yes")) {
					// Controler.debugLoger.debug("confirmed this task");
					task.confirmed();
					inScene = true;
				} else {
					session.releaseTask();
				}

			} else if (task != null
					&& fact != null
					&& !task.isNeededConfirm()
					&& task.getTaskType().getNeededFacts().values()
							.contains(fact.getType())) {
				// Controler.debugLoger.debug("ggggggg : ");
				inScene = true;
			} else if (task != null
					&& fact != null
					&& !task.isNeededConfirm()
					&& !task.getTaskType().getNeededFacts().values()
							.contains(fact.getType())) {
				// Controler.debugLoger.debug("ggggggg : ");
				notInScene = true;
			} else if (lastTask != null
					&& fact != null
					&& lastTask.getTaskType().getNeededFacts().values()
							.contains(fact.getType())) {
				inScene = true;
				isLast = true;
			} else if (lastTask != null
					&& fact != null
					&& lastTask.getTaskType().getNeededFacts().values()
							.contains(fact.getType())) {
				notInScene = true;
			}
		}
		if (inScene && !isLast && !notInScene)
			return session.doTask();
		else if (inScene && isLast && !notInScene) {
			/*
			 * Controler.debugLoger.debug("ttttttttt : " +
			 * lastTask.getTaskType().getTypeName());
			 */
			List<FactType> factTypeList = new LinkedList<FactType>();
			for (Fact current : session.getAllFacts()) {
				factTypeList.add(current.getType());
			}
			for (Fact oldFact : lastTask.getFactList()) {
				if (!factTypeList.contains(oldFact.getType())) {
					session.addFact(oldFact);
				}
			}
			lastTask.cleanFactList();
			return session.doLastTask();
		} else if (isConfirmed && session.getPopTask() == null) {
			ResultBean result = new ResultBean();
			result.setAnswer("很高兴为您服务，请问还有其它什么可以帮助您？");
			List<ResultBean> resultList = new ArrayList<ResultBean>(1);
			resultList.add(result);
			return resultList;

		} else if (!isConfirmed && session.getPopTask() != null
				&& session.getPopTask().isNeededConfirm()) {
			session.releaseTask();
			if (session.getPopTask() != null) {
				return this.doLastTask(condition);
			} else
				return null;
		} else
			return null;
	}

	public List<ResultBean> doLastTask(ConditionBean condition) {
		Session session = this.context.getSession(condition.getSessionId());
		Task task = null, lastTask = null;
		boolean allNeededByTopTask = true, allNeededByLastTask = true;
		// boolean hasResult = false;
		FactType factType;
		@SuppressWarnings("rawtypes")
		Set<Fact> factSet = new HashSet<Fact>();

		// Controler.debugLoger.debug("ooo : " +session.getFacts().size());

		Entity positionEntity = (Entity) this.knowledgeManager
				.uniqueIndex(condition.getPosition());
		if (session.getPosition() == null
				|| session.getPosition().getInfo().getInfo().get(0) != positionEntity) {
			factType = this.context.getFactType("Division");
			FactInfo<Entity> factInfo = new FactInfo<Entity>(positionEntity);
			Fact<Entity> position = new InternalFact<Entity>(factType, factInfo);
			session.setPosition(position);
		}

		for (@SuppressWarnings("rawtypes")
		EntryInfo info : condition.getFactSet()) {
			// Controler.debugLoger.debug("hhhh : " + info.getTypeName());
			if (info.getTypeName().startsWith("__ATTRIBUTE__")) {
				factType = this.context.getFactType(info.getTypeName()
						.replaceFirst("__ATTRIBUTE__", ""));
				if (factType != null) {
					@SuppressWarnings({ "rawtypes", "unchecked" })
					Fact fact = new InternalFact(factType, info.getInfo(),
							session, true);
					factSet.add(fact);
					session.addFact(fact);
				}
				// allNeededByTopTask = false;
				// allNeededByLastTask = false;
			} else {
				factType = this.context.getFactType(info.getTypeName());
				if (factType != null) {
					@SuppressWarnings({ "rawtypes", "unchecked" })
					Fact fact = new InternalFact(factType, info.getInfo(),
							session, false);
					factSet.add(fact);
					session.addFact(fact);
				}
			}
		}

		task = session.getPopTask();

		if (factSet.size() <= 0) {
			return null;
		}

		if (task != null && task.isNeededConfirm()) {
			boolean hasOtherFact = false;
			boolean isConfirmed = false;
			for (@SuppressWarnings("rawtypes")
			Fact fact : factSet) {
				if (fact != null && !fact.isName()
						&& fact.getType().getTypeName().equals("Confirm")) {
					// isConfirmed = true;
					/*
					 * Controler.debugLoger.debug("uuu : " + ((Entity)
					 * fact.getInfo().getInfo().get(0)).getName());
					 */
					if (((Entity) fact.getInfo().getInfo().get(0)).getName()
							.equals("Yes")) {
						// Controler.debugLoger.debug("confirmed this task");
						task.confirmed();
						isConfirmed = true;
					} else {
						// session.releaseTask();
						/*
						 * Controler.debugLoger.debug("qqqqq"); session.releaseTask();
						 * ResultBean resultBean = new ResultBean();
						 * resultBean.setAnswer("很高兴为您服务，请问有什么可以帮助您？");
						 * resultBean.setSessionId(condition.getSessionId());
						 * List<ResultBean> resultList = new
						 * ArrayList<ResultBean>(1); resultList.add(resultBean);
						 * return resultList;
						 */
					}
					// break;
				} else if (fact != null
						&& (fact.isName() || !fact.getType().getTypeName()
								.equals("Confirm"))) {
					hasOtherFact = true;
				}
			}

			// Controler.debugLoger.debug("rrr : " + isConfirmed + " : " +
			// hasOtherFact);

			if (isConfirmed) {
				return session.doTask();
			} else {
				// Controler.debugLoger.debug("qqqqq1");
				if (!hasOtherFact) {
					session.releaseTask();
					ResultBean resultBean = new ResultBean();
					resultBean.setAnswer("很高兴为您服务，请问还有其它什么可以帮助您？");
					resultBean.setSessionId(condition.getSessionId());
					List<ResultBean> resultList = new ArrayList<ResultBean>(1);
					resultList.add(resultBean);
					return resultList;
				} else {
					boolean allNeedByTask = true;
					for (@SuppressWarnings("rawtypes")
					Fact fact : factSet) {
						if (!task.getTaskType().getNeededFacts().values()
								.contains(fact.getType())) {
							allNeedByTask = false;
							Controler.debugLoger.debug("poiu");
							break;
						}
					}

					// Controler.debugLoger.debug("poiu : " + allNeedByTask);

					if (allNeedByTask) {
						return session.doTask();
					}
				}
			}

			task = session.getPopTask();
		}

		if (task != null) {
			// Controler.debugLoger.debug("edrf : " + allNeededByTopTask);
			boolean isNeedConfirm = false;
			for (FactType factType1 : task.getTaskType().getNeededFacts()
					.values()) {
				if (factType1.getTypeName().equals("Confirm")) {
					isNeedConfirm = true;
					break;
				}
			}

			if (checkConfirm(condition.getFactSet()) == -1 && !isNeedConfirm) {
				session.releaseTask();
				ResultBean resultBean = new ResultBean();
				resultBean.setAnswer("很高兴为您服务，请问还有其它什么可以帮助您？");
				resultBean.setSessionId(condition.getSessionId());
				List<ResultBean> resultList = new ArrayList<ResultBean>(1);
				resultList.add(resultBean);
				return resultList;
			}

			for (@SuppressWarnings("rawtypes")
			Fact fact : factSet) {
				if (fact != null
						&& !fact.getType().getTypeName().equals("Confirm")
						&& !task.getTaskType().getNeededFacts().values()
								.contains(fact.getType())) {
					allNeededByTopTask = false;
				}
			}

			// Controler.debugLoger.debug("edrf1234 : " + allNeededByTopTask);

			if (allNeededByTopTask) {
				return session.doTask();
			} else {
				return null;
			}

		} else {
			lastTask = session.getLastFinishedTask();

			if (lastTask == null) {
				return null;
			}

			for (@SuppressWarnings("rawtypes")
			Fact fact : factSet) {
				if (!lastTask.getTaskType().getNeededFacts().values()
						.contains(fact.getType())) {
					allNeededByLastTask = false;
				}
			}

			if (allNeededByLastTask) {
				List<ResultBean> resultList = session.doLastTask();
				return resultList;
			} else
				return null;
		}
	}

	private int checkConfirm(
			@SuppressWarnings("rawtypes") Set<EntryInfo> factSet) {
		if (factSet.size() == 1) {
			@SuppressWarnings("rawtypes")
			EntryInfo fact = factSet.iterator().next();
			if (fact.getTypeName().equals("Confirm")) {
				Controler.debugLoger.debug(fact.getInfo().getInfo().get(0).getClass()
						.getName());
				if (((Entity) fact.getInfo().getInfo().get(0)).getName().equals("Yes"))
					return 1;
				else
					return -1;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
}
