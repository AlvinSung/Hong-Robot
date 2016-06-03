package com.founder.chatRobot.task.treader;

import java.util.LinkedList;
import java.util.List;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public class StockTrendTreader extends TaskTreader {

	public StockTrendTreader() {
		super(null, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ResultBean tread(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		ResultBean result = new ResultBean();
		Entity stock = null;
		for (@SuppressWarnings("rawtypes")
		Fact fact : factList) {
			if (fact.getType().getTypeName().equals("Stock")) {
				stock = (Entity) fact.getInfo().getInfo().get(0);
			}
		}

		if (stock != null) {
			result.setAnswer("您好！" + stock.getName() + "的走势图如下。");
			result.addResult("StockTrend", stock.getValue());
		} else {
			result.setAnswer("您好！您需要提供要查询走势的股票。");
		}

		result.setFinished();

		return result;
	}

	public List<String> checkFact(Task task) {
		List<String> result = new LinkedList<String>();
		task.cleanFactList();
		boolean hasStock = true;

		for (FactType factType : task.checkFacts(true)) {
			// System.out.println("trew : " + factType.getTypeName());
			if (factType.getTypeName().equals("Stock")) {
				hasStock = false;
				break;
			}
		}

		if (!hasStock) {
			result.add(TaskTreader.BACKMESSAGE);
			result.add("您好，请您提供需要查询走势的股票。");
		} else {
			result.add(TaskTreader.DOTASK);
		}

		return result;
	}

	@Override
	public ResultBean reload(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		List<String> checkResult = this.checkFact(task);

		if (checkResult.get(0).equals(TaskTreader.BACKMESSAGE)) {
			ResultBean result = new ResultBean();
			String answer = "";
			for (int i = 0; i < checkResult.size(); i++) {
				answer += "\n" + checkResult.get(i);
			}
			result.setAnswer(answer);
			return result;
		} else {
			return this.tread(factList, task);
		}
	}

}
