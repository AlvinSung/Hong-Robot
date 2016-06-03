package com.founder.chatRobot.task.treader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.controler.common.Controler;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public class DepartmentAddressTreader extends TaskTreader {

	public DepartmentAddressTreader() {
		super(null,null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ResultBean tread(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		ResultBean result = new ResultBean();
		boolean hasAddress = false;
		Entity position = null;

		for (@SuppressWarnings("rawtypes")
		Fact fact : factList) {
			if(fact.getType().getTypeName().equals("Division") && position == null){
				hasAddress = true;
				position = (Entity) fact.getInfo().getInfo().get(0);
			}
		}

		if (!hasAddress) {
			result.setAnswer("非常抱歉，该地区区域路段暂无我司营业部。如需开户，您可以通过其他方式开户。\n"
					+ "网上开户可以登录我司官网www.gtja.com，选择（左侧）在线开户进行操作。手机开户可以发送88到95521下载开户程序进行操作。");
			result.setNotStartSubsequentTask();
		}else {
			Entity situated = (Entity) Controler.knowledgeManager
					.uniqueIndex("Situated");
			Set<Entity> departments = new HashSet<Entity>(
					position.getValueToProduct());
			departments.retainAll(situated.getAttributeToProduct());
			String answer = "";
			answer += "您好，" + position.getMainExpress()
					+ "的营业部有：\n";
			for (Entity department : departments) {
				if (department.getParent().getName()
						.equals("Department")) {
					answer += department.getMainExpress() + "\n";
				}
			}
			answer += "请问您要了解哪家营业部的地址和电话？";
			result.setAnswer(answer);
		}
		result.setFinished();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<String> checkFact(Task task) {
		List<String> result = new ArrayList<String>();
		boolean hasAddress = true;
		Set<FactType> findedFactTypes = task.checkFacts(false);

		for (FactType facType : findedFactTypes) {
			if (facType.getTypeName().equals("Division")) {
				hasAddress = false;
			} 
		}

		if (hasAddress) {
			Entity situated = (Entity) Controler.knowledgeManager
					.uniqueIndex("Situated");
			Entity province = null, position = null;
			for (@SuppressWarnings("rawtypes")
			Fact fact : task.getFactList()) {
				/*System.out.println("ggg : " +
						fact.getType().getTypeName());*/
				if (fact.getType().getTypeName().equals("Division")) {
					position = (Entity) fact.getInfo().getInfo().get(0);;
					if (position.getParent()
							.getName().equals("Province")) {
						// System.out.println(1);
						province = position;
					} else if (position.getParent().getName().equals("City")) {
						// System.out.println(2);
						Entity city = position;
						Set<Entity> provinces = new HashSet<Entity>(
								city.getProductToValue());
						provinces.retainAll(situated.getAttributeToValue());
						if (provinces != null && !provinces.isEmpty()) {
							province = provinces.iterator().next();
						}
					} else if (position.getParent().getName().equals("County")) {
						// System.out.println(3);
						Entity county = position;
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

					/*System.out.println(province);
					System.out.println(province.getName());
					System.out.println(province.getMainExpress());*/

					if (province != null) {
						Set<Entity> departments = new HashSet<Entity>(
								province.getValueToProduct());
						departments.retainAll(situated.getAttributeToProduct());
						if (departments.size() > 0) {
							result.add(TaskTreader.DOTASK);
							fact.getInfo().getInfo().clear();
							fact.getInfo().getInfo().add(province);
							/*result.add("您好，" + province.getMainExpress()
									+ "的营业部有：");
							for (Entity department : departments) {
								if (department.getParent().getName()
										.equals("Department")) {
									result.add(department.getMainExpress());
								}
							}
							result.add("请问您要了解哪家营业部的地址和电话？");*/
						} else {
							result.add(TaskTreader.DOTASK);
							result.add("非常抱歉，该地区区域路段暂无我司营业部。如需开户，您可以通过其他方式开户。\n"
									+ "网上开户可以登录我司官网www.gtja.com，选择（左侧）在线开户进行操作。手机开户可以发送88到95521下载开户程序进行操作。");
						}
					}
				}
			}
		} else {
			result.add(TaskTreader.BACKMESSAGE);
			result.add("您好，我司在全国有200多家营业部网点，请问您是需要咨询哪个省份、城市或地区的营业部信息呢？");
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
