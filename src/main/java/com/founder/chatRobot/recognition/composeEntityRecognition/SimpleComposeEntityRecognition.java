package com.founder.chatRobot.recognition.composeEntityRecognition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.recognition.common.interfaces.old.PostTreader;

public class SimpleComposeEntityRecognition implements PostTreader {
	private Set<String> characters = new HashSet<String>();

	public SimpleComposeEntityRecognition(Set<String> characters) {
		this.characters = characters;
	}

	public ConditionBean tread(ConditionBean condition) {
		ConditionBean result = new ConditionBean(condition.getSentence());
		@SuppressWarnings("rawtypes")
		Set<EntryInfo> infoSet = condition.getFactSet();
		boolean need;

		for (@SuppressWarnings("rawtypes")
		EntryInfo info : infoSet) {
			@SuppressWarnings({ "unchecked" })
			List<Object> entityList = info.getInfo().getInfo();
			for (Object entity : entityList)
				if (!info.getTypeName().startsWith("__ATTRIBUTE__")
						&& (entity instanceof Entity)
						&& ((Entity) entity).getValueToProduct().size() == 1) {

					
					//System.out.println("compose : " + ((Entity) entity).getMainExpress());
					 

					Entity product = ((Entity) entity).getValueToProduct()
							.iterator().next();
					Set<Entity> attributeSet = new HashSet<Entity>(
							product.getProductToAttribute());
					attributeSet.retainAll(((Entity) entity)
							.getValueToAttribute());
					need = true;

					for (Entity attribute : attributeSet) {
						// System.out.println("asdfg : " + attribute);
						if (attribute.isValueComparable()) {
							need = false;
						} else {
							boolean isRight = false;
							for (String character : this.characters) {
								if (attribute.getName().equals(character)) {
									isRight = true;
								}
							}
							need = isRight;
						}
					}

					if (need) {
						Entity newEntity = ((Entity) entity)
								.getValueToProduct().iterator().next();
						FactInfo<Entity> factInfo = new FactInfo<Entity>(
								newEntity);
						EntryInfo<Entity> entryinfo = new EntryInfo<Entity>(newEntity.getParent().getName(),
								factInfo, info.startPos(), info.length());
						result.addFact(entryinfo);
						/*System.out.println("aaaaaa");
						
						System.out.println(newEntity.getProductToAttribute());
						System.out.println(newEntity.getProductToValue());*/
						 
					} else
						result.addFact(info);

				} else {
					result.addFact(info);
					// System.out.println("bbbbbb");
				}
		}

		result.setSessionId(condition.getSessionId());
		result.setPosition(condition.getPosition());
		result.setUserId(condition.getUserId());
		// result.setSentence(condition.getSentence());

		return result;
	}
}
