package com.founder.chatRobot.knowledgeMap.init;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.founder.chatRobot.common.exception.DoubleInsertException;
import com.founder.chatRobot.common.exception.TaskTypeNotFoundException;
import com.founder.chatRobot.common.exception.TypeMissException;
import com.founder.chatRobot.common.exception.TypeUnmatchException;
import com.founder.chatRobot.context.fact.InternalFactType;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.manager.InternalContextManager;
import com.founder.chatRobot.context.manager.common.ContextManager;
import com.founder.chatRobot.knowledgeMap.conception.entity.InternalEntity;
import com.founder.chatRobot.knowledgeMap.conception.entity.common.Entity;
import com.founder.chatRobot.knowledgeMap.conception.taskType.InternalTaskConception;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;
import com.founder.chatRobot.knowledgeMap.init.common.Loader;
import com.founder.chatRobot.knowledgeMap.manager.KnowledgeManager;
import com.founder.chatRobot.recognition.comparisionMethod.ComparisionRecognition;
import com.founder.chatRobot.recognition.departmentRecognition.DepartmentRecognition;
import com.founder.chatRobot.task.treader.SearchCommissionTaskTreader;

public class DepartmentIniter extends Loader {

	public DepartmentIniter(String fileName, KnowledgeManager knowledgeManager,
			ComparisionRecognition recognition, ContextManager contextManager) {
		super(fileName, knowledgeManager, recognition, contextManager);
	}

