package com.founder.chatRobot.task.treader;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.task.InternalTask;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public class SaleTaskTreader extends TaskTreader {

	public SaleTaskTreader() {
		super("还需要为您推荐基金吗？", "需要为您推荐基金吗？");
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("rawtypes")
	public ResultBean tread(List<Fact> factList, Task task) {
		ResultBean result = new ResultBean();
		Set<Entity> filter = new HashSet<Entity>();
		Entity type = null;
		boolean isFilted = false;

		for (Fact fact : factList) {
			if (fact.getType().getTypeName().equals("InvestmentProduct")) {
				type = (Entity) fact.getInfo().getInfo().get(0);
				continue;
			}
		}

		for (Fact fact : factList) {
			@SuppressWarnings("unchecked")
			List<Entity> allEntities = (List<Entity>) fact.getInfo().getInfo();
			Set<Entity> oneEntities = new HashSet<Entity>();
			Set<Entity> filterEntities = new HashSet<Entity>();

			for (Entity value : allEntities) {
				oneEntities.addAll(value.getValueToProduct());
			}

			for (Entity product : oneEntities) {
				if (product.getParent() == type) {
					filterEntities.add(product);
				}
			}

			if (!isFilted) {
				filter.addAll(filterEntities);
				isFilted = true;
			} else if (filterEntities.size() > 0) {
				filter.retainAll(filterEntities);
			}

		}

		if (filter.size() == 0) {
			result.setAnswer("没有符合您要求的投资产品");
			result.setFinished();
			return result;
		} else {
			String answer = "为您推荐以下投资产品:\n";
			Iterator<Entity> filterIterator = filter.iterator();
			int i = 0;
			while (filterIterator.hasNext()) {
				answer += filterIterator.next().getMainExpress();
				// System.out.println(filterIterator.next().getMainExpress());
				answer += "\n";
				if (++i == 10)
					break;
			}

			if (filter.size() <= 5
					|| task.getTaskType().getNeededFacts().size() == ((InternalTask) task)
							.getFactList().size())
				result.setFinished();
			else
				answer += "您是否还有其它要求？";
			result.setAnswer(answer);

			// System.out.println(answer);

			return result;
		}
	}

	@Override
	// 返回结果的第一行是是否执行tread，第二行是提示标题，后面是需要输入的事实类型信息
	public List<String> checkFact(Task task) {
		List<String> result = new LinkedList<String>();
		Entity type = null;

		task.cleanFactList();
		List<String> answer = new LinkedList<String>();
		Set<FactType> foundFactTypes = task.checkFacts(false);

		for (int i = 0; i < task.getFactList().size(); i++) {
			if (task.getFactList().get(i).getType().getTypeName()
					.equals("InvestmentProduct")) {
				type = (Entity) task.getFactList().get(i).getInfo().getInfo()
						.get(0);
				break;
			}
		}

		if (type == null) {
			result.add(TaskTreader.BACKMESSAGE);
			result.add("您想买基金还是理财产品？");
		} else if (task.getFactList().size() > 1) {
			result.add(TaskTreader.DOTASK);
			if (type.getName().equals("Fund"))
				result.add("您对基金还有什么要求吗？例如:");
			else
				result.add("你对理财产品还有什么要求吗？例如:");
			for (FactType factType : foundFactTypes) {
				if (factType.getAttribute().getAttributeToProduct()
						.contains(type))
					result.add(factType.getMessage());
			}
		} else {
			result.add(TaskTreader.BACKMESSAGE);
			if (type.getName().equals("Fund"))
				result.add("您对基金有什么要求吗？例如:");
			else
				result.add("您对理财产品有什么要求，例如:");
			for (FactType factType : foundFactTypes) {
				if (factType.getAttribute().getAttributeToProduct()
						.contains(type))
					result.add(factType.getMessage());
			}
		}
		result.addAll(answer);
		return result;
	}

	@Override
	public ResultBean reload(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		ResultBean result = new ResultBean();
		String answer = "是否还需要继续推荐基金？您刚才输入的条件是：";

		for (int i = 0; i < factList.size(); i++) {
			answer += factList.get(i).getType().getMessage() + "\n";
		}

		answer += "您还可以提供下列信息：" + "\n";

		List<String> checkMessage = this.checkFact(task);
		for (int i = 2; i < checkMessage.size(); i++) {
			String message = checkMessage.get(i);
			answer += message + "\n";
		}

		result.setAnswer(answer);
		return result;
	}
}
