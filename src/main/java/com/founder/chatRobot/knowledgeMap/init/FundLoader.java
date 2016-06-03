package com.founder.chatRobot.knowledgeMap.init;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.founder.chatRobot.common.exception.DoubleInsertException;
import com.founder.chatRobot.common.exception.TypeMissException;
import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.context.fact.InternalFactType;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.manager.common.ContextManager;
import com.founder.chatRobot.knowledgeMap.conception.entity.InternalEntity;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.conception.status.AccountStatusType;
import com.founder.chatRobot.knowledgeMap.conception.status.common.Status;
import com.founder.chatRobot.knowledgeMap.conception.taskType.InternalTaskConception;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;
import com.founder.chatRobot.knowledgeMap.init.common.Loader;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;
import com.founder.chatRobot.recognition.comparisionMethod.ComparisionRecognition;
import com.founder.chatRobot.task.treader.CreatAccountTreader;

public class FundLoader extends Loader {

	public FundLoader(String fileName, KnowledgeManager knowledgeManager,
			ComparisionRecognition recognition, ContextManager contextManager) {
		super(fileName, knowledgeManager, recognition, contextManager);
	}

	/*private TaskConception saleTaskInit() {
		try {
			TaskConception saleTask = new InternalTaskConception("SaleTask",
					new SaleTaskTreader(), "推荐投资产品");
			this.contextManager.addTaskType(saleTask);
			return saleTask;
		} catch (DoubleInsertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeUnmatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}*/

