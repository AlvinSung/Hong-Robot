package com.founder.chatRobot.recognition.common.interfaces.old;

import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.common.task.ConditionBean;

public interface Recognition {
	public ConditionBean recogn(String sentence, String sessionId,
			String userId, String position) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			TypeUnmatchException;

}
