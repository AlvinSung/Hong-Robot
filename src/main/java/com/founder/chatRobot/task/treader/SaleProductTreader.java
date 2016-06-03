package com.founder.chatRobot.task.treader;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.InternalFact;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.fact.common.FactInfo;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.task.InternalTask;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.controler.common.Controler;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.recognition.confirmRecognition.ConfirmRecognition;
import com.founder.chatRobot.recognition.selectRecognition.SelectionRecognition;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public class SaleProductTreader extends TaskTreader {

	public SaleProductTreader() {
		super("还需要为您推荐理财产品吗？", "需要为您推荐理财产品吗？");
	}

	public ResultBean tread(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		ResultBean result = new ResultBean();
		Set<Entity> filter = new HashSet<Entity>();
		boolean isFilted = false;
		Entity financialProduct = (Entity) Controler.knowledgeManager
				.uniqueIndex("理财产品");

		for (@SuppressWarnings("rawtypes")
		Fact fact : factList) {
			if (fact.getType().getTypeName().equals("AnnualYield")
					|| fact.getType().getTypeName().equals("RiskType")
					|| fact.getType().getAttribute().getAttributeToProduct()
							.contains(financialProduct)) {

				@SuppressWarnings("unchecked")
				List<Entity> allEntities = (List<Entity>) fact.getInfo()
						.getInfo();
				Set<Entity> oneEntities = new HashSet<Entity>();

				for (Entity value : allEntities) {
					oneEntities.addAll(value.getValueToProduct());
				}

				if (!isFilted) {
					filter.addAll(oneEntities);
					isFilted = true;
				} else if (oneEntities.size() > 0) {
					filter.retainAll(oneEntities);
				}
			}
		}

		if (filter.size() == 0) {
			result.setAnswer("没有符合您要求的理财产品");
			result.setFinished();
			return result;
		} else {
			String answer = "为您推荐以下理财产品:\n";
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
		task.cleanFactList();
		boolean hasRiskType = true, hasPrincipalRisk = true, hasRiskLevel = true;
		boolean hasSelection = true, hasConfirm = true, hasAnnualYield = true;

		for (FactType factType : task.checkFacts(false)) {
			// System.out.println("trew : " + factType.getTypeName());
			if (factType.getTypeName().equals("PrincipalRisk")) {
				hasPrincipalRisk = false;
				continue;
			}
			if (factType.getTypeName().equals("RiskType")) {
				hasRiskType = false;
				continue;
			}
			if (factType.getTypeName().equals("AnnualYield")) {
				hasAnnualYield = false;
				continue;
			}
			if (factType.getTypeName().equals("RiskLevel")) {
				hasRiskLevel = false;
				continue;
			}
			if (factType.getTypeName().equals("SelectItem")) {
				hasSelection = false;
				continue;
			}
			if (factType.getTypeName().equals("Confirm")) {
				hasConfirm = false;
				continue;
			}
			
		}

		// System.out.println(task.getFactList().size());
		// System.out.println(hasPrincipalRisk + " : " + hasRiskType + " : " +
		// hasRiskLevel);

		// System.out.println("poiuy : " + hasConfirm);

		if (!hasRiskType && !hasPrincipalRisk && !hasRiskLevel && !hasAnnualYield) {
			result.add(TaskTreader.BACKMESSAGE);
			result.add("您好，我司理财产品很多，有保守型（相对低风险）产品、稳健型（相对中风险）产品和积极型（相对高风险）产品，"
					+ "收益与风险相对成正比，请问您需要哪一种风险级别的产品呢？");
		} else if(hasAnnualYield && !hasRiskType && !hasPrincipalRisk && !hasRiskLevel){
			result.add(TaskTreader.BACKMESSAGE);
			result.add("一般风险比较高的收益会比较好，反之风险低的一般收益会差一些。您希望买风险高的还是风险低的产品呢？");
		}else if (!hasRiskType && hasPrincipalRisk && !hasRiskLevel
				&& !hasConfirm) {
			result.add(TaskTreader.BACKMESSAGE);
			result.add("非常抱歉，我司理财产品目前均无法向您承诺保本。但是保守型产品的投资风险是相对最小的。");
			ConfirmRecognition reg = new ConfirmRecognition();
			this.addRecognition(task, "confirm", reg);
		} else if (!hasRiskType && hasPrincipalRisk && !hasRiskLevel
				&& hasConfirm) {
			result.add(TaskTreader.DOTASK);
			FactType factType = task.getSession().getContext()
					.getFactType("RiskType");
			FactInfo<Entity> info = new FactInfo<Entity>(
					(Entity) Controler.knowledgeManager
							.uniqueIndex("Conservative"));
			task.getSession().addFact(
					new InternalFact<Entity>(factType, info, task.getSession(),
							false));
		} else if (!hasRiskType && hasRiskLevel) {
			result.add(TaskTreader.BACKMESSAGE);
			Entity riskLevel = null;
			for (@SuppressWarnings("rawtypes")
			Fact fact : task.getFactList()) {
				if (fact.getType().getTypeName().equals("RiskLevel")) {
					riskLevel = (Entity) fact.getInfo().getInfo().get(0);
					break;
				}
			}
			if (riskLevel.getName().equals("LowRiskLevel")) {
				result.add("保守型产品的投资风险是相对最小的,但是预期收益较低。稳健型的风险稍高，但收益一般也会好一些，您要选择哪一种？");
				SelectionRecognition reg = new SelectionRecognition();
				this.addRecognition(task, "selection", reg);
			} else if (riskLevel.getName().equals("MiddleRiskLevel")) {
				result.add("稳健型的风险适中，但收益会比保守型的高，您要选择稳健型的吗？");
				ConfirmRecognition reg = new ConfirmRecognition();
				this.addRecognition(task, "confirm", reg);
			} else if (riskLevel.getName().equals("HighRiskLevel")) {
				result.add("积极型的风险最高，但收益往往比较高，您要选择积极型的吗？");
				ConfirmRecognition reg = new ConfirmRecognition();
				this.addRecognition(task, "confirm", reg);
			}
		} else if (!hasRiskType && hasRiskLevel && (hasSelection || hasConfirm)) {
			Entity riskLevel = null;
			for (@SuppressWarnings("rawtypes")
			Fact fact : task.getFactList()) {
				if (fact.getType().getTypeName().equals("RiskLevel")) {
					riskLevel = (Entity) fact.getInfo().getInfo().get(0);
					break;
				}
			}
			if (riskLevel.getName().equals("LowRiskLevel")) {
				if (hasSelection) {
					result.add(TaskTreader.DOTASK);
					FactType factType = task.getSession().getContext()
							.getFactType("RiskType");
					FactInfo<Entity> info = new FactInfo<Entity>(
							(Entity) Controler.knowledgeManager
									.uniqueIndex("Conservative"));
					task.getSession().addFact(
							new InternalFact<Entity>(factType, info, task
									.getSession(), false));
				} else {
					result.add(TaskTreader.BACKMESSAGE);
					result.add("保守型产品的投资风险是相对最小的,但是预期收益较低。稳健型的风险稍高，但收益一般也会好一些，您要选择哪一种？");
					SelectionRecognition reg = new SelectionRecognition();
					this.addRecognition(task, "selection", reg);
				}
			} else if (riskLevel.getName().equals("MiddleRiskLevel")) {
				if (hasConfirm) {
					result.add(TaskTreader.DOTASK);
					FactType factType = task.getSession().getContext()
							.getFactType("RiskType");
					FactInfo<Entity> info = new FactInfo<Entity>(
							(Entity) Controler.knowledgeManager
									.uniqueIndex("Steady"));
					task.getSession().addFact(
							new InternalFact<Entity>(factType, info, task
									.getSession(), false));
				} else {
					result.add(TaskTreader.BACKMESSAGE);
					result.add("稳健型的风险适中，但收益会比保守型的高，您要选择稳健型的吗？");
					ConfirmRecognition reg = new ConfirmRecognition();
					this.addRecognition(task, "confirm", reg);
				}
			} else if (riskLevel.getName().equals("HighRiskLevel")) {
				if (hasConfirm) {
					result.add(TaskTreader.DOTASK);
					FactType factType = task.getSession().getContext()
							.getFactType("RiskType");
					FactInfo<Entity> info = new FactInfo<Entity>(
							(Entity) Controler.knowledgeManager
									.uniqueIndex("Aggressive"));
					task.getSession().addFact(
							new InternalFact<Entity>(factType, info, task
									.getSession(), false));
				} else {
					result.add(TaskTreader.BACKMESSAGE);
					result.add("积极型的风险最高，但收益往往比较高，您要选择积极型的吗？");
					ConfirmRecognition reg = new ConfirmRecognition();
					this.addRecognition(task, "confirm", reg);
				}
			}
		} else if (hasRiskType) {
			result.add(TaskTreader.DOTASK);
		}

		task.checkFacts(false);

		return result;
	}

	@Override
	public ResultBean reload(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		ResultBean result = new ResultBean();
		String answer = "是否还需要为您继续推荐理财产品？您刚才输入的条件是：";

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
