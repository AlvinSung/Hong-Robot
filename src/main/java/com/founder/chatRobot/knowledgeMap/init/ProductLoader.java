package com.founder.chatRobot.knowledgeMap.init;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
import com.founder.chatRobot.knowledgeMap.conception.taskType.InternalTaskConception;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;
import com.founder.chatRobot.knowledgeMap.init.common.Loader;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;
import com.founder.chatRobot.recognition.comparisionMethod.ComparisionRecognition;
import com.founder.chatRobot.task.treader.SaleProductTreader;

public class ProductLoader extends Loader {

	public ProductLoader(String fileName, KnowledgeManager knowledgeManager,
			ComparisionRecognition recognition, ContextManager contextManager) {
		super(fileName, knowledgeManager, recognition, contextManager);
	}

	private Double transeToDouble(String candidate) {
		boolean hasPrecent = candidate.endsWith("%");

		if (hasPrecent) {
			candidate = candidate.replace("%", "");
		}

		String regEx = "[0-9]{1,3}([,]{1}[0-9]{3})*[.][0-9]*";
		String regEx2 = "[0-9]*[.][0-9]*";

		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(candidate);

		Pattern pat2 = Pattern.compile(regEx2);
		Matcher mat2 = pat2.matcher(candidate);

		if (mat.matches()) {
			if (hasPrecent)
				return (new Double(mat.group(0).replaceAll(",", ""))) / 100;
			else
				return new Double(mat.group(0).replaceAll(",", ""));
		} else if (mat2.matches()) {
			if (hasPrecent)
				return (new Double(mat2.group(0))) / 100;
			else
				return new Double(mat2.group(0));
		} else
			return null;

	}

