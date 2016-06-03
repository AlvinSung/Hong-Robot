package com.founder.chatRobot.task.treader;

import java.util.LinkedList;
import java.util.List;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.controler.common.Controler;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.recognition.regular.RegularRecognition;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public class UnsubscribeMessageTreader extends TaskTreader {

	public UnsubscribeMessageTreader() {
		super("您要继续退订短信吗？","您要退订短信吗？");
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultBean tread(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		ResultBean result = new ResultBean();

		Fact<Long> mobileNumber = null;
		Fact<Long> checkCode = null;
		for (@SuppressWarnings("rawtypes")
		Fact fact : factList) {
			if (fact.getType().getTypeName().equals("MobileNumber")) {
				mobileNumber = fact;
			} else if (fact.getType().getTypeName().equals("CheckCode")) {
				checkCode = fact;
			}
		}

		Controler.debugLoger.debug("调用取消短息服务接口.\n参数是： \n电话号码："
				+ mobileNumber.getInfo().getInfo() + "\n效验码："
				+ checkCode.getInfo().getInfo());
		result.setAnswer("您订阅的短信已经取消");

		result.setFinished();

		return result;
	}

	@Override
	public List<String> checkFact(Task task) {
		List<String> result = new LinkedList<String>();
		boolean hasMobileNumber = true, hasCheckCode = true;

		// System.out.println("fffff : " + facts.size());
		for (FactType factType : task.checkFacts(false)) {
			//System.out.println(factType.getTypeName());
			if (factType.getTypeName().equals("MobileNumber"))
				hasMobileNumber = false;
			else if (factType.getTypeName().equals("CheckCode"))
				hasCheckCode = false;
		}

		if (!hasMobileNumber) {
			result.add(TaskTreader.BACKMESSAGE);
			result.add("请输入退订短信的手机号");
		} else if (!hasCheckCode) {
			Controler.debugLoger.debug("调用发送验证码的接口");
			result.add(TaskTreader.BACKMESSAGE);
			result.add("请输入验证码");
			Entity checkCode1 = (Entity) task.getSession().getContext()
					.getKnowledgeManager().uniqueIndex("效验码");

			RegularRecognition<String> checkCodeRecognition = new RegularRecognition<String>(
					"(^|[^0-9])[0-9]{4}([^0-9]|$)", "\\d{4}", String.class,
					checkCode1, "CheckCode");
			this.addRecognition(task, "CheckCode", checkCodeRecognition);
		}else{
			result.add(TaskTreader.DOTASK);
		}
		return result;
	}

	@Override
	public ResultBean reload(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		ResultBean result = new ResultBean();
		String answer = "是否还需要继续退订短信？如果需要，";
		List<String> checkReulst = this.checkFact(task);

		if (checkReulst.get(0).equals(BACKMESSAGE)) {
			answer += checkReulst.get(1);
			result.setAnswer(answer);
			return result;
		}
		return null;
	}

}
