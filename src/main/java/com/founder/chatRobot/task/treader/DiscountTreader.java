package com.founder.chatRobot.task.treader;

import java.util.List;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.task.common.Task;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public class DiscountTreader extends TaskTreader {

	public DiscountTreader() {
		super(null,null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ResultBean tread(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		ResultBean result = new ResultBean();

		if (factList == null || factList.size() == 0) {
			result.setAnswer("您好，根据中登公司的相关规定，目前开立上海A股股东账号收费20元，深证股东账号20元，由证券公司代为收取，我司开立资金账号不收费。\n"
					+ "优惠活动由各地区的营业部自行筹划安排，您可以留下您的开户信息（姓名或称呼、联系方式、开户营业部名称），"
					+ "我们可以帮您做预约登记，稍后会有营业部专人直接联系您，关于开户优惠活动的详情，您可以与我司营业部专人直接沟通。");
		} else {
			for (@SuppressWarnings("rawtypes")
			Fact fact : factList) {
				//System.out.println("wwww : " + fact.getType().getTypeName());
				if (fact.getType().getTypeName().equals("ChargeType")) {
					Entity chargeType = (Entity) fact.getInfo().getInfo()
							.get(0);
					if (chargeType.getName().equals("CreatAccountCharge")) {
						result.setAnswer("您好，根据中登公司的相关规定，目前开立上海A股股东账号收费20元，深证股东账号20元，由证券公司代为收取，我司开立资金账号不收费。\n"
								+ "优惠活动由各地区的营业部自行筹划安排，您可以留下您的开户信息（姓名或称呼、联系方式、开户营业部名称），"
								+ "我们可以帮您做预约登记，稍后会有营业部专人直接联系您，关于开户优惠活动的详情，您可以与我司营业部专人直接沟通。");
					} else if (chargeType.getName().equals("Commission")) {
						result.setAnswer("您好，客户的佣金率是由营业部根据客户的资金量、交易量以及营业部的运营成本综合考量制定的，"
								+ "具体您的个人佣金率，需要营业部专人直接联系您进行沟通。没有统一的优惠活动和政策。");
					} else if (chargeType.getName().equals("ChargCommision")) {
						result.setAnswer("您好，客户的佣金率是由营业部根据客户的资金量、交易量以及营业部的运营成本综合考量制定的，"
								+ "具体您的个人佣金率，需要营业部专人直接联系您进行沟通。没有统一的优惠活动和政策。\n"
								+ "开户费的优惠活动由各地区的营业部自行筹划安排，您可以留下您的开户信息（姓名或称呼、联系方式、开户营业部名称），"
								+ "我们可以帮您做预约登记，稍后会有营业部专人直接联系您，关于开户优惠活动的详情，您可以与我司营业部专人直接沟通。");
					} else if (chargeType.getName().equals("TransactionCost")) {
						result.setAnswer("您好，印花税和过户费不是由我司收取，无法给您提供优惠。/n"
								+ "客户的佣金率是由营业部根据客户的资金量、交易量以及营业部的运营成本综合考量制定的，"
								+ "具体您的个人佣金率，需要营业部专人直接联系您进行沟通。");
					}
				}
			}
		}

		result.setFinished();
		return result;
	}

	@Override
	public ResultBean reload(@SuppressWarnings("rawtypes") List<Fact> factList,
			Task task) {
		// TODO Auto-generated method stub
		return null;
	}

}
