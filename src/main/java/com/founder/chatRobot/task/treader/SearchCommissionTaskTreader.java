package com.founder.chatRobot.task.treader;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.controler.common.Controler;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public class SearchCommissionTaskTreader extends TaskTreader {

	public SearchCommissionTaskTreader() {
		super(null,null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ResultBean tread(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		ResultBean result = new ResultBean();
		Entity department = null;
		Entity commission = (Entity) Controler.knowledgeManager
				.uniqueIndex("Commission");

		for (@SuppressWarnings("rawtypes")
		Fact fact : task.getFactList()) {
			// System.out.println("ggg : " + fact.getType().getTypeName());
			if (fact.getType().getTypeName().equals("Department")) {
				department = (Entity) fact.getInfo().getInfo().get(0);
				break;
			}
		}
		Set<Entity> commissionSet = null;
		if (department != null) {
			commissionSet = new HashSet<Entity>(department.getProductToValue());
			commissionSet.retainAll(commission.getAttributeToValue());
		}

		if (!commissionSet.isEmpty()) {
			result.setAnswer("该营业部的默认费率为："
					+ commissionSet.iterator().next().getMainExpress()
					+ "\n具体费率需要根据账户金额、交易频率等决定。具体费率只有和营业部联系才能确定");
		} else {
			result.setAnswer("无法查询到该营业部的默认费率。具体费率需要根据账户金额、交易频率等决定。具体费率只有和营业部联系才能确定");
		}
		result.setFinished();

		return result;
	}

	public List<String> checkFact(Task task) {
		List<String> result = new LinkedList<String>();
		task.cleanFactList();
		boolean hasDivision = true, hasDepartment = true;

		for (FactType factType : task.checkFacts(false)) {
			//System.out.println(factType.getTypeName());
			if (factType.getTypeName().equals("Division"))
				hasDivision = false;
			else if (factType.getTypeName().equals("Department"))
				hasDepartment = false;
		}

		if (!hasDepartment && !hasDivision) {
			result.add(TaskTreader.BACKMESSAGE);
			result.add("您在哪个城市，或者您在哪个营业部开的户？");
		} else if (!hasDepartment) {
			result.add(TaskTreader.BACKMESSAGE);
			result.add("您想查询哪个营业部的佣金？");
			Entity situated = (Entity) Controler.knowledgeManager
					.uniqueIndex("Situated");
			Entity province = null;
			for (@SuppressWarnings("rawtypes")
			Fact fact : task.getFactList()) {
				// System.out.println("ggg : " + fact.getType().getTypeName());
				if (fact.getType().getTypeName().equals("Division")) {
					/*
					 * System.out.println("ggg : " + ((Entity)
					 * fact.getInfo().getInfo().get(0)) .getName());
					 */
					if (((Entity) fact.getInfo().getInfo().get(0)).getParent()
							.getName().equals("Province")) {
						// System.out.println(1);
						province = (Entity) fact.getInfo().getInfo().get(0);
					} else if (((Entity) fact.getInfo().getInfo().get(0))
							.getParent().getName().equals("City")) {
						// System.out.println(2);
						Entity city = (Entity) fact.getInfo().getInfo().get(0);
						Set<Entity> provinces = new HashSet<Entity>(
								city.getProductToValue());
						provinces.retainAll(situated.getAttributeToValue());
						if (provinces != null && !provinces.isEmpty()) {
							province = provinces.iterator().next();
						}
					} else if (((Entity) fact.getInfo().getInfo().get(0))
							.getParent().getName().equals("County")) {
						// System.out.println(3);
						Entity county = (Entity) fact.getInfo().getInfo()
								.get(0);
						Set<Entity> citys = new HashSet<Entity>(
								county.getProductToValue());
						citys.retainAll(situated.getAttributeToValue());
						if (citys != null && !citys.isEmpty()) {
							Entity city = citys.iterator().next();
							Set<Entity> provinces = new HashSet<Entity>(
									city.getProductToValue());
							provinces.retainAll(situated.getAttributeToValue());
							if (provinces != null && !provinces.isEmpty()) {
								province = provinces.iterator().next();
							}
						}
					}

					if (province != null) {
						Set<Entity> departments = new HashSet<Entity>(
								province.getValueToProduct());
						departments.retainAll(situated.getAttributeToProduct());
						for (Entity department : departments) {
							if (department.getParent().getName()
									.equals("Department")) {
								result.add(department.getMainExpress());
								// System.out.println(department.getMainExpress());
							}
						}
					}
				}
			}
		} else {
			result.add(TaskTreader.DOTASK);
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
