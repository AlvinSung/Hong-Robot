package com.founder.chatRobot.task.treader;

import java.util.LinkedList;
import java.util.List;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public class BuyMethodTreader extends TaskTreader {

	public BuyMethodTreader() {
		super(null, null);
	}

	@Override
	public ResultBean tread(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		ResultBean result = new ResultBean();

		if (factList == null || factList.size() == 0) {
			result.setAnswer("购买需要您先在我司开立账户。");
		} else {
			String answer = "您可以在下列机构购买该理财产品：\n";
			for (@SuppressWarnings("rawtypes")
			Fact fact : factList) {
				if (fact.getType().getTypeName().equals("FinancialProduct")) {
					Entity product = (Entity) fact.getInfo().getInfo().get(0);
					for (Entity value : product.getProductToValue()) {
						boolean isMarketingMechanism = false;
						for (Entity attribute : value.getValueToAttribute()) {
							if (attribute.getName()
									.equals("MarketingMechanism")) {
								isMarketingMechanism = true;
								break;
							}
						}
						if (isMarketingMechanism) {
							answer += value.getMainExpress() + "、";
						}
					}
				}
			}
			answer += "\n";
			if(task.getSession().getSessionId().equals("11111")){
				
			}else if(task.getSession().getSessionId().equals("22222")){
				answer += "您的开立账户申请，我们真在处理，为您完成开户流程之后，您就可以购买该产品了。";
			}else{
				answer += "购买时需要您先在我司开立账户。";
			}
			result.setAnswer(answer);
		}

		result.setFinished();
		return result;
	}

	public List<String> checkFact(Task task) {
		List<String> result = new LinkedList<String>();
		task.cleanFactList();

		task.checkFacts(true);
		result.add(TaskTreader.DOTASK);

		return result;
	}

	@Override
	public ResultBean reload(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		this.checkFact(task);
		return this.tread(factList, task);
	}

}