	private Integer transeToInteger(String candidate) {
		String regEx = "[0-9]{1,3}([,]{1}[0-9]{3}){1,}";
		String regEx2 = "[0-9]{1,}";

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

	private TaskConception saleTaskInit() {
		try {
			TaskConception saleTask = new InternalTaskConception("SaleTask",
					new SaleProductTreader(), "推荐投资产品");
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

	}

	@Override
	public void deal() {
		FactType factType;
		Workbook book = null;

		try {
			// book = Workbook.getWorkbook(this.file);
			book = Workbook.getWorkbook(is);
			String[] sheetNames = book.getSheetNames();
			Entity entity, product, financialProduct, actionSubject, actionObject, action, descript;
			// Entity[] attributes = new InternalEntity[sheet.getColumns() +
			// 1];
			Map<String, Entity> attributeMap = new HashMap<String, Entity>();
			financialProduct = (Entity) this.knowledgeManager
					.uniqueIndex("理财产品");

			actionSubject = (Entity) this.knowledgeManager.uniqueIndex("行为主体");
			actionObject = (Entity) this.knowledgeManager.uniqueIndex("行为客体");
			action = (Entity) this.knowledgeManager.uniqueIndex("行为");

			/*
			 * Status precondition = (Status) this.knowledgeManager
			 * .uniqueIndex("CreatAccountStatus");
			 */
			// TaskConception saleTaskType = this.saleTaskInit();
			// precondition.addRestrictTaskType(saleTaskType);
			// saleTaskType.addPrecondition(precondition);

			this.saleTaskInit();

			/*
			 * this.contextManager.addTypeRelation("investmentProduct",
			 * "InvestmentProduct", "SaleTask");
			 */

			descript = new InternalEntity(false);
			descript.setName("Description");
			descript.setMainExpress("简介");
			attributeMap.put("Description", descript);
			for (String sheetName : sheetNames) {
				Sheet sheet = book.getSheet(sheetName);
				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell cCell = sheet.getCell(j, 1);
					Cell cell = sheet.getCell(j, 2);
					Cell eCell = sheet.getCell(j, 0);
					Cell actCell = sheet.getCell(j, 3);
					//System.out.println();
					//System.out.println(cell.getContents() + " ");
					entity = (Entity) this.knowledgeManager.uniqueIndex(eCell
							.getContents().trim());
					/*if(entity != null){
						System.out.println(entity.getName());
						System.out.println(entity.getMainExpress());
					}*/
					if (entity == null)
						entity = attributeMap.get(eCell.getContents().trim());
					else if (attributeMap.get(eCell.getContents().trim()) == null) {
						attributeMap.put(eCell.getContents().trim(), entity);
					}

					if (entity == null) {
						if (cell.getContents().equals("每万份收益（元）")
								|| cell.getContents().equals("7日年化收益率")
								|| cell.getContents().equals("预计7日年化收益率")
								|| cell.getContents().equals("预期年化收益率(%)")
								|| cell.getContents().equals("最新净值")
								|| cell.getContents().equals("累计净值")) {
							entity = new InternalEntity(true);
							entity.addAdverbOfValue("非常差", (short) 0,
									(short) 15);
							entity.addAdverbOfValue("比较差", (short) 15,
									(short) 30);
							entity.addAdverbOfValue("稍差", (short) 30,
									(short) 45);
							entity.addAdverbOfValue("一般差", (short) 15,
									(short) 30);
							entity.addAdverbOfValue("中等", (short) 40,
									(short) 60);
							entity.addAdverbOfValue("稍好", (short) 55,
									(short) 70);
							entity.addAdverbOfValue("比较好", (short) 70,
									(short) 85);
							entity.addAdverbOfValue("非常好", (short) 85,
									(short) 100);
							entity.addAdverbOfValue("好", (short) 60, (short) 80);
							entity.addAdverbOfValue("差", (short) 20, (short) 40);
							entity.addAdverbOfValue("较差", (short) 15,
									(short) 30);
							entity.addAdverbOfValue("较好", (short) 70,
									(short) 85);
							entity.addAdverbOfValue("非常低", (short) 0,
									(short) 15);
							entity.addAdverbOfValue("比较低", (short) 15,
									(short) 30);
							entity.addAdverbOfValue("稍低", (short) 30,
									(short) 45);
							entity.addAdverbOfValue("一般低", (short) 15,
									(short) 30);
							entity.addAdverbOfValue("一般", (short) 40,
									(short) 60);
							entity.addAdverbOfValue("稍高", (short) 55,
									(short) 70);
							entity.addAdverbOfValue("比较高", (short) 70,
									(short) 85);
							entity.addAdverbOfValue("非常高", (short) 85,
									(short) 100);
							entity.addAdverbOfValue("高", (short) 65,
									(short) 100);
							entity.addAdverbOfValue("低", (short) 0, (short) 35);
							entity.addAdverbOfValue("较低", (short) 15,
									(short) 30);
							entity.addAdverbOfValue("较高", (short) 70,
									(short) 85);
						} else
							entity = new InternalEntity(false);

						
						/*System.out.println("adding factType : " +
								eCell.getContents() + " : " + cell.getContents() +
									":" + entity);
						System.out.println(attributeMap.containsKey(eCell
								.getContents().trim()));*/
						

						entity.setMainExpress(cell.getContents().trim());
						entity.setName(eCell.getContents().trim());
						attributeMap.put(eCell.getContents().trim(), entity);

						financialProduct.addProductToAttribute(entity);
						entity.addAttributeToProduct(financialProduct);
						this.addToRecognition(entity,
								"__ATTRIBUTE__" + eCell.getContents(),
								cell.getContents());
						if (cCell.getContents() != null
								&& !cCell.getContents().equals("")) {
							String[] represents = cCell.getContents()
									.split(" ");
							for (String represent : represents) {
								this.addToRecognition(entity, "__ATTRIBUTE__"
										+ eCell.getContents(), represent);
							}
						}

						factType = new InternalFactType(eCell.getContents(),
								cell.getContents(), true, entity);
						
						/*System.out.println("factType : " +
								eCell.getContents() + " : " + cell.getContents());
						System.out.println(attributeMap.containsKey(eCell
								.getContents().trim()));*/
						
						this.contextManager.addFactType(factType);

						this.contextManager.addTypeRelation(
								eCell.getContents(), eCell.getContents(),
								"SaleTask");

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
							newAction = (Entity) knowledgeManager
									.uniqueIndex(actCell.getContents());
							if (newAction == null) {
								newAction = new InternalEntity(false);
								newAction.setName(actCell.getContents());
								newAction.setMainExpress(actCell.getContents());
								this.knowledgeManager.addUniqueIndex(
										actCell.getContents(), newAction);

								newAction.addProductToAttribute(actionObject);
								newAction.addProductToValue(financialProduct);
								financialProduct
										.addValueToAttribute(actionObject);
								financialProduct.addValueToProduct(newAction);
								actionObject.addAttributeToProduct(entity);
								actionObject.addAttributeToValue(newAction);
								// System.out.println("aaaa : " +
								// actCell.getContents());
								for (String content : actCell.getContents()
										.split(" ")) {
									// System.out.println(content);
									this.addToRecognition(newAction, "Action",
											content);
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
				}

				// System.out.println();

				for (int i = 4; i < sheet.getRows(); i++) {
					product = new InternalEntity(false);
					product.setParent(financialProduct);
					/*
					 * for (int j = 0; j < sheet.getColumns() + 1; j++) {
					 * product.addProductToAttribute(attributes[j]);
					 * attributes[j].addAttributeToProduct(product); }
					 */

					for (int j = 0; j < sheet.getColumns(); j++) {
						Cell cell = sheet.getCell(j, i);
						Cell eCell = sheet.getCell(j, 0);
						Entity attribute = attributeMap
								.get(eCell.getContents());
						Double d;
						Integer i1;
						// System.out.println("asdfg : " + j);
						// System.out.println(eCell.getContents() + ":");
						if (attribute.getName().equals("InvestmentManager")) {
							String[] contents = cell.getContents().split("\n");

							for (int pos = 0; pos < contents.length; pos += 2) {
								String content = contents[pos];
								content = content.trim();
								entity = (Entity) knowledgeManager
										.uniqueIndex(content);
								if (entity == null) {
									entity = new InternalEntity(false);
									entity.setName(content);
									entity.setMainExpress(content);
									this.knowledgeManager.addUniqueIndex(
											attribute.getName() + content,
											entity);
									String description = contents[pos + 1];
									Entity persionDescription = new InternalEntity(
											false);
									persionDescription.setName(description);
									persionDescription
											.setMainExpress(description);
									persionDescription
											.addValueToAttribute(descript);
									persionDescription
											.addValueToProduct(entity);
									descript.addAttributeToValue(persionDescription);
									descript.addAttributeToProduct(entity);
									entity.addProductToAttribute(descript);
									entity.addProductToValue(persionDescription);
								}
								attribute.addAttributeToValue(entity);
								entity.addValueToAttribute(attribute);
								entity.addValueToProduct(product);
								product.addProductToValue(entity);
								if (!product.getProductToAttribute().contains(
										attribute)) {
									product.addProductToAttribute(attribute);
									attribute.addAttributeToProduct(product);
								}
							}
						} else if (attribute.getName().equals(
								"MarketingMechanism")) {
							String[] contents = cell.getContents().split("、");
							/*
							 * System.out.println(product.getMainExpress() +
							 * " : " + cell.getContents());
							 */

							for (int pos = 0; pos < contents.length; pos++) {
								String content = contents[pos]
										.replaceAll("（直销）", "")
										.replaceAll("(直销)", "")
										.replaceAll(
												"以及中国证监会认可的并与管理人签署相关协议的其他推广机构",
												"");
								content = content.trim();
								if (content.equals(""))
									continue;

								String[] contents1 = content.split(",");
								for (String content1 : contents1) {
									content1 = content1.trim();
									entity = (Entity) knowledgeManager
											.uniqueIndex(content1);
									if (entity == null) {
										entity = new InternalEntity(false);
										entity.setName(content1);
										entity.setMainExpress(content1);
										this.knowledgeManager.addUniqueIndex(
												attribute.getName() + content1,
												entity);
									}
									attribute.addAttributeToValue(entity);
									entity.addValueToAttribute(attribute);
									entity.addValueToProduct(product);
									product.addProductToValue(entity);
									if (!product.getProductToAttribute()
											.contains(attribute)) {
										product.addProductToAttribute(attribute);
										attribute
												.addAttributeToProduct(product);
									}
								}
							}

						} else if ((i1 = this.transeToInteger(cell
								.getContents())) != null) {
							entity = new InternalEntity(false);
							// entity.setName(i1.toString());
							entity.setMainExpress(cell.getContents());
							/*
							 * this.knowledgeManager.addUniqueIndex(
							 * attributes[j].getName() + cell.getContents(),
							 * entity);
							 */
							entity.setValue(i1);
							attribute.addAttributeToValue(entity);
							entity.addValueToAttribute(attribute);
							entity.addValueToProduct(product);
							product.addProductToValue(entity);
							if (!product.getProductToAttribute().contains(
									attribute)) {
								product.addProductToAttribute(attribute);
								attribute.addAttributeToProduct(product);
							}

							this.addToRecognition(entity, attribute.getName(),
									cell.getContents());

							if (attribute.getName().equals("ProductCode")) {
								product.setName(entity.getName());
							}

						} else if ((d = this.transeToDouble(cell.getContents())) != null) {
							entity = new InternalEntity(true);
							// entity.setName(d.toString());
							entity.setMainExpress(d.toString());
							entity.setValue(d);
							attribute.addAttributeToValue(entity);
							entity.addValueToAttribute(attribute);
							if (!entity.getValueToProduct().contains(product))
								entity.addValueToProduct(product);
							product.addProductToValue(entity);
							if (!product.getProductToAttribute().contains(
									attribute)) {
								product.addProductToAttribute(attribute);
								attribute.addAttributeToProduct(product);
							}

							// System.out.print(2 + " ");
						} else {
							String content = cell.getContents();
							content = content.trim();
							// System.out.println(content == null);
							if (content.equals(""))
								continue;
							entity = (Entity) knowledgeManager
									.uniqueIndex(content);
							if (entity == null) {
								entity = new InternalEntity(false);
								entity.setName(content);
								entity.setMainExpress(content);
								if (content.length() < 50) {
									this.knowledgeManager.addUniqueIndex(
											attribute.getName() + content,
											entity);
									this.addToRecognition(entity,
											attribute.getName(), content);
								}
								if (attribute.getName().equals("ProductName")) {
									product.setMainExpress(entity
											.getMainExpress());

								} /*
								 * else if (attributes[j].getName().equals(
								 * "FundSponsor") ||
								 * attributes[j].getName().equals(
								 * "FundManageCompany")) {
								 * entity.setParent(fundCompany);
								 * 
								 * System.out.println(entity.getMainExpress() +
								 * " : " + entity + " : " +
								 * product.getParent());
								 * 
								 * }
								 */
							}
							attribute.addAttributeToValue(entity);
							entity.addValueToAttribute(attribute);
							entity.addValueToProduct(product);
							product.addProductToValue(entity);
							if (!product.getProductToAttribute().contains(
									attribute)) {
								product.addProductToAttribute(attribute);
								attribute.addAttributeToProduct(product);
							}
							/*
							 * if (content.equals("刘宁")) {
							 * System.out.println(entity.getExpress());
							 * System.out.println(attributes[j].getExpress());
							 * System.out.println(entity.getValueToProduct()
							 * .size());
							 * System.out.println(entity.getValueToAttribute()
							 * .size());
							 * System.out.println(product.getExpress()); }
							 */
							// System.out.print(3 + " ");
						}
					}

					// System.out.println();
					// System.out.println(product.getProductToAttribute());
					// System.out.println(product.getProductToValue());

				}

				// 统计每种属性对应父类情况。

				// System.out.println(sheet.getRows());
				// System.out.println(sheet.getColumns());
			}

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
		} catch (TypeMissException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != book) {
				book.close();
			}
		}
	}

}
