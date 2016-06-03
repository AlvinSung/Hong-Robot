package com.founder.chatRobot.task.treader;

import java.util.LinkedList;
import java.util.List;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public class CheckDetailTreader extends TaskTreader {

	public CheckDetailTreader() {
		super(null, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ResultBean tread(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		ResultBean result = new ResultBean();
		Entity financialProduct = null;
		Integer productCode = null;

		for (@SuppressWarnings("rawtypes")
		Fact fact : task.getFactList()) {
			if (fact.getType().getTypeName().equals("FinancialProduct")) {
				financialProduct = (Entity) fact.getInfo().getInfo().get(0);
			}
		}

		if (financialProduct == null) {
			result.setAnswer("您需要告诉我，您想购买的产品，才能帮您查看产品的信息");
		} else {
			for (Entity entity : financialProduct.getProductToValue()) {
				for (Entity attribute : entity.getValueToAttribute()) {
					if (attribute.getName().equals("ProductCode")) {
						productCode = new Integer(entity.getMainExpress());
						break;
					}
				}
				if (productCode != null)
					break;
			}

			if (productCode != null)
				result.setAnswer("您可以在<a href=\"http://www.gtjazg.com/product/product_info.jsp?pCode="
						+ productCode + "\" target=_blank>" + "http://www.gtjazg.com/product/product_info.jsp?pCode="
						+ productCode + "</a>下查看相关产品信息");
			else
				result.setAnswer("无法找到该产品的介绍。");
		}

		result.setFinished();

		return result;
	}

	@Override
	public List<String> checkFact(Task task) {
		List<String> result = new LinkedList<String>();

		task.checkFacts(false);
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
