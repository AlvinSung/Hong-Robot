package com.founder.chatRobot.task.treader;

import java.util.LinkedList;
import java.util.List;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.recognition.actionConfirmRecognition.ActionConfirmRecognition;
import com.founder.chatRobot.task.treader.common.TaskTreader;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;

public class StackOperationMthodTreader extends TaskTreader {

	public StackOperationMthodTreader() {
		super(null,null);
		// TODO Auto-generated constructor stub
	}
	@Override
	public ResultBean tread(@SuppressWarnings("rawtypes") List<Fact> factList, Task task) {
		ResultBean result = new ResultBean();
		result.setFinished();
		result.setAnswer("您好，在我司开立完A股交易账户后，您可以通过我司的电脑端交易软件（富易、锐智内嵌交易）、"
				+ "手机端交易软件（易阳指）、网页在线交易（富通在线交易）、"
				+ "电话委托（95521）等方式进行股票的买卖和资金的转入转出等相关操作。");
		
		for(@SuppressWarnings("rawtypes") Fact fact : factList){
			//System.out.println(fact.getType().getTypeName());
			//System.out.println(((Entity)fact.getInfo().getInfo().get(0)).getName());
			if(fact.getType().getTypeName().equals("Confirm")){
				if(((Entity)fact.getInfo().getInfo().get(0)).getName().equals("No")){
					//System.out.println("needed creating account");
					task.addSubsequentTask("CreateAccount");
				}
			}
		}
		
		return result;
	}
	@Override
	public List<String> checkFact(Task task) {
		List<String> result = new LinkedList<String>();
		boolean hasConfirm = true;

		// System.out.println("fffff : " + facts.size());
		for (FactType factType : task.checkFacts(false)) {
			//System.out.println(factType.getTypeName());
			if (factType.getTypeName().equals("Confirm"))
				hasConfirm = false;
		}
		
		if(hasConfirm){
			result.add(TaskTreader.DOTASK);
		}else{
			result.add(TaskTreader.BACKMESSAGE);
			result.add("请问您有没有在国泰君安开立过资金账户和股票账户？");
			ActionConfirmRecognition actionConfirmRecognition = new ActionConfirmRecognition("开","开户");
			this.addRecognition(task, "actionConfirmRecognition", actionConfirmRecognition);
		}
		return result;
	}
	
	
	@Override
	public ResultBean reload(@SuppressWarnings("rawtypes") List<Fact> factList, Task task) {
		// TODO Auto-generated method stub
		return null;
	}

}
