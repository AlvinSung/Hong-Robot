package com.founder.chatRobot.recognition.statusActionRecognition;

import java.util.List;

import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.knowledgeMap.conception.status.common.Status;
import com.founder.chatRobot.recognition.common.interfaces.old.PostTreader;

public class StatusActionRecognition implements PostTreader {

	@SuppressWarnings("unchecked")
	public ConditionBean tread(ConditionBean condition) {
		for (@SuppressWarnings("rawtypes")
		EntryInfo info : condition.getFactSet()) {
			for (Object entity : (List<Object>) info.getInfo().getInfo()) {
				if (entity instanceof Status) {
					condition.addStatu(((Status) entity));
					condition.getFactSet().remove(entity);
				}
			}
		}
		return condition;
	}
}
