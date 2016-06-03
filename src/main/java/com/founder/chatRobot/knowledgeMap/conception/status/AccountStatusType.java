package com.founder.chatRobot.knowledgeMap.conception.status;

import com.founder.chatRobot.context.session.common.Session;
import com.founder.chatRobot.knowledgeMap.conception.status.common.StatusInfo;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;

public class AccountStatusType extends InternalStatus {
	public static final int UNCREATING = 0, CHECKING = 1, CREATED = 2;

	public AccountStatusType(TaskConception treaderTask, String meetedInfo,
			String unmeetedInfo) {
		super(treaderTask, meetedInfo, unmeetedInfo);
	}

	public StatusInfo check(Session session) {
		//String userId = (String) session.getSessionId();
		
		if(session.getTempDate("CreatedCount") != null 
				&& (Boolean)session.getTempDate("CreatedCount")){
			return new StatusInfo(CREATED);
		} else {
			return new StatusInfo(true, "您需要首先开户，才能继续进行操作", true, UNCREATING);
		}
		
		/*if (userId.equals("11111")) {
			return new StatusInfo(CREATED);
		} else if (userId.equals("22222")) {
			return new StatusInfo(false, "账户信息正在审核中，请您稍加等候", false, CHECKING);
		} else {
			Task task = new InternalTask(this.getTreaderTask(), session, false);
			session.addNewTask(task);
			return new StatusInfo(true, "您需要首先开户，才能继续进行操作", true, UNCREATING);
		}*/
	}

}