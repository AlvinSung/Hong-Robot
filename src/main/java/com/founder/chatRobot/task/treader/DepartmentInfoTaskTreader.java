package com.founder.chatRobot.task.treader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public class DepartmentInfoTaskTreader extends TaskTreader {

	public DepartmentInfoTaskTreader() {
		super(null, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ResultBean tread(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		ResultBean result = new ResultBean();
		String name = null, address = null, phoneNumber = null;
		for (@SuppressWarnings("rawtypes")
		Fact fact : factList) {
			if (fact.getType().getTypeName().equals("Department")) {
				Entity department = (Entity) fact.getInfo().getInfo().get(0);
				for (Entity value : department.getProductToValue()) {
					for (Entity attribute : value.getValueToAttribute()) {
						if (attribute.getName().equals("DepartmentName"))
							name = value.getMainExpress();
						else if (attribute.getName().equals("Address"))
							address = value.getMainExpress();
						else if (attribute.getName().equals("PhoneNumber"))
							phoneNumber = value.getMainExpress();
					}
				}

				if (name != null && address != null && phoneNumber != null) {
					result.setAnswer("您好，" + name + "的地址是:" + address + "\n"
							+ "电话是： " + phoneNumber);
					result.addResult("Address", address);
				}
			}
		}
		result.setFinished();
		return result;
	}

	public List<String> checkFact(Task task) {
		List<String> result = new ArrayList<String>();
		boolean hasDeparement = true;
		Set<FactType> findedFactTypes = task.checkFacts(false);

		for (FactType facType : findedFactTypes) {
			if (facType.getTypeName().equals("Department")) {
				hasDeparement = false;
			}
		}

		if (hasDeparement) {
			result.add(TaskTreader.DOTASK);
		} else {
			result.add(TaskTreader.BACKMESSAGE);
			result.add("您好，我司在全国有200多家营业部网点，请问您是需要咨询哪个营业部的信息呢？");
		}

		return result;
	}

	@Override
	public ResultBean reload(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		// TODO Auto-generated method stub
		return null;
	}

}
