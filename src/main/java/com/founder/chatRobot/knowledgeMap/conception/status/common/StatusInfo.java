package com.founder.chatRobot.knowledgeMap.conception.status.common;

import java.util.Map;

import com.founder.chatRobot.knowledgeMap.conception.common.Conception;
import com.founder.chatRobot.knowledgeMap.conception.common.ConceptionType;
import com.founder.chatRobot.knowledgeMap.conception.internalImplements.InternalConception;

public class StatusInfo extends InternalConception implements Conception {
	private boolean isMeet;
	private boolean isAddNewTask;
	private boolean isNeedDo;
	private String Message;
	private Map<String, Object> messageMap;
	private int currentStatus;

	/*
	 * public StatusInfo(boolean isMeet, boolean isAddNewTask, String Message) {
	 * this.isMeet = isMeet; this.isAddNewTask = isAddNewTask; this.Message =
	 * Message; }
	 */

	// create statusInfo for meeting status
	public StatusInfo(int currentStatus) {
		super(ConceptionType.STATUS);
		this.isMeet = true;
		this.isAddNewTask = false;
		this.Message = null;
		this.isNeedDo = false;
	}

	// create statusInfo for not meeting status
	public StatusInfo(boolean isAddNewTask, String Message, boolean isNeedDo,
			int currentStatus) {
		super(ConceptionType.STATUS);
		this.isMeet = false;
		this.isAddNewTask = isAddNewTask;
		this.Message = Message;
		// this.currentStatus = currentStatus;
		this.isNeedDo = isNeedDo;
	}

	public boolean isMeet() {
		return isMeet;
	}

	public boolean isAddNewTask() {
		return isAddNewTask;
	}

	public boolean isNeedDo() {
		return this.isNeedDo;
	}

	public String getMessage() {
		return Message;
	}

	public Map<String, Object> getMessageMap() {
		return messageMap;
	}

	public void setMessageMap(Map<String, Object> messageMap) {
		this.messageMap = messageMap;
	}

	public int getCurrentStatus() {
		return this.currentStatus;
	}

}
