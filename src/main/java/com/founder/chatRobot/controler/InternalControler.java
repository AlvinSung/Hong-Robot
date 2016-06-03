package com.founder.chatRobot.controler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.founder.chatRobot.common.GeneralApi.TwoValue;
import com.founder.chatRobot.common.exception.DoubleInsertException;
import com.founder.chatRobot.common.exception.TaskTypeNotFoundException;
import com.founder.chatRobot.common.exception.TypeMissException;
import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.common.semanticRecognition.EntryInfo;
import com.founder.chatRobot.common.task.ConditionBean;
import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.constants.ConfigConstants;
import com.founder.chatRobot.context.manager.InternalContextManager;
import com.founder.chatRobot.context.session.common.Session;
import com.founder.chatRobot.controler.common.Controler;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.conception.taskType.InternalTaskConception;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;
import com.founder.chatRobot.knowledgeMap.init.DepartmentIniter;
import com.founder.chatRobot.knowledgeMap.init.DivisionLoader;
import com.founder.chatRobot.knowledgeMap.init.FundLoader;
import com.founder.chatRobot.knowledgeMap.init.NationLoader;
import com.founder.chatRobot.knowledgeMap.init.PositionIniter;
import com.founder.chatRobot.knowledgeMap.init.ProductLoader;
import com.founder.chatRobot.knowledgeMap.init.ProductRiskLoader;
import com.founder.chatRobot.knowledgeMap.init.StocksIniter;
import com.founder.chatRobot.knowledgeMap.init.TaskTypeLoader;
import com.founder.chatRobot.knowledgeMap.init.WhatLoader;
import com.founder.chatRobot.knowledgeMap.treader.InfoVerification;
import com.founder.chatRobot.recognition.actionRecognitionTreader.ActionRecognitionTreader;
import com.founder.chatRobot.recognition.addressRegular.AddressRegular;
import com.founder.chatRobot.recognition.common.RecognitionManager;
import com.founder.chatRobot.recognition.common.interfaces.old.Recognition;
import com.founder.chatRobot.recognition.comparisionMethod.ComparisionRecognition;
import com.founder.chatRobot.recognition.composeEntityRecognition.SimpleComposeEntityRecognition;
import com.founder.chatRobot.recognition.degreeRecognition.DegreeRecignition;
import com.founder.chatRobot.recognition.manage.InternalRecognitionManager;
import com.founder.chatRobot.recognition.normalizer.RecognitionNormalizer;
import com.founder.chatRobot.recognition.regular.RegularRecognition;
import com.founder.chatRobot.recognition.statusActionRecognition.StatusActionRecognition;
import com.founder.chatRobot.svm.Identify;
import com.founder.chatRobot.task.treader.SimpleAnswerTreader;
import com.founder.chatRobot.task.treader.UnsubscribeMessageTreader;

public class InternalControler extends Controler {

	static String[] signalModalParticles = { "喔", "哦", "吧", "罢", "呗", "啵", "的",
			"价", "家", "啦", "来", "唻", "了", "嘞", "哩", "咧", "咯", "啰", "喽", "吗",
			"嘛", "嚜", "么", "麽", "哪", "呢", "呐", "否", "呵", "哈", "兮", "般", "则",
			"连", "罗", "给", "噻", "哉", "阿", "啊", "呃", "欸", "哇", "呀", "也", "耶",
			"哟", "欤", "呕", "噢", "呦", "嘢" };

	public static final String CLASS_MAP_FILE = ConfigConstants.MODEL_DATA_PATH
			+ File.separator + "classmap.txt";
	public static final String CHOSEN_TERMS_FILE = ConfigConstants.MODEL_DATA_PATH
			+ File.separator + "chosenTerms.txt";
	public static final String MODEL_FILE = ConfigConstants.MODEL_DATA_PATH
			+ File.separator + "model.model";
	private ComparisionRecognition semanticRecognition;
	private Recognition mobileNumberRecognition, byeRecognition;
	private SimpleComposeEntityRecognition simpleComposeEntityRecognition;
	private ActionRecognitionTreader actionRecognitionTreader;
	private RecognitionNormalizer recognitionNormalizer;
	private RecognitionManager recognitionManager;
	private StatusActionRecognition statusActionRecognition;

	public InternalControler() {
		this.identify = new Identify();
		this.recognitionManager = new InternalRecognitionManager();
		this.contextManager = new InternalContextManager(knowledgeManager);
		this.semanticRecognition = new ComparisionRecognition(
				this.contextManager);
		this.recognitionNormalizer = new RecognitionNormalizer();
		this.statusActionRecognition = new StatusActionRecognition();
	}

