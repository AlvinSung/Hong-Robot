package com.founder.chatRobot.task.treader;

import java.util.List;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public class SimpleAnswerTreader extends TaskTreader {
	String message;

	public SimpleAnswerTreader(String message) {
		super(null,null);
		this.message = message;
	}

	@Override
	public ResultBean tread(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		ResultBean result = new ResultBean();

		result.setAnswer(this.message);
		result.setFinished();
		return result;
	}

	@Override
	public ResultBean reload(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		// TODO Auto-generated method stub
		return null;
	}

}
