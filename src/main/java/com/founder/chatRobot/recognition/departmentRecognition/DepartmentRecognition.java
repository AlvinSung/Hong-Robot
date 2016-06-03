package com.founder.chatRobot.recognition.departmentRecognition;

import java.util.HashMap;
import java.util.Map;

import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.recognition.common.interfaces.old.Recognition;

public class DepartmentRecognition implements Recognition {
	private static Map<String, Entity> departmentMap = new HashMap<String, Entity>();

	public static void addDepartment(String name, Entity department) {
		DepartmentRecognition.departmentMap.put(name, department);
	}

	public ConditionBean recogn(String sentence, String sessionId,
			String userId, String position) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			TypeUnmatchException {
		ConditionBean result = new ConditionBean(sentence);

		result.setPosition(position);
		result.setSessionId(sessionId);
		result.setUserId(userId);

		for (String name : DepartmentRecognition.departmentMap.keySet()) {
			int star;
			if ((star = sentence.indexOf(name)) >= 0) {
				FactInfo<Entity> info = new FactInfo<Entity>(
						DepartmentRecognition.departmentMap.get(name));
				result.addFact(new EntryInfo<Entity>("Department", info, star,
						name.length()));
			}
		}

		return result;
	}

}