	private void initKnowledgeMap() {
		NationLoader nationLoader = new NationLoader(
				ConfigConstants.META_DATA_PATH + File.separator + "nation.txt",
				knowledgeManager, this.semanticRecognition, this.contextManager);
		nationLoader.deal();

		FundLoader loader = new FundLoader(ConfigConstants.META_DATA_PATH
				+ File.separator + "fund.xls", knowledgeManager,
				this.semanticRecognition, this.contextManager);
		loader.deal();

		StocksIniter stocksIniter = new StocksIniter(
				ConfigConstants.META_DATA_PATH + File.separator + "stocks.txt",
				knowledgeManager, this.semanticRecognition, this.contextManager);
		stocksIniter.deal();

		ProductLoader productLoader = new ProductLoader(
				ConfigConstants.META_DATA_PATH + File.separator + "product.xls",
				knowledgeManager, this.semanticRecognition, this.contextManager);
		productLoader.deal();

		ProductRiskLoader productRidkLoader = new ProductRiskLoader(
				ConfigConstants.META_DATA_PATH + File.separator
						+ "product-risk.xls", knowledgeManager,
				this.semanticRecognition, this.contextManager);
		productRidkLoader.deal();

		DivisionLoader divisionLoader = new DivisionLoader(
				ConfigConstants.META_DATA_PATH + File.separator
						+ "division.txt", knowledgeManager,
				this.semanticRecognition, this.contextManager);
		divisionLoader.deal();

		PositionIniter initer = new PositionIniter(
				ConfigConstants.META_DATA_PATH + File.separator
						+ "position.txt", knowledgeManager,
				this.semanticRecognition, this.contextManager);
		initer.deal();

		WhatLoader whatLoader = new WhatLoader(ConfigConstants.META_DATA_PATH
				+ File.separator + "descript.xls", knowledgeManager,
				this.semanticRecognition, this.contextManager);
		whatLoader.deal();

		DepartmentIniter departmentIniter = new DepartmentIniter(
				ConfigConstants.META_DATA_PATH + File.separator
						+ "department1.xls", knowledgeManager,
				this.semanticRecognition, this.contextManager);
		departmentIniter.deal();

		TaskTypeLoader taskTypeLoader = new TaskTypeLoader(knowledgeManager,
				this.semanticRecognition, this.contextManager);
		taskTypeLoader.deal();

		try {
			this.contextManager.addTypeRelation("annualYield", "AnnualYield",
					"SaleTask");
		} catch (TypeMissException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initUnsubscribeMessage() {
		TaskConception taskType;

		taskType = new InternalTaskConception("UnsubscribeMessage",
				new UnsubscribeMessageTreader(), "退订短信");
		try {
			this.contextManager.addTaskType(taskType);
			this.contextManager.addTypeRelation("mobilePhoneNumber",
					"MobileNumber", taskType.getTypeName());
			this.contextManager.addTypeRelation("checkNumber", "CheckCode",
					taskType.getTypeName());
		} catch (TypeMissException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DoubleInsertException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TypeUnmatchException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void initRecognition() {
		Set<String> characters = new HashSet<String>();
		Entity mobileNumber = (Entity) knowledgeManager.uniqueIndex("手机号码");

		this.mobileNumberRecognition = new RegularRecognition<Long>(
				"(^|[^0-9])[0]?[1]([3,5,8][0-9]|[7][0])[0-9]{8,8}([^0-9]|$)",
				"[1]([3,5,8][0-9]|[7][0])[0-9]{8}", Long.class, mobileNumber,
				"MobileNumber");

		Entity phoneNumber = (Entity) Controler.knowledgeManager
				.uniqueIndex("PhoneNumber");

		/*
		 * FactType phoneNumberType = new InternalFactType("PhoneNumber",
		 * "座机号码", false, phoneNumber);
		 * contextManager.addFactType(phoneNumberType);
		 */
		Recognition phoneRecognition = new RegularRecognition<Long>(
				"(^|[^0-9])[0]?([0][0-9]{2,3}|[(][0][0-9]{2,3}[)]|[0][0-9]{2,3}[\\-]|)([1-9][0-9]{5,7})([^0-9]|$)",
				"([0][0-9]{2,3}|[(][0][0-9]{2,3}[)]|[0][0-9]{2,3}[\\-]|)([1-9][0-9]{5,7})",
				String.class, phoneNumber, "PhoneNumber");

		this.byeRecognition = new RegularRecognition<Long>(
				"(^|[^0-9])[0]?(8{1,2})([^0-9]|$)", "8{1,2}", Long.class,
				mobileNumber, "Bye");

		DegreeRecignition degreeRecignition = new DegreeRecignition(
				Controler.knowledgeManager, contextManager);

		characters.add("SecurityCode");
		characters.add("SecurityName");
		characters.add("FundShortName");
		characters.add("FundCode");
		characters.add("FundFullName");
		characters.add("DepartmentName");
		characters.add("ProductName");
		characters.add("ProductCode");
		characters.add("StockCode");
		characters.add("StockName");

		this.simpleComposeEntityRecognition = new SimpleComposeEntityRecognition(
				characters);

		actionRecognitionTreader = new ActionRecognitionTreader(
				knowledgeManager);

		AddressRegular addressRegular = new AddressRegular();

		this.recognitionManager.addRecognition("Degree", degreeRecignition);
		this.recognitionManager.addRecognition("Bye", this.byeRecognition);
		this.recognitionManager.addRecognition("PhoneNumber", phoneRecognition);
		this.recognitionManager.addRecognition("MobileNumber",
				this.mobileNumberRecognition);
		this.recognitionManager.addRecognition("ComparableMethod",
				this.semanticRecognition);
		this.recognitionManager.activeRecognition("ComparableMethod");
		this.recognitionManager.activeRecognition("Bye");
		this.recognitionManager.activeRecognition("PhoneNumber");
		this.recognitionManager.activeRecognition("MobileNumber");
		this.recognitionManager.activeRecognition("Degree");
		// this.recognitionManager.activeRecognition("Confirm");

		this.recognitionManager
				.addPostTreader("AddressRegular", addressRegular);
		this.recognitionManager.addPostTreader("ComposeMethod",
				this.simpleComposeEntityRecognition);
		this.recognitionManager.addPostTreader("ActionRecognition",
				this.actionRecognitionTreader);
		this.recognitionManager.addPostTreader("Normalizer",
				this.recognitionNormalizer);
		this.recognitionManager.addPostTreader("StatusActionRecognition",
				this.statusActionRecognition);
		this.recognitionManager.activePostTreader("StatusActionRecognition");
		this.recognitionManager.activePostTreader("AddressRegular");
		this.recognitionManager.activePostTreader("Normalizer");
		this.recognitionManager.activePostTreader("ActionRecognition");
		this.recognitionManager.activePostTreader("ComposeMethod");
	}

	public void init() {
		this.identify.loadModel(CLASS_MAP_FILE, CHOSEN_TERMS_FILE, MODEL_FILE);
		this.initKnowledgeMap();
		this.initUnsubscribeMessage();
		// this.initSimpleTask();
		this.initRecognition();
	}

	public ConditionBean recognize(String sentence) {
		try {
			ConditionBean condition = this.recognitionManager.recogn(sentence,
					"111", "111", "上海");
			return condition;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeUnmatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<ResultBean> tread(String sentence, String sessionId,
			String userId, String position) {
		ConditionBean condition, sessionCondition;
		Session session = this.contextManager.getSession(sessionId);
		ResultBean result;
		List<ResultBean> resultList;
		RecognitionManager taskRecognitionManager;

		sentence = sentence.trim();
		Controler.debugLoger.debug(sentence);
		Controler.chatRecordLogger.info(sessionId + " : sentence user inputing : " + sentence);

		try {
			taskRecognitionManager = session.getRecognitionManager();
			if (taskRecognitionManager != null) {
				sessionCondition = taskRecognitionManager.recogn(sentence,
						sessionId, userId, position);
				condition = this.recognitionManager.recogn(sentence, sessionId,
						userId, position);
				condition.addFacts(sessionCondition.getFactSet());
			} else {
				condition = this.recognitionManager.recogn(sentence, sessionId,
						userId, position);
			}

			Controler.debugLoger.debug("Entities has been found : "
					+ condition.getSentence());

			for (@SuppressWarnings("rawtypes")
			EntryInfo fact : condition.getFactSet()) {
				Controler.debugLoger.debug(fact.getTypeName());
				// Controler.debugLoger.debug(fact.getInfo().getInfo().get(0));
			}

			if ((resultList = InfoVerification.tread(condition, session)) == null)
				if ((resultList = this.contextManager.doLastTask(condition)) == null
						|| resultList.size() == 0) {
					sentence = condition.getSentence().trim();
					String oraSentence = sentence;
					sentence = ((sentence = sentence.replaceAll("怎么", ""))
							.length() > 3 ? sentence : oraSentence);
					oraSentence = sentence;
					sentence = ((sentence = sentence.replaceAll("如何", ""))
							.length() > 3 ? sentence : oraSentence);
					oraSentence = sentence;
					sentence = ((sentence = sentence.replaceAll("请问", ""))
							.length() > 3 ? sentence : oraSentence);
					oraSentence = sentence;
					sentence = ((sentence = sentence.replaceAll("请", ""))
							.length() > 3 ? sentence : oraSentence);
					oraSentence = sentence;
					sentence = ((sentence = sentence.replaceAll("谢谢", ""))
							.length() > 3 ? sentence : oraSentence);
					oraSentence = sentence;
					sentence = ((sentence = sentence.replaceAll("你好", ""))
							.length() > 3 ? sentence : oraSentence);
					oraSentence = sentence;
					sentence = ((sentence = sentence.replaceAll("谢谢", ""))
							.length() > 3 ? sentence : oraSentence);
					oraSentence = sentence;
					sentence = ((sentence = sentence.replaceAll("再见", ""))
							.length() > 3 ? sentence : oraSentence);

					sentence = sentence.replaceAll("[0-9]*", "");

					// Controler.debugLoger.debug("trwwewe : " + sentence);

					if (sentence.length() <= 0) {
						resultList = new ArrayList<ResultBean>(1);
						if (session.getPopTask() != null) {
							result = session.getPopTask().doTask();
						} else {
							result = new ResultBean();
							result.setAnswer("您好！请问还有什么可以为您服务的？");
						}
						resultList.add(result);
						return resultList;
					}

					oraSentence = sentence;
					for (String word : InternalControler.signalModalParticles) {
						sentence = sentence.replaceAll(word, "");
					}

					if (sentence.length() <= 0) {
						resultList = new ArrayList<ResultBean>(1);
						result = new ResultBean();
						result.setAnswer("您好！请问还有什么可以为您服务的？");
						resultList.add(result);
						return resultList;
					}

					sentence = oraSentence;

					TwoValue<Double, String> value = this.identify
							.identifySentence(sentence);
					if (value != null) {
						Controler.debugLoger.debug("sentence is : " + sentence);
						Controler.debugLoger.debug("分类结果：" + value.getB() + " , "
								+ value.getA());

						String taskTypeName = value.getB();
						TaskConception taskType;
						try {
							taskType = this.contextManager
									.findTaskType(taskTypeName);
							condition.addTask(taskType);
						} catch (TaskTypeNotFoundException e) {
							Controler.debugLoger.debug("TaskTypeNotFoundException : "
									+ e.getMessage());
						}

						resultList = this.contextManager.doTask(condition);
					}
					if (resultList == null || resultList.size() == 0) {
						resultList = new ArrayList<ResultBean>(1);
						if (session.getPopTask() != null) {
							result = session.getPopTask().doTask();
						} else {
							result = new ResultBean();
							result.setAnswer("很抱歉，系统正在完善，还不能处理您的问题。\n"
									+ "请问还有什么可以为您服务的？");
						}
						resultList.add(result);
					}
					//Controler.debugLoger.debug("aaaaa");
					Controler.chatRecordLogger.info(sessionId + ": sentence returned by classtify" );
					for(ResultBean answer: resultList){
						Controler.chatRecordLogger.info(sessionId + " : " + answer.getAnswer());
					}
					return resultList;
				} else {
					//Controler.debugLoger.debug("bbbbb");
					Controler.chatRecordLogger.info(sessionId + ": sentence returned by lastTask");
					for(ResultBean answer: resultList){
						Controler.chatRecordLogger.info(sessionId + " : " + answer.getAnswer());
					}
					return resultList;
				}
			else {
				//Controler.debugLoger.debug("ccccc");
				Controler.chatRecordLogger.info(sessionId + ": sentence returned by knowledge treader");
				for(ResultBean answer: resultList){
					Controler.chatRecordLogger.info(sessionId + " : " + answer.getAnswer());
				}
				return resultList;
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeUnmatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		InternalControler controler = new InternalControler();
		String sentenceString = null;

		controler.init();
		// controler.initWithModleTrain();

		try {
			while (true) {
				System.out.println("请输入：");
				byte[] tmp = new byte[100];
				System.in.read(tmp);
				sentenceString = new String(tmp);
				System.out.println("用户输入：" + sentenceString);
				long currentTime = System.currentTimeMillis();
				List<ResultBean> beanList = controler.tread(sentenceString,
						"11111", "11111", "上海");
				System.out.println("用时："
						+ (System.currentTimeMillis() - currentTime));
				if (beanList != null) {
					for (ResultBean bean : beanList) {
						System.out.println(bean.getAnswer().trim());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addSimpleTask(String typeName, String chineseTypeName,
			String answer) {
		try {
			TaskConception chargeTaskType = new InternalTaskConception(
					typeName, new SimpleAnswerTreader(answer), chineseTypeName);

			this.contextManager.addTaskType(chargeTaskType);
		} catch (DoubleInsertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeUnmatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