	private TaskConception createAccountInit() {
		TaskConception creatAccountTaskType = null;
		try {
			creatAccountTaskType = new InternalTaskConception("CreateAccount",
					new CreatAccountTreader(), "预约开户");

			Entity phoneNumber = new InternalEntity(false);
			phoneNumber.setName("PhoneNumber");
			phoneNumber.setMainExpress("联系电话");
			this.knowledgeManager.addUniqueIndex("PhoneNumber", phoneNumber);
			FactType factType = new InternalFactType("PhoneNumber", "电话", true,
					phoneNumber);
			this.contextManager.addFactType(factType);

			this.contextManager.addTaskType(creatAccountTaskType);
			this.contextManager.addTypeRelation("mobilePhoneNumber",
					"MobileNumber", creatAccountTaskType.getTypeName());
			this.contextManager.addTypeRelation("checkNumber", "CheckCode",
					creatAccountTaskType.getTypeName());
			this.contextManager.addTypeRelation("phoneNumber", "PhoneNumber",
					creatAccountTaskType.getTypeName());

			Entity SurnameEntity = new InternalEntity(false);
			SurnameEntity.setName("Surname");
			SurnameEntity.setMainExpress("姓");

			FactType Surname = new InternalFactType("Surname", "姓", true,
					SurnameEntity);
			contextManager.addFactType(Surname);
			contextManager.addTypeRelation("surname", "Surname",
					creatAccountTaskType.getTypeName());
			
			Entity fullNameEntity = new InternalEntity(false);
			fullNameEntity.setName("FullName");
			fullNameEntity.setMainExpress("姓名");
			this.addToRecognition(fullNameEntity, "", "姓名");

			FactType fullName = new InternalFactType ("FullName", "姓名", true,
					fullNameEntity);
			contextManager.addFactType(fullName);
			contextManager.addTypeRelation("fullName", "FullName",
					creatAccountTaskType.getTypeName());
			this.addToRecognition(fullNameEntity, "__ATTRIBUTE__FullName", "姓名");
			this.addToRecognition(fullNameEntity, "__ATTRIBUTE__FullName", "全名");
			this.addToRecognition(fullNameEntity, "__ATTRIBUTE__FullName", "名字");

			Entity Gender = new InternalEntity(false);
			Gender.setName("Gender");
			Gender.setMainExpress("性别");

			FactType GenderFact = new InternalFactType("Gender", "性别", true,
					Gender);
			contextManager.addFactType(GenderFact);
			contextManager.addTypeRelation("gender", "Gender", "CreateAccount");

			Entity male = new InternalEntity(false);
			male.setName("male");
			male.setMainExpress("男");
			this.addToRecognition(male, "Gender", "男");
			this.addToRecognition(male, "Gender", "先生");
			this.addToRecognition(male, "Gender", "公");

			Entity female = new InternalEntity(false);
			female.setName("male");
			female.setMainExpress("女");
			this.addToRecognition(female, "Gender", "女");
			this.addToRecognition(female, "Gender", "女士");
			this.addToRecognition(female, "Gender", "母");
			this.addToRecognition(female, "Gender", "小姐");
			this.addToRecognition(female, "Gender", "太太");

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
		return creatAccountTaskType;
	}

	public void deal() {
		FactType factType;
		Workbook book = null;

		try {
			Entity checkCodeEntity = new InternalEntity(false);
			checkCodeEntity.setName("CheckCode");
			checkCodeEntity.setMainExpress("效验码");
			this.knowledgeManager.addUniqueIndex("效验码", checkCodeEntity);

			Entity mobileNumberEntity = new InternalEntity(false);
			mobileNumberEntity.setName("MobilNumber");
			mobileNumberEntity.setMainExpress("手机号码");
			this.knowledgeManager.addUniqueIndex("手机号码", mobileNumberEntity);

			factType = new InternalFactType("CheckCode", "效验码", true,
					checkCodeEntity);
			this.contextManager.addFactType(factType);

			FactType mobileFactType = new InternalFactType("MobileNumber",
					"手机号码", true, mobileNumberEntity);
			this.contextManager.addFactType(mobileFactType);

			// book = Workbook.getWorkbook(this.file);
			book = Workbook.getWorkbook(is);
			Sheet sheet = book.getSheet(0);
			Entity entity, product, fund, financialProduct, actionSubject, actionObject, investmentProduct, action, fundCompany;
			Entity[] attributes = new InternalEntity[sheet.getColumns() + 1];
			investmentProduct = new InternalEntity(false);
			investmentProduct.setMainExpress("投资产品");
			investmentProduct.setName("InvestmentProduct");
			this.addToRecognition(investmentProduct, "InvestmentProduct",
					"投资产品");
			factType = new InternalFactType("InvestmentProduct", "投资产品", true,
					investmentProduct);
			this.contextManager.addFactType(factType);

			fund = new InternalEntity(false);
			fund.setMainExpress("基金");
			fund.setName("Fund");
			this.addToRecognition(fund, "InvestmentProduct", "基金");
			fund.setParent(investmentProduct);
			factType = new InternalFactType("Fund", "基金", true, fund);
			this.contextManager.addFactType(factType);

			financialProduct = new InternalEntity(false);
			financialProduct.setMainExpress("理财产品");
			financialProduct.setName("FinancialProduct");
			financialProduct.setParent(investmentProduct);
			this.knowledgeManager.addUniqueIndex("理财产品", financialProduct);
			this.addToRecognition(financialProduct, "InvestmentProduct", "理财产品");
			factType = new InternalFactType("FinancialProduct", "理财产品", true,
					financialProduct);
			this.contextManager.addFactType(factType);

			actionSubject = new InternalEntity(false);
			actionSubject.setMainExpress("行为主体");
			actionSubject.setName("ActionSubject");
			this.knowledgeManager.addUniqueIndex("行为主体", actionSubject);
			// this.addToRecognition(actionSubject, "ActionSubject", "行为主体");
			actionObject = new InternalEntity(false);
			actionObject.setMainExpress("行为客体");
			actionObject.setName("ActionObject");
			this.knowledgeManager.addUniqueIndex("行为客体", actionObject);
			// this.addToRecognition(actionObject, "ActionObject", "行为客体");
			action = new InternalEntity(false);
			action.setMainExpress("行为");
			action.setName("Action");
			this.knowledgeManager.addUniqueIndex("行为", action);
			// this.addToRecognition(action, "Action", "行为");
			fundCompany = new InternalEntity(false);
			fundCompany.setMainExpress("基金公司");
			fundCompany.setName("FundCompany");
			this.addToRecognition(fundCompany, "FundCompany", "基金公司");
			this.knowledgeManager.addUniqueIndex("基金公司", fundCompany);

			TaskConception creatAccountTaskType = this.createAccountInit();

			Status precondition = new AccountStatusType(creatAccountTaskType,
					"您已经开过户了。", "您还没有开户，请先开户。");
			precondition.setName("CreatAccountStatus");
			this.knowledgeManager.addUniqueIndex("CreatAccountStatus", precondition);
			
			creatAccountTaskType.setPoscondition(precondition);
			
			//precondition.setMainExpress("开户");
			// this.addToRecognition(precondition, "CreateAccount", "开户");

			/*TaskConception saleTaskType = this.saleTaskInit();
			precondition.addRestrictTaskType(saleTaskType);
			saleTaskType.addPrecondition(precondition);

			this.contextManager.addTypeRelation("investmentProduct",
					"InvestmentProduct", "SaleTask");*/

			/*
			 * this.contextManager.addTypeRelation("foud", "Fund",
			 * saleTaskType.getTypeName());
			 */

			for (int j = 0; j < sheet.getColumns(); j++) {
				Cell cCell = sheet.getCell(j, 1);
				Cell cell = sheet.getCell(j, 2);
				Cell eCell = sheet.getCell(j, 0);
				Cell actCell = sheet.getCell(j, 3);
				// System.out.println(cell.getContents() + " ");
				if (cell.getContents().equals("基金份额")) {
					entity = new InternalEntity(true);
					entity.addAdverbOfValue("非常小", (short) 0, (short) 15);
					entity.addAdverbOfValue("比较小", (short) 15, (short) 30);
					entity.addAdverbOfValue("稍小", (short) 30, (short) 45);
					entity.addAdverbOfValue("一般小", (short) 15, (short) 30);
					entity.addAdverbOfValue("中等", (short) 40, (short) 60);
					entity.addAdverbOfValue("稍大", (short) 55, (short) 70);
					entity.addAdverbOfValue("比较大", (short) 70, (short) 85);
					entity.addAdverbOfValue("非常大", (short) 85, (short) 100);
					entity.addAdverbOfValue("大", (short) 65, (short) 100);
					entity.addAdverbOfValue("小", (short) 0, (short) 35);
					entity.addAdverbOfValue("较小", (short) 15, (short) 30);
					entity.addAdverbOfValue("较大", (short) 70, (short) 85);
					// System.out.println(entity.isValueComparable());
				} else if (cell.getContents().equals("单位净值")) {
					entity = new InternalEntity(true);
					// entity = new InternalEntity(this.knowledgeManager, true);
					entity.addAdverbOfValue("非常小", (short) 0, (short) 15);
					entity.addAdverbOfValue("比较小", (short) 15, (short) 30);
					entity.addAdverbOfValue("稍小", (short) 30, (short) 45);
					entity.addAdverbOfValue("一般小", (short) 15, (short) 30);
					entity.addAdverbOfValue("中等", (short) 40, (short) 60);
					entity.addAdverbOfValue("稍大", (short) 55, (short) 70);
					entity.addAdverbOfValue("比较大", (short) 70, (short) 85);
					entity.addAdverbOfValue("非常大", (short) 85, (short) 100);
					entity.addAdverbOfValue("大", (short) 60, (short) 80);
					entity.addAdverbOfValue("小", (short) 20, (short) 40);
					entity.addAdverbOfValue("较小", (short) 15, (short) 30);
					entity.addAdverbOfValue("较大", (short) 70, (short) 85);
					// entity = new InternalEntity(this.knowledgeManager, true);
					entity.addAdverbOfValue("非常低", (short) 0, (short) 15);
					entity.addAdverbOfValue("比较低", (short) 15, (short) 30);
					entity.addAdverbOfValue("稍低", (short) 30, (short) 45);
					entity.addAdverbOfValue("一般低", (short) 15, (short) 30);
					entity.addAdverbOfValue("中等", (short) 40, (short) 60);
					entity.addAdverbOfValue("稍高", (short) 55, (short) 70);
					entity.addAdverbOfValue("比较高", (short) 70, (short) 85);
					entity.addAdverbOfValue("非常高", (short) 85, (short) 100);
					entity.addAdverbOfValue("高", (short) 65, (short) 100);
					entity.addAdverbOfValue("低", (short) 0, (short) 35);
					entity.addAdverbOfValue("较低", (short) 15, (short) 30);
					entity.addAdverbOfValue("较高", (short) 70, (short) 85);
					// System.out.println(entity.isValueComparable());
				} else
					entity = new InternalEntity(false);
				entity.setMainExpress(cell.getContents());
				entity.setName(eCell.getContents());
				this.knowledgeManager
						.addUniqueIndex(cell.getContents(), entity);
				attributes[j] = entity;
				fund.addProductToAttribute(entity);
				entity.addAttributeToProduct(fund);
				this.addToRecognition(entity,
						"__ATTRIBUTE__" + eCell.getContents(),
						cell.getContents());
				/*
				 * System.out.println("__ATTRIBUTE__" + eCell.getContents() +
				 * " : " + cell.getContents());
				 */
				if (cCell.getContents() != null
						&& !cCell.getContents().equals("")) {
					String[] represents = cCell.getContents().split(" ");
					for (String represent : represents) {
						/*
						 * System.out.println("__ATTRIBUTE__" +
						 * eCell.getContents() + " : " + represent + " : " +
						 * entity);
						 */
						this.addToRecognition(entity,
								"__ATTRIBUTE__" + eCell.getContents(),
								represent);
					}
				}

				factType = new InternalFactType(eCell.getContents(),
						cell.getContents(), true, entity);
				this.contextManager.addFactType(factType);
				
				//System.out.println("factType : " + factType.getTypeName());

				/*this.contextManager.addTypeRelation(eCell.getContents(),
						eCell.getContents(), saleTaskType.getTypeName());*/

				/*
				 * if (cell.equals("基金份额")) {
				 * 
				 * } else if (cell.equals("单位净值")) {
				 * 
				 * }
				 */

				Entity newAction;
				if (actCell.getContents() != null
						&& !actCell.getContents().equals("")) {
					newAction = (Entity) knowledgeManager.uniqueIndex(actCell
							.getContents());
					if (newAction == null) {
						newAction = new InternalEntity(false);
						newAction.setName(actCell.getContents());
						newAction.setMainExpress(actCell.getContents());
						this.knowledgeManager.addUniqueIndex(
								actCell.getContents(), newAction);

						newAction.addProductToAttribute(actionObject);
						newAction.addProductToValue(fund);
						fund.addValueToAttribute(actionObject);
						fund.addValueToProduct(newAction);
						actionObject.addAttributeToProduct(entity);
						actionObject.addAttributeToValue(newAction);
						// System.out.println("aaaa : " +
						// actCell.getContents());
						for (String content : actCell.getContents().split(" ")) {
							// System.out.println(content);
							this.addToRecognition(newAction, "Action", content);
						}
					}

					newAction.addProductToAttribute(actionSubject);
					newAction.addProductToValue(entity);
					entity.addValueToAttribute(actionSubject);
					entity.addValueToProduct(newAction);
					actionSubject.addAttributeToProduct(entity);
					actionSubject.addAttributeToValue(newAction);

					newAction.setParent(action);
				}
			}

			// 增加收益率
			entity = new InternalEntity(true);
			entity.addAdverbOfValue("非常低", (short) 0, (short) 15);
			entity.addAdverbOfValue("比较低", (short) 15, (short) 30);
			entity.addAdverbOfValue("稍低", (short) 30, (short) 45);
			entity.addAdverbOfValue("一般低", (short) 15, (short) 30);
			entity.addAdverbOfValue("中等", (short) 40, (short) 60);
			entity.addAdverbOfValue("稍高", (short) 55, (short) 70);
			entity.addAdverbOfValue("比较高", (short) 70, (short) 85);
			entity.addAdverbOfValue("非常高", (short) 85, (short) 100);
			entity.addAdverbOfValue("高", (short) 65, (short) 100);
			entity.addAdverbOfValue("低", (short) 0, (short) 35);
			entity.addAdverbOfValue("较低", (short) 15, (short) 30);
			entity.addAdverbOfValue("较高", (short) 70, (short) 85);
			entity.addAdverbOfValue("非常差", (short) 0, (short) 15);
			entity.addAdverbOfValue("比较差", (short) 15, (short) 30);
			entity.addAdverbOfValue("稍差", (short) 30, (short) 45);
			entity.addAdverbOfValue("一般差", (short) 15, (short) 30);
			entity.addAdverbOfValue("中等", (short) 40, (short) 60);
			entity.addAdverbOfValue("稍好", (short) 55, (short) 70);
			entity.addAdverbOfValue("比较好", (short) 70, (short) 85);
			entity.addAdverbOfValue("非常好", (short) 85, (short) 100);
			entity.addAdverbOfValue("好", (short) 65, (short) 100);
			entity.addAdverbOfValue("差", (short) 0, (short) 35);
			entity.addAdverbOfValue("较差", (short) 15, (short) 30);
			entity.addAdverbOfValue("较好", (short) 70, (short) 85);
			entity.setMainExpress("预期年化收益率(%)");
			entity.setName("AnnualYield");
			this.knowledgeManager.addUniqueIndex("基金收益率", entity);
			this.knowledgeManager.addUniqueIndex("AnnualYield", entity);
			attributes[sheet.getColumns()] = entity;
			fund.addProductToAttribute(entity);
			entity.addAttributeToProduct(fund);
			this.addToRecognition(entity, "__ATTRIBUTE__" + "AnnualYield",
					"基金收益率");
			this.addToRecognition(entity, "__ATTRIBUTE__" + "AnnualYield", "收益率");
			this.addToRecognition(entity, "__ATTRIBUTE__" + "AnnualYield", "收益");

			//factType = new InternalFactType("ReturnRate", "基金收益率", true, entity);
			factType = new InternalFactType("AnnualYield", "基金收益率", true, entity);
			this.contextManager.addFactType(factType);

			/*this.contextManager.addTypeRelation("ReturnRate", "ReturnRate",
					saleTaskType.getTypeName());*/

			// System.out.println();

			for (int i = 4; i < sheet.getRows(); i++) {
				product = new InternalEntity(false);
				product.setParent(fund);
				for (int j = 0; j < sheet.getColumns() + 1; j++) {
					product.addProductToAttribute(attributes[j]);
					attributes[j].addAttributeToProduct(product);
				}

				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell cell = sheet.getCell(j, i);
					Double d;
					Integer i1;
					// System.out.print(cell.getContents() + ":");
					if ((i1 = this.transeToInteger(cell.getContents())) != null) {
						entity = new InternalEntity(false);
						// entity.setName(i1.toString());
						entity.setMainExpress(cell.getContents());
						this.knowledgeManager.addUniqueIndex(
								attributes[j].getName() + cell.getContents(),
								entity);
						entity.setValue(i1);
						attributes[j].addAttributeToValue(entity);
						entity.addValueToAttribute(attributes[j]);
						entity.addValueToProduct(product);
						product.addProductToValue(entity);

						this.addToRecognition(entity, attributes[j].getName(),
								cell.getContents());

						if (attributes[j].getName().equals("FundCode")) {
							product.setName(entity.getName());
						}

						// System.out.println(1 + " " + cell.getContents());
					} else if ((d = this.transeToDouble(cell.getContents())) != null) {
						entity = new InternalEntity(true);
						// entity.setName(d.toString());
						entity.setMainExpress(d.toString());
						entity.setValue(d);
						attributes[j].addAttributeToValue(entity);
						entity.addValueToAttribute(attributes[j]);
						if (!entity.getValueToProduct().contains(product))
							entity.addValueToProduct(product);
						product.addProductToValue(entity);
						// System.out.print(2 + " ");
					} else {
						String[] contents = cell.getContents().split(",");
						for (String content : contents) {
							content = content.trim();
							// System.out.println(content == null);
							entity = (Entity) knowledgeManager
									.uniqueIndex(content);
							if (entity == null) {
								entity = new InternalEntity(false);
								entity.setName(content);
								entity.setMainExpress(content);
								this.knowledgeManager.addUniqueIndex(
										attributes[j].getName() + content,
										entity);
								/*
								 * this.knowledgeManager.addUniqueIndex(content,
								 * entity);
								 */
								// System.out.print(" a ");
								this.addToRecognition(entity,
										attributes[j].getName(), content);
								if (content.equals("契约型开放式")) {
									this.addToRecognition(entity,
											attributes[j].getName(), "开放式");
									this.addToRecognition(entity,
											attributes[j].getName(), "开放式基金");
								} else if (content.equals("契约型封闭式")) {
									this.addToRecognition(entity,
											attributes[j].getName(), "封闭式");
									this.addToRecognition(entity,
											attributes[j].getName(), "封闭式基金");
								} else if (attributes[j].getName().equals(
										"FundFullName")) {
									product.setMainExpress(entity
											.getMainExpress());

								} else if (attributes[j].getName().equals(
										"FundSponsor")
										|| attributes[j].getName().equals(
												"FundManageCompany")) {
									entity.setParent(fundCompany);
									/*
									 * System.out.println(entity.getMainExpress()
									 * + " : " + entity + " : " +
									 * product.getParent());
									 */
								}
							}
							attributes[j].addAttributeToValue(entity);
							entity.addValueToAttribute(attributes[j]);
							entity.addValueToProduct(product);
							product.addProductToValue(entity);
						}
					}
				}

				// 随机产生收益率数据 5%-20%
				entity = new InternalEntity(true);
				// entity.setName(d.toString());
				long returnRate = ((System.currentTimeMillis() + 500) % 2000);
				Double d = ((double) returnRate) / 10000;
				entity.setMainExpress(d.toString());
				entity.setValue(d);
				// System.out.println(attributes[sheet.getColumns()].getExpress());
				attributes[sheet.getColumns()].addAttributeToValue(entity);
				entity.addValueToAttribute(attributes[sheet.getColumns()]);
				if (!entity.getValueToProduct().contains(product))
					entity.addValueToProduct(product);
				product.addProductToValue(entity);

				// System.out.println();
				// System.out.println(product.getProductToAttribute());
				// System.out.println(product.getProductToValue());

			}

			// 统计每种属性对应父类情况。

			// System.out.println(sheet.getRows());
			// System.out.println(sheet.getColumns());

		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DoubleInsertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeUnmatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != book) {
				book.close();
			}
		}
	}

	private Double transeToDouble(String candidate) {
		String regEx = "[0-9]{1,3}([,]{1}[0-9]{3})*[.][0-9]*";
		String regEx2 = "[0-9]*[.][0-9]*";

		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(candidate);

		Pattern pat2 = Pattern.compile(regEx2);
		Matcher mat2 = pat2.matcher(candidate);

		if (mat.matches()) {
			return new Double(mat.group(0).replaceAll(",", ""));
		} else if (mat2.matches()) {
			return new Double(mat2.group(0));
		} else
			return null;

	}

	private Integer transeToInteger(String candidate) {
		String regEx = "[0-9]{1,3}([,]{1}[0-9]{3})*";
		String regEx2 = "[0-9]*";

		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(candidate);

		Pattern pat2 = Pattern.compile(regEx2);
		Matcher mat2 = pat2.matcher(candidate);

		if (mat.matches()) {
			return new Integer(mat.group(0).replaceAll(",", ""));
		} else if (mat2.matches()) {
			return new Integer(mat2.group(0));
		} else
			return null;

	}

	/*
	 * public static void main(String[] args) { ExceLoader loader = new
	 * ExceLoader("D:\\dataset\\fund.xls", new KnowledgeManager(), new
	 * ComparisionRecognition( new InternalContextManager()), new
	 * InternalContextManager()); loader.deal();
	 * 
	 * // String test = "32,727,309,642.1400";
	 * 
	 * // System.out.println(loader.transeToInteger(test)); //
	 * System.out.println(loader.transeToDouble(test));
	 * 
	 * }
	 */
}
