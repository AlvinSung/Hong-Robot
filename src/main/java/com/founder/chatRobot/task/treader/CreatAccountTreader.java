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
import com.founder.chatRobot.recognition.nameRecognition.NameRecognition;
//import com.founder.chatRobot.recognition.common.interfaces.old.Recognition;
//import com.founder.chatRobot.recognition.departmentRecognition.DepartmentRecognition;
import com.founder.chatRobot.recognition.regular.RegularRecognition;
import com.founder.chatRobot.recognition.surnameRecognition.SurnameRecognition;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public class CreatAccountTreader extends TaskTreader {

	public CreatAccountTreader() {
		super("还需要我帮您做预约开户吗？", "您方便留下联系方式，做预约开户吗？营业部工作人员会主动和您联系，帮您开户。");
		// TODO Auto-generated constructor stub
	}

	@Override
	public ResultBean tread(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		ResultBean result = new ResultBean();
		boolean hasTread = false;
		String answer = "";

		for (@SuppressWarnings("rawtypes")
		Fact fact : task.getFactList()) {
			for (Object division : fact.getInfo().getInfo()) {
				if (division instanceof Entity) {
					// System.out.println(((Entity) division).getName());
					if (((Entity) division).getName().equals("台湾省")) {
						answer += "您好，只有在境内工作生活的台湾居民才能开立A股账户，需要提供：台湾居民往来内地通行证、台湾居民身份证、"
								+ "公安机关出具的临时住宿登记证明表。";
						answer += "\n您是否方便提供您的联系方式?以便营业部工作人员主动联系您，为您开户。\n";
						answer += "请问您在哪个城市";
						result.setAnswer(answer);
						task.getSession().addTempDate("CreatedCount",
								new Boolean(true));
						task.removeFact(fact);
						hasTread = true;
						break;
					} else if (((Entity) division).getName().equals("香港特别行政区")
							|| ((Entity) division).getName().equals("澳门特别行政区")) {
						answer += "您好，只有在境内工作生活的香港、澳门居民才能开立A股账户，需要提供：港澳居民往来内地通行证、港澳居民身份证、"
								+ "公安机关出具的临时住宿登记证明表。";
						answer += "\n您是否方便提供您的联系方式?以便营业部工作人员主动联系您，为您开户。\n";
						answer += "请问您在哪个城市";
						result.setAnswer(answer);
						task.getSession().addTempDate("CreatedCount",
								new Boolean(true));
						task.removeFact(fact);
						hasTread = true;
						break;
					}
				}
			}
		}

		if (!hasTread) {
			task.getSession().addTempDate("CreatedCount", new Boolean(true));
			result.setAnswer("您提交的信息我们已经记录，工作人员会在3个工作日内和您联系");
			result.setFinished();
			task.getSession().addTempDate("CreatedCount", new Boolean(true));
		}

		return result;
	}

	public List<String> checkFact(Task task) {
		List<String> result = new ArrayList<String>();
		boolean hasMobile = true, hasCheckCode = true, hasAddress = true, hasDeparement = true;
		boolean hasGender = true, hasSurname = true, hasPhoneNumber = true, hasFullName = true;
		Set<FactType> notFindedFactTypes = task.checkFacts(false);

		for (FactType facType : notFindedFactTypes) {
			// System.out.println("nnn ： " + facType.getTypeName());
			if (facType.getTypeName().equals("MobileNumber")) {
				hasMobile = false;
			} else if (facType.getTypeName().equals("CheckCode")) {
				hasCheckCode = false;
			} else if (facType.getTypeName().equals("Division")) {
				hasAddress = false;
			} else if (facType.getTypeName().equals("Department")) {
				hasDeparement = false;
			} else if (facType.getTypeName().equals("Gender")) {
				hasGender = false;
			} else if (facType.getTypeName().equals("Surname")) {
				hasSurname = false;
			} else if (facType.getTypeName().equals("PhoneNumber")) {
				hasPhoneNumber = false;
			} else if (facType.getTypeName().equals("FullName")) {
				hasFullName = false;
			}
		}

		/*
		 * if(hasSurname){ for(Fact fact: task.getFactList()){
		 * System.out.println(fact.getType().getTypeName());
		 * System.out.println(fact.getInfo().getInfo().get(0)); } }
		 */

		if (hasMobile && hasCheckCode && hasDeparement
				&& ((hasGender && hasSurname) || hasFullName))
			result.add(TaskTreader.DOTASK);
		else if (((hasGender && hasSurname) || hasFullName) && hasDeparement
				&& hasMobile && !hasCheckCode) {
			result.add(TaskTreader.BACKMESSAGE);
			result.add("请您输入效验码");
			Entity checkCode1 = (Entity) task.getSession().getContext()
					.getKnowledgeManager().uniqueIndex("效验码");

			RegularRecognition<String> checkCodeRecognition = new RegularRecognition<String>(
					"(^|[^0-9])[0-9]{4}([^0-9]|$)", "\\d{4}", String.class,
					checkCode1, "CheckCode");
			this.addRecognition(task, "CheckCode", checkCodeRecognition);
		} else if (((hasGender && hasSurname) || hasFullName) && hasDeparement
				&& hasPhoneNumber && !hasMobile) {
			result.add(TaskTreader.BACKMESSAGE);
			result.add("目前只支持手机号进行预约开户，请您输入手机号，以便和您联系。");
			this.removeRecognition(task, "departmentRecognition");
		} else if (((hasGender && hasSurname) || hasFullName) && hasDeparement
				&& !hasMobile) {
			result.add(TaskTreader.BACKMESSAGE);
			result.add("请您输入手机号，以便和您联系。");
			this.removeRecognition(task, "departmentRecognition");
		} else if (((hasGender && hasSurname) || hasFullName) && hasAddress
				&& !hasDeparement) {
			boolean isOutDivision = false;
			for (@SuppressWarnings("rawtypes")
			Fact fact : task.getFactList()) {
				// System.out.println(((Entity)
				// fact.getInfo().getInfo().get(0)).getName());
				if (fact.getType().getTypeName().equals("Division")) {
					/*
					 * System.out.println("ggg : " + ((Entity)
					 * fact.getInfo().getInfo().get(0)) .getName());
					 */
					Entity division;
					if ((division = (Entity) fact.getInfo().getInfo().get(0))
							.getName().equals("台湾省")
							|| division.getName().equals("香港")
							|| division.getName().equals("澳门")) {
						result.add(TaskTreader.DOTASK);
						isOutDivision = true;
						// System.out.println(division.getParent().getName());
						break;
					}
				} else {
					this.removeRecognition(task, "departmentRecognition");
				}
			}

			if (!isOutDivision) {
				result.add(TaskTreader.BACKMESSAGE);
				result.add("请选择对您比较方便的营业部。");

				/*
				 * Recognition departmentRecognition = new
				 * DepartmentRecognition(); this.addRecognition(task,
				 * "departmentRecognition", departmentRecognition);
				 */

				Entity situated = (Entity) Controler.knowledgeManager
						.uniqueIndex("Situated");
				Entity province = null;
				for (@SuppressWarnings("rawtypes")
				Fact fact : task.getFactList()) {
					// System.out.println("ggg : " +
					// fact.getType().getTypeName());
					if (fact.getType().getTypeName().equals("Division")) {

						/*
						 * System.out.println("ggg : " +
						 * ((Entity)fact.getInfo().getInfo()
						 * .get(0)).getName()); System.out.println("ggg : " +
						 * fact.getInfo().getInfo().get(0));
						 * System.out.println("ggg : " + ((Entity)fact.getInfo()
						 * .getInfo().get(0)).getParent());
						 */

						if (((Entity) fact.getInfo().getInfo().get(0))
								.getParent().getName().equals("Province")) {
							// System.out.println(1);
							province = (Entity) fact.getInfo().getInfo().get(0);
						} else if (((Entity) fact.getInfo().getInfo().get(0))
								.getParent().getName().equals("City")) {
							// System.out.println(2);
							Entity city = (Entity) fact.getInfo().getInfo()
									.get(0);
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
								provinces.retainAll(situated
										.getAttributeToValue());
								if (provinces != null && !provinces.isEmpty()) {
									province = provinces.iterator().next();
								}
							}
						}

						if (province != null) {
							Set<Entity> departments = new HashSet<Entity>(
									province.getValueToProduct());
							departments.retainAll(situated
									.getAttributeToProduct());
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
			}
		} else if (((hasGender && hasSurname) || hasFullName) && !hasAddress
				&& !hasDeparement) {
			result.add(TaskTreader.BACKMESSAGE);
			result.add("请问您在哪个城市？");
		} else if (hasSurname) {
			result.add(TaskTreader.BACKMESSAGE);
			String surname = "";
			for (@SuppressWarnings("rawtypes")
			Fact fact : task.getFactList()) {
				if (fact.getType().getTypeName().equals("Surname")) {
					surname = (String) fact.getInfo().getInfo().get(0);
				}
			}
			result.add("请问称呼您" + surname + "先生还是" + surname + "女士？");
		} else if (hasAddress) {
			boolean found = false;
			for (@SuppressWarnings("rawtypes")
			Fact fact : task.getFactList()) {
				for (Object division : fact.getInfo().getInfo()) {
					if (division instanceof Entity) {
						// System.out.println(((Entity) division).getName());
						if (((Entity) division).getName().equals("台湾省")
								|| ((Entity) division).getName().equals(
										"香港特别行政区")
								|| ((Entity) division).getName().equals(
										"澳门特别行政区")) {
							found = true;
						}
					}
				}
			}
			if (found) {
				result.add(TaskTreader.DOTASK);
			} else {
				result.add(TaskTreader.BACKMESSAGE);
				result.add("请问您贵姓？");

				SurnameRecognition surnameRecognition = new SurnameRecognition();
				this.addRecognition(task, "surname", surnameRecognition);
			}
		} else {
			result.add(TaskTreader.BACKMESSAGE);
			result.add("您好，开户有现场开户和非现场开户两种。现场开户需要您在交易时间前往想要开户的那家营业部进行办理，非现场开户可以通过我司网站、"
					+ "手机软件进行操作等。您也可以留下您的开户信息（姓名或称呼、联系方式、开户营业部名称），我们可以帮您做预约登记，"
					+ "稍后会有营业部专人直接联系您，帮您安排与开户有关的事宜。\n"
					+ "下面小章机器人带您进入预约开户流程（输入“不用”可以退出该流程）：\n" + "请问您贵姓？");

			SurnameRecognition surnameRecognition = new SurnameRecognition();
			this.addRecognition(task, "surname", surnameRecognition);

			NameRecognition nameRecognition = new NameRecognition();
			this.addRecognition(task, "fullName", nameRecognition);
		}
		return result;
	}

	@Override
	public ResultBean reload(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		//System.out.println(1 + " : " + task.isNeededConfirm());
		String answer = "";
		ResultBean result = new ResultBean();
		List<String> checkResult;

		/*
		 * if (task.isNeededConfirm()) { answer += "需要我帮您做预约开户吗？" + "\n"; //
		 * task.setNeededConfirm(); Recognition recognition = new
		 * ConfirmRecognition(); this.addRecognition(task, "ConfirmInfo",
		 * recognition); } else {
		 */
		checkResult = this.checkFact(task);

		if (checkResult.get(0).equals(TaskTreader.DOTASK)) {
			// System.out.println(2);
			return this.tread(factList, task);
		} else {
			answer += "您是不是还要继续预约开户？" + "\n";
			for (int i = 1; i < checkResult.size(); i++) {
				answer += checkResult.get(i) + "\n";
			}
		}

		result.setAnswer(answer);

		return result;
	}
}
