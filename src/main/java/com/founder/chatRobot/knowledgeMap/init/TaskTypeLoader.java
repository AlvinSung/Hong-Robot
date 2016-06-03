package com.founder.chatRobot.knowledgeMap.init;

import com.founder.chatRobot.common.exception.DoubleInsertException;
import com.founder.chatRobot.common.exception.TaskTypeNotFoundException;
import com.founder.chatRobot.common.exception.TypeMissException;
import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.context.fact.InternalFactType;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.manager.common.ContextManager;
import com.founder.chatRobot.knowledgeMap.conception.entity.InternalEntity;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.conception.taskType.InternalTaskConception;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;
import com.founder.chatRobot.knowledgeMap.init.common.Loader;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;
import com.founder.chatRobot.recognition.comparisionMethod.ComparisionRecognition;
import com.founder.chatRobot.task.treader.BuyMethodTreader;
import com.founder.chatRobot.task.treader.CheckDetailTreader;
import com.founder.chatRobot.task.treader.DepartmentAddressTreader;
import com.founder.chatRobot.task.treader.DepartmentInfoTaskTreader;
import com.founder.chatRobot.task.treader.DiscountTreader;
import com.founder.chatRobot.task.treader.NeededCertificatesTreader;
import com.founder.chatRobot.task.treader.SimpleAnswerTreader;
import com.founder.chatRobot.task.treader.StackOperationMthodTreader;
import com.founder.chatRobot.task.treader.StockOutlookTreader;
import com.founder.chatRobot.task.treader.StockTrendTreader;

public class TaskTypeLoader extends Loader {

	public TaskTypeLoader(KnowledgeManager knowledgeManager,
			ComparisionRecognition recognition, ContextManager contextManager) {
		super(null, knowledgeManager, recognition, contextManager);
	}

