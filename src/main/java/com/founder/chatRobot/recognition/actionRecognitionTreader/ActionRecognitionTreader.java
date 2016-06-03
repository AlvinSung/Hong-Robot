package com.founder.chatRobot.recognition.actionRecognitionTreader;

import java.util.HashSet;
import java.util.Set;

import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.controler.common.Controler;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;
import com.founder.chatRobot.recognition.common.interfaces.old.PostTreader;

public class ActionRecognitionTreader implements PostTreader {
	private KnowledgeManager knowledgeManager;
	private Entity actionObject, actionSubject;

	public ActionRecognitionTreader(KnowledgeManager knowledgeManager) {
		this.knowledgeManager = knowledgeManager;
		this.actionSubject = (Entity) this.knowledgeManager.uniqueIndex("行为主体");
		this.actionObject = (Entity) this.knowledgeManager.uniqueIndex("行为客体");
	}

	public ConditionBean tread(ConditionBean condition) {
		ConditionBean result = new ConditionBean(condition.getSentence());
		@SuppressWarnings("rawtypes")
		Set<EntryInfo> infoSet = condition.getFactSet();
		Set<Entity> entitySet = new HashSet<Entity>();

		/*
		 * for (@SuppressWarnings("rawtypes") EntryInfo info : infoSet) { for
		 * (Object entity : info.getInfo().getInfo()) {
		 * System.out.println(info.getTypeName());
		 * System.out.println(entity.getClass().getName()); if (entity
		 * instanceof Entity) System.out.println(((Entity)
		 * entity).getMainExpress()); } }
		 */

		for (@SuppressWarnings("rawtypes")
		EntryInfo info : infoSet) {
			for (Object entity : info.getInfo().getInfo()) {
				// System.out.println(info.getTypeName());
				// System.out.println(entity.getClass().getName());
				if (entity instanceof Entity)
					entitySet.add((Entity) entity);
			}
		}

		for (@SuppressWarnings("rawtypes")
		EntryInfo info : infoSet) {
			/*
			 * System.out.println("aaaa : " + info.getTypeName());
			 * 
			 * System.out.println(((Entity) info.getInfo().getInfo().get(0))
			 * .getMainExpress());
			 */

			if (info.getTypeName().equals("Action")) {
				Set<Entity> targets = new HashSet<Entity>();
				for (Object action : info.getInfo().getInfo()) {
					for (Entity entity : ((Entity) action).getProductToValue()) {
						if (entity.getValueToAttribute().contains(
								this.actionSubject)) {
							Set<Entity> tmp = new HashSet<Entity>(entitySet);
							tmp.retainAll(entity.getAttributeToValue());
							if (tmp.size() > 0) {
								targets.add(entity);
								break;
							}
						} else if (entity.getValueToAttribute().contains(
								this.actionObject)) {
							// System.out.println("bbbb");
							Set<Entity> tmp = new HashSet<Entity>(entitySet);
							tmp.retainAll(entity.getChildren());
							if (tmp.size() > 0) {
								for (Entity each : ((Entity) action)
										.getProductToValue()) {
									if (each.getValueToAttribute().contains(
											this.actionSubject)) {
										// System.out.println("cccc : ");
										targets.add(each);
									}
								}
								break;
							}
						}
					}
				}

				if (targets.size() > 0) {
					for (Entity newEntity : targets) {
						FactInfo<Entity> factInfo = new FactInfo<Entity>(
								newEntity);
						EntryInfo<Entity> entryinfo = new EntryInfo<Entity>(
								"__ATTRIBUTE__" + newEntity.getName(),
								factInfo, info.startPos(), info.length());
						result.addFact(entryinfo);
					}
				} else {
					Controler.debugLoger
							.debug("error for recognition of action");
					Controler.debugLoger.debug(condition.getSentence());
				}
			} else {
				result.addFact(info);
			}
		}

		result.setSessionId(condition.getSessionId());
		result.setPosition(condition.getPosition());
		result.setUserId(condition.getUserId());
		// result.setSentence(condition.getSentence());

		return result;
	}
}
