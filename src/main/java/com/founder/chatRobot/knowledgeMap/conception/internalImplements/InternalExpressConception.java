package com.founder.chatRobot.knowledgeMap.conception.internalImplements;

import com.founder.chatRobot.knowledgeMap.conception.common.ConceptionType;
import com.founder.chatRobot.knowledgeMap.conception.common.ExpressConception;

public abstract class InternalExpressConception extends InternalConception
		implements ExpressConception {
	private String mainExpress = "";

	public InternalExpressConception(ConceptionType type) {
		super(type);
	}

	public String getMainExpress() {
		return mainExpress;
	}

	public void setMainExpress(String express) {
		this.mainExpress = express;
	}

}