	private void initSimpleTask() {
		try {
			this.contextManager.addTaskType(new InternalTaskConception(
					"OpeningComtask", new SimpleAnswerTreader(
							"很高兴为您服务，请问有什么可以帮助您？"), "开头语"));

			this.contextManager.addTaskType(new InternalTaskConception(
					"ClosingComtask", new SimpleAnswerTreader(
							"希望您满意我们的服务，不足之处我们正在努力改进！"), "结束语"));

			TaskConception creatAccountTaskType = this.contextManager
					.findTaskType("CreateAccount");

			TaskConception chargeTaskType = new InternalTaskConception(
					"Charge", new SimpleAnswerTreader(
							"您好，根据中登公司的相关规定，目前开立上海A股股东账号收费20元，深证股东账号20元，由证券公司代为收取，"
									+ "我司开立资金账号不收费。"), "查询开户收费");
			this.contextManager.addTaskType(chargeTaskType);
			chargeTaskType.addSubSequentTaskType(creatAccountTaskType);

			TaskConception chargeCommissionTaskType = new InternalTaskConception(
					"ChargeCommision",
					new SimpleAnswerTreader(
							"您好，根据中登公司的相关规定，目前开立上海A股股东账号收费20元，深证股东账号20元，由证券公司代为收取，"
									+ "我司开立资金账号不收费。\n"
									+ "客户的佣金率是由营业部根据客户的资金量、交易量以及营业部的运营成本综合考量制定的，"
									+ "最高上限为成交金额的千分之3，起点5元，具体您的个人佣金率，需要营业部专人直接联系您进行沟通。"),
					"查询开户交易收费");
			this.contextManager.addTaskType(chargeCommissionTaskType);
			chargeCommissionTaskType
					.addSubSequentTaskType(creatAccountTaskType);

			TaskConception commissionTaskType = new InternalTaskConception(
					"Commission",
					new SimpleAnswerTreader(
							"您好，客户的佣金率是由营业部根据客户的资金量、交易量以及营业部的运营成本综合考量制定的，"
									+ "最高上限为成交金额的千分之3，起点5元，具体您的个人佣金率，需要营业部专人直接联系您进行沟通。"),
					"查询佣金");
			this.contextManager.addTaskType(commissionTaskType);
			commissionTaskType.addSubSequentTaskType(creatAccountTaskType);

			TaskConception accountMigrationTaskType = new InternalTaskConception(
					"AccountMigration",
					new SimpleAnswerTreader(
							"您好，根据目前证券账户的相关规定，上海A股账户实行指定交易制度。"
									+ "除机构客户和参与沪港通业务的客户外，其余均只能指定在一家券商的营业部，若您已在其他证券公司开立过，"
									+ "则需要前往办理撤销指定交易手续；深圳A股账户可以多处增挂，如您不想前往原券商办理转户手续（即股票的转托管），"
									+ "可以在我司重新增挂深A账户。"), "外司账户迁入");
			this.contextManager.addTaskType(accountMigrationTaskType);
			accountMigrationTaskType
					.addSubSequentTaskType(creatAccountTaskType);

			TaskConception stockTradingFeeTaskType = new InternalTaskConception(
					"StockTradingFee",
					new SimpleAnswerTreader("您好，A股股票交易费用一般包括：印花税、佣金和过户费。"
							+ "印花税：出让方单边缴纳成交金额的1‰；佣金：买卖双向收取，上限为成交金额的3‰，起点5元；"
							+ "过户费：仅上海市场收取，买卖双向，成交面额（一般1元/股）的0.6‰"), "股票交易费用");
			this.contextManager.addTaskType(stockTradingFeeTaskType);
			stockTradingFeeTaskType.addSubSequentTaskType(creatAccountTaskType);

			TaskConception accountPurposeTaskType = new InternalTaskConception(
					"AccountPurpose",
					new SimpleAnswerTreader(
							"您好，账号分为我司给予您的资金账号和您的股东帐号，在我司开立资金账号后，"
									+ "您还需要根据个人需求选择开立上海A股账号和深圳A股账号才能进行最终的股票交易（若已有股东账号则不需要再开立）。"
									+ "其中，上海A股账号用于交易上海市场的股票，深圳A股账号用于交易深圳市场的股票（包含中小板和创业板）。"),
					"账号分类及用途");
			this.contextManager.addTaskType(accountPurposeTaskType);
			accountPurposeTaskType.addSubSequentTaskType(creatAccountTaskType);

			TaskConception createAccountBankCardTaskType = new InternalTaskConception(
					"CreateAccountBankCard",
					new SimpleAnswerTreader("您好，我司支持部分银行（农行、招行、中行、兴业、浦发、"
							+ "平安、上海银行、光大、民生、华夏及中信银行）的一站式签约业务，若您有以上银行的银行卡，"
							+ "可以携带着，营业部可以在开立资金帐户时直接帮您指定第三方存管银行，无需您再前往银行网点确认；"
							+ "若您不需要办理第三方存管一站式签约业务的，在前往营业部办理开户时，可以选择不携带银行卡。"),
					"开户银行卡");
			this.contextManager.addTaskType(createAccountBankCardTaskType);
			createAccountBankCardTaskType
					.addSubSequentTaskType(creatAccountTaskType);

			TaskConception createAccountConditionTaskType = new InternalTaskConception(
					"CreateAccountCondition",
					new SimpleAnswerTreader(
							"您好，只要您没有下述情况，均可开立A股账户：\n"
									+ "(1)16周岁以下自然人不得开立资金账户、股东账户，16-18周岁自然人开立资金账户、股东账户应提供收入证明；\n"
									+ "(2)现役军人、人民武装警察持军人证、武警证不得设立证券账户；\n"
									+ "(3)境外企业不得设立A股证券账户，合格境外机构投资者根据中国证券登记结算公司《合格境外机构投资者境内证券投资登记结算业务指南》直接到中国证券登记结算公司办理。\n"
									+ "(4)境外投资者包括：外国的自然人、法人和其它组织、中国港、澳、台地区的法人和其它组织，未经审批不得开立A股证券账户投资A股。\n"
									+ "(5)法人营业执照过期、未年检、吊销、注销的情况，不得设立账户。\n"
									+ "(6)营业部不得受理证券公司、信托投资公司、证券投资基金、基金管理公司、保险公司、银行、社保基金等所有证券账户开户登记业务，该业务由中国证券登记结算公司统一受理。"),
					"开户条件");
			this.contextManager.addTaskType(createAccountConditionTaskType);
			createAccountConditionTaskType
					.addSubSequentTaskType(creatAccountTaskType);

			/*
			 * TaskConception createAccountDiscountTaskType = new
			 * InternalTaskConception( "CreateAccountDiscount", new
			 * SimpleAnswerTreader(
			 * "您好，根据中登公司的相关规定，目前开立上海A股股东账号收费40元，深证股东账号50元，由证券公司代为收取，我司开立资金账号不收费。\n"
			 * + "优惠活动由各地区的营业部自行筹划安排，您可以留下您的开户信息（姓名或称呼、联系方式、开户营业部名称），" +
			 * "我们可以帮您做预约登记，稍后会有营业部专人直接联系您，关于开户优惠活动的详情，您可以与我司营业部专人直接沟通。"),
			 * "开户优惠");
			 */

			TaskConception createAccountDiscountTaskType = new InternalTaskConception(
					"CreateAccountDiscount", new DiscountTreader(), "开户优惠");
			this.contextManager.addTaskType(createAccountDiscountTaskType);
			this.contextManager.addTypeRelation("chargeType", "ChargeType",
					"CreateAccountDiscount");
			createAccountDiscountTaskType
					.addSubSequentTaskType(creatAccountTaskType);

			TaskConception entrustCreateAccountTaskType = new InternalTaskConception(
					"EntrustCreateAccount",
					new SimpleAnswerTreader(
							"您好，开户手续一般都需要开户者本人办理，若因特殊情况需要委托他人办理开户的，"
									+ "需提供开户者本人有效期内的二代身份证、经公证的委托代办书、代办人的有效身份证明文件。"),
					"代办开户");
			this.contextManager.addTaskType(entrustCreateAccountTaskType);
			entrustCreateAccountTaskType
					.addSubSequentTaskType(creatAccountTaskType);

			TaskConception otherCreateAccountMethodTaskType = new InternalTaskConception(
					"OtherCreateAccountMethod", new SimpleAnswerTreader(
							"您好，非现场开户有网上开户和手机开户两种渠道。网上开户可以登录我司官网www.gtja.com，"
									+ "选择（左侧）在线开户进行操作。\n"
									+ "手机开户可以发送88到95521下载开户程序进行操作。"), "开户渠道");
			this.contextManager.addTaskType(otherCreateAccountMethodTaskType);
			otherCreateAccountMethodTaskType
					.addSubSequentTaskType(creatAccountTaskType);

			TaskConception neededCertificateTaskType = new InternalTaskConception(
					"NeededCertificates", new NeededCertificatesTreader(),
					"查询所需证件");
			this.contextManager.addTaskType(neededCertificateTaskType);
			neededCertificateTaskType
					.addSubSequentTaskType(creatAccountTaskType);
			this.contextManager.addTypeRelation("division", "Division",
					neededCertificateTaskType.getTypeName());

			TaskConception departmentAddressTaskType = new InternalTaskConception(
					"DepartmentAddress", new DepartmentAddressTreader(),
					"查询营业部");
			this.contextManager.addTaskType(departmentAddressTaskType);
			/*
			 * departmentAddressTaskType
			 * .addSubSequentTaskType(creatAccountTaskType);
			 */
			this.contextManager.addTypeRelation("division", "Division",
					departmentAddressTaskType.getTypeName());

			TaskConception departmentInfoTaskType = new InternalTaskConception(
					"DepartmentInfo", new DepartmentInfoTaskTreader(),
					"查询营业部信息");
			this.contextManager.addTaskType(departmentInfoTaskType);
			this.contextManager.addTypeRelation("department", "Department",
					departmentInfoTaskType.getTypeName());

			TaskConception CheckDetailTaskType = new InternalTaskConception(
					"CheckDetail", new CheckDetailTreader(), "自营产品信息查询");
			this.contextManager.addTaskType(CheckDetailTaskType);
			this.contextManager.addTypeRelation("financialProduct",
					"FinancialProduct", CheckDetailTaskType.getTypeName());

			/*
			 * TaskConception buyMethodTaskType = new InternalTaskConception(
			 * "BuyMethod", new SimpleAnswerTreader("购买需要您先在我司开立账户。"), "购买方法");
			 * this.contextManager.addTaskType(buyMethodTaskType);
			 * buyMethodTaskType.addSubSequentTaskType(creatAccountTaskType);
			 */

			TaskConception buyMethodTaskType = new InternalTaskConception(
					"BuyMethod", new BuyMethodTreader(), "购买方法");
			this.contextManager.addTaskType(buyMethodTaskType);
			this.contextManager.addTypeRelation("financialProduct",
					"FinancialProduct", buyMethodTaskType.getTypeName());
			buyMethodTaskType.addSubSequentTaskType(creatAccountTaskType);

			TaskConception stockTrendTaskType = new InternalTaskConception(
					"StockTrend", new StockTrendTreader(), "股票走势");
			this.contextManager.addTaskType(stockTrendTaskType);
			this.contextManager.addTypeRelation("stock", "Stock",
					stockTrendTaskType.getTypeName());

			TaskConception stockOutlookTaskType = new InternalTaskConception(
					"StockOutlook", new StockOutlookTreader(), "股票后市");
			this.contextManager.addTaskType(stockOutlookTaskType);
			this.contextManager.addTypeRelation("stock", "Stock",
					stockOutlookTaskType.getTypeName());

		} catch (DoubleInsertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeUnmatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TaskTypeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeMissException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initConfirm() {
		try {
			Entity confirm = new InternalEntity(false);
			confirm.setName("Confirm");
			confirm.setMainExpress("是否确认");

			FactType confirmType = new InternalFactType("Confirm", "是否确认",
					true, confirm);
			contextManager.addFactType(confirmType);
			this.contextManager.addTypeRelation("confirm", "Confirm",
					"SaleTask");

			Entity yesConfirm = new InternalEntity(false);
			yesConfirm.setName("Yes");
			yesConfirm.setMainExpress("确认");
			this.knowledgeManager.addUniqueIndex("确认", yesConfirm);

			Entity noConfirm = new InternalEntity(false);
			noConfirm.setName("No");
			noConfirm.setMainExpress("不同意");
			this.knowledgeManager.addUniqueIndex("不同意", noConfirm);

			/*
			 * System.out.println(Controler.knowledgeManager.uniqueIndex("不同意"));
			 * System.out.println(Controler.knowledgeManager.uniqueIndex("确认"));
			 */
		} catch (DoubleInsertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeUnmatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeMissException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initChargType() {
		try {
			Entity chargeTypeEntity = new InternalEntity(false);
			chargeTypeEntity.setName("ChargeType");
			chargeTypeEntity.setMainExpress("费用类型");
			this.addToRecognition(chargeTypeEntity, "__ATTRIBUTE__ChargeType",
					"费用类型");
			this.addToRecognition(chargeTypeEntity, "__ATTRIBUTE__ChargeType",
					"费用");
			this.addToRecognition(chargeTypeEntity, "__ATTRIBUTE__ChargeType",
					"收费");

			FactType chargeType = new InternalFactType("ChargeType", "是否确认",
					true, chargeTypeEntity);

			contextManager.addFactType(chargeType);

			Entity creatAccountCharge = new InternalEntity(false);
			creatAccountCharge.setName("CreatAccountCharge");
			creatAccountCharge.setMainExpress("开户收费");
			this.addToRecognition(creatAccountCharge, "ChargeType", "开户费");
			this.addToRecognition(creatAccountCharge, "ChargeType", "开户费用");
			this.addToRecognition(creatAccountCharge, "ChargeType", "开户成本");
			this.addToRecognition(creatAccountCharge, "ChargeType", "开户收费");

			Entity commission = (Entity) this.knowledgeManager
					.uniqueIndex("Commission");
			/*
			 * factType = new InternalFactType("Commission", "commission", true,
			 * commission); this.contextManager.addFactType(factType);
			 * this.addToRecognition(commission, "__ATTRIBUTE__Commission",
			 * "佣金"); this.addToRecognition(commission,
			 * "__ATTRIBUTE__Commission", "交易费率");
			 * this.addToRecognition(commission, "__ATTRIBUTE__Commission",
			 * "费率");
			 */
			this.addToRecognition(commission, "ChargeType", "佣金");
			this.addToRecognition(commission, "ChargeType", "佣金费率");
			this.addToRecognition(commission, "ChargeType", "佣金率");
			this.addToRecognition(commission, "ChargeType", "交易费率");
			this.addToRecognition(commission, "ChargeType", "手续费率");
			this.addToRecognition(commission, "ChargeType", "交易手续费率");
			this.addToRecognition(commission, "ChargeType", "开户佣金");
			this.addToRecognition(commission, "ChargeType", "开户佣金率");
			this.addToRecognition(commission, "ChargeType", "买卖佣金");
			this.addToRecognition(commission, "ChargeType", "交易佣金");
			this.addToRecognition(commission, "ChargeType", "买卖佣金率");
			this.addToRecognition(commission, "ChargeType", "交易佣金率");

			Entity chargeCommision = new InternalEntity(false);
			chargeCommision.setName("ChargCommision");
			chargeCommision.setMainExpress("开户费佣金");
			this.addToRecognition(chargeCommision, "ChargeType", "开户手续费");
			this.addToRecognition(chargeCommision, "ChargeType", "开户交易费");
			this.addToRecognition(chargeCommision, "ChargeType", "开户买卖费用");
			this.addToRecognition(chargeCommision, "ChargeType", "开户费率");

			Entity transactionCost = new InternalEntity(false);
			transactionCost.setName("TransactionCost");
			transactionCost.setMainExpress("交易收费");
			this.addToRecognition(transactionCost, "ChargeType", "交易费用");
			this.addToRecognition(transactionCost, "ChargeType", "交易收费");
			this.addToRecognition(transactionCost, "ChargeType", "买卖费用");
			this.addToRecognition(transactionCost, "ChargeType", "交易成本");
			this.addToRecognition(transactionCost, "ChargeType", "买卖成本");

		} catch (DoubleInsertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeUnmatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initStackOperationMethod() {
		try {
			// Entity stock = (Entity) this.knowledgeManager.uniqueIndex("股票");

			TaskConception stackOperationMethod = new InternalTaskConception(
					"StackOperationMethod", new StackOperationMthodTreader(),
					"股票操作方式");
			this.contextManager.addTaskType(stackOperationMethod);
			this.contextManager.addTypeRelation("confirm", "Confirm",
					stackOperationMethod.getTypeName());
		} catch (DoubleInsertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeUnmatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeMissException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initFactType() {

	}

	@Override
	public void deal() {
		this.initChargType();
		this.initFactType();
		this.initSimpleTask();
		this.initConfirm();
		this.initStackOperationMethod();
	}

}
