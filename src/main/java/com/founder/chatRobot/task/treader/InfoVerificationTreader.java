package com.founder.chatRobot.task.treader;

import java.util.ArrayList;
import java.util.List;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public class InfoVerificationTreader extends TaskTreader {

	public InfoVerificationTreader() {
		super(null,null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ResultBean tread(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		ResultBean result = new ResultBean();
		result.setFinished();
		result.setAnswer("");
		return result;
	}
	
	@Override
	public List<String> checkFact(Task task) {
		List<String> result = new ArrayList<String>(1);
		result.add(TaskTreader.DOTASK);
		return result;
	}

	@Override
	public ResultBean reload(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		// TODO Auto-generated method stub
		return null;
	}

}
