package com.founder.chatRobot.recognition.addressRegular;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.controler.common.Controler;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.recognition.common.interfaces.old.PostTreader;

public class AddressRegular implements PostTreader {

	@SuppressWarnings("unchecked")
	public ConditionBean tread(ConditionBean condition) {
		ConditionBean result = new ConditionBean(condition.getSentence());
		@SuppressWarnings("rawtypes")
		List<EntryInfo> divisionList = new LinkedList<EntryInfo>();

		result.setPosition(condition.getPosition());
		result.setSessionId(condition.getSessionId());
		result.setUserId(condition.getUserId());

		Entity situated = (Entity) Controler.knowledgeManager
				.uniqueIndex("Situated");
		//System.out.println("situated : " + ((Entity) situated));
		for (@SuppressWarnings("rawtypes")
		EntryInfo info : condition.getFactSet()) {
			// 需要修改判定条件为是否关联“位于”属性名点
			// if (info.getTypeName().equals("Division")) {
			Object entity = info.getInfo().getInfo().get(0);
			
			/*System.out.println("entity : " + ((Entity) entity));
			System.out.println("entity : " + ((Entity) entity).getName());
			System.out.println("entity : " + info.getTypeName());
			System.out.println(((Entity) entity).getProductToAttribute().contains(
					situated));
			for(Entity tmp : ((Entity) entity).getProductToAttribute()){
				System.out.println("attribute : " + tmp.getName());
			}*/
			
			if (entity instanceof Entity
					&& (((Entity) entity).getProductToAttribute().contains(
							situated) || ((Entity) entity)
							.getValueToAttribute().contains(situated))) {
				//System.out.println("situated : " + ((Entity) entity).getName());
				divisionList.add(info);
			} else {
				result.addFact(info);
			}
		}

		@SuppressWarnings("rawtypes")
		Set<EntryInfo> treadedDivision = new HashSet<EntryInfo>();
		/*
		 * @SuppressWarnings("rawtypes") Set<EntryInfo> addingDivision = new
		 * HashSet<EntryInfo>();
		 */

		for (@SuppressWarnings("rawtypes")
		EntryInfo division : divisionList) {
			for (@SuppressWarnings("rawtypes")
			EntryInfo next : divisionList) {
				if (division != next
						&& (division.startPos() + division.length()) == next
								.startPos()) {
					boolean isLocalIn = false;
					Entity large = (Entity) division.getInfo().getInfo().get(0);
					Entity small = (Entity) next.getInfo().getInfo().get(0);
					Set<Entity> middle = new HashSet<Entity>();
					for (Entity entity : small.getProductToValue()) {
						if (entity.getValueToAttribute().contains(situated))
							middle.add(entity);
					}
					if (middle.contains(large)) {
						isLocalIn = true;
					} else {
						Set<Entity> largeMiddle = new HashSet<Entity>();
						for (Entity entity : large.getValueToProduct()) {
							if (entity.getProductToAttribute().contains(
									situated))
								largeMiddle.add(entity);
						}
						middle.retainAll(largeMiddle);
						if (middle.size() > 0) {
							isLocalIn = true;
						}
					}

					if (isLocalIn) {
						// addingDivision.add(e)
						result.addFact(new EntryInfo<Entity>(
								next.getTypeName(), next.getInfo(), division
										.startPos(), division.length()
										+ next.length()));
						treadedDivision.add(division);
						treadedDivision.add(next);
					}
				}
			}
		}

		divisionList.removeAll(treadedDivision);
		for (@SuppressWarnings("rawtypes")
		EntryInfo division : divisionList) {
			result.addFact(division);
		}

		return result;
	}
}
