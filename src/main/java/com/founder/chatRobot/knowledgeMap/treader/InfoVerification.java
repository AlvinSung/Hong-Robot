package com.founder.chatRobot.knowledgeMap.treader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.InternalFact;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.session.common.Session;
import com.founder.chatRobot.context.task.InternalTask;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.controler.common.Controler;
import com.founder.chatRobot.knowledgeMap.conception.common.Conception;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.conception.taskType.InternalTaskConception;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;
import com.founder.chatRobot.task.treader.InfoVerificationTreader;

class Pair {
	private Entity product;
	private Entity attribute;

	public Pair(Entity product, Entity attribute) {
		this.product = product;
		this.attribute = attribute;
	}

	public Entity getProduct() {
		return product;
	}

	public Entity getAttribute() {
		return attribute;
	}

}

public class InfoVerification {
	private static TaskConception taskType;
	private static Map<String, Set<Pair>> pairSetMap = new HashMap<String, Set<Pair>>();

	static {
		InfoVerification.taskType = new InternalTaskConception(
				"InforVerificationTask", new InfoVerificationTreader(), "信息查詢");
	}

	private static boolean isParent(Entity parent, Entity entity) {
		if (entity.getParent() == null) {
			return false;
		} else if (entity.getParent().equals(parent)) {
			return true;
		} else {
			return isParent(parent, (Entity) entity.getParent());
		}
	}

