package com.founder.chatRobot.knowledgeMap.conception.common;

import java.util.Map;

public interface ComposeConception extends Conception {
	public Conception getSubConception(String key);

	public Map<String, Conception> getSubConceptionMap();
}