	public void deal() {
		Workbook book = null;

		try {
			book = Workbook.getWorkbook(is);
			Sheet sheet = book.getSheet(0);

			Entity departmentType = new InternalEntity(false);
			departmentType.setName("Department");
			departmentType.setMainExpress("营业部");
			FactType factType = new InternalFactType("Department", "营业部", true,
					departmentType);
			this.contextManager.addFactType(factType);
			this.addToRecognition(departmentType, "__ATTRIBUTE__Department",
					"营业部");
			this.addToRecognition(departmentType, "__ATTRIBUTE__Department",
					"营业厅");
			this.contextManager.addTypeRelation("department", "Department",
					"CreateAccount");

			Entity departmentName = new InternalEntity(false);
			departmentName.setName("DepartmentName");
			departmentName.setMainExpress("营业部名称");
			factType = new InternalFactType("DepartmentName", "营业部名称", true,
					departmentName);
			this.contextManager.addFactType(factType);
			this.addToRecognition(departmentName,
					"__ATTRIBUTE__DepartmentName", "营业部名称");

			Entity address = new InternalEntity(false);
			address.setName("Address");
			address.setMainExpress("地址");
			factType = new InternalFactType("Address", "地址", true, address);
			this.contextManager.addFactType(factType);
			this.addToRecognition(address, "__ATTRIBUTE__Address", "地址");
			this.addToRecognition(address, "__ATTRIBUTE__Address", "在哪");
			this.addToRecognition(address, "__ATTRIBUTE__Address", "位置");

			Entity phoneNumber = (Entity) this.knowledgeManager
					.uniqueIndex("PhoneNumber");
			this.addToRecognition(phoneNumber, "__ATTRIBUTE__PhoneNumber",
					"联系电话");
			this.addToRecognition(phoneNumber, "__ATTRIBUTE__PhoneNumber", "电话");
			this.addToRecognition(phoneNumber, "__ATTRIBUTE__PhoneNumber",
					"联系方式");

			Entity commission = new InternalEntity(false);
			commission.setName("Commission");
			commission.setMainExpress("股票交易默认佣金");

			TaskConception searchCommissionTaskType = new InternalTaskConception(
					"SearchCommission", new SearchCommissionTaskTreader(),
					"费率查询");
			this.contextManager.addTaskType(searchCommissionTaskType);
			this.contextManager.addTypeRelation("department", "Department",
					searchCommissionTaskType.getTypeName());
			this.contextManager.addTypeRelation("division", "Division",
					searchCommissionTaskType.getTypeName());
			this.knowledgeManager.addUniqueIndex("Commission", commission);

			Entity situated = (Entity) this.knowledgeManager
					.uniqueIndex("Situated");

			// System.out.println("department-situated : " + ((Entity)
			// situated));

			Entity degreeEntity = new InternalEntity(false);
			degreeEntity.setName("Degree");
			degreeEntity.setMainExpress("程度");
			knowledgeManager.addUniqueIndex("Degree", degreeEntity);
			factType = new InternalFactType("Degree", "程度", true, degreeEntity);
			contextManager.addFactType(factType);

			Entity increaseDegree = new InternalEntity(false);
			increaseDegree.setName("IncreaseDegree");
			increaseDegree.setMainExpress("程度增加");
			increaseDegree.setParent(degreeEntity);
			knowledgeManager.addUniqueIndex("IncreaseDegree", increaseDegree);

			Entity reduceDegree = new InternalEntity(false);
			reduceDegree.setName("ReduceDegree");
			reduceDegree.setMainExpress("程度减少");
			reduceDegree.setParent(degreeEntity);
			knowledgeManager.addUniqueIndex("ReduceDegree", reduceDegree);
			
			Entity degreeEntityAnswer = new InternalEntity(false);
			degreeEntityAnswer.setName("CommissionDegreeAnswer");
			degreeEntityAnswer.setMainExpress("具体费率需要根据账户金额、交易频率等决定。"
					+ "开户的时候营业部一定会给您一个满意的费率。");
			
			commission.addProductToAttribute(degreeEntity);
			commission.addProductToValue(degreeEntityAnswer);
			degreeEntity.addAttributeToProduct(commission);
			degreeEntity.addAttributeToValue(degreeEntityAnswer);
			degreeEntityAnswer.addValueToAttribute(degreeEntity);
			degreeEntityAnswer.addValueToProduct(commission);
			
			/*Set<Conception> infoSet = new HashSet<Conception>(
					degreeEntity.getAllConnectConception());
			infoSet.retainAll(commission.getAllConnectConception());
			System.out.println(infoSet.size());
			System.out.println(infoSet.iterator().next().getName());*/

			String lastDivision = "";
			for (int i = 2; i < sheet.getRows(); i++) {
				Entity department = new InternalEntity(false);
				department.setName(sheet.getCell(1, i).getContents());
				department.setMainExpress(sheet.getCell(1, i).getContents());
				department.setParent(departmentType);

				/*
				 * if(department.getName().equals("上海打浦路证券营业部")){
				 * System.out.println(department); }
				 */

				String currentDivision = sheet.getCell(0, i).getContents();
				if (currentDivision == null || currentDivision.equals("")) {
					currentDivision = lastDivision;
				} else {
					currentDivision = currentDivision.replace("地区", "");
					lastDivision = currentDivision;
				}

				// System.out.println(currentDivision + "\t");

				Entity division = (Entity) this.knowledgeManager
						.uniqueIndex(currentDivision);
				division.addValueToAttribute(situated);
				division.addValueToProduct(department);
				department.addProductToAttribute(situated);
				department.addProductToValue(division);
				situated.addAttributeToProduct(department);
				situated.addAttributeToValue(division);

				/*
				 * if (division == null) { division = new InternalEntity(false);
				 * division.setName(currentDivision);
				 * division.setMainExpress(new Express<ExpressConception>(
				 * currentDivision, division, this.expressRecognition)); }
				 */

				String nameString = sheet.getCell(1, i).getContents(), tmp;
				if (nameString == null || nameString.equals(""))
					continue;
				Entity name = new InternalEntity(false);
				name.setName(nameString);
				name.setMainExpress(nameString);
				this.addToRecognition(name, "DepartmentName", nameString);
				// System.out.println(nameString + "\t");
				tmp = nameString.replaceAll(currentDivision + "省", "");
				if (!tmp.equals(nameString)) {
					this.addToRecognition(name, "DepartmentName", tmp);
					nameString = tmp;
				} else if (!(tmp = nameString.replaceAll(currentDivision + "市",
						"")).equals(nameString)) {
					this.addToRecognition(name, "DepartmentName", tmp);
					nameString = tmp;
				} else if (!(tmp = nameString.replaceAll(currentDivision, ""))
						.equals(nameString)) {
					this.addToRecognition(name, "DepartmentName", tmp);
					nameString = tmp;
				}

				if (!(tmp = nameString.replaceAll("证券", "")).equals(nameString)) {
					this.addToRecognition(name, "DepartmentName", tmp);
				}
				if (!(tmp = nameString.replaceAll("证券营业部", ""))
						.equals(nameString)) {
					nameString = tmp;
					this.addToRecognition(name, "DepartmentName", tmp);
					DepartmentRecognition.addDepartment(tmp, name);
				}
				if (!(tmp = nameString.replaceAll("营业部", ""))
						.equals(nameString)) {
					nameString = tmp;
					this.addToRecognition(name, "DepartmentName", tmp);
					DepartmentRecognition.addDepartment(tmp, name);
				}

				Set<Entity> subDivision = new HashSet<Entity>(
						division.getValueToProduct());
				subDivision.retainAll(situated.getAttributeToProduct());
				String tmpString = nameString;
				for (Entity div : subDivision) {
					if (!(tmp = tmpString.replace(div.getName(), ""))
							.equals(tmpString) && tmp.length() > 1) {
						tmpString = tmp;
					}
				}
				if (!tmpString.equals(nameString) && tmpString.length() > 1) {
					this.addToRecognition(name, "DepartmentName", tmpString);
					DepartmentRecognition.addDepartment(tmpString, name);
				}

				name.addValueToAttribute(departmentName);
				name.addValueToProduct(department);
				department.addProductToAttribute(departmentName);
				department.addProductToValue(name);
				departmentName.addAttributeToProduct(department);
				departmentName.addAttributeToValue(name);

				Entity departmentAddress = new InternalEntity(false);
				departmentAddress.setName(sheet.getCell(2, i).getContents());
				departmentAddress.setMainExpress(sheet.getCell(2, i)
						.getContents());
				this.addToRecognition(departmentAddress, "Address", sheet
						.getCell(2, i).getContents());
				// System.out.println(sheet.getCell(2, i).getContents() + "\t");
				departmentAddress.addValueToAttribute(address);
				departmentAddress.addValueToProduct(department);
				department.addProductToAttribute(address);
				department.addProductToValue(departmentAddress);
				address.addAttributeToProduct(department);
				address.addAttributeToValue(departmentAddress);

				Entity connectPhone = new InternalEntity(false);
				connectPhone.setName(sheet.getCell(3, i).getContents());
				connectPhone.setMainExpress(sheet.getCell(3, i).getContents());
				this.addToRecognition(connectPhone, "PhoneNumber", sheet
						.getCell(3, i).getContents());
				// System.out.println(sheet.getCell(3, i).getContents() + "\t");
				connectPhone.addValueToAttribute(phoneNumber);
				connectPhone.addValueToProduct(department);
				department.addProductToAttribute(phoneNumber);
				department.addProductToValue(connectPhone);
				phoneNumber.addAttributeToProduct(department);
				phoneNumber.addAttributeToValue(connectPhone);

				String commisionString = "";
				if (!sheet.getCell(4, i).getContents().equals("无")) {
					commisionString += "现场:"
							+ sheet.getCell(4, i).getContents() + "‰ ; ";
				}
				if (!sheet.getCell(5, i).getContents().equals("无")) {
					commisionString += "电话："
							+ sheet.getCell(5, i).getContents() + "‰ ; ";
				}
				if (!sheet.getCell(6, i).getContents().equals("无")) {
					commisionString += "网上："
							+ sheet.getCell(6, i).getContents() + "‰ ; ";
				}
				if (!sheet.getCell(6, i).getContents().equals("无")) {
					commisionString += "手机："
							+ sheet.getCell(7, i).getContents() + "‰ ; ";
				}
				if (!commisionString.equals("")) {
					Entity commissionEntity = new InternalEntity(false);
					commissionEntity.setName(commisionString);
					commissionEntity.setMainExpress(commisionString);
					// System.out.println(sheet.getCell(3, i).getContents() +
					// "\t");
					commissionEntity.addValueToAttribute(commission);
					commissionEntity.addValueToProduct(department);
					department.addProductToAttribute(commission);
					department.addProductToValue(commissionEntity);
					commission.addAttributeToProduct(department);
					commission.addAttributeToValue(commissionEntity);
				}
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
		}

		try {
			TaskConception creatAccountTaskType = this.contextManager
					.findTaskType("CreateAccount");

			TaskConception departmentAddressTaskType = new InternalTaskConception(
					"Department-Address", null, "查询");
			this.contextManager.addTaskType(departmentAddressTaskType);
			departmentAddressTaskType
					.addSubSequentTaskType(creatAccountTaskType);
			this.contextManager.addTypeRelation("department", "Department",
					departmentAddressTaskType.getTypeName());
			this.contextManager.addTypeRelation("address", "Address",
					departmentAddressTaskType.getTypeName());

			TaskConception departmentPhoneTaskType = new InternalTaskConception(
					"Department-Address", null, "查询");
			departmentPhoneTaskType.addSubSequentTaskType(creatAccountTaskType);
			this.contextManager.addTaskType(departmentPhoneTaskType);
			this.contextManager.addTypeRelation("department", "Department",
					departmentPhoneTaskType.getTypeName());
			this.contextManager.addTypeRelation("phoneNumber", "PhoneNumber",
					departmentPhoneTaskType.getTypeName());

		} catch (TaskTypeNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TypeMissException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DoubleInsertException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TypeUnmatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		KnowledgeManager knowledgeManager = new KnowledgeManager();

		DepartmentIniter loader = new DepartmentIniter(
				"resources/department.xls", new KnowledgeManager(),
				new ComparisionRecognition(new InternalContextManager(
						knowledgeManager)), new InternalContextManager(
						knowledgeManager));
		loader.deal();

		// String test = "32,727,309,642.1400";

		// System.out.println(loader.transeToInteger(test)); //
	}
}