	private static List<ResultBean> infoFinder(String sessionId,
			Entity product, Collection<Entity> attributes,
			Collection<Entity> values) {
		List<ResultBean> resultList = new ArrayList<ResultBean>(10);
		Set<Pair> pairSet = InfoVerification.pairSetMap.get(sessionId);

		if (attributes != null && attributes.size() > 0) {
			for (Entity attribute : attributes) {
				Set<Entity> usingValues = new HashSet<Entity>();
				if (values != null) {
					usingValues.addAll(values);
					usingValues.retainAll(attribute.getAttributeToValue());
				}
				if (usingValues.size() > 0) {
					for (Entity value : usingValues) {
						if (value.getValueToProduct().contains(product)) {
							String answer = product.getMainExpress() + "的"
									+ attribute.getMainExpress() + "是"
									+ value.getMainExpress();
							ResultBean result = new ResultBean();
							result.setAnswer(answer);
							result.setFinished();
							result.setSessionId(sessionId);
							result.addResult(attribute.getName(),
									value.getMainExpress());
							// Controler.debugLoger.debug(0);
							resultList.add(result);
							pairSet.add(new Pair(product, attribute));
						} else {/*
								 * Set<Entity> cancaditeProducts = new
								 * HashSet<Entity>( value.getValueToProduct());
								 * cancaditeProducts.retainAll(attribute
								 * .getAttributeToProduct()); boolean found =
								 * false; for (Entity cancadite :
								 * cancaditeProducts) { if (isParent(product,
								 * cancadite)) { found = true; String answer =
								 * cancadite.getMainExpress() + "的" +
								 * attribute.getMainExpress() + "是" +
								 * value.getMainExpress(); ResultBean result =
								 * new ResultBean(); result.setAnswer(answer);
								 * result.setFinished();
								 * result.setSessionId(sessionId);
								 * result.addResult(attribute.getName(),
								 * value.getMainExpress()); //
								 * Controler.debugLoger.debug(1);
								 * resultList.add(result); pairSet.add(new
								 * Pair(cancadite, attribute)); } } if (!found)
								 * { String answer = "没有" +
								 * product.getMainExpress() + "的" +
								 * attribute.getMainExpress() + "是" +
								 * value.getMainExpress(); ResultBean result =
								 * new ResultBean(); result.setAnswer(answer);
								 * result.setFinished();
								 * result.setSessionId(sessionId); //
								 * Controler.debugLoger.debug(2);
								 * resultList.add(result); }
								 */
						}
					}
				} else {
					Set<Entity> cancaditeValues = new HashSet<Entity>(
							attribute.getAttributeToValue());
					cancaditeValues.retainAll(product.getProductToValue());
					ResultBean result = new ResultBean();
					result.setFinished();
					result.setSessionId(sessionId);
					if (cancaditeValues.size() > 0) {
						Iterator<Entity> cancaditeIterator = cancaditeValues
								.iterator();
						Entity next = cancaditeIterator.next();
						String answer = product.getMainExpress() + "的"
								+ attribute.getMainExpress() + "是"
								+ next.getMainExpress();
						/*
						 * Controler.debugLoger.debug("ytrewq : " +
						 * cancaditeIterator.hasNext());
						 */
						result.addResult(attribute.getName(),
								next.getMainExpress());
						while (cancaditeIterator.hasNext()) {
							next = cancaditeIterator.next();
							answer += "、" + next.getMainExpress();
							result.addResult(attribute.getName(),
									next.getMainExpress());
						}
						result.setAnswer(answer);
					} else {
						String answer = "没有" + product.getMainExpress() + "的"
								+ attribute.getMainExpress() + "的相关信息";
						result.setAnswer(answer);
					}
					// Controler.debugLoger.debug(3);
					pairSet.add(new Pair(product, attribute));
					resultList.add(result);
				}
			}
		} else if (values != null && values.size() > 0) {
			for (Entity value : values) {
				Set<Entity> cancaditeProducts = new HashSet<Entity>(
						value.getValueToProduct());
				if (cancaditeProducts.contains(product)) {
					Set<Entity> cancaditeAttributes = new HashSet<Entity>(
							value.getValueToAttribute());
					cancaditeAttributes.retainAll(product
							.getProductToAttribute());
					ResultBean result = new ResultBean();
					String answer = product.getMainExpress() + "的";
					for (Entity attribute : cancaditeAttributes) {
						answer += attribute.getMainExpress() + "、";
						pairSet.add(new Pair(product, attribute));
						result.addResult(attribute.getName(),
								value.getMainExpress());
					}
					answer = answer.substring(0, answer.length() - 1);
					answer += "是" + value.getMainExpress();
					result.setAnswer(answer);
					result.setFinished();
					result.setSessionId(sessionId);
					// Controler.debugLoger.debug(4);
					resultList.add(result);
				} else {
					boolean found = false;
					for (Entity cancadite : cancaditeProducts) {
						if (isParent(product, cancadite)) {
							found = true;
							Set<Entity> cancaditeAttributes = new HashSet<Entity>(
									value.getValueToAttribute());
							cancaditeAttributes.retainAll(product
									.getProductToAttribute());
							ResultBean result = new ResultBean();
							String answer = cancadite.getMainExpress() + "的";
							for (Entity attribute : cancaditeAttributes) {
								answer += attribute.getMainExpress() + "、";
								pairSet.add(new Pair(cancadite, attribute));
								result.addResult(attribute.getName(),
										value.getMainExpress());
							}
							answer = answer.substring(0, answer.length() - 1);
							answer += "是" + value.getMainExpress();
							result.setAnswer(answer);
							result.setFinished();
							result.setSessionId(sessionId);
							// Controler.debugLoger.debug(5);
							resultList.add(result);
						}
					}
					if (!found) {
						String answer = "没有" + product.getMainExpress() + "和"
								+ value.getMainExpress() + "相关信息";
						ResultBean result = new ResultBean();
						result.setAnswer(answer);
						result.setFinished();
						result.setSessionId(sessionId);
						// Controler.debugLoger.debug(6);
						resultList.add(result);
					}
				}
			}
		}

		/*
		 * Controler.debugLoger.debug("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
		 * Controler.debugLoger.debug(pairSet.size());
		 */

		if (resultList.size() > 0) {
			// Controler.debugLoger.debug(7);
			return resultList;
		} else {
			// Controler.debugLoger.debug(8);
			return null;
		}
	}

	private static Set<Entity> foudAttribute(Entity product,
			Collection<Entity> cancadite, Set<Entity> conditionSet) {
		Set<Entity> result = new HashSet<Entity>();

		for (Entity attribute : cancadite) {
			if (product.getProductToAttribute().contains(attribute)
					&& (conditionSet.contains(attribute) || conditionSet
							.contains(product))) {
				result.add(attribute);
			} else {
				Set<Entity> cancaditeProducts = new HashSet<Entity>(
						attribute.getAttributeToProduct());
				if (cancaditeProducts.size() > 0) {
					for (Entity cancaditeProduct : cancaditeProducts) {
						if (isParent(product, cancaditeProduct)
								&& (conditionSet.contains(attribute) || conditionSet
										.contains(product))) {
							result.add(attribute);
							break;
						}
					}
				}
			}
		}

		if (result.size() > 0)
			return result;
		else
			return null;
	}

	private static Set<Entity> foudValue(Entity product,
			Collection<Entity> cancadite, Set<Entity> conditionSet) {
		Set<Entity> result = new HashSet<Entity>();

		for (Entity value : cancadite) {
			if (product.getProductToValue().contains(value)
					&& (conditionSet.contains(value) || conditionSet
							.contains(product))) {
				result.add(value);
			} else {
				Set<Entity> cancaditeProducts = new HashSet<Entity>(
						value.getValueToProduct());
				if (cancaditeProducts.size() > 0) {
					for (Entity cancaditeProduct : cancaditeProducts) {
						if (isParent(product, cancaditeProduct)
								&& (conditionSet.contains(value) || conditionSet
										.contains(product))) {
							result.add(value);
							break;
						}
					}
				}
			}
		}

		if (result.size() > 0)
			return result;
		else
			return null;
	}

	private static List<ResultBean> posTread(Set<Pair> pairSet,
			Session session,
			@SuppressWarnings("rawtypes") Map<Entity, Fact> typeMap) {
		List<ResultBean> resultList = new ArrayList<ResultBean>();
		for (Pair pair : pairSet) {
			if ((pair.getAttribute().getFactType() != null && pair.getProduct()
					.getFactType() != null)
					|| (pair.getAttribute().getFactType() != null && ((Entity) pair
							.getProduct().getParent()).getFactType() != null)) {
				Set<TaskConception> taskTypeSet = new HashSet<TaskConception>(
						pair.getAttribute().getFactType().getUsingTaskType());
				if (pair.getProduct().getFactType() != null)
					taskTypeSet.retainAll(pair.getProduct().getFactType()
							.getUsingTaskType());
				else
					taskTypeSet.retainAll(((Entity) pair.getProduct()
							.getParent()).getFactType().getUsingTaskType());
				for (TaskConception taskType : taskTypeSet) {
					Set<TaskConception> subsequentTaskTypeSet = taskType
							.getSubsequentTaskType();
					for (TaskConception subsequentTaskType : subsequentTaskTypeSet) {
						if (subsequentTaskType.checkPosCondition(session)) {
							Task subsequentTask = new InternalTask(
									subsequentTaskType, session, true);
							session.addNewTask(subsequentTask);
							/*
							 * Controler.debugLoger.debug("qqq : " +
							 * subsequentTask.getTaskType().getName());
							 */
						}
					}
				}
			}
		}

		Task task = new InternalTask(taskType, session, false);
		Entity product;
		for (Pair pair : pairSet) {
			product = pair.getProduct();
			Entity attribute = pair.getAttribute();

			/*
			 * Controler.debugLoger.debug("ppp : " + product + " : " +
			 * typeMap.get(product) + " : " + attribute + " : " +
			 * typeMap.get(attribute));
			 */

			task.addFact(typeMap.get(product));
			task.addFact(typeMap.get(attribute));
		}

		if (session.getPopTask() != null
				&& session.getPopTask().isNeededConfirm()
				&& session.getPopTask().getAge() == 0) {
			// Controler.debugLoger.debug("tttttttttttttttttttttttttttt");
			session.releaseTask();
		}

		// Controler.debugLoger.debug("tttttttttttttttttttttttttttt");
		session.addNewTask(task);
		resultList.addAll(session.doTask());
		return resultList;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<ResultBean> tread2(ConditionBean condition,
			Session session) {
		List<ResultBean> resultList = new ArrayList<ResultBean>(10);
		List<Entity> entitySet = new LinkedList<Entity>();
		List<Entity> candidateSet = new LinkedList<Entity>();
		Set<Entity> lastEntitySet = new HashSet<Entity>();
		FactType factType;
		Map<Entity, Fact> typeMap = new HashMap<Entity, Fact>();
		Set<Entity> newEntity = new HashSet<Entity>();
		// boolean allNeededByLastFinishedTask = true, allNeedByLastDoTask =
		// true;
		Set<Pair> pairSet = new HashSet<Pair>();
		boolean allNeededByPopTask = true, allNeededByLastTask = true;

		InfoVerification.pairSetMap.put(session.getSessionId(), pairSet);

		for (EntryInfo info : condition.getFactSet()) {
			if (session.getPopTask() != null
					&& !session.getPopTask().isNeededConfirm()
					&& session.getContext().getFactType(info.getTypeName()) != null
					&& !info.getTypeName().startsWith("__ATTRIBUTE__")
					&& session
							.getPopTask()
							.getTaskType()
							.getNeededFacts()
							.values()
							.contains(
									session.getContext().getFactType(
											info.getTypeName()))) {
			} else
				allNeededByPopTask = false;
		}

		if (allNeededByPopTask) {
			// Controler.debugLoger.debug("ccccccccccc");
			return null;
		}

		for (EntryInfo info : condition.getFactSet()) {
			if (session.getLastDoTask() != null
					&& session.getContext().getFactType(info.getTypeName()) != null
					&& !info.getTypeName().startsWith("__ATTRIBUTE__")
					&& session
							.getLastDoTask()
							.getTaskType()
							.getNeededFacts()
							.values()
							.contains(
									session.getContext().getFactType(
											info.getTypeName()))) {
			} else
				allNeededByLastTask = false;
		}

		if (allNeededByLastTask) {
			// Controler.debugLoger.debug("xxxxxxxxxxxxxxxx");
			return null;
		}

		// Controler.debugLoger.debug("xxxxxxxxxxxxxxxx");

		for (EntryInfo info : condition.getFactSet()) {
			List<Object> ComparableList = info.getInfo().getInfo();
			for (Object entity : ComparableList)
				if (entity instanceof Entity) {
					entitySet.add((Entity) entity);
					newEntity.add((Entity) entity);
					Fact fact = null;

					/*
					 * Controler.debugLoger.debug(entity);
					 * 
					 * Controler.debugLoger.debug(((Entity) entity).getName() + " : " +
					 * ((Entity) entity).getMainExpress());
					 */

					if (info.getTypeName().startsWith("__ATTRIBUTE__")) {
						factType = session.getContext().getFactType(
								info.getTypeName().replaceFirst(
										"__ATTRIBUTE__", ""));
						if (factType != null) {
							fact = new InternalFact(factType, info.getInfo(),
									session, true);
							typeMap.put((Entity) entity, fact);
							break;
						}
					} else {
						factType = session.getContext().getFactType(
								info.getTypeName());
						if (factType != null) {
							fact = new InternalFact(factType, info.getInfo(),
									session, false);
							typeMap.put((Entity) entity, fact);
							break;
						}
					}

				}
		}

		candidateSet.addAll(entitySet);

		// Controler.debugLoger.debug("xxxxxxxxxxxxxxxx");

		/*
		 * Controler.debugLoger.debug("ggggggggggggggg"); if
		 * (session.getLastFinishedTask() != null){
		 * Controler.debugLoger.debug(session.getLastFinishedTask
		 * ().getTaskType().getTypeName());
		 * Controler.debugLoger.debug(session.getLastFinishedTask
		 * ().getFactList().size()); }
		 */
		if (session.getLastDoTask() != null) {
			// && session.getLastFinishedTask().getTaskType() == taskType) {
			// Controler.debugLoger.debug("hhhhhhhhhhhhhhhhhh");
			for (Fact fact1 : session.getLastDoTask().getFactList()) {
				if (fact1 != null) {
					for (Object entity : fact1.getInfo().getInfo()) {
						if (entity instanceof Entity) {
							lastEntitySet.add((Entity) entity);
							/*
							 * if (newEntity.contains(entity)) {
							 * newEntity.remove(entity); }
							 */
							typeMap.put((Entity) entity, fact1);
						}
					}
				} else {
					Controler.debugLoger.debug("null entity");
				}
			}
		}

		// Controler.debugLoger.debug("last entitys : " + lastEntitySet.size());

		if (lastEntitySet.size() > 0 && entitySet.containsAll(lastEntitySet)) {
			// Controler.debugLoger.debug("vvvvvvvvvvvvv");
			return null;
		} else {
			for (Entity entity : lastEntitySet) {
				if (!entitySet.contains(entity)) {
					entitySet.add(0, entity);
				}
				if (session.getLastFinishedTask() != null
						&& session.getLastFinishedTask().getTaskType() == taskType) {
					candidateSet.add(0, entity);
				}
			}
			// entitySet.addAll(lastEntitySet);
		}

		for (Entity entity : entitySet) {
			Set<Entity> attributeSet = foudAttribute(entity, candidateSet,
					newEntity);
			Set<Entity> valueSet = foudValue(entity, candidateSet, newEntity);
			List<ResultBean> tmpList = infoFinder(session.getSessionId(),
					entity, attributeSet, valueSet);
			if (tmpList != null)
				resultList.addAll(tmpList);
		}

		/*
		 * Controler.debugLoger.debug("yyyyyyyyyyy");
		 * Controler.debugLoger.debug(pairSet.size());
		 */

		if (resultList.size() > 0) {
			resultList.addAll(posTread(pairSet, session, typeMap));
			return resultList;
		} else
			return null;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<ResultBean> tread(ConditionBean condition,
			Session session) {
		ResultBean result = new ResultBean();
		List<ResultBean> resultList = new ArrayList<ResultBean>(10);
		boolean hasResult = false;
		Entity product = null;
		List<Entity> entitySet = new LinkedList<Entity>();
		Set<Entity> lastEntitySet = new HashSet<Entity>();
		List<Entity> productList = new ArrayList<Entity>();
		Set<Conception> filter1;
		Set<Entity> filter2, uniqueSet;
		Set<Pair> pairSet = new HashSet<Pair>();
		String answer = "";
		FactType factType;
		Map<Entity, Fact> typeMap = new HashMap<Entity, Fact>();
		Set<Entity> newEntity = new HashSet<Entity>();
		boolean allNeededByPopTask = true, allNeededByLastTask = true;

		// Controler.debugLoger.debug("uuu : " + session.getFacts().size());

		List<ResultBean> tmpResultList = treadDegree(condition, session);
		if (tmpResultList != null) {
			return tmpResultList;
		}

		for (EntryInfo info : condition.getFactSet()) {
			if (session.getPopTask() != null
					&& !session.getPopTask().isNeededConfirm()
					&& session.getContext().getFactType(info.getTypeName()) != null
					&& !info.getTypeName().startsWith("__ATTRIBUTE__")
					&& session
							.getPopTask()
							.getTaskType()
							.getNeededFacts()
							.values()
							.contains(
									session.getContext().getFactType(
											info.getTypeName()))) {
			} else
				allNeededByPopTask = false;
		}

		if (allNeededByPopTask) {
			// Controler.debugLoger.debug("ccccccccccc");
			return null;
		}

		for (EntryInfo info : condition.getFactSet()) {
			if (session.getLastDoTask() != null
					&& session.getContext().getFactType(info.getTypeName()) != null
					&& !info.getTypeName().startsWith("__ATTRIBUTE__")
					&& session
							.getLastDoTask()
							.getTaskType()
							.getNeededFacts()
							.values()
							.contains(
									session.getContext().getFactType(
											info.getTypeName()))) {
			} else
				allNeededByLastTask = false;
		}

		if (allNeededByLastTask) {
			// Controler.debugLoger.debug("xxxxxxxxxxxxxxxx");
			return null;
		}

		// Controler.debugLoger.debug("xxxxxxxxxxxxxxxx");

		for (EntryInfo info : condition.getFactSet()) {
			List<Object> ComparableList = info.getInfo().getInfo();
			for (Object entity : ComparableList)
				if (entity instanceof Entity) {
					entitySet.add((Entity) entity);
					newEntity.add((Entity) entity);
					Fact fact = null;

					if (info.getTypeName().startsWith("__ATTRIBUTE__")) {
						factType = session.getContext().getFactType(
								info.getTypeName().replaceFirst(
										"__ATTRIBUTE__", ""));
						if (factType != null) {
							fact = new InternalFact(factType, info.getInfo(),
									session, true);
							typeMap.put((Entity) entity, fact);
							break;
						}
					} else {
						factType = session.getContext().getFactType(
								info.getTypeName());
						if (factType != null) {
							fact = new InternalFact(factType, info.getInfo(),
									session, false);
							typeMap.put((Entity) entity, fact);
							break;
						}
					}

				}
		}

		// Controler.debugLoger.debug("xxxxxxxxxxxxxxxx");

		/*
		 * Controler.debugLoger.debug("ggggggggggggggg"); if
		 * (session.getLastFinishedTask() != null){
		 * Controler.debugLoger.debug(session.getLastFinishedTask
		 * ().getTaskType().getTypeName());
		 * Controler.debugLoger.debug(session.getLastFinishedTask
		 * ().getFactList().size()); }
		 */
		if (session.getLastDoTask() != null) {
			// && session.getLastFinishedTask().getTaskType() == taskType) {
			// Controler.debugLoger.debug("hhhhhhhhhhhhhhhhhh");
			for (Fact fact1 : session.getLastDoTask().getFactList()) {
				if (fact1 != null) {
					for (Object entity : fact1.getInfo().getInfo()) {
						if (entity instanceof Entity) {
							lastEntitySet.add((Entity) entity);
							/*
							 * if (newEntity.contains(entity)) {
							 * newEntity.remove(entity); }
							 */
							typeMap.put((Entity) entity, fact1);
						}
					}
				} else {
					Controler.debugLoger.debug("null entity");
				}
			}
		}

		// Controler.debugLoger.debug("last entitys : " + lastEntitySet.size());

		if (lastEntitySet.size() > 0 && entitySet.containsAll(lastEntitySet)) {
			// Controler.debugLoger.debug("vvvvvvvvvvvvv");
			return null;
		} else {
			for (Entity entity : lastEntitySet) {
				if (!entitySet.contains(entity)) {
					entitySet.add(0, entity);
				}
			}
			// entitySet.addAll(lastEntitySet);
		}

		for (Entity entity : entitySet) {
			filter1 = new HashSet<Conception>(entitySet);
			filter2 = new HashSet<Entity>(entitySet);
			filter1.retainAll(entity.getProductToAttribute());
			filter2.retainAll(entity.getProductToValue());

			// Controler.debugLoger.debug("entity : " + entity.getName());

			if (filter1.size() > 0 || filter2.size() > 0) {
				productList.add(entity);
			}
		}

		if (entitySet.size() > 0) {
			for (Entity entity : entitySet) {
				product = entity;
				Entity value = null;
				Entity attribute = null;
				boolean isNewAttribute = false;
				boolean isNewValue = false;
				// boolean isNewProduct = newEntity.contains(product);
				for (Entity each : entitySet) {
					if (each != entity) {
						if (entity.getProductToAttribute().contains(each)) {
							if (attribute == null) {
								attribute = each;
								if (newEntity.contains(attribute)) {
									isNewAttribute = true;
								}
							} else if (!isNewAttribute
									&& newEntity.contains(each)) {
								attribute = each;
								isNewAttribute = true;
							}
						}
					}
				}

				for (Entity each : entitySet) {
					if (each != entity) {
						filter1 = new HashSet<Conception>(
								each.getValueToAttribute());
						filter1.retainAll(product.getProductToAttribute());
						if (attribute == null
								|| (each != attribute && attribute
										.getAttributeToValue().contains(each))) {
							if (filter1.size() > 0) {
								if (value == null) {
									value = each;
									if (newEntity.contains(value)) {
										isNewValue = true;
									}
								}
							} else if (!isNewValue && newEntity.contains(value)) {
								value = each;
								isNewValue = true;
							}
						} else {

						}
					}
				}

				if ((product != null && newEntity.contains(product))
						|| (attribute != null && newEntity.contains(attribute))
						|| (value != null && newEntity.contains(value)))
					if (attribute != null) {
						if (value != null) {
							if (value.getValueToProduct().contains(product)) {
								/*
								 * Controler.debugLoger.debug("fff : " +
								 * attribute.getName());
								 * Controler.debugLoger.debug("fff : " +
								 * attribute.getMainExpress());
								 * Controler.debugLoger.debug("ggg : " +
								 * value.getName()); Controler.debugLoger.debug("ggg : "
								 * + value.getMainExpress());
								 */
								/*
								 * result.setAnswer(product.getExpress() + "的" +
								 * attribute.getExpress() + "是" +
								 * value.getExpress() + "。\n");
								 */
								result.setAnswer("是的");
								result.setFinished();
								result.setSessionId(condition.getSessionId());
								// Controler.debugLoger.debug(8);
								hasResult = true;
								pairSet.add(new Pair(product, attribute));
							} else {
								if (product.getChildren().size() <= 0) {
									result.setAnswer("不是");
									result.setFinished();
									result.setSessionId(condition
											.getSessionId());
									// Controler.debugLoger.debug(6);
									hasResult = true;
									pairSet.add(new Pair(product, attribute));
								} else {
									filter1 = new HashSet<Conception>(
											product.getChildren());
									filter1.retainAll(value.getValueToProduct());
									if (filter1.size() > 0) {
										answer = "";
										int i = 0;
										for (Conception each : filter1) {
											if (i > 0) {
												answer += "、";
											}
											i++;
											answer += ((Entity) each)
													.getMainExpress();
										}
										answer += "的"
												+ attribute.getMainExpress()
												+ "是" + value.getMainExpress();
										result.setAnswer(answer);
										result.setFinished();
										result.setSessionId(condition
												.getSessionId());
										// Controler.debugLoger.debug(6);
										hasResult = true;
										pairSet.add(new Pair(product, attribute));
									}
								}
							}

						} else if ((answer = findProduct(entitySet, attribute))
								.equals("")) {
							// Controler.debugLoger.debug(5);

							uniqueSet = new HashSet<Entity>(
									attribute.getAttributeToValue());
							uniqueSet.retainAll(product.getProductToValue());
							if (uniqueSet.size() > 0) {
								answer += product.getMainExpress() + "的"
										+ attribute.getMainExpress() + "是";
								int i = 0;
								for (Entity each : uniqueSet) {
									if (i != 0)
										answer += "和";
									i++;
									if (each.getMainExpress() == null
											|| each.getMainExpress().equals("")) {
										answer += each.getValue();
									} else
										answer += each.getMainExpress();
									result.addResult(attribute.getName(),
											each.getMainExpress());
								}
								// answer += "";
							} else {
								answer += "无法找到" + product.getMainExpress()
										+ "的" + attribute.getMainExpress()
										+ "相关描述";
							}
							result.setAnswer(answer);
							result.setFinished();
							result.setSessionId(condition.getSessionId());
							hasResult = true;
							pairSet.add(new Pair(product, attribute));
							/*
							 * Controler.debugLoger.debug("kkk : " + product + " :" +
							 * attribute);
							 */
						} else {
							// Controler.debugLoger.debug(4);
							result.setAnswer(answer);
							result.setFinished();
							result.setSessionId(condition.getSessionId());
							hasResult = true;
							pairSet.add(new Pair(product, attribute));
						}
					} else if (value != null) {
						uniqueSet = new HashSet<Entity>(
								product.getProductToAttribute());
						uniqueSet.retainAll(value.getValueToAttribute());

						if (product.getProductToValue().contains(value)) {
							answer = product.getMainExpress() + "的";
							int i = 0, attributeCount = 0;
							for (Entity each : uniqueSet) {
								if (!each.getName().equals("Situated")) {
									if (i > 0)
										answer += "和";
									i++;
									answer += each.getMainExpress();
									attributeCount++;
								}
							}
							if (attributeCount > 0) {
								answer += "是" + value.getMainExpress() + "。";
								result.setAnswer(answer);
								result.setFinished();
								result.setSessionId(condition.getSessionId());
								hasResult = true;
							}
						} else {
							result.setAnswer(product.getMainExpress() + "不是"
									+ value.getMainExpress() + "。");
							result.setFinished();
							result.setSessionId(condition.getSessionId());
							hasResult = true;
						}
					}

			}
		}

		if (hasResult) {
			for (Pair pair : pairSet) {
				if ((pair.getAttribute().getFactType() != null && pair
						.getProduct().getFactType() != null)
						|| (pair.getAttribute().getFactType() != null && ((Entity) pair
								.getProduct().getParent()).getFactType() != null)) {
					Set<TaskConception> taskTypeSet = new HashSet<TaskConception>(
							pair.getAttribute().getFactType()
									.getUsingTaskType());
					if (pair.getProduct().getFactType() != null)
						taskTypeSet.retainAll(pair.getProduct().getFactType()
								.getUsingTaskType());
					else
						taskTypeSet.retainAll(((Entity) pair.getProduct()
								.getParent()).getFactType().getUsingTaskType());
					for (TaskConception taskType : taskTypeSet) {
						Set<TaskConception> subsequentTaskTypeSet = taskType
								.getSubsequentTaskType();
						for (TaskConception subsequentTaskType : subsequentTaskTypeSet) {
							if (subsequentTaskType.checkPosCondition(session)) {
								Task subsequentTask = new InternalTask(
										subsequentTaskType, session, true);
								session.addNewTask(subsequentTask);
								/*
								 * Controler.debugLoger.debug("qqq : " +
								 * subsequentTask.getTaskType().getName());
								 */
							}

						}
					}
				}
			}

			Task task = new InternalTask(taskType, session, false);
			for (Pair pair : pairSet) {
				product = pair.getProduct();
				Entity attribute = pair.getAttribute();

				if (product != null) {
					task.addFact(typeMap.get(product));
					//Controler.debugLoger.debug("product : " + product.getName());
				}
				if (attribute != null) {
					task.addFact(typeMap.get(attribute));
					//Controler.debugLoger.debug("attribute : " + attribute.getName());
				}
			}

			/*
			 * if (session.getPopTask() != null &&
			 * session.getPopTask().isNeededConfirm() &&
			 * session.getPopTask().getAge() == 0) { //
			 * Controler.debugLoger.debug("tttttttttttttttttttttttttttt");
			 * session.releaseTask(); }
			 */

			// Controler.debugLoger.debug("tttttttttttttttttttttttttttt");
			session.addNewTask(task);
			resultList.add(result);
			List<ResultBean> tempList = session.doTask();
			for (ResultBean bean : tempList) {
				if (bean.getAnswer() != null && bean.getAnswer().length() > 0) {
					resultList.add(bean);
				}
			}
			// resultList.addAll(session.doTask());
			return resultList;
		} else {
			// Controler.debugLoger.debug("uuu : " + session.getFacts().size());
			return null;
		}
	}

	private static String findProduct(Collection<Entity> entityList,
			Entity entity) {
		return "";
	}

	private static List<ResultBean> treadDegree(ConditionBean condition,
			Session session) {
		List<ResultBean> resultList = new ArrayList<ResultBean>(10);
		Task lastTask = session.getLastDoTask();
		@SuppressWarnings("rawtypes")
		Fact product, attribute;

		if (lastTask != null
				&& lastTask.getTaskType().getTypeName()
						.equals("InforVerificationTask")) {
			if (lastTask.getFactList().size() >= 2
					&& ((Entity) lastTask.getFactList().get(0).getInfo()
							.getInfo().get(0)).getProductToAttribute()
							.contains(
									(Entity) lastTask.getFactList().get(1)
											.getInfo().getInfo().get(0))) {
				product = lastTask.getFactList().get(0);
				attribute = lastTask.getFactList().get(1);
			} else {
				product = lastTask.getFactList().get(1);
				attribute = lastTask.getFactList().get(0);
			}

			boolean isDegree = true;
			boolean hasDegree = false;
			Entity degreeEntity = null;
			for (@SuppressWarnings("rawtypes")
			EntryInfo fact : condition.getFactSet()) {
				if ((fact.getTypeName().equals(product.getType().getTypeName()) && fact
						.getInfo().getInfo().get(0).equals(product))
						|| (fact.getTypeName().equals(
								attribute.getType().getTypeName()) && fact
								.getInfo().getInfo().get(0).equals(attribute))
						|| (fact.getTypeName()
								.equals("ATTRIBUTE"
										+ attribute.getType().getTypeName()) && fact
								.getInfo().getInfo().get(0).equals(attribute))) {
					continue;
				} else if (fact.getTypeName().equals("Degree")) {
					hasDegree = true;
					degreeEntity = (Entity) fact.getInfo().getInfo().get(0);
					continue;
				} else {
					isDegree = false;
					break;
				}
			}

			if (hasDegree && isDegree) {
				Set<Conception> infoSet = new HashSet<Conception>(
						degreeEntity.getAllConnectConception());
				infoSet.retainAll(((Entity) attribute.getInfo().getInfo()
						.get(0)).getAllConnectConception());
				if (infoSet.size() == 1) {
					ResultBean result = new ResultBean();
					result.setAnswer(((Entity) infoSet.iterator().next())
							.getMainExpress());
					resultList.add(result);
					return resultList;
				} else if (infoSet.size() == 0) {
					return null;
				} else {
					infoSet.retainAll(((Entity) product.getInfo().getInfo()
							.get(0)).getAllConnectConception());
					if (infoSet.size() > 0) {
						ResultBean result = new ResultBean();
						result.setAnswer(((Entity) infoSet.iterator().next())
								.getMainExpress());
						resultList.add(result);
						return resultList;
					} else {
						return null;
					}
				}
			} else {
				return null;
			}
		} else {
			return null;
		}

	}
}
